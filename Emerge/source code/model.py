#import database here
import sql
from datetime import *
from flask import *
import jwt
import time
import requests
import json
import hashlib
import sqlite3


now = datetime.now()




    #------------------------------------------------------------

""" Check if the login inforamtion is correct"""
def login_check(username, password):
    password = hash_function(password)
    dbConnect = sql.SQLDatabase()
    result = dbConnect.login_credential_check(username, password)

    if result == None:
        return False
    else:
        return result

    #------------------------------------------------------------


""" Register new account and Check if the account registered available(if username already used) """
def register_check(fname, lname, email, username, password, gender):
    password = hash_function(password)
    dbConnect = sql.SQLDatabase()
    if gender == "None":
        gender = None
    result = dbConnect.registration_check(fname, lname, email, username, password, gender)
    if result:
        return True
    else:
        return False


    #------------------------------------------------------------

""" Get the information about schedule of the leader """
def mainPage_leader_schedule(now,user_id):
    dbConnect = sql.SQLDatabase()
    schedule_res = dbConnect.get_schedule_event(user_id)

    return sort_schedule_event(schedule_res,now,"leader")


    #------------------------------------------------------------

""" Get the information about schedule of the coach """
def get_coach_schedule_event(now,user_id):
    dbConnect = sql.SQLDatabase()
    schedule_res = dbConnect.get_coach_schedule_event(user_id)

    return sort_schedule_event(schedule_res,now,"coach")


    #------------------------------------------------------------

def sort_schedule_event(schedule_res,now,identity):
    i = 0
    while i < len(schedule_res):
        schedule_res[i] = list(schedule_res[i])
        if len(schedule_res[i][0]) == 15:
            schedule_res[i][0] = schedule_res[i][0][0:8]+"0"+schedule_res[i][0][8:]
        i+=1
    result = sorted(schedule_res,key = lambda x: x[0])

    if len(result) == 0:
        return None
    else:
        selected_result = []

        #check if the schedule is expire, and separate y/m/d with h:m
        i = 0
        while i<len(result):
            time = result[i][0]
            newd = datetime(int(result[i][0].split(" ")[0].split("-")[0]),int(result[i][0].split(" ")[0].split("-")[1]),int(result[i][0].split(" ")[0].split("-")[2]),int(result[i][0].split(" ")[1][0:-3]))
            if newd >= now:
                daytime = result[i][0]
                event = result[i][1]
                link = result[i][2]
                day = daytime.split(" ")[0].replace("-","/")
                time = daytime.split(" ")[1]
                if identity == "leader":
                    daytime_event_list = (day, time, event,link)
                elif identity == "coach":
                    leader_name = result[i][3]
                    daytime_event_list = (day, time, event,link, leader_name)
                selected_result.append(daytime_event_list)
            i+=1
        return selected_result

def get_profile(user_id,identity):
    db = sql.SQLDatabase()
    result = db.getprofile(user_id,identity)
    return result

def editprofile(username,firstname,lastname,age,gender,birthday,email,location,mobile,business,company_name,company_size,user_id):
    db = sql.SQLDatabase()
    db.editprofile(username,firstname,lastname,age,gender,birthday,email,location,mobile,business,company_name,company_size,user_id)

#------------------------------------------------------------
"""Things for goals in learders' main page"""

"""Get all goals with all imformation splitted"""
def get_goals(user_id):
    db = sql.SQLDatabase()
    result = db.get_goals(user_id)
    goal_num = len(result)
    goals = []
    total_times = []
    progress = []
    ids = []
    for goal in result:
        goals.append(goal[0])
        total_times.append(goal[1])
        progress.append(goal[2])
        ids.append(goal[3])
    return goal_num, goals, total_times, progress, ids

def make_progress(user_id, goal_id):
    db = sql.SQLDatabase()
    result = db.make_progress(user_id, goal_id)

def add_new_goal(user_id, new_goal, new_times):
    db = sql.SQLDatabase()
    result = db.add_new_goal(user_id, new_goal, new_times)


#------------------------------------------------------------


def forgotPassword(username, password):
    password = hash_function(password)
    dbConnect = sql.SQLDatabase()
    result = dbConnect.forgotPassword_handler(username, password)
    return result

def reset_check(username, email, fname, lname):
    dbConnect = sql.SQLDatabase()
    result = dbConnect.resetPassword_check(username, email, fname, lname)
    return result

#------------------------------------------------------------
def get_coachid(user_id):

    db = sql.SQLDatabase()
    return db.get_coachid(user_id)[0]
def get_userid_for_msg(leader_id):

    db = sql.SQLDatabase()
    res = db.get_userid_for_msg(leader_id)
    if res == None:
        return None
    else:
        return res[0]
