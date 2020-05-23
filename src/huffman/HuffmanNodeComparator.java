package huffman;

import java.util.Comparator;

public class HuffmanNodeComparator implements Comparator<HuffmanNode> {
    @Override
    public int compare(HuffmanNode firstNode, HuffmanNode secondNode) {
        return (int) (firstNode.getOccurenceCount() - secondNode.getOccurenceCount());
    }
}
