from shutil import copyfile
import os
import unittest
import model
import run
from flask import *
from datetime import datetime
zoom_key = "DEE-DBfnQBiAGyYlpgRxDg"
zoom_sec = "eL0cgDAWg95uO52LLpO8OjKKYLMkvG5oFFbH"
app = Flask(__name__)
app.secret_key = """U29tZWJvZHkgb25jZSB0b2xkIG1lIFRoZSB3b3JsZCBpcyBnb25uYSBy
        b2xsIG1lIEkgYWluJ3QgdGhlIHNoYXJwZXN0IHRvb2wgaW4gdGhlIHNoZWQgU2hlIHdhcyBsb29r
        aW5nIGtpbmRhIGR1bWIgV2l0aCBoZXIgZmluZ2VyIGFuZCBoZXIgdGh1bWIK"""
class TestModel(unittest.TestCase):

    def setUp(self):
        # keeping a copy of the database and using the original one for testing
        copyfile("database.db", "database_cp.db")

    # test login with correct login detail
    def test_login_checkT(self):
        self.assertTrue(model.login_check("test","test"),"the login doesn't work")

    # test login with incorrect login detail
    def test_login_checkF(self):
        self.assertFalse(model.login_check("test","test1"),"the login doesn't work")

    # test register with username that not exist in the database
    def test_register_checkT(self):
        self.assertTrue(model.register_check("abcd","abcd","abcd@email.com","abcd","abcd","Male"),"register doesn't work")

    # test register with an username that exist in the database
    def test_register_checkF(self):
        self.assertFalse(model.register_check("test","test","abcd@email.com","test","abcd","Male"),"register doesn't work")

    # test function that read feedbacks of one coach from database
#     def test_get_feedback_coach(self):
#         self.assertEquals(model.get_feedback_coach("8")[0],('test','snajdnjsa','good',3),"feedback of the coach isn't correct")

    # test the function that get the scheduled sessions of a leader from database and test if the result are sorted correctly
    def test_mainPage_leader_scheduleT(self):
        self.assertEqual(model.mainPage_leader_schedule(datetime(2021,11,3,6),"1")[0:6],[('2021/11/05', '06:00', 'meeting', None), ('2021/11/05', '07:00', 'meeting', None), ('2021/11/05', '08:00', 'meeting', None), ('2021/11/05', '09:00', 'meeting', None), ('2021/11/05', '10:00', 'meeting', None), ('2021/11/05', '11:00', 'meeting', None)],"scheduled session doesn't output successfully")

    # test if the leader has no scheduled session, will be the function return none or not
    def test_mainPage_leader_scheduleF(self):
        self.assertEqual(model.mainPage_leader_schedule(datetime(2021,11,3,6),"17"),None,"result check of get session doesn't work")

    # test the function that get the scheduled sessions of a coach from database and test if the result are sorted correctly
    def test_mainPage_coach_scheduleT(self):
        self.assertEqual(model.get_coach_schedule_event(datetime(2021,8,3,6),"2")[0:4],[('2021/10/14', '11:00', 'meeting', 'asd', 'test'), ('2021/10/14', '12:00', 'meeting', None, 'test'), ('2021/10/30', '06:00', 'meeting', None, 'test'), ('2021/10/30', '07:00', 'meeting', None, 'test')],"scheduled session doesn't output successfully")

    # test if the coach has no scheduled session, will be the function return none or not
    def test_mainPage_coach_scheduleF(self):
        self.assertEqual(model.get_coach_schedule_event(datetime(2021,8,3,6),"100"),None,"result check of get session doesn't work")

    # test if profile of a leader can be read from database successfully
    def test_get_leader(self):
        self.assertEqual(model.get_profile("1","Leader"),('test', 'test', 'test', 21, 'Male', None, 'test@test', 'Sydney', None, None, None, None),"leader's profile information incorrect")

    # test if profile of a coach can be read from database successfully
    def test_get_coach(self):
        self.assertEqual(model.get_profile("8","Coach"),('testcoach2', 'testcoach2', 'testcoach2', 1, 'Female', 'testcoach2@testcoach2', None, 2, 'Mining', 'Building an inclusive team', 'Delating workload and manage your team', 'Managing your inner critic/Imposter Syndrome'),"leader's profile information incorrect")

    # test if the function return empty list if the identity is not Leader nor Coach
    def test_get_error_identity(self):
        self.assertEqual(model.get_profile("8","admin"),[],"leader's profile information incorrect")

    #test if the function really edit the leader profile
    def test_edit_profile(self):
        model.editprofile('test', 'test', 'test', 22, 'Male', "None", 'test@test', 'Sydney', "None", "None", "None", "None","1")
        self.assertEqual(model.get_profile("1","Leader"),('test', 'test', 'test', 22, 'Male', None, 'test@test', 'Sydney', None, None, None, None),"leader's profile didn't edited correctly")

    # test if the goals of a leader can be read successfully from database
    def test_get_goal(self):
        goal_num, goals, total_times, progress, ids = model.get_goals("1")
        self.assertEqual(goal_num,3,"number of goals incorrect")
        self.assertEqual(goals,['test', 'become a social person', 'reading a book'],"name of goals incorrect")
        self.assertEqual(total_times,[12, 12, 15],"total time of goals incorrect")
        self.assertEqual(progress,[3, 0, 1],"progress of goal incorrect")
        self.assertEqual(ids,[4, 5, 6],"ids of goals incorrect")

    #test if the progress ca be increased successfully and modified to the record in the database
    def test_make_progress(self):
        model.make_progress("1","5")
        goal_num, goals, total_times, progress, ids = model.get_goals("1")
        self.assertEqual(goal_num,3,"number of goals incorrect")
        self.assertEqual(goals,['test', 'become a social person', 'reading a book'],"name of goals incorrect")
        self.assertEqual(total_times,[12, 12, 15],"total time of goals incorrect")
        self.assertEqual(progress,[3, 1, 1],"progress of goal incorrect")
        self.assertEqual(ids,[4, 5, 6],"ids of goals incorrect")

    #test if the goal is removed from database when the the progress reached 100%
    def test_make_progress(self):
        for i in range(0,12):
            model.make_progress("1","5")
        goal_num, goals, total_times, progress, ids = model.get_goals("1")
        self.assertEqual(goal_num,2,"number of goals incorrect")
        self.assertEqual(goals,['test', 'reading a book'],"name of goals incorrect")
        self.assertEqual(total_times,[12, 15],"total time of goals incorrect")
        self.assertEqual(progress,[3, 1],"progress of goal incorrect")
        self.assertEqual(ids,[4, 6],"ids of goals incorrect")

    #test if new goal can be added to database successfully
    def test_add_goal(self):
        model.add_new_goal("2","reading","13")
        goal_num, goals, total_times, progress, ids = model.get_goals("2")
        self.assertEqual(goal_num,1,"number of goals incorrect")
        self.assertEqual(goals,['reading'],"name of goals incorrect")
        self.assertEqual(total_times,[13],"total time of goals incorrect")
        self.assertEqual(progress,[0],"progress of goal incorrect")
        self.assertEqual(ids,[7],"ids of goals incorrect")

