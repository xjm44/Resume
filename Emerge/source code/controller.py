"""
Route management.

This provides all of the websites routes and handles what happens each
time a browser hits each of the paths. This serves as the interaction
between the browser and the database while rendering the HTML templates
to be displayed.

You will have to make
"""

# Importing the required packages
from flask import *
import model
from time import sleep
from datetime import datetime, timezone
import sqlite3
import werkzeug
import AIMatchingSys

# Initialise the application
app = Flask(__name__)
app.secret_key = """U29tZWJvZHkgb25jZSB0b2xkIG1lIFRoZSB3b3JsZCBpcyBnb25uYSBy
b2xsIG1lIEkgYWluJ3QgdGhlIHNoYXJwZXN0IHRvb2wgaW4gdGhlIHNoZWQgU2hlIHdhcyBsb29r
aW5nIGtpbmRhIGR1bWIgV2l0aCBoZXIgZmluZ2VyIGFuZCBoZXIgdGh1bWIK"""
page = {}



#####################################################
#   INDEX
#####################################################


@app.route('/')
def introduction_page():
    """ Here is the route for introduction page """
    return render_template('landingPage.html', page=page)

@app.route('/coachApplication', methods = ["GET", "POST"])
def coachApplication():
    return render_template('coachApplication.html', page=page)

@app.route('/login', methods = ["GET","POST"])
def login_page():
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    if is_login[0]:
        identity = model.get_identify(is_login[1])
        if identity == "Leader":
            resp = make_response(redirect(url_for('main_page')))
            return resp
        elif identity == "Coach":
            resp = make_response(redirect(url_for('coachMainPage')))
            return resp

    if request.method == "POST":
        username = request.form["username"]
        password = request.form["password"]
        result = model.login_check(username, password)
        if result:
            if result[1] == "Leader":
                resp = make_response(redirect(url_for('main_page')))
                encode_cookie = model.encode_auth_token(result[0], app)
                resp.set_cookie('user_id', encode_cookie)
                return resp
            elif result[1] == "Coach":
                resp = make_response(redirect(url_for('coachMainPage')))
                encode_cookie = model.encode_auth_token(result[0], app)
                # encode_cookie = encode_cookie.encode('utf')
                resp.set_cookie('user_id', encode_cookie)
                return resp
            elif result[1] == "Admin":
                resp = make_response(redirect(url_for('admin')))
                encode_cookie = model.encode_auth_token(result[0], app)
                resp.set_cookie('user_id', encode_cookie)
                return resp
        else:
            page["bar"] = False
            flash("Incorrect username/password, please try again")
            return redirect(url_for('login_page'))
        # Remember to give a token for identification here
    else:
        return render_template('login.html', page = page)

@app.route('/forgotpassword', methods = ["GET", "POST"])
def forgotPassword_page():
    if request.method == "POST":
        username = request.form["username"]
        fname = request.form["firstname"]
        lname = request.form["lastname"]
        email = request.form["email"]
        password = request.form["newPassword"]
        check = model.reset_check(username, email, fname, lname)
        if check:
            result = model.forgotPassword(username, password)
            return redirect(url_for('login_page'))
        else:
            page["bar"] = False
            flash("Account does not exists")
            return render_template("ForgotPassword.html", page=page)
    else:
        return render_template('ForgotPassword.html', page=page)

@app.route('/register', methods = ["GET","POST"])
def register_page():
    if request.method == "POST":
        fname = request.form["firstname"]
        lname = request.form["lastname"]
        email = request.form["email"]
        username = request.form["username"]
        password = request.form["password"]
        gender = request.form["gender"]
        result = model.register_check(fname, lname, email, username, password, gender)
        if result:
            return redirect(url_for('login_page'))
        else:
            page["bar"] = False
            flash("The username already exists, please log in")
            return render_template('Register.html', page = page)
    else:
        return render_template('Register.html', page = page)

