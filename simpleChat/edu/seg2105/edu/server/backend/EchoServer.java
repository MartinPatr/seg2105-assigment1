package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;
import edu.seg2105.common.*;
import java.io.*;


/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  ChatIF serverUI;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) {
    super(port);
    this.serverUI = serverUI;
    try {
      listen(); // Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {

    System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));

    String stringMsg = (String)msg;
    String[] stringMsgs = stringMsg.split(" ");
  
    // if the message is the login commands
    if (stringMsgs[0].equals("#login")){
      if(client.getInfo("loginId") != null ){
        try {
          client.sendToClient("Error: You are already logged in");
          client.close();
        } catch (IOException e) {
          System.out.println("Error: Could not close client connection after failed login attempt");
        }
      } else{
        client.setInfo("loginId", stringMsgs[1]);
        try{
          System.out.println(client.getInfo("loginId") + " has logged on.");
          this.sendToAllClients(client.getInfo("loginId") + " has logged on.");
        } catch (Exception e){
          System.out.println("Error: Could not send login confirmation to all clients");
        }
      }

    }else{ 

    msg = client.getInfo("loginId") + ": " + msg;
    this.sendToAllClients(msg);
    }
  }


  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    System.out.println("Server listening for clients on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }

  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("Client connected: " + client.toString());
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    System.out.println("Client disconnected: " + client.toString());
    }
    
  
  
  /**
   * Handle a message sent from the server console
   * @param message
   */
  public void handleMessageFromServerUI(String message){
    if(message.charAt(0) == '#'){
      runCommand(message);
    }else{
      serverUI.display(message);
      sendToAllClients("SERVER MSG> " + message);
    }
  }

  /**
   * This method executes server commands.
   *
   * @param message String from the server console.
   */
  public void runCommand(String Message) {
    String[] command = Message.split(" ");
    switch (command[0]) {
        case "#quit":
            quit();
            break;
        case "#stop":
            stopListening();
            break;
        case "#close":
            try {
                close();
            } catch (IOException e) {
                System.out.println("Error closing the server");
            }
            break;
        case "#setport":
            if (!isListening()) {
              try{
                setPort(Integer.parseInt(command[1]));
              }catch(ArrayIndexOutOfBoundsException e){
                serverUI.display("Please enter a port number");
              } 
              catch (NumberFormatException e) {
                serverUI.display("Port must be a number");
              }
            } else {
                serverUI.display("Server must be closed to change port");
            }
            break;
        case "#start":
            if (!isListening()) {
                try {
                    listen();
                } catch (IOException e) {
                    System.out.println("Error starting the server");
                }
            } else {
                serverUI.display("Server is already listening for connections");
            }
            break;
        case "#getport":
            serverUI.display("Port: " + getPort());
            break;
        default:
            serverUI.display("Command not recognized");
            break;
    }
}

/**
 * This method terminates the server.
 */
public void quit(){
  try{
    close();
  }catch(IOException e){
    System.out.println("Error closing the server");
  }
  System.exit(0);
}

}
// End of EchoServer class
