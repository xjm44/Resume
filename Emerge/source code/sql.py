import sqlite3
from datetime import datetime, timezone

class SQLDatabase():
    '''
        Our SQL Database

    '''

    # Get the database running
    def __init__(self):
        self.conn = sqlite3.connect("database.db")
        self.cur = self.conn.cursor()

    # SQLite 3 does not natively support multiple commands in a single statement
    # Using this handler restores this functionality
    # This only returns the output of the last command

    # def execute(self, sql_string):
    #     out = None
    #     for string in sql_string.split(";"):
    #         try:
    #             out = self.cur.execute(string)
    #         except:
    #             pass
    #     return out

    # Commit changes to the database
    def commit(self):
        self.conn.commit()

    #-----------------------------------------------------------------------------

    def dictfetchone(self, query, params=""):
        self.cur.execute(query, params)
        returnres = self.cur.fetchone()
        self.conn.close()
        result = returnres
        return result

    #-----------------------------------------------------------------------------

    def dictfetchall(self, query, params=""):
        self.cur.execute(query, params)
        returnres = self.cur.fetchall()
        self.conn.close()
        return returnres

    #-----------------------------------------------------------------------------

    def add(self, query, params=""):
        self.cur.execute(query, params)
        self.conn.commit()
        self.conn.close()

    #-----------------------------------------------------------------------------
    # User handling
    #-----------------------------------------------------------------------------

    #-----------------------------------------------------------------------------

    def login_credential_check(self,username,password):
        sql_query = """
                        SELECT user_id, identity
                        FROM Users
                        WHERE username = ? AND password = ?;
                    """
        result = self.dictfetchone(sql_query,(username, password))
        return result

    #-----------------------------------------------------------------------------

    # Now only check if the username exist (Maybe add more repetition check later)
    def registration_check(self, fname, lname, email, username, password, gender):
        check_query = """
                        SELECT *
                        FROM Users
                        WHERE username = ?
                      """
        register_query = """
                        INSERT INTO Users (username, password, fname, lname, gender, email)
                        VALUES (?, ?, ?, ?, ?, ?);
        """
        self.cur.execute(check_query, (username,))
        check_exist = self.cur.fetchone()
        if check_exist == None:
            self.cur.execute(register_query, (username, password, fname, lname, gender, email))
            self.conn.commit()
            self.conn.close()
            return True
        else:
            return False

    #-----------------------------------------------------------------------------

    #Get username through id provided
    def get_username(self,id):
        sql_query = """
                        SELECT username
                        FROM Users
                        WHERE user_id = ?
                    """
        username = self.dictfetchone(sql_query, (id,))
        return username

    #-----------------------------------------------------------------------------

    #Get the schedules and events of the client
    def get_schedule_event(self,user_id):
        sql_query = """
                        SELECT start, date, event,zoom_link
                        FROM AvailableTime
                        WHERE leader_id = ?
                        ORDER BY start;
                    """
        result = self.dictfetchall(sql_query,(user_id,))
        result_resized = []
        for i in result:
            start = i[0]
            date = i[1]
            event = i[2]
            zoom_link = i[3]
            #print("db step:",zoom_link)
            datetime = date + " " + start
            result_resized.append((datetime, event,zoom_link))
        return result_resized

    #-----------------------------------------------------------------------------

    #Get the schedules and events of the coach
    def get_coach_schedule_event(self,user_id):
        sql_query = """
                        SELECT a.start, a.date, a.event, a.zoom_link, u.username
                        FROM AvailableTime a JOIN USERS u ON (a.leader_id = u.user_id)
                        WHERE coach_id = ?
                        ORDER BY start;
                    """
        result = self.dictfetchall(sql_query,(user_id,))
        result_resized = []
        for i in result:
            start = i[0]
            date = i[1]
            event = i[2]
            zoom_link = i[3]
            leader_name = i[4]
            datetime = date + " " + start
            result_resized.append((datetime, event,zoom_link,leader_name))
        return result_resized
    #-----------------------------------------------------------------------------
    #Get Data of User profile
    def getprofile(self,user_id,identity):
        if identity == "Leader":
            sql_query = """
                            SELECT username,fname,lname,Leaders.age,gender,birthday,email,location,mobile_phone,business_phone,company_name,company_size
                            FROM Users U JOIN Leaders USING (user_id)
                            WHERE user_id = ?
                        """
            self.cur.execute(sql_query,(user_id,))
            result  = self.cur.fetchone()

        elif identity == "Coach":
            sql_query1 = """
                            SELECT username,fname,lname,C.age,gender,email,phone, C.experience
                            FROM Users U JOIN Coach C ON (U.user_id = C.coach_id)
                            WHERE user_id = ?
                        """
            sql_query2 = """
                            SELECT I.industry_name,G1.goal_name as goal_1_name, G2.goal_name as goal_2_name, G3.goal_name as goal_3_name
							FROM Matching_Coach_Info M
							JOIN Matching_Industry I ON (M.industry_id = I.industry_id)
							JOIN Matching_Goals G1 ON (M.goal_1_id = G1.id)
							JOIN Matching_Goals G2 ON (M.goal_2_id = G2.id)
							JOIN Matching_Goals G3 ON (M.goal_3_id = G3.id)
                            WHERE coach_id = ?
                        """
            self.cur.execute(sql_query1,(user_id,))
            result1  = self.cur.fetchone()
            self.cur.execute(sql_query2,(user_id,))
            result2 = self.cur.fetchone()
            if result2 == None:
                result2 = (None,None,None,None)
            result = result1+result2
        else:
            return []



        self.conn.commit()
        self.conn.close()
        return result

    def editprofile(self,username,firstname,lastname,age,gender,birthday,email,location,mobile,business,company_name,company_size,user_id):

        if mobile == "None":
            mobile = None
        if email == "None":
            email= None
        if lastname == "None":
            lastname= None
        if firstname == "None":
            firstname= None
        if birthday == "None":
            birthday = None
        if location == "None":
            location = None
        if business == "None":
            business = None
        if company_name == "None":
            company_name = None
        if company_size == "None":
            company_size = None

        sql_query = """
                        UPDATE Leaders
                        SET age = ?,
                            birthday = ?,
                            location = ?,
                            mobile_phone = ?,
                            business_phone = ?,
                            company_name = ?,
                            company_size = ?
                        WHERE user_id = ?;
        """
        self.cur.execute(sql_query,(age,birthday,location,mobile,business,company_name,company_size, user_id))



        query2 = """
                    UPDATE Users
                    SET username = ?,
                        fname = ?,
                        lname = ?,
                        gender = ?,
                        email = ?
                    WHERE user_id = ?;
        """

        self.cur.execute(query2,(username,firstname,lastname,gender,email,user_id))
        self.conn.commit()
        self.conn.close()

    def EditCoachProfile(self,username,firstname,lastname,age,gender,email,mobile,experience,industry,goal1,goal2,goal3,user_id):

        if age == "None":
            age = None
        if mobile == "None":
            mobile = None
        if email == "None":
            email= None
        if lastname == "None":
            lastname= None
        if firstname == "None":
            firstname= None

        sql_query = """
                        UPDATE Coach
                        SET age = ?,
                            phone = ?,
                            experience = ?
                        WHERE coach_id = ?;
        """

        self.cur.execute(sql_query,(age,mobile,experience,user_id))

        query2 = """
                    UPDATE Users
                    SET username = ?,
                        fname = ?,
                        lname = ?,
                        gender = ?,
                        email = ?
                    WHERE user_id = ?;
        """

        self.cur.execute(query2,(username,firstname,lastname,gender,email,user_id))

        querys = """
                    SELECT *
                    FROM Matching_Coach_Info
                    WHERE coach_id = ?;
        """

        self.cur.execute(querys,(user_id,))
        result = self.cur.fetchone()

        if result != None:

            query3 = """
                        UPDATE Matching_Coach_Info
                        SET goal_1_id = ?,
                            goal_2_id = ?,
                            goal_3_id = ?,
                            industry_id = ?
                        WHERE coach_id = ?;
            """

            self.cur.execute(query3,(goal1,goal2,goal3,industry,user_id))
        elif result == None:
            query3 = """
                INSERT INTO Matching_Coach_Info (coach_id, goal_1_id, goal_2_id, goal_3_id, industry_id)
                VALUES (?, ?, ?, ? ,?)
            """

            self.cur.execute(query3,(user_id,goal1,goal2,goal3,industry))


        self.conn.commit()
        self.conn.close()


    def get_identify(self, user_id):
        sql_query = """
                    SELECT identity
                        FROM Users
                        WHERE user_id = ?
                    """
        result = self.dictfetchone(sql_query,(user_id,))
        return result

    def get_goals(self, user_id):
        sql_query = """
                    SELECT goal, total_times, progress, goal_id, complete
                    FROM Goals
                    WHERE user_id = ? AND complete = 0
                    """
        result = self.dictfetchall(sql_query,(user_id,))
        return result


    def make_progress(self, user_id, goal_id):
        sql_query = """
                    UPDATE Goals
                    SET progress = progress + 1
                    WHERE user_id = ? AND goal_id = ?
                    """
        self.add(sql_query,(user_id, goal_id))

    def add_new_goal(self, user_id, new_goal, new_times):
        sql_query = """
                    INSERT INTO Goals (user_id, goal, total_times)
                    VALUES (?, ?, ?)
                    """
        self.add(sql_query,(user_id, new_goal, new_times))

    #update the password of the user
    def forgotPassword_handler(self, username, password):
        sql_query = """
                    UPDATE Users
                    SET password = ?
                    WHERE username = ?
                    """
        self.cur.execute(sql_query, (password, username))
        self.conn.commit()
        self.conn.close()
        return True

    #check if the information user provides is correct when user is going to change password
    def resetPassword_check(self, username, email, fname, lname):
        identity_query = """
                    SELECT *
                    FROM Users
                    WHERE username = ?
                    AND email = ?
                    AND fname = ?
                    AND lname = ?
        """
        check_exist = self.dictfetchone(identity_query, (username, email, fname, lname))

        if check_exist == None:
            return False
        else:
            return True
    #-----------------------------------------------------------------------------
    #Get username through id provided
    def get_user_id(self,name):
        sql_query = """
                        SELECT user_id
                        FROM Users
                        WHERE username= ?
                    """
        result = self.dictfetchone(sql_query, (name,))
        return result

    def get_coachid(self,userid):
        query="""
            SELECT coach_id
            FROM Leaders
            WHERE user_id = ?
        """

        result = self.dictfetchone(query,(userid,))
        return result

    def get_userid_for_msg(self,userid):
        # WHAT IF ONE COACH -> MANY LEADERS
        # TODO

        query="""
            SELECT Leaders.user_id
            FROM Leaders JOIN Users USING(user_id)
            WHERE coach_id = ?
        """

        result = self.dictfetchone(query,(userid,))

        return result


    # get chat history
    def get_chat_history(self, sender_id, receiver_id):
        query = """
                SELECT text, sender, receiver, time, "check"
                FROM message
                WHERE (sender = ? and receiver = ?) or (receiver = ? and sender = ?)
                ORDER BY time ASC
                """
        cursor = self.cur.execute(query, (sender_id, receiver_id, sender_id, receiver_id))

        result = []
        # print(result)
        # print("---")
        for x in cursor.fetchall():
            result.append([x[0],
                        x[1],
                        x[2],
                        x[3],
                        x[4]])
        self.conn.close()


        return result


    # user send msg
    def send_message_user(self, receiver_id, sender_id, text):

        query="""
            insert INTO message
            (text, sender, receiver,time)
             VALUES (?, ?, ?, ?)
             """
        current_time = datetime.now().strftime("%m/%d/%Y %H:%M:%S")
        self.conn.execute(query,(text, sender_id, receiver_id,current_time))
        self.conn.commit()
        self.conn.close()

    # coach send msg
    def send_message_coach(self, receiver_id, sender_id, text): # need coach page

        query="""
            insert INTO message
            (text, sender, receiver, "check",time)
             VALUES (?, ?, ?,1,?)
             """
        current_time = datetime.now().strftime("%m/%d/%Y %H:%M:%S")
        self.conn.execute(query,(text, sender_id, receiver_id,current_time))
        self.conn.commit()
        self.conn.close()


    #-----------------------------------------------------------------------------
    """ Get the list of leaders based on the username of the coach"""

    def get_leaders_list(self,user_id):
        query = """
                SELECT username, Leaders.user_id
                FROM Leaders JOIN Users USING(user_id)
                WHERE coach_id = ?
                """
        result = self.dictfetchall(query,(user_id,))
        return result

    #-----------------------------------------------------------------------------

    def get_coach_time(self, coach_id):
        query="""
            SELECT AvailableTime.start, AvailableTime.end, AvailableTime.date, AvailableTime.picked, AvailableTime.event, Users.username
            FROM AvailableTime LEFT JOIN Users ON AvailableTime.leader_id = Users.user_id
            WHERE coach_id = ?
            ORDER BY AvailableTime.start;
        """

        result = self.dictfetchall(query,(coach_id,))
        return result

    def pick_time(self,leader_id,coach_id,start,end,date):

        query = """
            UPDATE AvailableTime
            SET picked = 1,
                leader_id = ?
            WHERE coach_id = ? AND
                  start = ? AND
                  end = ? AND
                  date = ?;
        """

        self.add(query,(leader_id,coach_id,start,end,date))

