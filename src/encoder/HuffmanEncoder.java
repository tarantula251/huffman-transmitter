package encoder;

import huffman.HuffmanNode;
import huffman.HuffmanNodeComparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HuffmanEncoder {
    BufferedReader bufferedReader;
    HashMap<Character, String> encodedCharactersMap = new HashMap<>();

    public HuffmanEncoder(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public void encode() throws IOException {
        ArrayList<Integer> charAsciiList = new ArrayList<>();
        int charAscii = bufferedReader.read();
        while (charAscii != -1) {
            charAsciiList.add(charAscii);
            charAscii = bufferedReader.read();
        }
        Map<Integer, Long> occurrenceMap = charAsciiList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue(Comparator.naturalOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
        HuffmanNode rootNode = createHuffmanTree(occurrenceMap);
        generateEncodedChars(rootNode, "");
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
}