@app.route('/main')
@app.route('/main/<section>', methods = ['GET', 'POST'])
def main_page(section = None):
    now = datetime.now()
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])

    if is_login[0] and identity == "Leader":
        user_id = is_login[1]
        username = model.get_username(user_id)
        if request.method == "POST":
            if request.form["form-name"] == "progress":
                goal_id = request.form["made-progress-button"]
                model.make_progress(user_id, goal_id)

            elif request.form["form-name"] == "new_goal":
                new_goal_content = request.form["new-goal-input"]
                new_goal_times = request.form["new-times-input"]
                model.add_new_goal(user_id, new_goal_content, new_goal_times)

        goal_results = model.get_goals(user_id)
        goal_num = goal_results[0]
        goals = goal_results[1]
        total_times = goal_results[2]
        progress = goal_results[3]
        ids= goal_results[4]
        maxgoals = 3

        session_results = model.mainPage_leader_schedule(now,user_id)
        return render_template('newhome.html',
                                username=username,
                                maxgoals=maxgoals,
                                goals=goals,
                                total_times=total_times,
                                goal_num=goal_num,
                                progress=progress,
                                ids=ids,
                                sessions = session_results,
                                page = page)
    else:
        page["bar"] = False
        flash("Please Login First")
        return redirect("/login")


@app.route('/findCoach')
def findCoachPage():
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    if is_login[0] and identity == "Leader":
        user_id = is_login[1]
        username = model.get_username(user_id)
        coach_id = model.get_coachid(user_id)
        if coach_id != None:
            return redirect("/yourcoach")
        else:
            return render_template('findCoachPage.html',
                                    username = username,
                                    page = page)
    else:
        page["bar"] = False
        flash("Please Login First")
        return redirect("/login")

@app.route('/matching', methods = ['GET', 'POST'])
def matchingPage():
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    if is_login[0] and identity == "Leader":
        user_id = is_login[1]
        coach_id = model.get_coachid(user_id)
        match_status = model.check_match_application(user_id)
        if coach_id != None:
            return redirect("/yourcoach")
        elif coach_id == None and match_status == 0:
            page["bar"] = False
            flash("Your application is processing")
            return redirect("/main")

        if request.method == "POST":
            main_goal = request.form["goal"]
            industry = request.form["industry"]
            exp = request.form["exp"]
            gender = request.form["gender"]
            if gender == "None":
                gender = None
            age = request.form["age"]
            if age == "None":
                age = None
            else:
                age = int(age)
            matchSys = AIMatchingSys.matchSystem(int(main_goal), int(industry), int(exp), gender, age)
            matched_coach_id = matchSys.get_matched_coach_id()
            model.new_match_application(user_id, matched_coach_id, main_goal, industry, exp, gender, age)
            page["bar"] = True
            flash("Your application is submitted successfully")
            return redirect("/main")
        else:
            main_goals_cates = model.get_matching_goal_cate()
            industries_cates = model.get_industries_cate()
            exp_cates = model.get_experience_cate()
            age_cates = model.get_age_cate()
            age_max_cate = age_cates[-1][1].split("-")[0]
            age_cates[-1] = [age_cates[-1][0], "Above " + age_max_cate]
            return render_template('matching.html',
                                    main_goals_cates = main_goals_cates,
                                    industries_cates = industries_cates,
                                    exp_cates = exp_cates,
                                    age_cates = age_cates,
                                    page = page)
    else:
        page["bar"] = False
        flash("Please Login First")
        return redirect("/login")


