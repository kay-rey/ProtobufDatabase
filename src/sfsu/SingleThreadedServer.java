package sfsu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Creates a server that runs on a single thread. It is only able to serve one client at a time, which is no fun.
 * <p>
 * Implements runnable so that it can be executed in a thread or as a future. This allows for multiple servers on
 * different ports.
 */
public class SingleThreadedServer implements Runnable {

    // The port that this server will bind to.
    private final int port;

    /**
     * Creates a server in the specified port. The server will not start to listen until run is called.
     */
    public SingleThreadedServer(int port) {
        this.port = port;
    }

    /**
     * Runs the server in a single thread.
     */
    @Override
    public void run() {
        singleThreadListen();
    }

    /**
     * Listens to a client in the specified port. Once the client is connected (i.e. the network socket is accepted)
     * it is processed, and the server returns to wait for the next client.
     * <p>
     * This function does not return unless interrupted.
     */
    private void singleThreadListen() {
        try {
            // Create a new server socket.
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println(String.format("Server on port %d ready\n", port));

            // Listen for clients until interrupted.
            while (true) {
                System.out.println("Accepting the next client\n");
                Socket clientSocket = serverSocket.accept();
                // A new client has been received. Process the contents of the socket.
                processClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a client request reading the specified socket. Pretends to work for a fixed number of seconds (as an
     * example, of course, it is not actually needed.) Creates and sends a dummy response.
     *
     * @param socket from where to read
     * @throws IOException whenever the network or the client go bad
     */
    private void processClient(Socket socket) throws IOException {
        // Parse the request directly from the socket. Protobufs do the heavy lifting.
        DatabaseProtos.Request request = DatabaseProtos.Request.parseDelimitedFrom(socket.getInputStream());
        System.out.println(String.format("Received request: %s\n", request));

        // Pretend some heavy work is going on (e.g. looking for a record, connecting to multiple systems, writing to a
        // shared resource...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        // Create a dummy response and send it to the client.
        DatabaseProtos.Response response = DatabaseProtos.Response.newBuilder()
                .setValue("This is a test value")
                .setKey("This is a test key")
                .build();
        response.writeDelimitedTo(socket.getOutputStream());

        // This interaction is done. A better server would listen for another request unless the client closes or sends
        // some sort of EOL.
        socket.close();
    }

    /**
     * Starts a demo server.
     * <p>
     * Example:
     * java -cp lib/*:out/production/Database sfsu.SingleThreadedServer 1080
     *
     * @param args the first element must be a port number
     * @throws Exception whenever anything bad happens, good enough for a quick test.
     */
    public static void main(String[] args) throws Exception {
        // Verify the command line arguments.
        if (args.length != 1) {
            System.out.println("Usage: SingleThreadedServer <port>");
            return;
        }
        // Parse the port number
        int port = Integer.parseInt(args[0]);

        // Create a pool thread with a single thread in it. Note that it would be possible to run multiple servers in
        // different ports using a pool of more than one thread.
        ExecutorService serverThreads = Executors.newSingleThreadExecutor();
        // Create an instance of the server.
        SingleThreadedServer server = new SingleThreadedServer(port);
        // Run the server as a future. Since this demo server does not do anything afterwards it is ok to wait on the
        // future to finish (which is effectively never).
        Future serverFuture = serverThreads.submit(server);
        serverFuture.get();
    }
}
