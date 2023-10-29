// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * The interface type variable. It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String host, int port, ChatIF clientUI)
      throws IOException {
    super(host, port); // Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());

  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message) {
    try {
      if (message.charAt(0) == '#') {
        runCommand(message);
      } else {
        sendToServer(message);
      }
    } catch (IOException e) {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
   * This method executes client commands
   *
   * @param message String from the client console
   */
  public void runCommand(String message) {
    String[] command = message.split(" ");
    switch (command[0]) {
      case "#quit":
        quit();
        break;
      case "#logoff":
        try {
          closeConnection();
        } catch (IOException e) {
        }
        break;
      case "#sethost":
        if (isConnected()) {
          System.out.println("You must log off before setting the host");
          break;
        }
        try{
          setHost(command[1]);
        }
        catch(Exception e){
          System.out.println("Invalid host");
        }
        break;
      case "#setport":
        if (isConnected()) {
          System.out.println("You must log off before setting the port");
          break;
        } try {
          setPort(Integer.parseInt(command[1]));
        }
        catch(Exception e){
          System.out.println("Invalid port");
        }
        break;
      case "#login":
        if (isConnected()) {
          System.out.println("You must log off before logging in");
          break;
        }
        try {
          System.out.println("Logging in...");
          openConnection();
        } catch (IOException e) {
        }
        break;
      case "#gethost":
        System.out.println(getHost());
        break;
      case "#getport":
        System.out.println(getPort());
        break;
      default:
        System.out.println("Command not recognized");
        break;
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }

  protected void connectionException(Exception exception) {
    clientUI.display("The server is shut down");
    quit();
  }

  protected void connectionClosed() {
    clientUI.display("Connection is closed");
  }
}
// End of ChatClient class