@app.route('/coachMain')
@app.route('/coachMain/<section>', methods = ['GET', 'POST'])
def coachMainPage(section=None):
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    now = datetime.now()
    if is_login[0] and identity == "Coach":
        user_id = is_login[1]
        username = model.get_username(user_id)
        leaders_list = model.get_leaders_list(user_id)
        session_results = model.get_coach_schedule_event(now,user_id)
        if request.method == "POST":
            if request.form["form-name"] == "leader_choice":
                leader_id = request.form["choosed_leader"]
                leader_goal_result = model.get_goals(leader_id)
                leader_goal_result_num = leader_goal_result[0]
                leader_goal_result_goals = leader_goal_result[1]
                leader_goal_result_total_times = leader_goal_result[2]
                leader_goal_result_progresses = leader_goal_result[3]

                leader_sessions_result = model.mainPage_leader_schedule(now,leader_id)
                next_session = ["No Appointment Yet", ""]
                if leader_sessions_result:
                    next_session = leader_sessions_result[0]


                sender = user_id
                result =  model.get_chat_history(sender,leader_id)
                hassent = 0

                return render_template("coachMain.html",
                                        username=username,
                                        leaders=leaders_list,
                                        goal_num = leader_goal_result_num,
                                        goal_names = leader_goal_result_goals,
                                        goal_total_times = leader_goal_result_total_times,
                                        goal_progresses = leader_goal_result_progresses,
                                        next_session = next_session,
                                        result=result,
                                        allmsg=len(result),
                                        hassent=hassent,
                                        choosed_leader_id = leader_id,
                                        board_display = True,
                                        sessions = session_results,
                                        page = page)
            # to check if msg --------------------------------
            elif request.form["form-name"] == "message":
                 # same as above ----------------------------
                leader_id = request.form["leader_id_msg"]
                leader_goal_result = model.get_goals(leader_id)
                leader_goal_result_num = leader_goal_result[0]
                leader_goal_result_goals = leader_goal_result[1]
                leader_goal_result_total_times = leader_goal_result[2]
                leader_goal_result_progresses = leader_goal_result[3]

                leader_sessions_result = model.mainPage_leader_schedule(now,leader_id)
                next_session = ["No Appointment Yet", ""]
                if leader_sessions_result:
                    next_session = leader_sessions_result[0]
                # ----------------------------------------------

                # print(sender)
                sender = user_id

                text = request.form['message']
                hassent = 1
                model.chat_fromcoach(leader_id, text, sender)
                result =  model.get_chat_history(sender,leader_id)



                return render_template("coachMain.html",
                                        username=username,
                                        leaders=leaders_list,
                                        goal_num = leader_goal_result_num,
                                        goal_names = leader_goal_result_goals,
                                        goal_total_times = leader_goal_result_total_times,
                                        goal_progresses = leader_goal_result_progresses,
                                        next_session = next_session,
                                        result=result,
                                        allmsg=len(result),
                                        hassent=hassent,
                                        choosed_leader_id = leader_id,
                                        board_display = True,
                                        sessions = session_results,
                                        page =page)

            elif request.form["form-name"] == "add_time":
                          start = request.form["start"]
                          end = request.form["end"]
                          date = request.form["addbutton"]
                          now = datetime.now()
                          weekday = int(request.form["weekday"])
                          if weekday == 0:
                              weekday = 7
                          numdays = int(request.form["numdays"])
                          check1 = False
                          check2 = False
                          try:
                              a = request.form["check1"]
                              check1 = True
                          except werkzeug.exceptions.BadRequestKeyError as e:
                              pass
                          try:
                              a = request.form["check2"]
                              check2 = True
                          except werkzeug.exceptions.BadRequestKeyError as e:
                              pass
                          model.processing_for_add_time(user_id,start,end,date,now,weekday,numdays,check1,check2)
                          coach_time = model.get_coach_time(user_id)

                          sender = user_id
                          receiver = model.get_userid_for_msg(user_id)
                          result =  model.get_chat_history(sender,receiver)
                          hassent = 0
                          return render_template("coachMain.html",
                                                  username=username,
                                                  leaders=leaders_list,
                                                  goal_num = 0,
                                                  next_session = ["", ""],
                                                  result=result,
                                                  allmsg=len(result),
                                                  hassent=hassent,
                                                  choosed_leader_id = None,
                                                  board_display = False,
                                                  coach_time = coach_time,
                                                  date=date,
                                                  sessions = session_results,
                                                  page = page)

        else:
            sender = user_id
            receiver = model.get_userid_for_msg(user_id)
            result =  model.get_chat_history(sender,receiver)
            hassent = 0
            coach_time = model.get_coach_time(user_id)

            return render_template("coachMain.html",
                                    username=username,
                                    leaders=leaders_list,
                                    goal_num = 0,
                                    next_session = ["", ""],
                                    result=result,
                                    allmsg=len(result),
                                    hassent=hassent,
                                    choosed_leader_id = None,
                                    board_display = False,
                                    coach_time = coach_time,
                                    date = " ",
                                    sessions = session_results,
                                    page = page)
    else:
        page["bar"] = False
        flash("Please Login First")
        return redirect("/login")


@app.route('/profile')
def profilepage():
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    if is_login[0]:
        user_id = is_login[1]
        profile = model.get_profile(user_id,identity)
        if identity == "Leader":
            return render_template('profile.html', profile = profile, page = page)
        elif identity == "Coach":
            return render_template('coachprofile.html', profile = profile, page = page)
    else:
        page["bar"] = False
        flash("Please Login First")
        return redirect("/login")

