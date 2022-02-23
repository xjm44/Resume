package controller;

import database.Database;
import javafx.stage.Stage;
import model.auth.Auth;
import model.auth.AuthDummy;
import model.auth.AuthModule;
import model.conversion.ConverseDummy;
import model.conversion.ConverseModule;
import model.detail.DetailDummy;
import model.detail.DetailModule;
import report.ReportDummy;
import report.ReportImpl;
import view.Gui;
import view.GuiImpl;

public class EngineImpl implements Engine {
    private Gui gui;

    public EngineImpl(Stage primaryStage, String version, String output){
        gui = new GuiImpl(primaryStage);
        Database db = new Database();
        gui.setDatabase(db);

        // check input version
        if (version.equals("online")) {
            Auth auth = new AuthModule();
            gui.setAuth(auth);
            gui.setConverse(new ConverseModule(auth));
            gui.setDetail(new DetailModule(auth));

        } else if (version.equals("offline")) {
            Auth auth = new AuthDummy();
            gui.setAuth(auth);
            gui.setConverse(new ConverseDummy());
            gui.setDetail(new DetailDummy());
        }
        // check output version
        if(output.equals("online")){
            gui.setReport(new ReportImpl());
        }else if(output.equals("offline")){
            gui.setReport(new ReportDummy());
        }

    }

    @Override
    public void display() {
        gui.basicPage();
    }

}
