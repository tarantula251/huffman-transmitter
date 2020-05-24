package decoder;

import huffman.HuffmanNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class HuffmanDecoder {
    String inputFilePath;
    String outputFilePath;
    HashMap<String, String> decodedStringMap = new HashMap<>();

    public HuffmanDecoder(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = (!outputFilePath.endsWith(".txt")) ? outputFilePath + ".txt" : outputFilePath;
    }

    public void decode(PriorityQueue<HuffmanNode> nodes) throws IOException {
        ArrayList<String> encodedStringList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(inputFilePath));
        scanner.useDelimiter(Pattern.compile(";"));
        while (scanner.hasNext()) {
            encodedStringList.add(scanner.next());
        }
        scanner.close();
        for (String encodedString : encodedStringList) {
            decodedStringMap.put(encodedString, generateDecodedChars(nodes.peek(), encodedString));
        }

        saveDecodedFile(encodedStringList);
    }

    private String generateDecodedChars(HuffmanNode root, String s) {
        StringBuilder decodedString = new StringBuilder();
        HuffmanNode currentRoot = root;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                currentRoot = currentRoot.leftNode;
            } else {
                currentRoot = currentRoot.rightNode;
            }

            if (currentRoot.leftNode == null && currentRoot.rightNode == null) {
                decodedString.append((char) currentRoot.getCharAscii());
                currentRoot = root;
            }
        }
        decodedString.append('\0');
        return String.valueOf(decodedString);
    }

    private void saveDecodedFile(ArrayList<String> encodedStringList) {
        try {
            File file = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String encodedString : encodedStringList) {
                String decodedString = decodedStringMap.get(encodedString);
                bufferedWriter.write(decodedString);
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Error during saving decoded file: " + e.getMessage());
        }
    }
}
