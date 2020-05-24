package huffman;

public class HuffmanNode {
    private long occurenceCount;
    private int charAscii;

    public HuffmanNode leftNode;
    public HuffmanNode rightNode;

    public long getOccurenceCount() {
        return occurenceCount;
    }

    public void setOccurenceCount(long occurenceCount) {
        this.occurenceCount = occurenceCount;
    }

    public int getCharAscii() {
        return charAscii;
    }

    public void setCharAscii(int charAscii) {
        this.charAscii = charAscii;
    }

    public String toString() {
        return "count : " + occurenceCount +
                ", char : " + (char) charAscii +
                ", leftNode : " + nodeToString(leftNode) +
                ", rightNode : " + nodeToString(rightNode) + "\n";
    }

    public String nodeToString(HuffmanNode node) {
        Long occurenceCount = Long.MIN_VALUE;
        char charAscii = Character.MIN_VALUE;
        if (node != null) {
            occurenceCount = node.getOccurenceCount();
            charAscii = (char) node.getCharAscii();
        }
        return "[ count : " + occurenceCount +
                ", char : " + charAscii + " ]";
    }
}
