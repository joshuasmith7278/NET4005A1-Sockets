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

    /*
     * RECEIVE FILE
     * 
     * PARAMS=
     * path/name of file destination
     * 
     * FUNC=
     * Read segments from SERVER and write them to file location.
     * Once the buffer size is empty, CLOSE the stream
     * 
     * 
     */
    public static synchronized void receiveFile(String filename) throws IOException{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(filename);

        long size = inFromServer.readLong();
       
        byte[] buffer = new byte[4 * 1024];

        while(size > 0 && (bytes = inFromServer.read(
            buffer, 0, (int)Math.min(buffer.length, size)
        )) != -1){
            fileOutputStream.write(buffer, 0, bytes);
            size-= bytes;
        }
        System.out.println("File Recieved");
        fileOutputStream.close();
    }

    
    /*
     * RUN
     * 
     * FUNC=
     * Establish a socket connection to the server
     * 
     * Send request to SERVER
     * 
     * Print SERVER response
     * Save filestatus response
     * If file exists, recieve file
     * 
     * 
     */
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

            

           


            


            //If file is found, recieve and save the file


            //Catch any errors with connecting to the socket. Print any errors
        }catch(IOException ex){
            System.out.println(ex);
        }



    }

     

    


}