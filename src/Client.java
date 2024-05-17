import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             Scanner userInput = new Scanner(System.in);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner in = new Scanner(socket.getInputStream())) {

            System.out.println("Connected to server.");

            // Receive and display the client ID
            String clientId = in.nextLine();
            System.out.println(clientId);

            while (true) {
                System.out.print("Enter notification message: ");
                String message = userInput.nextLine();
                if (message.trim().isEmpty()) {
                    System.out.println("Notification message cannot be empty.");
                    continue;
                }

                System.out.print("Enter time to send notification (in seconds from now): ");
                int timeToSend;
                try {
                    timeToSend = Integer.parseInt(userInput.nextLine());
                    if(timeToSend>180 || timeToSend<0){
                        throw new ExceededDelay(timeToSend);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid time format. Time must be an integer.");
                    continue;
                } catch (ExceededDelay e) {
                    System.out.println(e);
                    continue;
                }

                out.println(message + ":" + timeToSend);

                System.out.println("Waiting for notification...");
                String response = in.nextLine();
                System.out.println("Server notification: " + response);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
