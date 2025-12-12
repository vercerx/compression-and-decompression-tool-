package src;
public class Main {
    public static void main(String[] args) {
        HuffCompression compression = new HuffCompression();
        //compresstion method
        compression.compress("Path of the file you want to compress", "Path of where the compressed file will be");

        //decompressed method
        //compression.decompress("Path of the file you want to decompress" ,"Path of where the decompressed file will be");
    }
}

