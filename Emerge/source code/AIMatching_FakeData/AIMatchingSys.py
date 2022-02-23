# goals, industry, years of experience, gender, age
import sql
import random
GOAL_DISTANCE = 500
INDUSTRY_DISTANCE = 50
YEARS_DISTANCE = 50
GENDER_DISTANCE = 10
AGE_DISTANCE = 4
class matchSystem:
    def __init__(self,lgoal, lindustry, lyears, lgender, lage):
        self.lgoal = lgoal
        self.lindustry = lindustry
        self.lyears = lyears #year category id
        self.lgender = lgender
        self.lage = lage # age category id
        db = sql.SQLDatabase()
        self.coaches_list = db.get_all_coaches()

        self.distances = {}
        for coach_info in self.coaches_list:
            self.distances[coach_info[0]] = 0


    def algorithm(self, leader, coach):
        distance = abs(leader - coach)
        return distance

    def age_distance(self, coach_info):
        if self.lage == None:
            return 0

        # get the cetegory of age of coach
        db = sql.SQLDatabase()
        coach_age = coach_info[7]
        age_cates_res = db.get_age_cate()
        age_cates = {}
        maximum_cate_id = 0
        maximum_cate_age = 1000
        for age_cate_res in age_cates_res:
            cate_id = age_cate_res[0]
            cate_string = age_cate_res[1]
            cate_string_split = cate_string.split("-")
            min = cate_string_split[0]
            max = cate_string_split[1]
            maximum_cate = 0
            if max == "":
                maximum_cate_age = int(min)
                maximum_cate_id = cate_id
            else:
                age_cates[cate_id] = list(range(int(min),int(max)+1))


        lage_cate = self.lage
        coachAge_cate = 0
        if coach_age >= maximum_cate_age:
            coachAge_cate = maximum_cate_id

        for key in age_cates:
            category = age_cates[key]
            if coach_age in category:
                coachAge_cate = key
        distance = self.algorithm(lage_cate, coachAge_cate)*AGE_DISTANCE
        return distance

    def year_distance(self, coach_info):
        # if self.lyears == None:
        #     return 0

        # get the cetegory of experience of coach
        db = sql.SQLDatabase()
        exp_cate_lists = db.get_experience_cate()
        min_cate_id = 1
        min_cate_exp = int(exp_cate_lists[0][1].split(" ")[1])
        max_cate_id = len(exp_cate_lists)
        max_cate_exp = int(exp_cate_lists[-1][1].split(" ")[1])
        exp_cates = {}

        i = 1
        while i<(len(exp_cate_lists)-1):
            exp_cate_id = exp_cate_lists[i][0]
            exp_cate = exp_cate_lists[i][1]
            min = exp_cate.split("-")[0]
            max = exp_cate.split("-")[1]
            exp_cates[exp_cate_id] = list(range(int(min),int(max)+1))
            i+=1

        # calculate the distance of experience
        coach_exp = coach_info[5]
        if coach_exp > max_cate_exp:
            return 0
        elif coach_exp < min_cate_exp:
            distance = self.algorithm(self.lyears, min_cate_id)*YEARS_DISTANCE
            return distance
        else:
            for key in exp_cates:
                cate = exp_cates[key]
                if coach_exp in cate:
                    if key > self.lyears:
                        return 0
                    distance = self.algorithm(self.lyears, key)*YEARS_DISTANCE
                    return distance

        return 0

    def all_data_distance(self):
        for coach_info in self.coaches_list:
            if self.lgoal not in [coach_info[1], coach_info[2], coach_info[3]]:
                self.distances[coach_info[0]] += GOAL_DISTANCE
            if self.lindustry != coach_info[4]:
                self.distances[coach_info[0]] += INDUSTRY_DISTANCE
            if self.lgender != None and self.lgoal != coach_info[6]:
                self.distances[coach_info[0]] += GENDER_DISTANCE
            age_distance = self.age_distance(coach_info)
            year_distance = self.year_distance(coach_info)
            self.distances[coach_info[0]] += age_distance
            self.distances[coach_info[0]] += year_distance

    def compare_coach(self, current_id, new_id):
        db = sql.SQLDatabase()
        numOfLeader_current = db.get_num_of_matched_leaders(current_id)
        db = sql.SQLDatabase()
        numOfLeader_new = db.get_num_of_matched_leaders(new_id)
        if numOfLeader_current < numOfLeader_new:
            return current_id
        elif numOfLeader_current > numOfLeader_new:
            return new_id
        else:
            return random.choice([new_id, current_id])

    def get_matched_coach_id(self):
        self.all_data_distance()
        current_min_distance = 100000 #give a value that is grater than the largest distance could have
        current_key = 0
        for key in self.distances:
            distance = self.distances[key]
            if distance < current_min_distance:
                current_min_distance = distance
                current_key = key
            elif distance == current_min_distance:
                #check the number of leaders a caoch have
                current_key = self.compare_coach(current_key, key)
        return current_key



# test = matchSystem(0, 0, 7, "Male", 1)
# test.all_data_distance()
# a = test.get_matched_coach_id()
# print(a)
# print(test.year_distance([0,0,0,0,1,0.5]))
