-------------=====---------- NET4005 Network Applications Assignment 1 ------------------------------------

Implement a Client-Server File transfer application. The Server should use multithreading and concurrency 
to handle client requests. Server will track and send statistics.

N = total client requests
M = successful file transfers

------------------------------------------- Logical Process ----------------------------------------------

Client.java requests Server.java for File "X"

If(File "X" exists on the Server)
    Write the File to the Client

Respond to the Client with the File status and server statistics


----------------------------------------- myfileclient.java -----------------------------------------------


Class myfileclient{

main()
- This function handles the client request input. We check to see that all 3 required arguments (host, port, filename)
are input from the user. 
- If we have all the arguments required, we can create a Socket with those parameters. This is done 
through the Socket Handler Class

}





Class SocketHandling extends Thread{

SocketHandling()
- Save local instances of the server hostname, port #, and filename



recieveFile()
- Creates a FILE OUTPUT STREAM that will name a file the name its passed in with ("filename")
- Read in the size of the file from the SERVER INPUT STREAM
- Create a buffer (4096 byte array) to read chunks of the file at once
- While the file size isnt 0, write up to 4096 bytes of the file to the FILE OUTPUT STREAM
- If the current array of bytes is NULL (-1), this means the SERVER has nothing else to send us to read. The file has been 
entirely read. You can break the while loop if this occurs before the file size is 0
- Subtract those bytes written from the file size variable, and continute to loop until the loop breaks.




main()
- Create a Socket object from the parameters passed in to SocketHandling (arguments 1 + 2 from the user).
- Create Stream Socket channels (input stream, output stream) with the Socket object
- Write to the Server (output stream) for the filename (argument 3 from the user)
- Read from the server (input stream) for the File Status (boolean), N (int), M (int)
- If the File Status is true, 
    - We display the USER INTERFACE
    - We call our recieve file method to "download" the file from the server

}



--------------------------------------- myfileserver.java -----------------------------------------------------------

Class myfileserver{

main()
- This function creates a MultiThreadServer object and starts its

}






class MultiThreadServer{

MultiThreadServer(int p)
- This class takes in the port we want to host our Application on (p) and save it to a local variable (port)

StartMultiThreadPool()
- Create a ServerSocket object to be the Server , and a Socket object to be the client
- Create a queue to hold excess requests
- Create a threadpool to limit the amount of threads working
- Create a ServerStatistics object to keep track of the servers statistics through its runtime
- While(true)
    - If a client tries to connect to the Server Socket, accept them 
    - Submit them to the thread pool for execution
    - If the thread pool is filled, the extra threads go to the queue

}







class WorkerThread{
- Creates a Thread for each Client Request
- Handles the file trasnfer for individual requests

WorkerThread()
- Creates a copy of the Server and Client objects to handle stream communication


checkForFile()
- Checks all the files in its current directory for the filename requested by the client
- If found
    - Set the file status variable to true
-else
    -The file status is set to false


sendFile()
- Uses FileInputStream to read the contents of a file
- Uses Stream Sockets to an array of bytes at a time (4096 bytes).
- While the FILE READER isnt null, write the byte array to the client through the STREAM OUTPUT
- The client reads and puts together the file



run()
- Creates Stream Socket channels (inFromClient and outToServer) between Server and Client
- Read for requested file from Client
- Check for file requested
- If File exists,
    - Call our send file method to "transfer" the file to the client

- Print the updated server statistics to include the current requests

}







class ServerStatistics{
- A class that holds information related to two statistics N and M
- Each have methods to GET and SET their objects variables.

}







