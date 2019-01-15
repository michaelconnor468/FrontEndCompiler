import java.io.*;

/**
 * Main class which pipelines stages of compilation and interacts with the user taking in file to be compiled. Creates
 * target file with compiled source code at the end of comilation.
 *
 * @author Michael Connor
 */
public class KrispCompiler
{
    public static void main(String[] args)
    {
        // Uncompiled Krisp source code
        String krispCode;

        // Gets code string from file opener object using given location in args[0]
        FileOpener fo = new FileOpener(args[0]);
        krispCode = fo.getContents();
    }
}
