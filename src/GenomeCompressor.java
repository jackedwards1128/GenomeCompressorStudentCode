/******************************************************************************
 *  Compilation:  javac GenomeCompressor.java
 *  Execution:    java GenomeCompressor - < input.txt   (compress)
 *  Execution:    java GenomeCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   genomeTest.txt
 *                virus.txt
 *
 *  Compress or expand a genomic sequence using a 2-bit code.
 ******************************************************************************/

/**
 *  The {@code GenomeCompressor} class provides static methods for compressing
 *  and expanding a genomic sequence using a 2-bit code.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Jack Michael Edwards
 *  @author GOURDON OHTANI (moral support)
 */
public class GenomeCompressor {

    /**
     * Reads a sequence of 8-bit extended ASCII characters over the alphabet
     * { A, C, T, G } from standard input; compresses and writes the results to standard output.
     */

    private static final int BITS_PER_NUCLEOTIDE = 2;

    // Binary (2-bit) codes for each nucleotide
    public static final char bitsA = 0x00;
    public static final char bitsT = 0x01;
    public static final char bitsC = 0x02;
    public static final char bitsG = 0x03;

    // These maps are used to convert between binary, base-4, and chars in constant time
    public static final char[] mapToBits = {bitsA, bitsT, bitsC, bitsG};
    private static final char[] mapToChar = {'A', 'T', 'C', 'G'};
    private static int[] bitMap = {0,-1,2,-1,-1,-1,3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,1,-1,-1,-1,-1,-1};

    public static void compress() {

        // Read in the string and find the first instance of TARGET
        String s = BinaryStdIn.readString();
        int length = s.length();

        // Create header
        BinaryStdOut.write(length);

        // Write out each character
        for (int i = 0; i < length; i++) {
            // Uses map to convert from char to 0,1,2, or 3 for A,T,C,G respectively
            int encoded = bitMap[(s.charAt(i)) - 'A'];

            // Use a map to convert the base-4 code (0,1,2, or 3) into 2 bits to write into the file
            BinaryStdOut.write(mapToBits[encoded], BITS_PER_NUCLEOTIDE);
        }

        BinaryStdOut.close();
    }

    /**
     * Reads a binary sequence from standard input; expands and writes the results to standard output.
     */
    public static void expand() {

        /*
        The first 4 bytes of the file will represent the length. We do this because the multiple of 2-bits that
         makes up the file might not neatly fit into an integer amount of bytes, so the compiler automatically
         will add zeros to the end of the data. Differentiating between these zeros and our data is impossible
         because we've used every combination of 2 bits possible, so we must provide our length to our expander
         ahead of time to understand where exactly the data truly ends.
         */

        int length = BinaryStdIn.readInt();

        // For each pair of bits, read them and use a map to convert them into the proper char to write in our output
        for (int i = 0; i < length; i++) {
            int nucleotide = BinaryStdIn.readChar(2);
            char chToWrite = mapToChar[nucleotide];
            BinaryStdOut.write(chToWrite);
        }

        BinaryStdOut.close();
    }

    /**
     * Main, when invoked at the command line, calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}