# chat history
def get_chat_history(sender_id,receiver_id):
    db = sql.SQLDatabase()

    if receiver_id == sender_id:
        return "wrong chat ! cannot chat to yourself !"

    return db.get_chat_history(sender_id, receiver_id)

# chat from user side
def chat_fromuser(receiver_id, text, sender_id):

    db = sql.SQLDatabase()
    if sender_id != receiver_id:
        return db.send_message_user(receiver_id, sender_id, text)

    else:
        return "failed sending msg"
# chat from coach side
def chat_fromcoach(receiver_id, text, sender_id):

    db = sql.SQLDatabase()
    if sender_id != receiver_id:
        return db.send_message_coach(receiver_id, sender_id, text) #TODO *********

    else:
        return "failed sending msg"

#------------------------------------------------------------
""" Get the leaders' list for a coach """

def get_leaders_list(user_id):
    db = sql.SQLDatabase()
    result = db.get_leaders_list(user_id)
    return result

#------------------------------------------------------------

def get_coach_time(coach_id):
    db = sql.SQLDatabase()
    return db.get_coach_time(coach_id)

def pick_time(leader_id,start,end,date):

    coach_id = get_coachid(leader_id)
    db = sql.SQLDatabase()
    return db.pick_time(leader_id,coach_id,start,end,date,)

#------------------------------------------------------------
"""
get username using user_id
"""
def get_username(user_id):
    db = sql.SQLDatabase()
    return db.get_username(user_id)[0]

#------------------------------------------------------------
"""
add token into blacklist
"""
def add_blacklist_token(token):
    db = sql.SQLDatabase()
    db.add_black_token(token)

#------------------------------------------------------------
"""
check if the token is not in the blacklist
True means the token is not in the blacklist
"""
def check_token(token):
    db = sql.SQLDatabase()
    result = db.check_token(token)
    return result

#------------------------------------------------------------
"""
Check the identity of given user_id
"""
def get_identify(user_id):
    db = sql.SQLDatabase()
    result = db.get_identify(user_id)
    if result == None:
        return None
    else:
        return result[0]

#------------------------------------------------------------
"""
JWT Auth_use: encode the user_id
Generates the Auth Token
return: string
"""
def encode_auth_token(user_id, app):
    try:
        payload = {
            'exp': datetime.utcnow() + timedelta(days=7, seconds=0),
            'iat': datetime.utcnow(),
            'sub': user_id
        }
        return jwt.encode(
            payload,
            app.config.get('SECRET_KEY'),
            algorithm='HS256',
        )
    except Exception as e:
        return e
#------------------------------------------------------------
"""
Decodes the auth token
param: auth_token:
return: integer|string
"""

def decode_auth_token(auth_token, app):
    db = sql.SQLDatabase()
    black_list_check = db.check_token(auth_token)
    if not black_list_check:
        return False, "Please log in first."
    try:
        payload = jwt.decode(auth_token, app.config.get('SECRET_KEY'), algorithms=['HS256'])
        return True, payload['sub']
    except jwt.ExpiredSignatureError:
        return False, 'Signature expired. Please log in again.'
    except jwt.InvalidTokenError:
        return False, 'Invalid token. Please log in again.'

#------------------------------------------------------------
"""
Functions about zoom link
"""

def get_zoom_key_sec(coach_id):

    db = sql.SQLDatabase()
    return db.get_zoom_key_sec(coach_id)

def generateToken(zoom_key,zoom_secert):
    token = jwt.encode({'iss': zoom_key, 'exp': time.time() + 5000},zoom_secert,algorithm='HS256')
    # token1 = token.encode().decode()
    # token1 = jwt.decode(token, zoom_secert, algorithms=['HS256'])
    return token

#generateToken = generateToken(zoom_key,zoom_secert)

def getUser_id(generateToken):
    headers = {'authorization': 'Bearer %s' % generateToken,'content-type': 'application/json'}
    r = requests.get('https://api.zoom.us/v2/users/', headers=headers)
    users = json.loads(r.text)["users"]
    id_json = json.dumps(users[0])
    id = json.loads(id_json)["id"]
    return id

#zoom_user_id = getUsers_id(zoom_key,zoom_secert)


def meeting_json(start,end,date,zoom_user_id):
    start_time = date+" "+start+":00"
    end_time = date+" "+end+":00"
    start_time1 = datetime.strptime(start_time, "%Y-%m-%d %H:%M:%S")
    end_time1 = datetime.strptime(end_time, "%Y-%m-%d %H:%M:%S")
    seconds_diff = (end_time1-start_time1).seconds
    duration = int(seconds_diff /60)
    start_date = date+"T"+start+":00"
    meetingdetails = {"topic": "The title",
                      "type": 2,
                      "start_time": start_date+"Z",
                      "duration": duration,
                      "schedule_for":zoom_user_id,
                      "timezone": "Australia/Sydney",
                      "agenda": "test",
                      "settings": {"host_video":False,
                                   "participant_video": True,
                                   "join_before_host": True,
                                   "jbh_time": 5,
                                   "mute_upon_entry": False,
                                   "watermark": True,
                                   "audio": "voip",
                                   "auto_recording": "cloud"
                                   }
                  }
    return meetingdetails