#     def test_processing_add_time(self):
#         model.processing_for_add_time("2","14:00","16:00","2022-11-1",datetime(2021,8,3,6),2,30,True,True)
#         self.assertEqual(model.get_coach_time("2")[22:-7],[('14:00', '15:00', '2022-10-31', 0, None, None), ('14:00', '15:00', '2022-11-1', 0, None, None), ('14:00', '15:00', '2022-11-15', 0, None, None), ('14:00', '15:00', '2022-11-2', 0, None, None), ('14:00', '15:00', '2022-11-22', 0, None, None), ('14:00', '15:00', '2022-11-29', 0, None, None), ('14:00', '15:00', '2022-11-3', 0, None, None), ('14:00', '15:00', '2022-11-4', 0, None, None), ('14:00', '15:00', '2022-11-5', 0, None, None), ('14:00', '15:00', '2022-11-6', 0, None, None), ('14:00', '15:00', '2022-11-8', 0, None, None), ('15:00', '16:00', '2021-10-30', 0, 'meeting', None), ('15:00', '16:00', '2022-10-31', 0, None, None), ('15:00', '16:00', '2022-11-1', 0, None, None), ('15:00', '16:00', '2022-11-15', 0, None, None), ('15:00', '16:00', '2022-11-2', 0, None, None), ('15:00', '16:00', '2022-11-22', 0, None, None), ('15:00', '16:00', '2022-11-29', 0, None, None), ('15:00', '16:00', '2022-11-3', 0, None, None), ('15:00', '16:00', '2022-11-4', 0, None, None), ('15:00', '16:00', '2022-11-5', 0, None, None), ('15:00', '16:00', '2022-11-6', 0, None, None)],"add_time doesn't work")
#     # -------------------------------------------------------------------------

    # 155 forgotPassword -252 getidentity
    # for changing pw
    def test_forgot_pw(self):
        self.assertTrue(model.forgotPassword("aaZ12j09Za", "bbb"),"can't forgot pw")
    # check user info
    def test_resetcheck(self):
        self.assertTrue(model.reset_check("test","test@test", "test","test"),"user not found")
    # get coach id from leader
    def test_getcoachid(self):
        self.assertNotEqual(model.get_coachid("1"),2,"no coach found")
    # get leader id
    def test_userid_for_msg(self):
        self.assertNotEqual(model.get_userid_for_msg("2"),1,"no leader found")
    # get chat history
    def test_get_chathistory(self):
        self.assertEqual(model.get_chat_history("1","1"),"wrong chat ! cannot chat to yourself !","no chat found")
    # get chat history
    def test_get_chathistory1(self):
        self.assertNotEqual(len(model.get_chat_history("1","2")),0,"no chat found")
    # leader send msg
    def test_chat(self):
        self.assertEqual(model.chat_fromuser("1","aaa","1"),"failed sending msg","failed sending msg")
    # leader send msg
    def test_chat_(self):
        self.assertEqual(model.chat_fromuser("1","aaa","2"),None,"failed sending msg")
    # coach send msg
    def test_chat1(self):
        self.assertEqual(model.chat_fromcoach("1","aaa","1"),"failed sending msg","failed sending msg")
    # coach send msg
    def test_chat1_(self):
        self.assertEqual(model.chat_fromcoach("1","aaa","2"),None,"failed sending msg")
    # get leaders
    def test_getleader_list(self):
        self.assertTrue(len(model.get_leaders_list("2")),"failed getting leaders")
    # get coach time
    def test_get_coachtime(self):
        self.assertTrue(len(model.get_coach_time("2")),"failed getting coach time")
    # pick time
    def test_pick_time(self):
        self.assertFalse(model.pick_time("1","a","b","c"),"failed picking time")
    # get username
    def test_get_username(self):
        self.assertEqual(model.get_username("1"),"test","no user found")
    #black list
    def test_add_blacklist(self):
        self.assertFalse(model.add_blacklist_token("a21sd"),"failed remove token")
    # check token
    def test_check_token(self):
        self.assertTrue(model.check_token("n12a11"),"wrong token")
    # get identity
    def test_get_identity(self):
        self.assertFalse(model.get_identify("dj9201"),"no user found")

    # 310 decode_auth_token to 400 addClient

    def test_decode_auth_token(self):
        #print("test endoce_auth: ",(model.encode_auth_token("2",app)))
        self.assertEqual(model.decode_auth_token(model.encode_auth_token("2",app),app)[1],"2")

    # get zoom key
    def test_get_zoom_key(self):
        self.assertEqual(model.get_zoom_key_sec(2)[0],zoom_key)
    # get zoom sec
    def test_get_zoom_sec(self):
        self.assertEqual(model.get_zoom_key_sec(2)[1],zoom_sec)
    # get incorrect generate token
    def test_generateToken(self):
        self.assertNotEqual(model.generateToken(zoom_key,zoom_sec),"123")
    # get incorrect meeting link
