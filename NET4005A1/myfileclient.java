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

    private static DataInputStream inFromServer;
    private static DataOutputStream outToServer;

    SocketHandling(String s, int p, String f){
        serverHost = s;
        serverPort = p;
        filename = f;
    }


    //With threads, call run method 
    public void run(){
        try{
            //Establish a socket connection to the server
            Socket socket = new Socket(serverHost, serverPort);
            inFromServer = new DataInputStream(socket.getInputStream());
            outToServer = new DataOutputStream(socket.getOutputStream());



            //Send Request to Server
            outToServer.writeUTF(filename);

            //Process and Display Server Response
            System.out.println(inFromServer.readUTF());

            //If file is found, recieve and save the file


            //Catch any errors with connecting to the socket. Print any errors
        }catch(IOException ex){
            System.out.println(ex);
        }



    }

     

    


}