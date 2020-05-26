package filetransfer;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileClient {

    private Socket socket;

    public FileClient(String host, int port) {
        try {
            Thread.sleep(100);
            socket = new Socket(host, port);
            System.out.println("FileClient socket is connected: " + socket.isConnected());
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException: Error during sending file via socket: " + e.getMessage() + " | " + this.getClass() + " | host: " + host + " | port: " + port);
        } catch (IOException ex) {
            System.err.println("IOException: Error during sending file via socket: " + ex.getMessage() + " | " + this.getClass() + " | host: " + host + " | port: " + port);
        } catch (SecurityException exe) {
            System.err.println("SecurityException: Error during sending file via socket: " + exe.getMessage() + " | " + this.getClass() + " | host: " + host + " | port: " + port);
        } catch (Exception exep) {
            System.err.println("Error during sending file via socket: " + exep.getMessage() + " | " + this.getClass() + " | host: " + host + " | port: " + port);
        }
    }

    public void sendFile(File file) {
        try {
            System.out.println("Sending file by client started.");
            OutputStream out = socket.getOutputStream();
            InputStream in = new FileInputStream(file);
            IOUtils.copy(in, out);

            out.flush();
            out.close();

            System.out.println("File " + file.getAbsolutePath() + " copied to output stream.");
        } catch (IOException e) {
            System.err.println("Error during sending file via socket: " + e.getMessage() + " | " + this.getClass());
        }
    }
}
