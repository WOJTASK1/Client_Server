import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket) throws IOException{
        this.client=clientSocket;
        in=new BufferedReader(new InputStreamReader(client.getInputStream()));
        out=new PrintWriter(client.getOutputStream());
    }

    @Override
    public void run() {
        try{
            while(true){
               String request=in.readLine();
               if(request.contains("name")){
                   out.println(Server.getRandomName());
               }else{
                   out.println("Type 'tell me a name' to get a random name");
               }
            }
        }catch(IOException e){
            System.err.println("Exception:\n"+e);
        }finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
