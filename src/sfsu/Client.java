package sfsu;

import java.net.Socket;
import java.util.Random;

/**
 * Verifies that the database server is able to receive a client request. Prints the response from the server.
 * <p>
 * Invocation and arguments:
 * java -cp lib/*:out/src/sfsu sfsu.Client <IP> <port>
 */
public class Client {

    static int REQUEST_AMOUNTS = 200;
    static int BOUND = 1000;

    /**
     * Connects to the sever process. Sends a dummy request and receives a response.
     *
     * @throws Exception whenever anything bad happens. This avoids the need to wrap most instructions in try/catch
     *                   blocks. Good enough for a dummy tester.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: Client <IP> <port>");
            return;
        }

        // Parse the IP and port
        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);

        for (int i = 0; i > REQUEST_AMOUNTS; ++i) {

            //if random out come is 0 then PUT operation. 1 = GET. 2 = DELETE
            int randomResult = getRandomNumberInRange(0, 2);

            if (randomResult == 0) {    //PUT operation

                // Create a dummy request. None of the arguments are important, as long as the request is syntactically valid.
                DatabaseProtos.Request request = DatabaseProtos.Request.newBuilder()
                        .setOperation(DatabaseProtos.Request.OperationType.PUT)
                        .setKey(Integer.toString(getRandomNumberInRange(0, BOUND)))
                        .setValue(Integer.toString(getRandomNumberInRange(0, BOUND)))
                        .build();

                // Create a socket and attempt to connect.
                Socket clientSocket = new Socket(serverAddress, port);

                // Write the request message to the socket.
                request.writeDelimitedTo(clientSocket.getOutputStream());
                System.out.println("Request sent, waiting for response.");

                // Receive and parse a response from the server.
                DatabaseProtos.Response response = DatabaseProtos.Response.parseDelimitedFrom(clientSocket.getInputStream());
                System.out.println(String.format("Response received: %s\n", response));

                // Close the sockets and finish.
                clientSocket.close();
            }

            if (randomResult == 1) {    //GET operation

                // Create a dummy request. None of the arguments are important, as long as the request is syntactically valid.
                DatabaseProtos.Request request = DatabaseProtos.Request.newBuilder()
                        .setOperation(DatabaseProtos.Request.OperationType.GET)
                        .setKey(Integer.toString(getRandomNumberInRange(0, BOUND)))
                        .setValue(Integer.toString(getRandomNumberInRange(0, BOUND)))
                        .build();

                // Create a socket and attempt to connect.
                Socket clientSocket = new Socket(serverAddress, port);

                // Write the request message to the socket.
                request.writeDelimitedTo(clientSocket.getOutputStream());
                System.out.println("Request sent, waiting for response.");

                // Receive and parse a response from the server.
                DatabaseProtos.Response response = DatabaseProtos.Response.parseDelimitedFrom(clientSocket.getInputStream());
                System.out.println(String.format("Response received: %s\n", response));

                // Close the sockets and finish.
                clientSocket.close();
            }

            if (randomResult == 2) {    //DELETE operation

                // Create a dummy request. None of the arguments are important, as long as the request is syntactically valid.
                DatabaseProtos.Request request = DatabaseProtos.Request.newBuilder()
                        .setOperation(DatabaseProtos.Request.OperationType.DELETE)
                        .setKey(Integer.toString(getRandomNumberInRange(0, BOUND)))
                        .setValue(Integer.toString(getRandomNumberInRange(0, BOUND)))
                        .build();

                // Create a socket and attempt to connect.
                Socket clientSocket = new Socket(serverAddress, port);

                // Write the request message to the socket.
                request.writeDelimitedTo(clientSocket.getOutputStream());
                System.out.println("Request sent, waiting for response.");

                // Receive and parse a response from the server.
                DatabaseProtos.Response response = DatabaseProtos.Response.parseDelimitedFrom(clientSocket.getInputStream());
                System.out.println(String.format("Response received: %s\n", response));

                // Close the sockets and finish.
                clientSocket.close();
            }

        }   //ends the if statement that makes the requests

        return;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
