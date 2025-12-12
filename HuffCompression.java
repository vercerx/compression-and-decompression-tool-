import java.io.*;
import java.util.HashMap;
public class HuffCompression {
    private static StringBuilder sb = new StringBuilder();
    private static HashMap<Byte, String> huffmap = new HashMap<>();

    public static void compress(String src, String dst) {
        try {
            FileInputStream inStream = new FileInputStream(src);
            byte[] b = new byte[inStream.available()];
            inStream.read(b);
            byte[] huffmanBytes = createZip(b);
            int bitStringLength = calculateBitStringLength(b, huffmap);
            OutputStream outStream = new FileOutputStream(dst);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeObject(huffmanBytes);
            objectOutStream.writeObject(huffmap);
            objectOutStream.writeObject(bitStringLength);
            inStream.close();
            objectOutStream.close();
            outStream.close();
        } catch (Exception e) { }
    }
    
    private static int calculateBitStringLength(byte[] bytes, HashMap<Byte, String> huffCodes) {
        int length = 0;
        for (byte b : bytes) {
            length += huffCodes.get(b).length();
        }
        return length;
    }

    private static byte[] createZip(byte[] bytes) {
        MinPriorityQueue<ByteNode> nodes = getByteNodes(bytes);
        ByteNode root = createHuffmanTree(nodes);
        HashMap<Byte, String> huffmanCodes = getHuffCodes(root);
        byte[] huffmanCodeBytes = zipBytesWithCodes(bytes, huffmanCodes);
        return huffmanCodeBytes;
    }

    private static MinPriorityQueue<ByteNode> getByteNodes(byte[] bytes) {
        MinPriorityQueue<ByteNode> nodes = new MinPriorityQueue<ByteNode>();
        HashMap<Byte, Integer> tempMap = new HashMap<>();
        for (byte b : bytes) {
            Integer value = tempMap.get(b);
            if (value == null)
                tempMap.put(b, 1);
            else
                tempMap.put(b, value + 1);
        }
        for (HashMap.Entry<Byte, Integer> entry : tempMap.entrySet()) {
            Byte key = entry.getKey();
            Integer value = entry.getValue();

            ByteNode node = new ByteNode(key, value);
            nodes.add(node);
        }

        return nodes;

    }

    private static ByteNode createHuffmanTree(MinPriorityQueue<ByteNode> nodes) {
        while (nodes.len() > 1) {
            ByteNode left = nodes.poll();
            ByteNode right = nodes.poll();
            ByteNode parent = new ByteNode(null, left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            nodes.add(parent);
        }
        return nodes.poll();
    }

    private static HashMap<Byte, String> getHuffCodes(ByteNode root) {
        if (root == null) return null;
        getHuffCode(root.left, "0", sb);
        getHuffCode(root.right, "1", sb);
        return huffmap;
    }

    private static void getHuffCode(ByteNode node, String code, StringBuilder sb1) {
        StringBuilder sb2 = new StringBuilder(sb1);
        sb2.append(code);
        if (node != null) {
            if (node.data == null) {
                getHuffCode(node.left, "0", sb2);
                getHuffCode(node.right, "1", sb2);
            } else
                huffmap.put(node.data, sb2.toString());
        }
    }

    private static byte[] zipBytesWithCodes(byte[] bytes, HashMap<Byte, String> huffCodes) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte b : bytes)
            strBuilder.append(huffCodes.get(b));

        int length=(strBuilder.length()+7)/8;
        byte[] huffCodeBytes = new byte[length];
        int idx = 0;
        for (int i = 0; i < strBuilder.length(); i += 8) {
            String strByte;
            if (i + 8 > strBuilder.length())
                strByte = strBuilder.substring(i);
            else strByte = strBuilder.substring(i, i + 8);
            huffCodeBytes[idx] = (byte) Integer.parseInt(strByte, 2);
            idx++;
        }
        return huffCodeBytes;
    }

    public static void decompress(String src, String dst) {
        try {
            FileInputStream inStream = new FileInputStream(src);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);
            byte[] huffmanBytes = (byte[]) objectInStream.readObject();
            HashMap<Byte, String> huffmanCodes =
                    (HashMap<Byte, String>) objectInStream.readObject();

            Integer bitStringLength = null;
            bitStringLength = (Integer) objectInStream.readObject();


            byte[] bytes = decomp(huffmanCodes, huffmanBytes, bitStringLength);
            OutputStream outStream = new FileOutputStream(dst);
            outStream.write(bytes);
            inStream.close();
            objectInStream.close();
            outStream.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static byte[] decomp(HashMap<Byte, String> huffmanCodes, byte[] huffmanBytes, Integer bitStringLength) {
        StringBuilder sb1 = new StringBuilder();
        for (int i=0; i<huffmanBytes.length; i++) {
            byte b = huffmanBytes[i];
            boolean flag = (i == huffmanBytes.length - 1);
            String bits = convertbyteInBit(!flag, b);
            if (flag && bitStringLength != null && bitStringLength > 0) {
                int bitsUsed = bitStringLength % 8;
                if (bitsUsed == 0) bitsUsed = 8;
                if (bits.length() > bitsUsed) {
                    bits = bits.substring(bits.length() - bitsUsed);
                }
            }
            sb1.append(bits);
        }
        
        if (bitStringLength != null && bitStringLength > 0 && sb1.length() > bitStringLength) {
            sb1.setLength(bitStringLength);
        }
        
        HashMap<String, Byte> map = new HashMap<>();
        for (HashMap.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        
        return decompWithTrialLength(sb1, map);
    }
    

    private static byte[] decompWithTrialLength(StringBuilder sb1, HashMap<String, Byte> map) {
        int originalLength = sb1.length();
        for (int tryLength = originalLength; tryLength >= originalLength - 8 && tryLength > 0; tryLength--) {
            StringBuilder testSb = new StringBuilder(sb1);
            testSb.setLength(tryLength);

            try {
                java.util.List<Byte> list = new java.util.ArrayList<>();
                int i = 0;
                while (i < testSb.length()) {
                    int count = 1;
                    boolean flag = true;
                    Byte b = null;
                    while (flag && i + count <= testSb.length()) {
                        String key = testSb.substring(i, i + count);
                        b = map.get(key);
                        if (b == null) count++;
                        else flag = false;
                    }
                    if (flag) {
                        break;
                    }
                    list.add(b);
                    i += count;
                }
                if (i == testSb.length()) {
                    byte b[] = new byte[list.size()];
                    for (int j = 0; j < b.length; j++)
                        b[j] = list.get(j);
                    return b;
                }
            } catch (Exception e) {
                continue;
            }
        }
        throw new RuntimeException("Invalid compressed data: unable to decode with any valid length");
    }

    private static String convertbyteInBit(boolean flag, byte b) {
        int byte0 = b;
        if (flag) byte0 |= 256;
        String str0 = Integer.toBinaryString(byte0);
        if (flag || byte0 < 0)
            return str0.substring(str0.length() - 8);
        else return str0;
    }

}