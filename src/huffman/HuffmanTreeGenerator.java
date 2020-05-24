package huffman;

import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTreeGenerator {

    public PriorityQueue<HuffmanNode> createHuffmanTree(Map<Integer, Long> occurrenceMap) {
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
        return queue;
    }
}
