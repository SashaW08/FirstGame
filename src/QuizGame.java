import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//hi
public class QuizGame {
        public QuizGame(String[] args) {
        try {
            String apiUrl = "https://opentdb.com/api.php?amount=1&category=27&difficulty=hard&type=multiple";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
//
            String responseStr = response.toString();

            // Decode basic HTML entities
            responseStr = responseStr.replace("&quot;", "\"").replace("&#039;", "'");

            // Extract question
            String question = responseStr.split("\"question\":\"")[1].split("\",")[0];

            // Extract correct answer
            String correct = responseStr.split("\"correct_answer\":\"")[1].split("\",")[0];

            // Extract incorrect answers
            String incorrectRaw = responseStr.split("\"incorrect_answers\":\\[")[1].split("]")[0];
            incorrectRaw = incorrectRaw.replace("\"", "");
            String[] incorrectArray = incorrectRaw.split(",");

            String incorrect1 = incorrectArray.length > 0 ? incorrectArray[0].trim() : "";
            String incorrect2 = incorrectArray.length > 1 ? incorrectArray[1].trim() : "";
            String incorrect3 = incorrectArray.length > 2 ? incorrectArray[2].trim() : "";

            // Print results
            System.out.println("Question: " + question);
            System.out.println("Correct Answer: " + correct);
            System.out.println("Incorrect Answer 1: " + incorrect1);
            System.out.println("Incorrect Answer 2: " + incorrect2);
            System.out.println("Incorrect Answer 3: " + incorrect3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
