package encoder;

import huffman.HuffmanNode;
import huffman.HuffmanTreeGenerator;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HuffmanEncoder {
    String inputFilePath;
    String outputFilePath;
    HashMap<Character, String> encodedCharactersMap = new HashMap<>();

    public HuffmanEncoder(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = (!outputFilePath.endsWith(".txt")) ? outputFilePath + ".txt" : outputFilePath;
    }

    public PriorityQueue<HuffmanNode> encode() throws IOException {
        ArrayList<Integer> charAsciiList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath));
        int charAscii = bufferedReader.read();
        while (charAscii != -1) {
            charAsciiList.add(charAscii);
            charAscii = bufferedReader.read();
        }
        bufferedReader.close();
        Map<Integer, Long> occurrenceMap = charAsciiList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue(Comparator.naturalOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
        HuffmanTreeGenerator treeGenerator = new HuffmanTreeGenerator();
        PriorityQueue<HuffmanNode> nodes = treeGenerator.createHuffmanTree(occurrenceMap);

        assert nodes.peek() != null;
        generateEncodedChars(nodes.peek(), "");
        saveEncodedFile(charAsciiList);

        return nodes;
    }

    private void generateEncodedChars(HuffmanNode root, String s) {
        if (root.leftNode == null && root.rightNode == null) {
            encodedCharactersMap.put((char) root.getCharAscii(), s);
            return;
        }
        generateEncodedChars(root.leftNode, s + "0");
        generateEncodedChars(root.rightNode, s + "1");
    }

    private void saveEncodedFile(ArrayList<Integer> charAsciiList) {
        try {
            File file = new File(outputFilePath);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (int charAscii : charAsciiList) {
                String encodedChar = encodedCharactersMap.get((char) charAscii);
                bufferedWriter.write(encodedChar + ";");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Error during saving encoded file: " + e.getMessage());
        }
    }
}
