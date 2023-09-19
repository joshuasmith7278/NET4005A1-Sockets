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
       
       
            try{
                 //Create thread pol for client requests
                MultiThreadServer mt = new MultiThreadServer(8000);
                mt.start();

                while(true){
                    //Implement logic to handle single client request
                    
                    
                }

            }catch(IOException ex){
                System.out.println(ex);
            }
           

        
    }
}

/*
 * MULTI THREAD SERVER
 * 
 * BLOCKING QUEUE=
 * array used to queue threads which are waiting for WorkerThread to be available
 * 
 * THREAD POOL EXECUTOR=
 * 
 * 
 * 
 * 
 * 
 */

class MultiThreadServer{
    static int port;

    MultiThreadServer(int p){
        port = p;
        
    }
    public void start() throws IOException{
        System.out.println("Server Running on " + port);
        ServerSocket server = new ServerSocket(port);

    
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);

        ThreadPoolExecutor executorpool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, queue);

        

       
        for(int i = 0; i < 20; i++){
            System.out.println("Thread : " + i);
            executorpool.submit(new WorkerThread(server));
            System.out.println(executorpool.getActiveCount());
            System.out.println(queue.remainingCapacity());


            
        }

        executorpool.shutdown();
    }

}



class WorkerThread extends Thread {
    
    static Socket client;
    static ServerSocket name;
    static DataInputStream inFromClient;
    static DataOutputStream outToClient;
    static String filename;
    static boolean filestatus = false;



    WorkerThread(ServerSocket s){
        name = s;
    }


    /*
     * SEARCH FOR FILE
     * 
     * PARAMS = 
     * filename of file you want to search for 
     * 
     * FUNC = 
     * Search list of files in current dir for PARAMS. 
     * If found, set filestatus to TRUE
     * 
     * 
     * 
     * 
     */

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



    /*
     * SEND FILE
     * 
     * PARAMS = 
     * filename of file you want to send
     * 
     * FUNC =
     * Segment file as BUFFER and send to CLIENT.
     * Once BUFFER is empty CLOSE file stream
     * 
     * 
     * 
     * 
     */
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
         
        try{
            client = name.accept();
            InetAddress inet = client.getInetAddress();
            System.out.println("Client hostname : " + inet.getHostAddress());
            System.out.println("Client IP : " + inet.getHostAddress());
            
           
            inFromClient = new DataInputStream(client.getInputStream());
            outToClient = new DataOutputStream(client.getOutputStream());
            outToClient.writeUTF("TCP Response from Server :");
            filename = inFromClient.readUTF();

            //CHECKS FILE AVAILABILITY
            checkForFile(filename);
            outToClient.writeBoolean(filestatus);

            //TRANSFERS FILE TO CLIENT
            if(filestatus){
                //sendFile(filename);
                System.out.println("DOWNLOADING FILE");
            }

            filestatus=false;

            


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

 

