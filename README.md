# Huffman Compression Project

A Java implementation of Huffman compression algorithm with custom data structures. This project demonstrates the use of priority queues, binary trees, and dynamic arrays to implement file compression and decompression.

## Overview

This project implements a complete Huffman coding compression system that can compress and decompress files. Huffman coding is a lossless data compression algorithm that assigns variable-length codes to input characters based on their frequencies, with more frequent characters getting shorter codes.


### Core Classes

#### 1. `ArrayList<T>`
A generic, dynamically resizing array implementation that serves as the foundation for other data structures.

**Features:**
- Dynamic capacity management (doubles when full)
- Generic type support
- Methods: `add()`, `get()`, `set()`, `remove()`, `clear()`, `size()`
- Index bounds checking
- Custom `toString()` method

**Time Complexity:**
- `add()`: O(1) amortized
- `get()`, `set()`: O(1)
- `remove()`: O(n)

#### 2. `ByteNode`
A node class for building Huffman binary trees.

**Properties:**
- `data`: The byte value (null for internal nodes)
- `frequency`: The frequency/weight of the byte
- `left`, `right`: Child nodes in the Huffman tree
- Implements `Comparable<ByteNode>` for priority queue ordering

#### 3. `MinPriorityQueue<T>`
A min-heap priority queue implementation using the custom `ArrayList`.

**Features:**
- Generic type with `Comparable` constraint
- Min-heap structure (smallest element at root)
- Methods: `add()`, `poll()`, `len()`, `isEmpty()`
- Internal heap operations: `siftUp()`, `siftDown()`

**Time Complexity:**
- `add()`: O(log n)
- `poll()`: O(log n)

#### 4. `HuffCompression`
The main compression/decompression engine implementing Huffman coding algorithm.

**Key Methods:**

- **`compress(String src, String dst)`**: Compresses a source file to a destination file
  - Reads input file as bytes
  - Builds Huffman tree based on byte frequencies
  - Generates Huffman codes
  - Compresses data and saves with code table

- **`decompress(String src, String dst)`**: Decompresses a compressed file
  - Reads compressed file and code table
  - Reconstructs original data using Huffman codes
  - Writes decompressed data to destination

**Internal Process:**
1. **Frequency Analysis**: Counts frequency of each byte in the input
2. **Tree Construction**: Builds Huffman tree using min priority queue
3. **Code Generation**: Traverses tree to generate binary codes for each byte
4. **Compression**: Converts input bytes to Huffman codes and packs into bytes
5. **Decompression**: Reverses the process using the stored code table

#### 5. `Main`
Example driver class demonstrating compression and decompression usage.

**Note**: Update the file paths in `Main.java` to match your system before running.

## How It Works

### Compression Process

1. **Frequency Counting**: Analyze input file to count frequency of each byte
2. **Priority Queue**: Create `ByteNode` for each unique byte and add to min priority queue
3. **Tree Building**: Repeatedly combine two nodes with lowest frequencies until one root remains
4. **Code Assignment**: Traverse tree to assign binary codes (0 for left, 1 for right)
5. **Encoding**: Replace each byte with its Huffman code
6. **Packing**: Convert bit string to byte array
7. **Saving**: Store compressed data, code table, and bit string length

### Decompression Process

1. **Loading**: Read compressed data, code table, and bit string length
2. **Bit Conversion**: Convert compressed bytes back to bit string
3. **Decoding**: Use code table to match bit sequences to original bytes
4. **Reconstruction**: Rebuild original file from decoded bytes


**Important**: Before running, update the file paths in `Main.java`:

```java
compression.compress("path/to/input.txt", "path/to/compressed.txt");
compression.decompress("path/to/compressed.txt", "path/to/decompressed.txt");
```



## Data Structures Used

- **Dynamic Array**: Custom `ArrayList` implementation
- **Min Heap**: `MinPriorityQueue` for efficient node selection
- **Binary Tree**: Huffman tree structure via `ByteNode`
- **HashMap**: For storing byte-to-code mappings (Java standard library)

## Algorithm Complexity

- **Compression**: O(n log n) where n is the number of unique bytes
- **Decompression**: O(m) where m is the length of the compressed bit string
- **Space**: O(k) where k is the number of unique bytes (for code table)

## Notes

- The compressed file format stores:
  1. Compressed byte array
  2. Huffman code table (HashMap)
  3. Original bit string length (for proper decompression)

- Files are saved using Java's `ObjectOutputStream` for serialization


