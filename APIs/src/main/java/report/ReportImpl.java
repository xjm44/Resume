package report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReportImpl implements Report {

    private String from;
    private String to;
    private String token;


    @Override
    public boolean send(String input) throws IllegalArgumentException {

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
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        from = temp.get(1).split(" ")[1];
        to = temp.get(2).split(" ")[1];
        token = temp.get(3).split(" ")[1];

        // edit input
        String output = " "; // ensure no input content in the email.
        if(input.length()!=0){
            String y = input.replace("\n","\\r\\n");
            String z = y.replace("\t","\\r\\t");
            output = z.replace("\"","");
//            System.out.println("zz");
        }

        // perform api
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sendgrid.com/v3/mail/send"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"personalizations\":[{\"to\":[{\"email\":\""+to+"\",\"name\":\"Receriver\"}],\"subject\":\"Soft 3202\"}],\"content\": [{\"type\": \"text/plain\", \"value\": \""+output+"\"}],\"from\":{\"email\":\""+from+"\",\"name\":\"Sender\"},\"reply_to\":{\"email\":\""+to+"\",\"name\":\"Receiver\"}}"))
                .header("authorization", "Bearer "+token)
                .header("content-type", "application/json")
                .build();

        try {
            Integer code = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::statusCode)
                    .join();
//            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                    .thenApply(HttpResponse::body)
//                    .join();
//            System.out.println(code);
//            System.out.println(response);

            if(code==202){
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    public boolean checkDummy() {
        return false;
    }
}
