package src;
public class ByteNode implements Comparable<ByteNode>{
    Byte data;
    int frequency;
    ByteNode left;
    ByteNode right;

    public ByteNode(Byte data, int weight)
    {
        this.data=data;
        this.frequency=weight;
    }
    public int compareTo(ByteNode o)
    {
        return this.frequency - o.frequency;
    }
}
