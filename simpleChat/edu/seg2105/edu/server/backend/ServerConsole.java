package edu.seg2105.edu.server.backend;

import java.util.Scanner;

import edu.seg2105.common.*;

/**
 * This class constructs the UI for a chat client. It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
*/
public class ServerConsole implements ChatIF {
  // Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Instance variables **********************************************

  /**
   * The instance of the server that created this ConsoleChat.
   */
  EchoServer server;

  /**
   * Scanner to read from the console
   */
  Scanner fromConsole;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) {
    server = new EchoServer(port, this);

    // Create scanner object to read from console
    fromConsole = new Scanner(System.in);
  }

  // Instance methods ************************************************

  /**
   * This method waits for input from the console. Once it is
   * received, it sends it to the server's message handler.
   */
  public void accept() {
    try {
      String message;
      while (true) {
        message = fromConsole.nextLine();
        server.handleMessageFromServerUI(message);
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface. It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    System.out.println("> " + message);
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Server UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) {
    int port = 0;  

    try {
      port = Integer.parseInt(args[0]);
    } catch (Throwable t) {
      port = DEFAULT_PORT; 
    }

    ServerConsole chat = new ServerConsole(port);
    chat.accept();  //Wait for console data
  }
}
// End of ServerConsole class