@app.route('/editprofile', methods = ['GET','POST'])
def editprofile():
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    if is_login[0]:
        user_id = is_login[1]
        if request.method == 'GET':
            profile = model.get_profile(user_id,identity)

            if identity == "Leader":
                return render_template('editprofile.html', profile = profile, page=page)
            elif identity == "Coach":
                goallist = model.get_all_goals()
                industrylist = model.get_all_industries()
                return render_template('EditCoachProfile.html', profile = profile,goallist=goallist,industrylist=industrylist, page=page)
        elif request.method == 'POST':
            username = request.form["username"]
            if model.checkDuplicateName(username):
                username = model.get_username(is_login[1])
            firstname = request.form["firstname"]
            lastname = request.form["lastname"]
            age = request.form["age"]
            gender = request.form["gender"]
            email = request.form["email"]
            mobile = request.form["mobile"]
            if identity == "Leader":
                birthday = request.form["birthday"]
                location = request.form["location"]
                business = request.form["business"]
                company_name = request.form["company_name"]
                company_size = request.form["company_size"]
                model.editprofile(username,firstname,lastname,age,gender,birthday,email,location,mobile,business,company_name,company_size,user_id)
                profile = model.get_profile(user_id, identity)
                return render_template('editprofile.html', profile = profile, user_id = user_id, page=page)
            elif identity == "Coach":
                experience = request.form["experience"]
                industry = request.form["industry"]
                goal1 = request.form["goal1"]
                goal2 = request.form["goal2"]
                goal3 = request.form["goal3"]
                model.EditCoachProfile(username,firstname,lastname,age,gender,email,mobile,experience,industry,goal1,goal2,goal3,user_id)
                profile = model.get_profile(user_id,identity)
                goallist = model.get_all_goals()
                industrylist = model.get_all_industries()
                return render_template('EditCoachProfile.html', profile = profile,goallist=goallist,industrylist=industrylist)
    else:
        page["bar"] = False
        flash("Please Login First")
        return redirect('/login')



@app.route('/evidencebasedcoach')
def evidencbasedcoach():
    return render_template('evidencebasedcoach.html', page=page)
@app.route('/feedback', methods = ['GET','POST'])
def feedback():
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    if is_login[0] and identity == "Leader":
        user_id = is_login[1]
        leader = user_id
        coach = model.get_coachid(leader)
        impression = model.show_current_impression(leader,coach)[0]
        if request.method == 'POST':
            if request.form["form-name"] == "content":
                content = request.form['content']
                model.write_feedback(leader,coach,content)
            elif request.form["form-name"] == "survey":
                q1 = request.form["q1"]
                q2 = request.form["q2"]
                q3 = request.form["q3"]
                q4 = request.form["q4"]
                q5 = request.form["q5"]
                q6 = request.form["q6"]
                q7 = request.form["q7"]
                q8 = request.form["q8"]
                q9 = request.form["q9"]
                q10 = request.form["q10"]
                model.survey(leader,coach,q1,q2,q3,q4,q5,q6,q7,q8,q9,q10)

            return render_template('feedback.html',impression=impression)

        return render_template('feedback.html',impression=impression)
    elif is_login[0] and identity == "Coach":
        user_id = is_login[1]
        leader_list=model.get_leaders_list(user_id)
        feedbacks = model.get_feedback_coach(user_id)
        return render_template("feedback_coach.html",feedbacks=feedbacks,leader_list=leader_list)
    else:
        page["bar"] = False
        flash("Please Login First")
        redirect("/login")

@app.route('/assessment')
def assessment():
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    if is_login[0] and identity == "Leader":
        user_id = is_login[1]
        all_asm_info = model.get_assessment_info(user_id)
        if all_asm_info == None:
            return render_template('assessment.html',
                                    all_asm_info = None)
        coach_fname = all_asm_info[0]
        coach_lname = all_asm_info[1]
        status = all_asm_info[2]
        goal = all_asm_info[3]
        industry = all_asm_info[4]
        exp = all_asm_info[5]
        prefer_age = all_asm_info[6]
        prefer_gender = all_asm_info[7]
        return render_template('assessment.html',
                                all_asm_info = all_asm_info)
    else:
        page["bar"] = False
        flash("Please Login First")
        redirect("/login")

