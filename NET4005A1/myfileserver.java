import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class myfileserver {
    public static void main(String[] args){
        //Create Server socket and listen for connections
        int port = 8000;

        try{
            ServerSocket server = new ServerSocket(port);

            //Create thread pol for client requests

            while(true){
                //Implement logic to handle single client request
                Socket client = server.accept();
                WorkerThread thread = new WorkerThread(client);
                thread.start();
            }

        }catch(IOException ex){
            System.out.println(ex);
        }
        
    }
}

class WorkerThread extends Thread {
    //Implememnt handle single client request including stats and transferring files
    Socket client;

    WorkerThread(Socket c){
        client = c;
    }

    public void run(){
        InetAddress inet = client.getInetAddress();
        System.out.println("Client hostname : " + inet.getHostAddress());
        System.out.println("Client IP : " + inet.getHostAddress());
        try{
            DataInputStream inFromClient = new DataInputStream(client.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
            outToClient.writeUTF("TCP Response from Server");
            System.out.println(inFromClient.readUTF());


        }catch(IOException ex){
            System.out.println(ex);
        }
    
       

    }

}




/* 
 class multiThreadServer {
    public static void main(String[] args){
        BlockingQueue queue = new ArrayBlockingQueue<>(100);

        ThreadPoolExecutor executorpool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, queue);

        executorpool.prestartAllCoreThreads();

       
        for(int i = 0; i < 10; i++){
            queue.offer(new WorkerThread(String.valueOf(i)));
            
        }

        executorpool.shutdown();
    }
}


*/

 