#-------------------------------------------------------

    #Black list to record ilegal tokens and token has logged out
    def add_black_token(self, token):
        query = """
                INSERT INTO Blacklist_tokens
                VALUES (?)
                """
        self.add(query,(token,))

    #Check if the token given is in the black list, return True if not in
    def check_token(self, token):
        query = """
                SELECT *
                FROM Blacklist_tokens
                WHERE token = ?
                """
        result = self.dictfetchone(query,(token,))
        if result == None:
            return True
        else:
            return False

    def add_time(self,coach_id,start,end,date):

        query = """
            INSERT INTO AvailableTime(coach_id,start,end,date)
            VALUES(?,?,?,?);
        """

        self.add(query,(coach_id,start,end,date))


     ##select coach infomation from db
    def info_coach(self):
        query = """
            SELECT C.coach_id, usersa.fname as coach_fname, usersa.lname as coach_lname, usersb.fname as leader_fname, usersb.lname as leader_lname
            FROM Coach C Left JOIN Leaders L Using (coach_id) LEFT JOIN Users usersa ON (C.coach_id = usersa.user_id) LEFT JOIN Users usersb ON (L.user_id = usersb.user_id)
            ORDER BY C.coach_id
            """

        fetch_coach_id = """
                        SELECT coach_id
                        FROM Coach
                        ORDER BY coach_id
                        """
        self.cur.execute(query)
        results = self.cur.fetchall()

        self.cur.execute(fetch_coach_id)
        coach_ids = self.cur.fetchall()
        if results == None:
            return []


        res_reform = {}
        for coach_id in coach_ids:
            res_reform[coach_id[0]] = []

        for result in results:
            result_coach_id = result[0]
            res_reform[result_coach_id].append(result[1:])

        return res_reform

    ##select client infomation from db
    def info_client(self):
        query = """
            SELECT usersb.user_id, usersb.fname as leader_fname, usersb.lname as leader_lname, usersa.fname as coach_fname, usersa.lname as coach_lname
            FROM Leaders  LEFT JOIN Users usersa ON (usersa.user_id = coach_id) JOIN Users usersb ON (Leaders.user_id = usersb.user_id)
            ORDER BY usersb.user_id
            """
        result = self.dictfetchall(query,)
        if result == None:
            return []
        return result

    def delete_user(self, user_id):
        query1 = """
            DELETE FROM Users
            WHERE user_id = ?
            """

        query2 = """
            DELETE FROM Leaders
            WHERE user_id = ?
            """

        query3 = """
            DELETE FROM Coach
            WHERE coach_id = ?
            """
        self.conn.execute(query1, (user_id, ))
        self.conn.execute(query2, (user_id, ))
        self.conn.execute(query3, (user_id, ))
        self.conn.commit()
        self.conn.close()

    def add_Coach(self, firstname, lastname, email, username, password, identity, age, zoomKey, zoomSecret):
        check_query = """
                        SELECT *
                        FROM Users
                        WHERE username = ?
        """
        query1 = """
            INSERT INTO Users(username, password, fname, lname, email, identity)
            VALUES (?, ?, ?, ?, ?, ?)
        """

        select_coach_id = """
                        SELECT user_id
                        FROM Users
                        WHERE username = ?
        """
        query2 = """
            UPDATE Coach
            SET age = ?, zoom_key = ?, zoom_secret = ?
            WHERE coach_id = ?
        """


        # self.cur.execute(check_query, (username,))
        # check_exist = self.cur.fetchone()

        # if check_exist == None:
        if True:
            self.cur.execute(query1, (username, password, firstname, lastname, email, identity))
            self.conn.commit()
            self.cur.execute(select_coach_id, (username, ))
            result = self.cur.fetchone()
            self.conn.execute(query2, (age, zoomKey, zoomSecret, result[0], ))
            self.conn.commit()
            self.conn.close()
            return True
        else:
            return False

    def add_Client(self, firstname, lastname, email, username, password, identity):
        check_query = """
                        SELECT *
                        FROM Users
                        WHERE username = ?
        """

        query1 = """
            INSERT INTO Users(username, password, fname, lname, email, identity)
            VALUES (?, ?, ?, ?, ?, ?)
        """


        self.cur.execute(check_query, (username,))
        check_exist = self.cur.fetchone()
        if check_exist == None:
            self.conn.execute(query1, (username, password, firstname, lastname, email, identity))
            self.conn.commit()
            self.conn.close()
            return True
        else:
            return False

    def get_all_coaches(self):
        query = """
                    SELECT coach_id, goal_1_id, goal_2_id, goal_3_id, industry_id, experience, gender, age
                    FROM Coach JOIN Users ON (coach_id = user_id) JOIN Matching_Coach_Info USING (coach_id)
                """
        query_overmatch_coach = """
                                    SELECT coach_id, count(coach_id) as num
                                    FROM Leaders
                                    GROUP BY coach_id
                                    HAVING num >= 10
                                """
        self.cur.execute(query_overmatch_coach)
        over_coaches = self.cur.fetchall()
        self.cur.execute(query)
        all_info = self.cur.fetchall()

        i = 0
        while i < len(all_info):
            coach_id = all_info[i][0]
            for over_coach in over_coaches:
                if coach_id == over_coach[0]:
                    all_info.pop(i)
                    continue
            i+=1

        self.conn.close()
        return all_info

    def get_num_of_matched_leaders(self, coach_id):
        query = """
                    SELECT COUNT(user_id)
                    FROM Leaders
                    WHERE coach_id = ?
                """
        res = self.dictfetchone(query,(coach_id,))[0]
        return res

    def get_age_cate(self):
        query = """
                    SELECT *
                    FROM Matching_Age_Category
                """
        res = self.dictfetchall(query)
        return res

    def get_experience_cate(self):
        query = """
                    SELECT *
                    FROM Matching_experience_Category
                """
        res = self.dictfetchall(query)
        return res


    def get_matching_goal_cate(self):
        query = """
                    SELECT *
                    FROM Matching_Goals
                """
        res = self.dictfetchall(query)
        return res

    def get_all_goals(self):
        query = """
                    SELECT *
                    FROM Matching_Goals;
                """
        res = self.dictfetchall(query)
        return res


    def get_matching_industry_cate(self):
        query = """
                    SELECT *
                    FROM Matching_Industry
                """
        res = self.dictfetchall(query)
        return res


    def get_all_industries(self):
        query = """
                    SELECT *
                    FROM Matching_Industry;
                """
        res = self.dictfetchall(query)
        return res

    def checkDuplicateName(self, username):

        query = """
                    SELECT *
                    FROM Users
                    WHERE username = ?;
                """
        res = self.dictfetchall(query,(username,))
        result = True
        if len(res) == 0:
            result = False
        return result

    def update_leader_coach(self, leader_id, coach_id):
        query = """
                    UPDATE Leaders
                    SET coach_id = ?
                    WHERE user_id = ?;
                """
        self.add(query, (coach_id, leader_id))

    def update_status_of_application(self, leader_id):
        query = """
                UPDATE Matching_Application
                SET status = 1
                WHERE leader_id = ?;
                """
        self.add(query, (leader_id,))

    #-----------------------------------------------------------------------------
    def write_feedback(self,leader_id,coach_id,feedback):
        query = """
                        UPDATE feedback
                        SET comment = ?
                        WHERE leader = ? AND coach = ?;
        """

        self.conn.execute(query,(feedback, leader_id, coach_id))
        self.conn.commit()
        self.conn.close()

    # yourcoach part
    def current_impression(self,leader_id,coach_id,impression):

        query1 = """
                    SELECT *
                    FROM feedback
                    WHERE leader =? AND coach = ?;
        """

        query_insert="""
            insert INTO feedback
            (impression, leader, coach)
             VALUES (?, ?, ?)
             """

        query_update = """
                        UPDATE feedback
                        SET impression = ?
                        WHERE leader = ? AND coach = ?;
        """

        self.cur.execute(query1,(leader_id, coach_id))
        res = self.cur.fetchone()
        # print(leader_id)
        # print(coach_id)
        # print(res)
        if res==None:
            self.conn.execute(query_insert,(impression, leader_id, coach_id))
            self.conn.commit()
            self.conn.close()
        else:
            self.conn.execute(query_update,(impression, leader_id, coach_id))
            self.conn.commit()
            self.conn.close()
        self.conn.close()

    def show_current_impression(self,leader_id,coach_id):

        query = """
                SELECT impression
                FROM feedback
                WHERE (leader = ? and coach = ?)
                """
        self.cur.execute(query,(leader_id, coach_id,))
        result = self.cur.fetchone()

        if result==None:
            self.conn.close()
            return [""]
        self.conn.close()
        return result

    def survey(self,leader_id,coach_id,q1,q2,q3,q4,q5,q6,q7,q8,q9,q10):
        query = """
            UPDATE feedback
            SET q1 = ?,
                q2 = ?,
                q3 = ?,
                q4 = ?,
                q5 = ?,
                q6 = ?,
                q7 = ?,
                q8 = ?,
                q9 = ?,
                q10 = ?
            WHERE leader = ? AND
                  coach = ?;
        """

        self.conn.execute(query,(q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,leader_id, coach_id))
        self.conn.commit()
        self.conn.close()


    # admin part
    def feedback_show_to_coach(self,leader_id,coach_id,content):
        query = """
                    SELECT contentshowtocoach
                    FROM feedback;
                """
        res = self.dictfetchall(query)
        return res


    def get_match_application(self):
        query = """
                SELECT M.leader_id, userl.fname, userl.lname, M.coach_id, userc.fname, userc.lname, status
                FROM Matching_Application M JOIN Users userl ON (M.leader_id = userl.user_id)
                                            JOIN Users userc ON (M.coach_id = userc.user_id)
                """
        res = self.dictfetchall(query)
        return res

    def new_match_application(self, leader_id, coach_id, main_goal, industry, exp, gender, age):
        query = """
                INSERT INTO Matching_Application
                VALUES (?, ?, 0, ?, ?, ?, ?, ?)
                """
        self.add(query, (leader_id, coach_id, main_goal, industry, exp, age, gender))

    def check_match_application(self, user_id):
        query = """
                SELECT status
                FROM Matching_Application
                WHERE leader_id = ?
                """
        res = self.dictfetchone(query, (user_id,))
        return res

    def get_assessment_info(self, leader_id):
        query = """
                SELECT U.fname, U.lname, status, goal_name, industry_name, ME.category, MA.category, M.gender
                FROM Matching_Application M JOIN Users U ON (M.coach_id = U.user_id)
                                            LEFT JOIN Matching_Goals MG ON (M.main_goal = MG.id)
                                            LEFT JOIN Matching_Industry MI ON (M.industry = MI.industry_id)
                                            LEFT JOIN Matching_experience_Category ME ON (M.experience = ME.id)
                                            LEFT JOIN Matching_Age_Category MA ON (M.age_cate = MA.id)
                WHERE M.leader_id = ?
                """
        res = self.dictfetchone(query, (leader_id,))
        return res

    def get_zoom_key_sec(self,coach_id):
        query = """
                        SELECT Coach.zoom_key,Coach.zoom_secret
                        FROM Coach
                        WHERE coach_id = ?
        """
        res = self.dictfetchone(query,(coach_id,))

        return res

    def update_zoom_link(self,link,coach_id,start,end,date):
        update = """
                        UPDATE AvailableTime
                        SET zoom_link = ?
                        WHERE coach_id = ?
                        AND start = ?
                        AND end = ?
                        AND date = ?
        """
        self.cur.execute(update,(link,coach_id,start,end,date))
        self.conn.commit()
        self.conn.close()
