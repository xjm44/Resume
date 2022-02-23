import pandas as pd
import AIMatchingSys
import sql
import random

# get the category of age
db = sql.SQLDatabase()
age_cates = db.get_age_cate()
age_dict = {}
age_cate_range = len(age_cates)
for cate in age_cates:
    age_dict[cate[0]] = cate[1]
age_dict["None"] = None
age_cates.append((None, "None"))

# get the category of goals
db = sql.SQLDatabase()
goal_cates = db.get_matching_goal_cate()
goal_dict = {}
goal_cate_range = len(goal_cates)
for cate in goal_cates:
    goal_dict[cate[0]] = cate[1]

#get the category of industry
db = sql.SQLDatabase()
industry_cates = db.get_matching_industry_cate()
industry_dict = {}
industry_cate_range = len(industry_cates)
for cate in industry_cates:
    industry_dict[cate[0]] = cate[1]

#get the category of experience years
db = sql.SQLDatabase()
exp_cates = db.get_experience_cate()
exp_dict = {}
exp_cate_range = len(exp_cates)
for cate in exp_cates:
    exp_dict[cate[0]] = cate[1]

#get the category of gender
gender_cates = ["Male", "Female", None]

# def get_ramdom_entry()

def leader_generate():
    num = 200
    ramdom_goal_entry = random.choice()

def create_coach_to_database(i, age):
    #create coach account
    db = sql.SQLDatabase()
    username = "AItestcoach"+str(i)
    fname = "AItestcoach"
    lname = str(i)
    db.add_Coach(fname,lname,"testcoach@test",username,i,"Coach",age, "DEE-DBfnQBiAGyYlpgRxDg", "eL0cgDAWg95uO52LLpO8OjKKYLMkvG5oFFbH")
    i+=1


def coach_generate():
    num = 20
    i = 0
    data = {'coach_id': [], 'coach_goal_1': [], 'coach_goal_2': [], 'coach_goal_3': [], 'coach_industry': [], 'coach_year_of_experience': [], 'coach_age': [], 'coach_gender': []}
    while i< num:
        coach_id = i+25
        data['coach_id'].append(coach_id)
        goal1 = random.choice(goal_cates)
        data['coach_goal_1'].append(goal1)
        goal2 = random.choice(goal_cates)
        data['coach_goal_2'].append(goal2)
        goal3 = random.choice(goal_cates)
        data['coach_goal_3'].append(goal3)
        industry = random.choice(industry_cates)
        data['coach_industry'].append(industry)
        exp = random.randint(1, 10)
        data['coach_year_of_experience'].append(exp)
        age = random.randint(20,60)
        data["coach_age"].append(age)
        gender = random.choice(["Male","Female"])
        data["coach_gender"].append(gender)
        i+=1
        create_coach_to_database(i,age)
        db = sql.SQLDatabase()
        username = "AItestcoach"+str(i)
        fname = "AItestcoach"
        lname = str(i)
        db.EditCoachProfile(username,fname,lname,age,gender,"testcoach@test",i,exp,industry[0],goal1[0],goal2[0],goal3[0],coach_id)

    coach_df = pd.DataFrame(data)
    coach_df.to_csv("coach_fake_data.csv")
    return coach_df

def leader_generate():
    i = 0
    num = 20
    data = {'leader_id': [], 'main_goal': [], 'industry': [], 'years_of_experience': [], 'prefer_age': [], 'prefer_gender': [], 'matched_coach_id': []}
    while i<num:
        leader_id = i+45
        data['leader_id'].append(leader_id)
        main_goal = random.choice(goal_cates)
        data['main_goal'].append(main_goal)
        industry = random.choice(industry_cates)
        data['industry'].append(industry)
        exp = random.choice(exp_cates)
        data['years_of_experience'].append(exp)
        prefer_age = random.choice(age_cates)
        data['prefer_age'].append(prefer_age)
        prefer_gender = random.choice(gender_cates)
        data['prefer_gender'].append(prefer_gender)
        matchingSystem = AIMatchingSys.matchSystem(main_goal[0], industry[0], exp[0], prefer_gender, prefer_age[0])
        result = matchingSystem.get_matched_coach_id()
        data['matched_coach_id'].append(result)

        db = sql.SQLDatabase()
        fname = "AItestleader"
        lname = str(leader_id)
        username = fname+lname
        gender = random.choice(gender_cates)
        db = sql.SQLDatabase()
        db.registration_check(fname, lname, i, username, i, prefer_gender)
        db = sql.SQLDatabase()
        db.new_match_application(leader_id, result, main_goal[0], industry[0], exp[0], prefer_gender, prefer_age[0])
        i+=1

    # leader_df = pd.DataFrame(data)
    # leader_df.to_csv("leader_fake_data.csv")
    # return leader_df

#generate 20 random coaches in blank database
coach_df = coach_generate()

#generate 100 random leaders
leader_df = leader_generate()
