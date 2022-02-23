package view;

import database.Database;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.MerchantFee;
import model.MerchantFeeImpl;
import model.auth.Auth;
import model.conversion.Converse;
import model.detail.Detail;
import report.Report;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuiImpl implements Gui {

    private Scene scene;
    private Stage primaryStage;
    private Text loading;
    private Auth auth;
    private Converse con;
    private Detail detail;
    private Report report;
    private Database db;
    private MerchantFee mf = new MerchantFeeImpl();
    private String fee;


    public GuiImpl(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    @Override
    public void basicPage() {
        Pane pane = new Pane();
        VBox vbox = new VBox();
        vbox.setSpacing(130);
        vbox.getChildren().add(new Separator(Orientation.VERTICAL));


        // for log in ******************************************************
        Label login = new Label("User's Api Key: ");
        TextField textField = new TextField();
        textField.setText("Api Key");
        HBox loginText = new HBox();
        Button logButton = new Button();
        logButton.setText("login");
        logButton.setOnAction((event) -> {

            String apiKey = textField.getText();

            // correct log in
            Text done = new Text(50, 80, "login successfully!");
            done.setFill(Paint.valueOf("RED"));
            done.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            done.setLayoutX(350);
            done.setLayoutY(200);

            // wrong log in
            Text notdone = new Text(50, 80, "login failed! Wrong Api Key");
            notdone.setFill(Paint.valueOf("RED"));
            notdone.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            notdone.setLayoutX(250);
            notdone.setLayoutY(200);
            Load(pane);
            // login process
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
                if (auth.login(apiKey)) {
                    removeText(pane, done);
                    nextPage(pane);

                } else {
                    removeText(pane, notdone);
                }
            });
            new Thread(sleeper).start();


        });
        loginText.getChildren().add(new Separator(Orientation.HORIZONTAL));
        loginText.getChildren().addAll(login, textField, logButton);
        loginText.setSpacing(20);
        // *****************************************************************
        // for extension ***********************
        Label mfee = new Label("Merchant Fee: ");
        TextField textField1 = new TextField();
        textField1.setText("0 to 99");
        HBox fee = new HBox();
        Button feeButton = new Button();
        feeButton.setText("check");
        feeButton.setOnAction((event) -> {

            String newfee = textField1.getText();

            // correctly input
            Text done = new Text(50, 80, "set!");
            done.setFill(Paint.valueOf("RED"));
            done.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            done.setLayoutX(350);
            done.setLayoutY(300);

            // wrongly input
            Text notdone = new Text(50, 80, "must be integer and 0 to 99!");
            notdone.setFill(Paint.valueOf("RED"));
            notdone.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            notdone.setLayoutX(280);
            notdone.setLayoutY(300);
            Load(pane);
            // check process
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
                if (mf.checkRange(newfee)) {
                    removeText(pane, done);
                    this.fee = newfee;
//                    System.out.println(this.fee);
                } else {
                    removeText(pane, notdone);
                }
            });
            new Thread(sleeper).start();

        });
        fee.getChildren().add(new Separator(Orientation.HORIZONTAL));
        fee.getChildren().addAll(mfee, textField1, feeButton);
        fee.setSpacing(20);

        // combine each column
        vbox.getChildren().addAll(loginText,fee);
        pane.getChildren().addAll(vbox);

        scene = new Scene(pane);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
    }

    @Override
    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    @Override
    public void setConverse(Converse con) {
        this.con = con;
    }

    @Override
    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    @Override
    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }

    // show loading text *****************************************************
    private void Load(Pane pane) {
        // loading
        loading = new Text(50, 80, "loading...");
        loading.setFill(Paint.valueOf("RED"));
        loading.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
        loading.setLayoutX(450);
        loading.setLayoutY(200);
        pane.getChildren().add(loading);
    }

    // for showing text after loading *****************************************************
    // 4 seconds of displaying
    private void removeText(Pane pane, Text text) {
        // remove showed text
        pane.getChildren().remove(loading);
        pane.getChildren().add(text);
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(xxx -> {
            pane.getChildren().remove(text);
        });
        new Thread(sleeper).start();
    }

    // from login page to next page button *****************************************************
    private void nextPage(Pane pane){
        Button next = new Button();
        next.setText("next");
        next.setOnAction((event) -> {
            mainPage();
        });
        next.setLayoutY(20);
        next.setLayoutX(700);
        next.setStyle("-fx-background-color: \n" +
                "        #707070,\n" +
                "        linear-gradient(#fafdfe, #e8f5fc),\n" +
                "        linear-gradient(#eaf6fd 0%, #d9f0fc 49%, #bee6fd 50%, #a7d9f5 100%);\n" +
                "    -fx-background-insets: 0,1,2;\n" +
                "    -fx-background-radius: 3,2,1;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 14px;");
        pane.getChildren().add(next);

    }
    // from other page to main page button *****************************************************
    private void back(Pane pane){
        Button back = new Button();
        back.setText("back");
        back.setOnAction((event) -> {
            mainPage();
        });
        back.setLayoutY(20);
        back.setLayoutX(700);
        back.setStyle("-fx-background-color: \n" +
                "        #707070,\n" +
                "        linear-gradient(#fafdfe, #e8f5fc),\n" +
                "        linear-gradient(#eaf6fd 0%, #d9f0fc 49%, #bee6fd 50%, #a7d9f5 100%);\n" +
                "    -fx-background-insets: 0,1,2;\n" +
                "    -fx-background-radius: 3,2,1;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 14px;");
        pane.getChildren().add(back);

    }
    // get email destination
    private String emailDestination(){
        // get the emails credentials
        List<String> temp = new ArrayList<>();
        try {
            File file = new File("src/main/resources/output.txt");
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                temp.add(data);
            }
            scan.close();
        } catch (FileNotFoundException ignored) {
        }
        return temp.get(2).split(" ")[1];
    }
    // for email *****************************************************
    private void email(Pane pane,String x){
        Button email = new Button();
        email.setText("send email");
        email.setOnAction((event) -> {
            Load(pane);
            // send email
            Text done;
            if(!report.checkDummy()){
                done = new Text(50, 80, "Send successfully to the email: "+emailDestination());
                done.setFill(Paint.valueOf("RED"));
                done.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
                done.setLayoutX(200);
                done.setLayoutY(200);

            }else{
                done = new Text(50, 80, "Send successfully to the email: dummy@dummy.com");
                done.setFill(Paint.valueOf("RED"));
                done.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
                done.setLayoutX(250);
                done.setLayoutY(200);
            }

            // wrong log in
            Text notdone = new Text(50, 80, "Send failed!");
            notdone.setFill(Paint.valueOf("RED"));
            notdone.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            notdone.setLayoutX(250);
            notdone.setLayoutY(200);

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
                if (report.send(x)) {
                    removeText(pane, done);

                } else {
                    removeText(pane, notdone);
                }
            });
            new Thread(sleeper).start();

        });
        email.setLayoutY(480);
        email.setLayoutX(665);
        email.setStyle("-fx-background-color: \n" +
                "        #707070,\n" +
                "        linear-gradient(#fafdfe, #e8f5fc),\n" +
                "        linear-gradient(#eaf6fd 0%, #d9f0fc 49%, #bee6fd 50%, #a7d9f5 100%);\n" +
                "    -fx-background-insets: 0,1,2;\n" +
                "    -fx-background-radius: 3,2,1;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 14px;");
        pane.getChildren().add(email);

    }
    // for the functionality ********************************************
    private void mainPage(){
        Pane info = new Pane();
        VBox vbox = new VBox();
        vbox.setSpacing(80);
        vbox.getChildren().add(new Separator(Orientation.VERTICAL));

        clearDatabase(info);

        // list of cryptocurrencies
        HBox hbox = new HBox();
        hbox.getChildren().add(new Separator(Orientation.HORIZONTAL));
        hbox.setSpacing(10);
        Label l = new Label("List of Cryptocurrencies:");
        TextField textField = new TextField ();
        textField.setText("start (in number)");
        TextField textField1 = new TextField ();
        textField1.setText("limit (in number)");
        TextField textField2 = new TextField ();
        textField2.setText("sort (id / cmc_rank)");
        Button but = new Button();
        but.setTranslateX(5);
        but.setText("submit");
        but.setOnAction((event) -> {
            Load(info);
            // loading to see the result
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
                // get list
                String start = textField.getText();
                String limit = textField1.getText();
                String sort = textField2.getText();

                String output = detail.getAllInfo(start,limit,sort);
                displayList(output);


            });
            new Thread(sleeper).start();

        });
        hbox.getChildren().addAll(l,textField,textField1,textField2,but);
        // Detail of crypto. ************************************************************************
        HBox hbox1 = new HBox();
        hbox1.getChildren().add(new Separator(Orientation.HORIZONTAL));
        hbox1.setSpacing(10);
        Label ll = new Label("Cryptocurrency detail:");
        // symbol must be capital
        TextField textField3 = new TextField ();
        textField3.setText("id / slug / symbol");
        Button but1 = new Button();
        but1.setTranslateX(5);
        but1.setText("submit");
        but1.setOnAction((event) -> {
            Load(info);
            // loading to see the result
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
                // get detail

                String input = textField3.getText();

                displayOne(input);


            });
            new Thread(sleeper).start();

        });
        hbox1.getChildren().addAll(ll,textField3,but1);
        // Conversion ************************************************************************
        HBox hbox2 = new HBox();
        hbox2.getChildren().add(new Separator(Orientation.HORIZONTAL));
        hbox2.setSpacing(10);
        Label lll = new Label("Conversion (id):");
        // id / symbol must match id / symbol
        // symbol must be capital
        TextField textField4 = new TextField ();
        textField4.setText("id / symbol");
        TextField textField5 = new TextField ();
        textField5.setText("id / symbol");
        TextField textField6 = new TextField ();
        textField6.setText("amount (in number)");

        Button but2 = new Button();
        but2.setTranslateX(5);
        but2.setText("submit");
        but2.setOnAction((event) -> {
            Load(info);
            // loading to see the result
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
                // conversion

                String input = textField4.getText();
                String input1 = textField5.getText();
                String input2 = textField6.getText();
                conversion(input,input1,input2);


            });
            new Thread(sleeper).start();

        });
        hbox2.getChildren().addAll(lll,textField4,textField5,textField6,but2);


        // combine everything tgt
        vbox.getChildren().addAll(hbox,hbox1,hbox2);
        info.getChildren().add(vbox);
        scene = new Scene(info);

        primaryStage.setTitle("Information of Each Section");
        primaryStage.setScene(scene);
    }

    // for display list of cryptocurrencies
    private void displayList(String x){

        Pane pane = new Pane();
        back(pane);
        email(pane,x);
        database(pane,"list",x);

        TextArea textArea = new TextArea();
        textArea.setTranslateX(150);
        textArea.setTranslateY(100);
        textArea.setText(x);

        pane.getChildren().addAll(textArea);

        scene = new Scene(pane);
        primaryStage.setTitle("Information of List of Cryptocurrencies");
        primaryStage.setScene(scene);

    }
    // for detail of cryptocurrency
    private void displayOne(String x){

        String temp ;
        // check for id / slug / symbol
        try {
            Integer.parseInt(x);
            temp = detail.getInfoID(x);
        }catch (NumberFormatException e){
            if(checkCapitals(x)){
                temp = detail.getInfoSymbol(x);
            }else{
                temp = detail.getInfoSlug(x);
            }
        }

        Pane pane = new Pane();
        back(pane);
        email(pane,temp);
        database(pane,"one",temp);

        TextArea textArea = new TextArea();
        textArea.setTranslateX(150);
        textArea.setTranslateY(100);
        textArea.setText(temp);

        pane.getChildren().addAll(textArea);

        scene = new Scene(pane);
        primaryStage.setTitle("Information of Cryptocurrency");
        primaryStage.setScene(scene);

    }

    // check for capitals
    private static boolean checkCapitals(String x){

        char[] list = x.toCharArray();
        for (char c : list) {
            if (!Character.isUpperCase(c))
                return false;
        }
        return true;
    }

    // for conversion
    private void conversion(String base, String convert, String amount){

        String temp ;
        // check for id / symbol for base and convert
        try {
            Integer.parseInt(base);
            Integer.parseInt(convert);
            temp = con.converseID(base,convert,amount,this.fee);
        }catch (NumberFormatException e){
            temp = con.converseSymbol(base,convert,amount,this.fee);
        }
        Pane pane = new Pane();
        back(pane);
        email(pane,temp);
        database(pane,"converse",temp);

        TextArea textArea = new TextArea();
        textArea.setTranslateX(150);
        textArea.setTranslateY(100);
        textArea.setText(temp);

        pane.getChildren().addAll(textArea);

        scene = new Scene(pane);
        primaryStage.setTitle("Information of Conversion");
        primaryStage.setScene(scene);

    }
    // database *****************************************************
    private void database(Pane pane,String category,String data){


        // load data from database
        Button load = new Button();
        load.setText("load from database");
        load.setOnAction((event) -> {
            // correct loading
            Text done = new Text(50, 80, "done");
            done.setFill(Paint.valueOf("RED"));
            done.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            done.setLayoutX(350);
            done.setLayoutY(200);

            Load(pane);
            // login process
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

                loadDatabase(category);
            });
            new Thread(sleeper).start();
        });
        load.setLayoutY(20);
        load.setLayoutX(20);

        pane.getChildren().add(load);

        // insert data into database *************************************************
        Button set = new Button();
        set.setText("insert into database");
        set.setOnAction((event) -> {
            // correct inserting
            Text done = new Text(50, 80, "insert successfully");
            done.setFill(Paint.valueOf("RED"));
            done.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            done.setLayoutX(350);
            done.setLayoutY(200);

            // wrong inserting
            Text notdone = new Text(50, 80, "insert failed");
            notdone.setFill(Paint.valueOf("RED"));
            notdone.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            notdone.setLayoutX(250);
            notdone.setLayoutY(200);

            Load(pane);
            // login process
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

                if(db.insertData(category,data)){
                    removeText(pane, done);
                }else{
                    removeText(pane, notdone);
                }
            });
            new Thread(sleeper).start();
        });
        set.setLayoutY(60);
        set.setLayoutX(20);

        pane.getChildren().add(set);

    }

    // load from database
    private void loadDatabase(String category){
        Pane pane = new Pane();
        back(pane);
        email(pane,db.getDetail(category));

        TextArea textArea = new TextArea();
        textArea.setTranslateX(150);
        textArea.setTranslateY(100);
        textArea.setText(db.getDetail(category));

        pane.getChildren().addAll(textArea);

        scene = new Scene(pane);
        primaryStage.setTitle("Information of Data in the Database");
        primaryStage.setScene(scene);
    }

    //clear database
    private void clearDatabase(Pane pane){
        Button back = new Button();
        back.setText("Clear Database");
        back.setOnAction((event) -> {
            // cleared database
            Text done = new Text(50, 80, "database cleared!");
            done.setFill(Paint.valueOf("RED"));
            done.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
            done.setLayoutX(460);
            done.setLayoutY(200);

            Load(pane);
            // login process
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

                db.clear();
                removeText(pane,done);
            });
            new Thread(sleeper).start();
        });

        back.setLayoutY(520);
        back.setLayoutX(20);
        back.setStyle("-fx-background-color: \n" +
                "        #707070,\n" +
                "        linear-gradient(#fafdfe, #e8f5fc),\n" +
                "        linear-gradient(#eaf6fd 0%, #d9f0fc 49%, #bee6fd 50%, #a7d9f5 100%);\n" +
                "    -fx-background-insets: 0,1,2;\n" +
                "    -fx-background-radius: 3,2,1;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 14px;");
        pane.getChildren().add(back);
    }




}
