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
                mt.startMultiThreadPool();

               

            }catch(IOException ex){
                System.out.println("Server error : " + ex);
            }
           

        
    }
}



class MultiThreadServer{
    static int port;
    static int activeThreads = 0;

    MultiThreadServer(int p){
        port = p;
        
    }

    public void getActiveThreads(){
        System.out.println(activeThreads);
    }


    public void startMultiThreadPool() throws IOException{
        ServerSocket server = new ServerSocket(port);
        Socket client = new Socket();

    
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);

        ThreadPoolExecutor executorpool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, queue);

        ServerStatistics stats = new ServerStatistics(0,  0);

        while(true){
            client = server.accept();
            executorpool.submit(new WorkerThread(client, stats));
           

        }

        
        
    }

}



class WorkerThread extends Thread {
    
    static Socket client;
    static DataInputStream inFromClient;
    static DataOutputStream outToClient;
    String filename;
    static boolean filestatus = false;
    static ServerStatistics stats;
    int N;
    
   
   



    WorkerThread(Socket c, ServerStatistics s){
        client = c;
        stats = s;
        N = stats.getN() + 1;
        
        
       
    }



     synchronized boolean checkForFile(String file) throws IOException{
  
            File dir = new File(".");
            String[] fileList = dir.list();

            for(int i = 0; i < fileList.length; i++){
                if(fileList[i].equals(file)){
                    filestatus = true;
                  
                    return true;
                    

                }
            }
            return false;

       
    }


    synchronized void sendFile(String f) throws IOException{


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




    @Override
    public synchronized void run() {
       
        try{

            stats.setN(N);
            
            
            InetAddress inet = client.getInetAddress();
        
            inFromClient = new DataInputStream(client.getInputStream());
            outToClient = new DataOutputStream(client.getOutputStream());

            //2. Server Checks for File "X" in current directory
            filename = inFromClient.readUTF();
            
            outToClient.writeBoolean(checkForFile(filename));

            
            
            int m;
            
            System.out.println("REQ " + N + ": File " + filename + " requested from " + inet.getHostAddress());
            outToClient.writeInt(stats.getN());
           
            //TRANSFERS FILE TO CLIENT
            if(checkForFile(filename)){
                
                m = stats.getM() + 1;
                outToClient.writeInt(m);
                stats.setM(m);
                
                
                
                sendFile(filename);
                Thread.sleep(5000);
                
                System.out.println("REQ " + N + ": Successful");
                

            }else{
                m = stats.getM();
                outToClient.writeInt(m);
                System.out.println("REQ " + N + ": Not Successful");
            }

           
           
            System.out.println("REQ " + N + ": Total successful requests so far = " + String.valueOf(m));

           
           


            filestatus=false;
            
            System.out.println("REQ " + N + ": File transfer complete");

            
           
          
            

        }catch(InterruptedException | IOException ex){
            System.out.println("Thread error : " + ex);
        }


    }


    

}


class ServerStatistics {
    int N;
    int M;

    ServerStatistics(int n, int m){
        N = n;
        M = m;
    }

    public synchronized int getM() {
        return M;
    }

    public synchronized int getN() {
        return N;
    }

    public synchronized void setM(int m) {
        M = m;
    }

    public synchronized void setN(int n) {
        N = n;
    }

}

 

