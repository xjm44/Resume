import requests
import time
import getpass
import selenium
import sys
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service

s = Service("webdriver/chromedriver")
default_target = "http://43.129.230.213/"
driver = webdriver.Chrome(service=s)

def register(target, firstname, lastname, email, username, password, gender):
    driver.get(target)
    try:
        driver.find_element(By.LINK_TEXT, "Login").click()
        time.sleep(1)
        driver.find_element(By.LINK_TEXT, "Create one").click()

        firstname_field = driver.find_element(By.NAME, "firstname")
        firstname_field.clear()
        firstname_field.send_keys(firstname)

        lastname_field = driver.find_element(By.NAME, "lastname")
        lastname_field.clear()
        lastname_field.send_keys(lastname)

        email_field = driver.find_element(By.NAME, "email")
        email_field.clear()
        email_field.send_keys(email)

        username_field = driver.find_element(By.NAME, "username")
        username_field.clear()
        username_field.send_keys(username)

        password_field = driver.find_element(By.NAME, "password")
        password_field.clear()
        password_field.send_keys(password)

        password_confirm_field = driver.find_element(By.NAME, "confirmpassword")
        password_confirm_field.clear()
        password_confirm_field.send_keys(password)

        gender_field = driver.find_element(By.CSS_SELECTOR, "input#{genders}".format(genders = gender)).click()

        driver.find_element(By.XPATH, "//input[@type='submit']").click()
        time.sleep(1)

        print("Registration Success")
        return True
    except Exception as e:
        print("Registration failed")
        print(e)
        return False



def login(target, username, password):
    driver.get(target)
    try:
        driver.find_element(By.LINK_TEXT, "Login").click()

        username_field = driver.find_element(By.NAME, "username")
        username_field.clear()
        username_field.send_keys(username)

        password_field = driver.find_element(By.NAME, "password")
        password_field.clear()
        password_field.send_keys(password)

        driver.find_element(By.XPATH, "//input[@type='submit']").click()

        print("Login Success")
        return True
    except Exception as e:
        print("Login Failed")
        print(e)
        return False

def mainPage(target, username, password):
    driver.get(target)
    try:
        login(target, username, password)
        driver.find_element(By.LINK_TEXT, "Logout")
        print("mainPage Success")
        return True

    except Exception as e:
        print("mainPage Failed")
        print(e)
        return False
    driver.get(target)
    

def yourCoach(target, username, password):
    driver.get(target)
    try:
        login(target, username, password)
        driver.find_element(By.LINK_TEXT, "YourCoach").click()

        print("yourCoach Success")
        return True

    except Exception as e:
        print("yourCoach Failed")
        print(e)
        return False

def feedback(target, username, password):
    driver.get(target)
    try:
        login(target, username, password)
        driver.find_element(By.LINK_TEXT, "Feedback").click()
        print("Feedback Success")
        return True

    except Exception as e:
        print("Feedback Failed")
        print(e)
        return False

def profile(target, username, password, firstname, age, birthday, lastname, email, phone, location, bphone, comName, comSize):
    driver.get(target)
    try:
        login(target, username, password)
        driver.find_element(By.LINK_TEXT, "Profile").click()
        driver.find_element(By.CLASS_NAME, "thebutton").click()

        firstname_field = driver.find_element(By.NAME, "username")
        firstname_field.clear()
        firstname_field.send_keys(username)

        firstname_field = driver.find_element(By.NAME, "firstname")
        firstname_field.clear()
        firstname_field.send_keys(firstname)

        age_field = driver.find_element(By.NAME, "age")
        age_field.clear()
        age_field.send_keys(age)

        birthday_field = driver.find_element(By.NAME, "birthday")
        birthday_field.clear()
        birthday_field.send_keys(birthday)

        firstname_field = driver.find_element(By.NAME, "firstname")
        firstname_field.clear()
        firstname_field.send_keys(firstname)

        lastname_field = driver.find_element(By.NAME, "lastname")
        lastname_field.clear()
        lastname_field.send_keys(lastname)

        driver.find_element(By.XPATH, "//select[@name='gender']/option[text()=' Female ']")

        email_field = driver.find_element(By.NAME, "email")
        email_field.clear()
        email_field.send_keys(email)

        phone_field = driver.find_element(By.NAME, "mobile")
        phone_field.clear()
        phone_field.send_keys(phone)
        
        location_field = driver.find_element(By.NAME, "location")
        location_field.clear()
        location_field.send_keys(location)

        bphone_field = driver.find_element(By.NAME, "business")
        bphone_field.clear()
        bphone_field.send_keys(bphone)

        comName_field = driver.find_element(By.NAME, "company_name")
        comName_field.clear()
        comName_field.send_keys(comName)

        comSize_field = driver.find_element(By.NAME, "company_size")
        comSize_field.clear()
        comSize_field.send_keys(comSize)

        # key_field = driver.find_element(By.NAME, "jwt_key")
        # key_field.clear()
        # key_field.send_keys(key)

        # sec_field = driver.find_element(By.NAME, "jwt_sec")
        # sec_field.clear()
        # sec_field.send_keys(sec)

        driver.find_element(By.XPATH, "//input[@type='submit']").click()

        print("Profile Success")
        return True

    except Exception as e:
        print("Profile Failed")
        print(e)
        return False

def assessment(target, username, password):
    driver.get(target)
    try:
        login(target, username, password)
        driver.find_element(By.LINK_TEXT, "Assessment").click()
        print("Assessment Success")
        return True

    except Exception as e:
        print("Assessment Failed")
        print(e)
        return False

def forgotPassword(target, firstname, lastname, email, username, password):
    driver.get(target)
    try:
        driver.find_element(By.LINK_TEXT, "Login").click()
        time.sleep(1)
        driver.find_element(By.LINK_TEXT, "Forgot password?").click()

        firstname_field = driver.find_element(By.NAME, "firstname")
        firstname_field.clear()
        firstname_field.send_keys(firstname)

        lastname_field = driver.find_element(By.NAME, "lastname")
        lastname_field.clear()
        lastname_field.send_keys(lastname)

        email_field = driver.find_element(By.NAME, "email")
        email_field.clear()
        email_field.send_keys(email)

        username_field = driver.find_element(By.NAME, "username")
        username_field.clear()
        username_field.send_keys(username)

        password_field = driver.find_element(By.NAME, "newPassword")
        password_field.clear()
        password_field.send_keys(password)

        password_confirm_field = driver.find_element(By.NAME, "confirm")
        password_confirm_field.clear()
        password_confirm_field.send_keys(password)

        driver.find_element(By.XPATH, "//input[@type='submit']").click()
        time.sleep(1)

        print("ForgotPassword Success")
        return True
    except Exception as e:
        print("ForgotPassword failed")
        print(e)
        return False


def main(target):

    # register(default_target, "virtual_user1", "virtual_user1", "virtual_user@123.com", "virtual_user@123.com", "123456", "male")
    # login(default_target, "test", "test")
    # profile(default_target, "test", "test", "TEST", "20", "07-08-1980", "TEST", "123@3333.com", "212312312", "NSW", "123123123", "Uii", "20")
    # mainPage(default_target, "test", "test")
    # forgotPassword(default_target, "test", "test", "test@test", "test", "test")
    

if __name__ == "__main__":
    main(default_target)
