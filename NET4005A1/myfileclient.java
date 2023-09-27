import java.io.*;
import java.net.*;

public class myfileclient {
    public static void main(String[] args){
        //Parse command line arguments (Server IP, Server Port, Filename)
        if(args.length != 3){
            System.out.println("Incorrect amount of Arguments in Request");
        }else{
            new SocketHandling(args[0], Integer.parseInt(args[1]), args[2]).start();
        }

    
    }


}


class SocketHandling extends Thread{
    String serverHost;
    int serverPort;
    String filename;

    Boolean filestatus;
    private static DataInputStream inFromServer;
    private static DataOutputStream outToServer;

    SocketHandling(String s, int p, String f){
        serverHost = s;
        serverPort = p;
        filename = f;
    }

    public static void receiveFile(String filename) throws IOException{
       
        FileOutputStream fileOutputStream = new FileOutputStream(filename);

        long size = inFromServer.readLong();

       
      

        while(size > 0){
            int currentByte = inFromServer.read();
            fileOutputStream.write(currentByte);
            size --;
        }

        System.out.println("File Recieved");
        fileOutputStream.close();
    }


    public void run(){
        try{
            
            Socket socket = new Socket(serverHost, serverPort);
            inFromServer = new DataInputStream(socket.getInputStream());
            outToServer = new DataOutputStream(socket.getOutputStream());



            //1. Client asks for File "X"
            outToServer.writeUTF(filename);

            //Process and Display Server Response
            
            filestatus = inFromServer.readBoolean();

             int N = inFromServer.readInt();
            int M = inFromServer.readInt();

           
            
            
            if(filestatus){
                System.out.println("File " + filename + " found at server");
                System.out.println("Server handled " + String.valueOf(N) + " requests, " + String.valueOf(M) + " requests were successful");
                System.out.println("Downloading file " + filename);
                receiveFile(filename);
                System.out.println("Download complete");


            }else{
                System.out.println("File " + filename + " not found at server");
                System.out.println("Server handled " + String.valueOf(N) + " requests, " + String.valueOf(M) + " requests were successful");

            }

        
        }catch(IOException ex){
            System.out.println(ex);
        }



    }

     

    


}