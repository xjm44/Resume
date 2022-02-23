SOFT3202 Final

Credit

Configuration files:
 - under src/main/resources:
    - output.txt:
	- second line: user enters the (domain) email
	- third line : destination email. (To be sent to)
	- fourth line: API key (for the output)
    - in this text file, my code only reads lines from 2nd to 4th line.
    - a space is needed between "from:", "to:", and "token:" with the corresponding	input.


Quirks/Instructions to use the application:

 - First Step:

	- User needs to -> gradle run --args="x y"
		- x and y must be replaced with "online" or "offline", must be in lower cases.
		- any invalid input will exit with status 99.

 - When application starts:

    - Any buttons to be pressed, a "loading..." text will be shown, and followed by a result text.
	- For example, from "loading..." to "send successfully to email: dummy@dummy.com".
	- For preventing the overlapping of texts, user is recommended to wait for the text to disappear before continuing the next step.

    - A login page will be shown to user:
	- API key is needed for online input
	- API key is not needed for offline input
	- If a user can be logged in successfully, with correct key (for online input), a "next" button will be shown at top right corner. 
	- Then, the user can click the next button to go to the next page.
----(final extension)----
	- user is needed to enter a merchant fee and check before pressing the "next" button.
		- if user did not do so, a text, talks about user has forgotten to input the merchant fee, will display for the conversion between 2 cryptocurrencies
	- the merchant fee must be integer and between 0 to 99.
		- any invalid input will lead to a display of text, something like an error message.
	- if user wish to change the merchant fee after pressing "next" button, the user needs to exit the application and restart.
--------------------------

    - Second Page (Main Page)
	- Three sections can be performed here: displaying a list of cryptocurrencies, detail of a cryptocurrency, conversion between 2 currencies.
	- For displaying a list of cryptocurrencies:
		- I did not display a list initially because the list would be super long, which I feel that if a user set a starting and a limit, with a sort, would be more reasonable and readability will increase.
		- 3 textfields are needed: a starting point, a limit (no. of cryptocurrencies to be shown), a sort (with id or cmc_rank).
		- For the first two textfields, only digits are allowed. For the last textfield, only "id" or "cmc_rank" is allowed.
		- any other inputs will result an output "invalid input", to be shown.
		- eg. 1, 5, id
	- For cryptocurrency detail:
		- user can choose to see the detail of a cryptocurrency with regards to its id, slug or symbol.
		- for id: user can simply input a digit. Eg. 1
		- for slug: user can simply input a slug. Eg. bitcoin
		- for symbol: user can simply input a symbol. Eg. BTC
		- take note, for the symbol, capital letter is required.
		- any other inputs will result an output "invalid input", to be shown.
	- For conversion:
		- 3 textfields are needed, base, to be compared, and amount.
		- For the first two textfields, id or symbol is needed. However, they must be synchronized, such as id can only compared with id.
		- For the last textfield, the amount needs to be entered with a digit.
		- eg. 1, 2, 10 
		- eg. BTC, VTA, 10
		- any other inputs will result an output "invalid input", to be shown.
		- no useful information (conversion price) will be shown if one of the currencies is not active.
	- At bottom left corner, a button, "Clear Database", is used to clear the database.
		- The database is used to stored information about these three sections separately. 
		- The database is empty initially.

    - Third Page (Each Section)
	- At top right corner, a "back" button is used to go back to the main page.
	- At bottom right corner, a "send email" button is used to send the data shown in the text box as an email.
		- user may need to press again if failed to receive the email (due to internet connection), which will very very unlikely to happen.
	- At top left corner, two buttons, "load from database" and "insert into database" are shown.
		- "load from database" is used to see the data, that was inserted before, with respect to each section (of each data).
		- "insert into database" is used to insert the data, in the text box, to the database, with respect to each section.
			- if a user fails to insert, a change to any of the inputs, such as id to cmc_rank or a different limit number, would help.
	- when "load from database" is pressed, the user will be directed to a new page. (To see the data in the database)
		- In the new page, a text box will show the data, that was stored before, with respect to each section.
		- At top right corner, a "back" button is used to go back to the main page.
		- At bottom right corner, a "send email" button is used to send the data shown in the text box as an email.
	- When a user wants to compare/check a specific data, he could stored the data into database first (may clear the database if needed).
	- Then, call the same piece of data from api a while later. 
	- Lastly, the result can be compared with the one in the database, when "load from database" is pressed.
	- Since the database will not automatically refresh itself, user can always clear the database before storing and comparing (each section respectively), for easier readability.


Codes I used from other sources:

 - For doing database, it was adapted from https://github.com/xerial/sqlite-jdbc
 - For using "sleep" function, I searched through the web and result into this code:
           Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                    return null;
                }
            };
            sleeper.setOnSucceeded(xxx -> {
                perform_my_action_here()
            });
            new Thread(sleeper).start();
     - Whenever I used Thread.sleep(), the whole application freezes, which I feel not comfortable.
     - However, I forgot where did I get this code, because I opened plenty of pages when I did the searching, maybe it is from Stack Overflow or GitHub.


Red-Green-Refactor:

- First One:
	- Red: 3e002a782982caec1904b93c163e0e8f7f7dcfb9
	- Green: b02964167f0e4f3a9f49b29683d28fe614e55173
   - In this:
	- I created Auth class and Crypto class, in the model package.
	- I mocked their methods.
		- In the ModelMock.java
	- In addition, I created simple frameworks of controller, view.
	- I mocked methods in the controller.
		- In the ControllerMock.java (which will be removed in the later stages)
	- I solved/implemented correct methods to the Auth class and Crypto class.
		- In the ModelTest.java
	- I did not solved/implemented correct methods to the controller, which is the Engine class.
	- I refactored the Engine class, and evidence is in b0efa41f68980df4646bad80a712f6e02bde62ec.
		- This is because I realized the methods used initially is not useful.
		- I can connect view and model via the constructor in the Engine class.
