
import controller.Engine;
import controller.EngineImpl;
import javafx.application.Application;

import javafx.stage.Stage;

public class App extends Application {

    private static String input;
    private static String output;


    public static void main(String[] args) {

        if(args.length==2 && args[0].contains("offline") && args[1].contains("offline")){
            input = "offline";
            output = "offline";
        }else if (args.length==2 && args[0].contains("offline") && args[1].contains("online")){
            input = "offline";
            output = "online";
        }else if(args.length==2 && args[0].contains("online") && args[1].contains("offline")){
            input = "online";
            output = "offline";
        }else if(args.length==2 && args[0].contains("online") && args[1].contains("online")){
            input = "online";
            output = "online";
        } else{
            System.out.println("bye");
            System.exit(99);
        }


        Application.launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Engine engine = new EngineImpl(primaryStage,input,output);
        engine.display();

    }
}