# -------------------------------------------------------------
    def get_coach_goals(self,coachid):
        query = """
                    SELECT goal_1_id,goal_2_id,goal_3_id
                    FROM Matching_Coach_Info
                    WHERE coach_id = ?;
        """

        self.cur.execute(query,(coachid,))
        result = self.cur.fetchall()

        res = []
        i = 0
        if len(result) > 0:
            while i<len(result[0]):
                query1 = """
                            SELECT goal_name
                            FROM Matching_Goals
                            WHERE id = ?;
                """
                self.cur.execute(query1,(result[0][i],))
                res.append(self.cur.fetchone()[0])
                i+=1

        self.conn.commit()
        self.conn.close()
        return res

    def get_coach_industry(self,coachid):
        query = """
                    SELECT industry_id
                    FROM Matching_Coach_Info
                    WHERE coach_id = ?;
        """

        self.cur.execute(query,(coachid,))
        result = self.cur.fetchone()


        res = []
        query1 = """
                    SELECT goal_name
                    FROM Matching_Goals
                    WHERE id = ?;
        """
        if result != None:
            self.cur.execute(query1,(result))
            res = self.cur.fetchone()

        self.conn.commit()
        self.conn.close()
        return res

    def get_coach_yoe(self,coachid):
        query = """
                    SELECT experience
                    FROM Coach
                    WHERE coach_id = ?;
        """

        self.cur.execute(query,(coachid,))
        result = self.cur.fetchone()



        query1 = """
                    SELECT category
                    FROM Matching_experience_Category
                    WHERE id = ?;
        """
        self.cur.execute(query1,(result))
        res = self.cur.fetchone()
        self.conn.commit()
        self.conn.close()
        return res

    def get_coach_info(self,coachid):
        query = """
                    SELECT age, phone
                    FROM Coach
                    WHERE coach_id = ?;
        """

        self.cur.execute(query,(coachid,))
        result = self.cur.fetchall()

        self.conn.commit()
        self.conn.close()
        return result[0]
    def get_coach_names(self,coachid):
        query = """
                    SELECT fname, lname
                    FROM Users
                    WHERE user_id = ?;
        """

        self.cur.execute(query,(coachid,))
        result = self.cur.fetchone()

        self.conn.commit()
        self.conn.close()
        return result

    def get_leader_name(self):
        query = """
                SELECT user_id, fname, lname
                FROM Users
                WHERE identity = "Leader"
        """
        self.cur.execute(query,)
        result = self.cur.fetchall()

        self.conn.commit()
        self.conn.close()
        return result

    def get_coach_name(self):
        query = """
                SELECT user_id, fname, lname
                FROM Users
                WHERE identity = "Coach"
        """
        self.cur.execute(query,)
        result = self.cur.fetchall()

        self.conn.commit()
        self.conn.close()
        return result

    def matchLeaderCoach(self, leader_id, coach_id):
        query = """
            UPDATE Leaders
            SET coach_id = ?
            WHERE user_id = ?
        """

        self.cur.execute(query, (coach_id, leader_id, ))
        self.conn.commit()
        self.conn.close()

    def matchLeaderCoachNull(self, leader_id):
        query = """
            UPDATE Leaders
            SET coach_id = NULL
            WHERE user_id = ?
        """

        self.cur.execute(query, (leader_id,))
        self.conn.commit()
        self.conn.close()


    def get_feedback(self):
        query = """
            SELECT f.id, ua.fname, ua.lname, ub.fname, ub.lname, f.comment, f.impression, (f.q1 + f.q2 + f.q3 + f.q4 + f.q5+ f.q6+ f.q7+ f.q8+ f.q9+ f.q10)/10, f.sendtocoach
            FROM feedback f left join Users ua on (f.leader = ua.user_id ) left join Users ub on (f.coach = ub.user_id)
        """

        self.cur.execute(query,)
        result = self.cur.fetchall()

        self.conn.commit()
        self.conn.close()
        return result


    def deleteFeedback(self, feedback_id):
        query = """
            DELETE FROM feedback
            WHERE id = ?
        """

        self.conn.execute(query, (feedback_id, ))
        self.conn.commit()
        self.conn.close()

    def approveFeedback(self, feedback_id):
        query = """
            UPDATE feedback
            SET sendtocoach = 1
            WHERE id = ?
        """

        self.cur.execute(query, (feedback_id, ))
        self.conn.commit()
        self.conn.close()

    def get_feedback_coach(self,coach_id):
        query = """
            SELECT u.username, f.comment, f.impression, (f.q1 + f.q2 + f.q3 + f.q4 + f.q5 + f.q6 + f.q7 + f.q8 + f.q9 + f.q10)/10
            FROM feedback f JOIN Users u ON (f.leader = u.user_id)
            WHERE f.coach = ? AND sendtocoach = 1;
        """

        self.cur.execute(query,(coach_id,))
        result = self.cur.fetchall()

        self.conn.commit()
        self.conn.close()
        return result
