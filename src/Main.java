import encoder.HuffmanEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length != 3) {
            System.err.println("Wrong number of arguments.\n");
            System.out.println("Syntax: [encode|decode] input_file_path output_file_path\n");
            return;
        }

        if (!args[1].endsWith(".txt")) {
            System.err.println("Wrong extension of input file.\n");
            System.out.println("Application allows only text files.\n");
            return;
        }

        if (args[0].equals("encode")) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(args[1]));
            HuffmanEncoder encoder = new HuffmanEncoder(bufferedReader, args[2]);
            encoder.encode();
        }
    }
}