#     def test_create_meeting(self):
#        id = model.generateToken(zoom_key,zoom_sec)
#        self.assertNotEqual(model.create_meeting(id,model.meeting_json("14:00","15:00","2021-10-01",id)),"acds")
    #false to update
    def test_update_link(self):
        self.assertFalse(model.update_link("link","2","14:00","15:00","2021-10-01"),"false update link")
    # add time
    def test_add_time(self):
        self.assertEqual(model.add_time("2","14:00","15:00","2023-10-01"),None)

    def test_coachName(self):
        self.assertNotEqual(model.coachName(),"abc")

    def test_clientName(self):
        self.assertNotEqual(model.clientName(),"incorrect name")

    def test_deletUsers(self):
        self.assertFalse(model.deleteUser(20),"failed to delete")


    def test_addCoach(self):
        self.assertTrue(model.addCoach("harry","porter","abc@mail","testCoach1","testpw","Leader","10","key","sec"), "Correct to add")

    def test_addClient(self):
        self.assertTrue(model.addClient("harry","porter","abc@mail","testleader1","testpw","Leader"), "Correct to add")

#--------------------Lucius Part--------------------#
    def test_get_all_marching_goals(self):
        self.assertEqual((1, "Perfectionism & Procrastination"),model.get_all_goals()[0])

    def test_get_all_industries(self):
        all_industries = model.get_all_industries()
        self.assertEqual((1, "Agriculture, Forestry and Fishing"), all_industries[0])

    def test_EditCoachProfile(self):
        self.assertEqual(model.get_username(18), "test24")
        model.EditCoachProfile('test_edit', 'test_edit', 'test_edit', 10, "Male", 'test_edit@test', '', 1,1,1,1,1,18)
        self.assertEqual(model.get_username(18), "test_edit")

    def test_checkDuplicateName(self):
        self.assertTrue(model.checkDuplicateName("test"))

    def test_get_matching_goal_cate(self):
        length_matching_goals = len(model.get_matching_goal_cate())
        self.assertEqual(length_matching_goals, 10)

    def test_get_industries_cate(self):
        length_matching_industries = len(model.get_industries_cate())
        self.assertEqual(length_matching_industries, 19)

    def test_get_experience_cate(self):
        length_matching_experience = len(model.get_experience_cate())
        self.assertEqual(length_matching_experience, 7)

    def test_get_age_cate(self):
        length_matching_age = len(model.get_age_cate())
        self.assertEqual(length_matching_age, 7)

    def test_update_leader_coach(self):
        self.assertEqual(model.get_coachid(1), 8)
        model.update_leader_coach(1, 2)
        self.assertEqual(model.get_coachid(1), 2)

