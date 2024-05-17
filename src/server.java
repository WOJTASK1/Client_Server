import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
    private static final int PORT = 12345;
    private static Map<Integer, PrintWriter> clients = new HashMap<>();
    private static int clientIdCounter = 1;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.println("Server is running...");
            while (true) {
                pool.execute(new ClientHandler(listener.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private Scanner in;
        private int clientId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new Scanner(socket.getInputStream());

                synchronized (clients) {
                    clientId = clientIdCounter++;
                    clients.put(clientId, out);
                }

                out.println("Your client ID is: " + clientId);

                while (in.hasNextLine()) {
                    String message = in.nextLine();
                    String[] parts = message.split(":", 2);
                    if (parts.length == 2) {
                        String msg = parts[0];
                        int delay = Integer.parseInt(parts[1]);

                        // Notify the client after the specified delay
                        new Thread(() -> {
                            try {
                                Thread.sleep(delay * 1000);
                                notifyClient(clientId, msg);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    synchronized (clients) {
                        clients.remove(clientId);
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void notifyClient(int clientId, String message) {
            PrintWriter clientOut;
            synchronized (clients) {
                clientOut = clients.get(clientId);
            }
            if (clientOut != null) {
                clientOut.println("Notification: " + message);
            }
        }
    }
}