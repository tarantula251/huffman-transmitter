package encoder;

import huffman.HuffmanNode;
import huffman.HuffmanNodeComparator;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HuffmanEncoder {
    BufferedReader bufferedReader;
    String outputFilePath;
    HashMap<Character, String> encodedCharactersMap = new HashMap<>();

    public HuffmanEncoder(BufferedReader bufferedReader, String outputFilePath) {
        this.bufferedReader = bufferedReader;
        this.outputFilePath = (!outputFilePath.endsWith(".txt")) ? outputFilePath + ".txt" : outputFilePath;
    }

    public void encode() throws IOException {
        ArrayList<Integer> charAsciiList = new ArrayList<>();
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
        HuffmanNode rootNode = createHuffmanTree(occurrenceMap);
        generateEncodedChars(rootNode, "");
        saveEncodedFile(charAsciiList);
    }

    private HuffmanNode createHuffmanTree(Map<Integer, Long> occurrenceMap) {
        System.out.println(occurrenceMap);
        int nodesCount = occurrenceMap.size();
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(nodesCount, new HuffmanNodeComparator());

        for (Integer charAscii : occurrenceMap.keySet()) {
            HuffmanNode node = new HuffmanNode();
            node.setCharAscii(charAscii);
            node.setOccurenceCount(occurrenceMap.get(charAscii));
            node.leftNode = null;
            node.rightNode = null;
            queue.add(node);
        }

        HuffmanNode root = null;
        while (queue.size() > 1) {
            HuffmanNode firstNode = queue.poll();
            HuffmanNode secondNode = queue.poll();
            HuffmanNode parentNode = new HuffmanNode();
            parentNode.setOccurenceCount(firstNode.getOccurenceCount() + secondNode.getOccurenceCount());
            parentNode.leftNode = firstNode;
            parentNode.rightNode = secondNode;

            root = parentNode;
            queue.add(root);
        }
        return root;
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
