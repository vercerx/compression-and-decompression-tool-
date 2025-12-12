package src;
public class Main {
    public static void main(String[] args) {
        HuffCompression compression = new HuffCompression();
        compression.compress("C:\\Users\\vercer\\Desktop\\just.txt", "C:\\Users\\vercer\\Desktop\\Compressed.txt");
        compression.decompress("C:\\Users\\vercer\\Desktop\\Compressed.txt" ,"C:\\Users\\vercer\\Desktop\\just.txt");
    }
}