def create_meeting(gen,meetingdetails):
    headers = {'authorization': 'Bearer %s' % gen,'content-type': 'application/json'}
    r = requests.post(f'https://api.zoom.us/v2/users/me/meetings', headers=headers, data=json.dumps(meetingdetails))
    join_url = json.loads(r.text)["join_url"]
    return join_url
    # return (join_url)

def update_link(link,coach_id,start,end,date):
        db = sql.SQLDatabase()
        db.update_zoom_link(link,coach_id,start,end,date)
#------------------------------------------------------------
def processing_for_add_time(user_id,start,end,date,now,weekday,numdays,check1,check2):
    sh = int(start[0:-3])
    eh = int(end[0:-3])

    if eh > sh :
        if check1:
            j = 1
            while j < 8:
                if j != weekday:
                    diff = j-weekday
                    daylist = date.split("-")
                    daylist[0] = int(daylist[0])
                    daylist[1] = int(daylist[1])
                    daylist[2] = int(daylist[2])
                    datet = datetime(daylist[0],daylist[1],daylist[2])
                    datet += timedelta(days=diff)
                    newdate = datet.strftime('%Y-%-m-%-d')
                    i=0
                    while i < eh-sh:
                        try:
                            newsta=str(sh+i)+":00"
                            if len(newsta) == 4:
                                newsta = "0" + newsta
                            newend=str(sh+i+1)+":00"
                            if len(newend) == 4:
                                newend = "0" + newend
                            newd = datetime(int(newdate.split("-")[0]),int(newdate.split("-")[1]),int(newdate.split("-")[2]),sh+i)
                            if newd >= now:
                                add_time(user_id,newsta,newend,newdate)
                        except sqlite3.IntegrityError as e:
                            pass
                        i+=1
                j+=1
        if check2:
            j = int(date.split("-")[2])%7
            if j == 0:
                j = 7
            while j <= numdays:
                if j != date.split("-")[2]:
                    newdate = "-".join(date.split("-")[0:2]+[str(j)])
                    i=0
                    while i < eh-sh:
                        try:
                            newsta=str(sh+i)+":00"
                            if len(newsta) == 4:
                                newsta = "0" + newsta
                            newend=str(sh+i+1)+":00"
                            if len(newend) == 4:
                                newend = "0" + newend
                            newd = datetime(int(newdate.split("-")[0]),int(newdate.split("-")[1]),int(newdate.split("-")[2]),sh+i)
                            if newd >= now:
                                add_time(user_id,newsta,newend,newdate)
                        except sqlite3.IntegrityError as e:
                            pass
                        i+=1
                j+=7
        i=0
        while i < eh-sh:
            try:
                newsta=str(sh+i)+":00"
                if len(newsta) == 4:
                    newsta = "0" + newsta
                newend=str(sh+i+1)+":00"
                if len(newend) == 4:
                    newend = "0" + newend
                newd = datetime(int(date.split("-")[0]),int(date.split("-")[1]),int(date.split("-")[2]),sh+i)
                if newd >= now:
                    add_time(user_id,newsta,newend,date)
            except sqlite3.IntegrityError as e:
                pass
            i+=1

def add_time(user_id,start,end,date):

    db = sql.SQLDatabase()
    db.add_time(user_id,start,end,date)

def coachName():
    db = sql.SQLDatabase()
    result = db.info_coach()
    return result

def clientName():
    db = sql.SQLDatabase()
    result = db.info_client()
    return result

def deleteUser(user_id):
    db = sql.SQLDatabase()
    result = db.delete_user(user_id)
    return result

def addCoach(firstname, lastname, email, username, password, identity, age, zoomKey, zoomSecret):
    password = hash_function(password)
    db = sql.SQLDatabase()
    result = db.add_Coach(firstname, lastname, email, username, password, identity, age, zoomKey, zoomSecret)
    if result:
        return result
    else:
        return False

def addClient(firstname, lastname, email, username, password, identity):
    password = hash_function(password)
    db = sql.SQLDatabase()
    result = db.add_Client(firstname, lastname, email, username, password, identity)
    if result:
        return result
    else:
        return False

def get_all_goals():
    db = sql.SQLDatabase()
    result = db.get_all_goals()
    if result:
        return result
    else:
        return False # why need here?

def get_all_industries():
    db = sql.SQLDatabase()
    result = db.get_all_industries()
    if result:
        return result
    else:
        return False

