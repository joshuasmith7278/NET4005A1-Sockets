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
    public static void receiveFile(String filename) throws IOException{
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



            //Send Request to Server
            outToServer.writeUTF(filename);

            //Process and Display Server Response
            System.out.println(inFromServer.readUTF());
            filestatus = inFromServer.readBoolean();
            
            if(filestatus){
                receiveFile(filename);

            }else{
                System.out.println("File not on Server");
            }

            


            //If file is found, recieve and save the file


            //Catch any errors with connecting to the socket. Print any errors
        }catch(IOException ex){
            System.out.println(ex);
        }



    }

     

    


}