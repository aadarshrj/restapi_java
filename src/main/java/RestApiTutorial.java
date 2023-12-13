import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestApiTutorial {
    public static void main(String[] args) throws Exception {
        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4");
        Gson gson = new Gson();
        String jsonRequeest = gson.toJson(transcript);
        System.out.println(jsonRequeest);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization","3bf63182c1274a7c8e2858d6110bce6b")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequeest))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

       HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(postResponse.body());

        transcript = gson.fromJson(postResponse.body(),Transcript.class);

        System.out.println(transcript.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/"+transcript.getId()))
                .header("Authorization","3bf63182c1274a7c8e2858d6110bce6b")
                .build();
        while (true){
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(),Transcript.class);

            System.out.println(transcript.getStatus());

            if(transcript.getStatus().equals("completed") || transcript.getStatus().equals("error")){
                break;
            }
            Thread.sleep(1000);
        }
        System.out.println("Transcription completed!");
        System.out.println(transcript.getText());
    }
}
