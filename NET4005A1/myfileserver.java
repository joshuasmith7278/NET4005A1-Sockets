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
    static DataInputStream inFromClient;
    static DataOutputStream outToClient;
    static String filename;
    static boolean filestatus = false;

    WorkerThread(Socket c){
        client = c;
    }

    public static void checkForFile(String file) throws IOException{
  
            File dir = new File(".");
            String[] fileList = dir.list();

            for(int i = 0; i < fileList.length; i++){
                if(fileList[i].equals(file)){
                    filestatus = true;
                    break;

                }
            }
       
    }




    public static void sendFile(String f) throws IOException{
        

        int bytes = 0;
        File file = new File(f);
        FileInputStream fileReader = new FileInputStream(file);

        outToClient.writeLong(file.length());
        byte[] buffer = new byte[4 * 1024];
        while((bytes = fileReader.read(buffer)) != -1){
            outToClient.write(buffer, 0 , bytes);
            outToClient.flush();
        }

           

        
        fileReader.close();

        

    }




    public void run(){
        InetAddress inet = client.getInetAddress();
        System.out.println("Client hostname : " + inet.getHostAddress());
        System.out.println("Client IP : " + inet.getHostAddress());
        try{
            inFromClient = new DataInputStream(client.getInputStream());
            outToClient = new DataOutputStream(client.getOutputStream());
            outToClient.writeUTF("TCP Response from Server :");
            filename = inFromClient.readUTF();
            checkForFile(filename);
            outToClient.writeBoolean(filestatus);
            if(filestatus){
                sendFile(filename);
            }
            


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

 