#chat
@app.route('/yourcoach', methods = ["GET","POST"])
def chat():
    encode_user_id = request.cookies.get("user_id")
    decode_result = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(decode_result[1])
    if decode_result[0] and identity == "Leader":
        user_id = decode_result[1]
        coach_id = model.get_coachid(user_id)
        if coach_id == None:
            return redirect("/findCoach")

        goals = model.get_coach_goals(coach_id)
        industry = model.get_coach_industry(coach_id)
        yoe = model.get_coach_yoe(coach_id)
        info = model.get_coach_info(coach_id)
        username = model.get_username(user_id)
        coachnames = model.get_coach_names(coach_id)
        if request.method == 'GET':
            sender = user_id
            receiver = model.get_coachid(user_id)

            coach_time = model.get_coach_time(model.get_coachid(user_id))
            result =  model.get_chat_history(sender,receiver)
            date=' '
            hassent = 0
            return render_template('your_coach.html',result=result,allmsg=len(result),hassent=hassent,coach_time=coach_time,date=date, page=page, goals=goals, industry=industry, yoe=yoe, info=info,coachnames=coachnames)
        if request.method == 'POST':
            if request.form["form-name"] == "message":
                sender = user_id
                receiver = model.get_coachid(user_id)
                text = request.form['message']
                date = ' '
                coach_time = model.get_coach_time(model.get_coachid(user_id))

                hassent =1
                model.chat_fromuser(receiver, text, sender)
                result =  model.get_chat_history(sender,receiver)
                return render_template('your_coach.html',result=result,allmsg=len(result),coach_time=coach_time,hassent=hassent,date=date, page=page, goals=goals, industry=industry, yoe=yoe, info=info,coachnames=coachnames)

            elif request.form["form-name"] == "pick_time":
                time = request.form['pick_button'].split(", ")
                start = time[0][1:-1]
                end = time[1][1:-1]
                date = time[2][1:-1]
                model.pick_time(user_id,start,end,date)
                coach_time = model.get_coach_time(model.get_coachid(user_id))

                sender = user_id
                receiver = model.get_coachid(user_id)
                result =  model.get_chat_history(sender,receiver)
                hassent = 0
                '''
                generate link
                '''
                coach_id = model.get_coachid(user_id)
                zoom_info = model.get_zoom_key_sec(coach_id)
                zoom_key = zoom_info[0]
                zoom_sec = zoom_info[1]

                gen=model.generateToken(zoom_key,zoom_sec)
                zoom_user_id = model.getUser_id(gen)
                meeting_detail = model.meeting_json(start,end,date,zoom_user_id)
                meeting_link = model.create_meeting(gen ,meeting_detail)

                model.update_link(meeting_link,coach_id,start,end,date)
                return render_template('your_coach.html',result=result,allmsg=len(result),coach_time=coach_time,hassent=hassent,date=date, page=page, goals=goals, industry=industry, yoe=yoe, info=info,coachnames=coachnames)

            elif request.form["form-name"] == "impressiongood":
                leader = user_id
                coach = model.get_coachid(leader)
                date = ' '
                coach_time = model.get_coach_time(model.get_coachid(user_id))

                sender = user_id
                receiver = model.get_coachid(user_id)
                result =  model.get_chat_history(sender,receiver)
                hassent = 0

                impression = "good"
                model.current_impression(leader,coach,impression)
                return render_template('your_coach.html',result=result,allmsg=len(result),coach_time=coach_time,hassent=hassent,date=date, page=page, goals=goals, industry=industry, yoe=yoe, info=info,coachnames=coachnames)
            elif request.form["form-name"] == "impressionbad":
                leader = user_id
                coach = model.get_coachid(leader)
                date = ' '
                coach_time = model.get_coach_time(model.get_coachid(user_id))

                sender = user_id
                receiver = model.get_coachid(user_id)
                result =  model.get_chat_history(sender,receiver)
                hassent = 0

                impression = "bad"
                model.current_impression(leader,coach,impression)
                return render_template('your_coach.html',result=result,allmsg=len(result),coach_time=coach_time,hassent=hassent,date=date, page=page, goals=goals, industry=industry, yoe=yoe, info=info,coachnames=coachnames)

    else:
        page["bar"] = False
        flash("Please Login First")
        return redirect("/login")

