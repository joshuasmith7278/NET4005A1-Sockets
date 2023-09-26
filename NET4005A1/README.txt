-------------=====-------- NET4005 Network Applications Assignment 1 ------------------------------------

Implement a Client-Server File transfer application. The Server should use multithreading and concurrency 
to handle client requests. Server will track and send statistics.

N = total client requests
M = successful file transfers

---------------------------------------- Logical Process ----------------------------------------------

Client.java requests Server.java for File "X"

If(File "X" exists on the Server)
    Write the File to the Client

Respond to the Client with the File status and server statistics


------------------------------------- myfileclient.java -----------------------------------------------


Class myfileclient
main()
- This function handles the client request input. We check to see that all 3 required arguments (host, port, filename)
are input from the user. 
- If we have all the arguments required, we can create a Socket with those parameters. This is done 
through the Socket Handler Class





Class SocketHandling extends Thread

SocketHandling()
- Create local instances of the server hostname, port #, and filename



recieveFile()
- Creates a file output stream that will name a file the name its passed in with ("filename")
- Read in from the Server output stream the transferred file 
- Create a buffer to read 



main()
- Create a Socket object from the parameters passed in to SocketHandling (arguments 1 + 2 from the user).
- Create Stream Socket channels (input stream, output stream) with the Socket object
- Write to the Server (output stream) for the filename (argument 3 from the user)
- Read from the server (input stream) for the File Status (boolean), N (int), M (int)
- If the File Status is true, 
    - We display the USER INTERFACE
    - We call our recieve file method to "download" the file from the server