- Second One:
	- Red: 6083d246980337d4896c9740ea95c49c8c7c4f83
	- Green: b7cc61090e398fdfa0a6715e00b4da2562b4255c
    - In this:
	- I created Detail class and Converse class, in the model package.
	- I mocked their methods.
		- In the ModelMock.java
	- I solved/implemented correct methods to them.
		- In the ModelTest.java
	- In this, I also refactored in the way of combining the Crypto and Detail classes. This is because the Crypto class is used to retrieve data from api, which Detail class is used to output the result in a reasonable and readable strings.

- Third One:
	- Red: 59a0b8232af1e191161caa04ca533abd244d2f4c 
	- Green: 60e8d35ebad7741ad61a8fb5966c1ec002390de5
    - In this:
	- I created dummies in the model package, for all the classes in there.
	- I did not create dummy version for Crypto class because the output is according to the Detail class.
	- I tested the dummy classes.
		- In the ModelDummyTest.java

- Fourth One:
	- Red: b5a7ff5229e44f644bf47775ef95051e94bec5a4
	- Green: cd7a587a321ef8b1df202738c6cc4e2be7590ea7
    - In this:
	- I created Report class, for the output part.
	- I mocked the methods.
		- In the ReportMock.java
	- I solved/implemented correct methods to it.
		- In the ReportTest.java

- Fifth One:
	- Red: ccd0af87eff9b8b4b9bce039ef623b082a721742
	- Green: 3ad9cdf5312f58e9ef1942bff2e3937200b255fa
    - In this:
	- I created Database class and mocked it.
		- In the DatabaseMock.java
	- I refactored this in a way to utilize it in the controller. Such that in the Engine class, the GUI class, from the view, is able to link with the database.
		- In the constructor of the EngineImpl.java

- Sixth One:
	- Red: 8d11b3c29e1570f39d87e0c890f1b93045b21257
	- Green: 090519ab371da08c062e59d8dba2aade0a569974
    - In this:
	- I created dummy version of the Report class, for the output part.
	- I tested them.
		- In the ReportDummyTest.java
	- I refactored it, as a whole Report class, as an evident in 28dd26918f643c142189d671afda51ebec6a56c4.
		- This is because I add a new method in the Report interface, which is to check if the output is online or offline, so that a correct text can be shown to user when he pressed "send email" in the application.
		- Testing on the new method is done in 28dd26918f643c142189d671afda51ebec6a56c4.

----(final extension)----
- Final First One:
	- Red: 9059fe2c0a2c224570ba56b801031f8206d68425
	- Green: ed1a72d228d1925c27a51287c0174b898a38ca80
    - In this:
	- I created a class, MerchantFee.java, to calculate the new value for the conversion.
	- I mocked the method, currency(String fee, String value), there.
		- In the MerchantFeeMock.java
	- I implemented the method, currency(String fee, String value), in MerchantFeeImpl.java, and tested it.
		- In the MerchantFeeTest.java

- Final Second One:
	- Red: ffaf80ce3bd5608d7e110364202d42327301edb3
	- Green: 339c9d17cd92533bc1b1e27d1a1ba3f0949b851b
    - In this:
	- I create a new method in MerchantFee.java, checkRange(String fee), used for checking if the input for merchant fee has errors.
	- I mocked this method in MerchantFeeMock.java.
	- I implemented this method in MerchantFeeImpl.java and tested it in MerchantFeeTest.java.
	- This one can be seen as a refactoring from the Final First One.
		- This is because I split the multiple work from one method into two.
- Final Third One:
	- Red: bb6f1bd962fe20f6c73109b99692d4acae53f6b1
	- Green: 8b7da2bc4a619a9d21fd40a608266fc067ebd9be
    - In this:
	- I created a class, Rate.java, with a method, rate(String fee), to provide a calculation algorithm for the MerchantFee class.
	- I mocked this method in RateMock.java
	- I implemented this method in RateImpl.java and tested the method in RateTest.java.
	- This one can be seen as a refactoring from the Final Second one.
		- I feel that a separation of the calculation algorithm from the method, currency(String fee, String value), in MerchantFeeImpl.java, would be a better choice.
		- In future, whenever a developer wants to change (1-(fee/100)) to something else, it would be much efficient to just edit in the RateImpl.java.
		
- Final Fourth One:
	- Refactor From: 65ce4228ab70e27ae0be179793340735c8bdccc9 , To: f8a62d9754dadf7fa815dab714ede452885a3050
    - In this:
	- I instantiated a MerchantFee object in the existing class, ConverseModule.java, which is used for the conversion between two cryptocurrencies.
	- There are two methods there, used for conversion via id and symbol.
	- Hence, for the interface Converse.java, which ConverseModule.java implements, I have added a new parameter to the two existing methods.
		- The new parameter serves as the merchant fee to the initial conversion value.
		- (The new parameter is came from the VIEW and Josh said only MODEl should be discussed here on ed, so I am not going to elaborate any further.)
		- The initial conversion value is edited via the method, currency(String fee, String value), from the MerchantFee object.
	- I edited the previous tests about Converse objects, in the ModelTest.java, ModelMock.java and ModelDummyTest.java, by adding the new parameter.