# use with other feedback functions
    def test_write_feedback(self):
        feedback_id = model.getFeedback()[0][0]
        self.assertEqual(feedback_id, 1)
        # model.deleteFeedback(38)
        model.write_feedback(1, 8, "test")
        feedback = model.getFeedback()[0][5]
        self.assertEqual(feedback, "test")
        # feedback_id = model.getFeedback()[0][0]
        model.approveFeedback(feedback_id)
        feedback_coach = model.get_feedback_coach(8)[0][1]
        self.assertEqual(feedback_coach, "test")


    def test_current_impression(self):
        model.current_impression(1,8,"bad")
        current_imp = model.show_current_impression(1,8)[0]
        self.assertEqual(current_imp, "bad")

    def test_survey(self):
        model.survey(1, 8, 1, 3, 2, 4, 5,1, 3, 2, 4, 5)

    def test_match_application(self):
        model.new_match_application(12, 2, 1, 1, 1, "Male", 1)
        self.assertEqual(model.check_match_application(12), 0)
        self.assertEqual(model.get_match_application()[-1][0], 64)
        model.update_status_of_application(12)
        self.assertEqual(model.check_match_application(12), 1)

    def test_no_application(self):
        self.assertEqual(model.check_match_application(123123), None)

    def test_get_assessment(self):
        goal_name = model.get_assessment_info(1)[3]
        self.assertEqual(goal_name, "Perfectionism & Procrastination")

    def test_get_coach_goals(self):
        coach_goal1 = model.get_coach_goals(2)[0]
        self.assertEqual(coach_goal1, "Self-care & Building effective work habits")

    def test_get_coach_industry(self):
        coach_industry = model.get_coach_industry(2)[0]
        self.assertEqual(coach_industry, "Increase uncertainty tolerance and resilience")

    def test_get_coach_yoe(self):
        coach_yoe = model.get_coach_yoe(2)[0]
        self.assertEqual(coach_yoe, "1-2")

    def test_get_coach_info(self):
        coach_age = model.get_coach_info(2)[0]
        self.assertEqual(coach_age, 20)

    def test_get_leader_name(self):
        leader_fname = model.get_leader_name()[0][1]
        self.assertEqual(leader_fname, "test")

    def test_get_coach_name(self):
        coach_fname = model.get_coach_name()[0][1]
        self.assertEqual(coach_fname, "testcoach")

    def test_matchLeaderCoach(self):
        model.matchLeaderCoach(1, 2)
        coach_id = model.get_coachid(1)
        self.assertEqual(coach_id, 2)

    def test_matchLeaderNone(self):
         model.matchLeaderCoach(1, None)
         coach_id = model.get_coachid(1)
         self.assertEqual(coach_id, None)

    def test_approveFeedback(self):
         feedback_id = model.getFeedback()[0][0]
         model.approveFeedback(feedback_id)
         feedback_status = model.getFeedback()[0][8]
         self.assertEqual(feedback_status, 1)


    # This case run after all testcases finished
    def tearDown(self):
        # remove the modified database and replace it with the copy after testing
        os.remove("database.db")
        os.rename("database_cp.db","database.db")

if __name__ == '__main__':

    unittest.main()