def EditCoachProfile(username,firstname,lastname,age,gender,email,mobile,experience,industry,goal1,goal2,goal3,user_id):
    db = sql.SQLDatabase()
    db.EditCoachProfile(username,firstname,lastname,age,gender,email,mobile,experience,industry,goal1,goal2,goal3,user_id)

def checkDuplicateName(username):

    db = sql.SQLDatabase()
    result = db.checkDuplicateName(username)

    return result

def get_matching_goal_cate():
    db = sql.SQLDatabase()
    res = db.get_matching_goal_cate()
    return res

def get_industries_cate():
    db = sql.SQLDatabase()
    res = db.get_matching_industry_cate()
    return res

def get_experience_cate():
    db = sql.SQLDatabase()
    res = db.get_experience_cate()
    return res

def get_age_cate():
    db = sql.SQLDatabase()
    res = db.get_age_cate()
    return res

def update_leader_coach(leader_id, coach_id):
    db = sql.SQLDatabase()
    db.update_leader_coach(leader_id, coach_id)
    time.sleep(2)

#------------------------------------------------------------
def write_feedback(leader_id,coach_id,feedback):
    db = sql.SQLDatabase()
    db.write_feedback(leader_id,coach_id,feedback)

# from yourcoach site
def current_impression(leader_id,coach_id,impression):
    db = sql.SQLDatabase()
    db.current_impression(leader_id,coach_id,impression)
    time.sleep(1)

def survey(leader_id,coach_id,q1,q2,q3,q4,q5,q6,q7,q8,q9,q10):
    db = sql.SQLDatabase()
    db.survey(leader_id,coach_id,q1,q2,q3,q4,q5,q6,q7,q8,q9,q10)

def show_current_impression(leader_id,coach_id):
    db = sql.SQLDatabase()
    res = db.show_current_impression(leader_id,coach_id)
    return res

#------------------------------------------------------------
def get_match_application():
    db = sql.SQLDatabase()
    res = db.get_match_application()
    result = []
    for application in res:
        if application[6] == 0:
            result.append(application)
    return result

def new_match_application(leader_id, coach_id, main_goal, industry, exp, gender, age):
    db = sql.SQLDatabase()
    db.new_match_application(leader_id, coach_id, main_goal, industry, exp, gender, age)

def update_status_of_application(leader_id):
    db = sql.SQLDatabase()
    db.update_status_of_application(leader_id)

def check_match_application(user_id):
    db = sql.SQLDatabase()
    res = db.check_match_application(user_id)
    if res == None:
        return None
    else:
        return res[0]

def get_assessment_info(user_id):
    db = sql.SQLDatabase()
    res = db.get_assessment_info(user_id)
    return res

#------------------------------------------------------------
def get_coach_goals(coachid):
    db = sql.SQLDatabase()
    return db.get_coach_goals(coachid)

def get_coach_industry(coachid):
    db = sql.SQLDatabase()
    return db.get_coach_industry(coachid)

def get_coach_yoe(coachid):
    db = sql.SQLDatabase()
    return db.get_coach_yoe(coachid)
def get_coach_info(coachid):
    db = sql.SQLDatabase()
    return db.get_coach_info(coachid)
def get_coach_names(coachid):
    db = sql.SQLDatabase()
    return db.get_coach_names(coachid)

def get_leader_name():
    db = sql.SQLDatabase()
    res = db.get_leader_name()
    if res == None:
        return None
    else:
        return res

def get_coach_name():
    db = sql.SQLDatabase()
    res = db.get_coach_name()
    if res == None:
        return None
    else:
        return res

def matchLeaderCoach(leader_id, coach_id):
    db = sql.SQLDatabase()
    res = db.matchLeaderCoach(leader_id, coach_id)
    if res == None:
        return None
    else:
        return res

def matchLeaderCoachNull(leader_id):
    db = sql.SQLDatabase()
    res = db.matchLeaderCoachNull(leader_id)
    if res == None:
        return None
    else:
        return res

def getFeedback():
    db = sql.SQLDatabase()
    res = db.get_feedback()
    if res == None:
        return None
    else:
        return res

def deleteFeedback(feedback_id):
    db = sql.SQLDatabase()
    res = db.deleteFeedback(feedback_id)
    if res == None:
        return None
    else:
        return res

def approveFeedback(feedback_id):
    db = sql.SQLDatabase()
    res = db.approveFeedback(feedback_id)
    if res == None:
        return None
    else:
        return res

def hash_function(password):
    salt = b'U29tZWJvZHkgb25jZSB0b2xkIG'
    return hashlib.pbkdf2_hmac('sha256', password.encode('utf-8'), salt, 8000)

def get_feedback_coach(coach_id):
    db = sql.SQLDatabase()
    res = db.get_feedback_coach(coach_id)

    return res