@app.route('/logout', methods = ["GET","POST"])
def logout():
    encode_user_id = request.cookies.get("user_id")
    decode_result = model.decode_auth_token(encode_user_id, app)
    if decode_result[0]:
        model.add_blacklist_token(encode_user_id)
        return redirect("/")
    else:
        return redirect("/login")


@app.route('/admin', methods=['GET','POST'])
@app.route('/admin/<section>', methods=['GET','POST'])
def admin(section = None):
    encode_user_id = request.cookies.get("user_id")
    is_login = model.decode_auth_token(encode_user_id, app)
    identity = model.get_identify(is_login[1])
    coachInfo = model.coachName()
    clientInfo = model.clientName()
    applications = model.get_match_application()
    leaderName = model.get_leader_name()
    coachName = model.get_coach_name()
    feedback = model.getFeedback()

    if is_login[0] and identity == "Admin":
        if request.method == 'POST':
            form_part = request.form["form-name"]
            if form_part == "application":

                app_leader_id = request.form["app-user-id"]
                matched_coach_id = request.form["matched-coach-id"]
                model.update_leader_coach(app_leader_id, matched_coach_id)
                model.update_status_of_application(app_leader_id)
                return redirect(url_for("admin"))
            elif form_part == "addCoach":

                username = request.form["username"]
                password = request.form["password"]
                firstname = request.form["firstname"]
                lastname = request.form["lastname"]
                email = request.form["email"]
                age = request.form["age"]
                zoomKey = request.form["zoomKey"]
                zoomSecret = request.form["zoomSecret"]
                result = model.addCoach(firstname, lastname, email, username, password, "Coach", age, zoomKey, zoomSecret)
                if result:
                    return redirect("/admin/Coach")
                else:
                    page["bar"] = False
                    flash("The username already exists")
                    return render_template('admin.html', page=page)
            elif form_part == "addLeader":

                username = request.form["username"]
                password = request.form["password"]
                firstname = request.form["firstname"]
                lastname = request.form["lastname"]
                email = request.form["email"]
                result = model.addClient(firstname, lastname, email, username, password, "Leader")
                if result:
                    return redirect("/admin/Client")
                else:
                    page["bar"] = False
                    flash("The username already exists")
                    return render_template('admin.html', page=page)
            elif form_part == "delete_coach":
                user_id = request.form["choosed_user"]
                result = model.deleteUser(user_id)
                return redirect("/admin/Coach")

            elif form_part == "delete_leader":
                user_id = request.form["choosed_user"]
                result = model.deleteUser(user_id)
                return redirect("/admin/Client")

            elif form_part == "match":
                leader_id = request.form["leader_list"]
                coach_id = request.form["coach_list"]
                if leader_id == "Leaders" and coach_id != "Coaches":
                    page["bar"] = False
                    flash("Please select valid leaders")
                    return redirect("/admin/match")


                elif coach_id == "Coaches" and leader_id != "Leaders":
                    page["bar"] = False
                    flash("Please select valid coaches")
                    return redirect("/admin/match")


                elif coach_id == "Coaches" and leader_id == "Leaders":
                    page["bar"] = False
                    flash("Please select valid users")
                    return redirect("/admin/match")

                elif coach_id == "NULL":
                    result = model.matchLeaderCoachNull(leader_id)
                    return redirect("/admin/Client")

                else:
                    result = model.matchLeaderCoach(leader_id, coach_id)
                    return redirect("/admin/Client")

            elif form_part == "approveFeedback":
                feedback_id = request.form["choosed_user"]
                result = model.approveFeedback(feedback_id)
                page["bar"] = True
                flash("Approved")
                return redirect("/admin/Feedback")



            elif form_part == "deleteFeedback":
                feedback_id = request.form["choosed_user"]
                result = model.deleteFeedback(feedback_id)
                page["bar"] = True
                flash("Deleted")
                return redirect("/admin/Feedback")




        else:
            return render_template('admin.html', coachInfo = coachInfo,
                                                 clientInfo = clientInfo,
                                                 applications = applications,
                                                 page=page,
                                                 leaderName = leaderName,
                                                 coachName = coachName,
                                                 feedback = feedback)
    page["bar"] = False
    flash("Please Login First")
    return redirect("/login")
