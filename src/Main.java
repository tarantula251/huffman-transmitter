import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import decoder.HuffmanDecoder;
import encoder.HuffmanEncoder;
import filetransfer.FileClient;
import filetransfer.FileServer;
import huffman.HuffmanNode;
import huffman.HuffmanNodeComparator;

import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length != 4) {
            System.err.println("Wrong number of arguments.\n");
            System.out.println("Syntax: [encode|decode] input_file_path output_file_path nodes_file_path\n");
            return;
        }

        if (!args[1].endsWith(".txt")) {
            System.err.println("Wrong extension of input file.\n");
            System.out.println("Application allows only text files.\n");
            return;
        }

        if (args[0].equals("encode")) {
            HuffmanEncoder encoder = new HuffmanEncoder(args[1], args[2]);
            PriorityQueue<HuffmanNode> nodes = encoder.encode();
            // serialize nodes to json
            File nodesFile = null;
            if (nodes != null && nodes.size() > 0) {
                ObjectMapper objectMapper = new ObjectMapper();
                nodesFile = new File(args[3]);
                objectMapper.writeValue(nodesFile, nodes);
            }
            // send nodes file via socket port
            FileClient nodesFileClient = new FileClient("169.254.34.120", 137);
            nodesFileClient.sendFile(nodesFile);
            // send encoded file via socket port
            File encodedFile = new File(args[2]);
            FileClient encodedFileClient = new FileClient("169.254.34.120", 138);
            encodedFileClient.sendFile(encodedFile);

        }

        if (args[0].equals("decode")) {
            // receive nodes file via socket port
            FileServer nodesFileServer = new FileServer(137);
            nodesFileServer.run(args[3]);
            // receive encoded file via socket port
            FileServer encodedFileServer = new FileServer(138);
            encodedFileServer.run(args[1]);

            HuffmanDecoder decoder = new HuffmanDecoder(args[1], args[2]);
            File nodesFile = new File(args[3]);
            if (nodesFile.exists()) {
                ArrayList<String> nodesList = new ArrayList<>();
                Scanner scanner = new Scanner(nodesFile);
                while (scanner.hasNext()) {
                    nodesList.add(scanner.next());
                }
                scanner.close();
                if (!nodesList.isEmpty()) {
                    String nodesJson = nodesList.get(0);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
                    ArrayList<HuffmanNode> nodesQueue = objectMapper.readValue(nodesJson, new TypeReference<ArrayList<HuffmanNode>>(){});

                    PriorityQueue priorityQueue = new PriorityQueue(nodesQueue.size(), new HuffmanNodeComparator());
                    priorityQueue.addAll(nodesQueue);
                    decoder.decode(priorityQueue);
                }
            } else {
                System.err.println("JSON file with nodes not found.\n");
            }
        }
    }
}
