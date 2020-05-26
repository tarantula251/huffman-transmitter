package filetransfer;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    private ServerSocket serverSocket;
    private Socket socket;

    public FileServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error during receiving file via socket: " + e.getMessage() + " | " + this.getClass() + " | port: " + port);
        }
    }

    public void run(String filePath) {
        try {
            System.out.println("Server started.");
            socket = serverSocket.accept();
            System.out.println("FileServer socket is connected: " + socket.isConnected());
            System.out.println("Socket connected. Started receiving data...");
            saveFile(filePath);
        } catch (IOException e) {
            System.err.println("Error during receiving file via socket: " + e.getMessage() + " | " + this.getClass());
        }
    }

    private void saveFile(String filePath) throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream(filePath);
        IOUtils.copy(in, out);

        out.flush();
        out.close();

        System.out.println("Data received and saved to a file: " + filePath);
    }
}
