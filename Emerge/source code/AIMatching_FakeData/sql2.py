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
                        SELECT start, date, event
                        FROM AvailableTime
                        WHERE leader_id = ?;
                    """
        result = self.dictfetchall(sql_query,(user_id,))
        result_resized = []
        for i in result:
            start = i[0]
            date = i[1]
            event = i[2]
            datetime = date + " " + start
            result_resized.append((datetime, event))
        return result_resized

    #-----------------------------------------------------------------------------

    #Get Data of User profile
    def getprofile(self,user_id,identity):
        if identity == "Leader":
            sql_query = """
                            SELECT username,fname,lname,Leaders.age,gender,birthday,email,location,mobile_phone,business_phone,company_name,company_size \
                            FROM Users U JOIN Leaders USING (user_id)
                            WHERE user_id = ?
                        """
        elif identity == "Coach":
            sql_query = """
                            SELECT username,fname,lname,C.age,gender,email,phone, C.experience, I.industry_name,G1.goal_name as goal_1_name, G2.goal_name as goal_2_name, G3.goal_name as goal_3_name
                            FROM Users U JOIN Coach C ON (U.user_id = C.coach_id)
							JOIN Matching_Coach_Info M ON (C.coach_id = M.coach_id)
							JOIN Matching_Industry I ON (M.industry_id = I.industry_id)
							JOIN Matching_Goals G1 ON (M.goal_1_id = G1.id)
							JOIN Matching_Goals G2 ON (M.goal_2_id = G2.id)
							JOIN Matching_Goals G3 ON (M.goal_3_id = G3.id)
                            WHERE user_id = ?
                        """
        else:
            return []

        result = self.dictfetchone(sql_query,(user_id,))
        return result

    def editprofile(self,username,firstname,lastname,age,gender,birthday,email,location,mobile,business,company_name,company_size,user_id):
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
        check_exist = self.fetchone(identity_query, (username, email, fname, lname))

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
        print(userid)
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
            WHERE coach_id = ? ;
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

    def add_time(self,coach_id,start,end,date,event):

        query = """
            INSERT INTO AvailableTime(coach_id,start,end,date,event)
            VALUES(?,?,?,?,?);
        """

        self.add(query,(coach_id,start,end,date,event))


     ##select coach infomation from db
    def info_coach(self):
        query = """
            SELECT *
            FROM Users
            WHERE identity = "Coach"
            """
        result = self.dictfetchall(query,)
        return result

    ##select client infomation from db
    def info_client(self):
        query = """
            SELECT *
            FROM Users
            WHERE identity = "Leader"
            """
        result = self.dictfetchall(query,)
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

    def add_Coach(self, firstname, lastname, email, username, password, identity):
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
        res = self.dictfetchall(query)
        return res

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

    def insert_coach_matching_info(self, coach_id, goal1_id, goal2_id, goal3_id, industry_id):
        query = """
                    INSERT INTO Matching_Coach_Info
                    VALUES ?, ?, ?, ?, ?
                """
        self.add(query, (coach_id, goal1_id, goal2_id, goal3_id, industry_id))

    def get_all_industries(self):
        query = """
                    SELECT *
                    FROM Matching_Industry;
                """
        res = self.dictfetchall(query)
        return res

    def EditCoachProfile(self,username,firstname,lastname,age,gender,email,mobile,experience,industry,goal1,goal2,goal3,user_id):
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

        query3 = """
                    UPDATE Matching_Coach_Info
                    SET goal_1_id = ?,
                        goal_2_id = ?,
                        goal_3_id = ?,
                        industry_id = ?
                    WHERE coach_id = ?;
        """

        self.cur.execute(query3,(goal1,goal2,goal3,industry,user_id))
        self.conn.commit()
        self.conn.close()
