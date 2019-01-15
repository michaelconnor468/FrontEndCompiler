import java.io.*;
import java.util.*;

/**
 * Main class which pipelines stages of compilation and interacts with the user taking in file to be compiled. Creates
 * target file with compiled source code at the end of comilation.
 *
 * @author Michael Connor
 */
public class KrispCompiler
{
    /**
     * Calls on all operations of the front end compiler using code from given file and outputs compiled code into
     * requested file.
     *
     * @param args - File read and final product write location
     * @author Michael Connor
     */
    public static void main(String[] args)
    {
        LinkedList<String> krispCode = new LinkedList<String>();
        //Reads to be compiled code from given file.
        try
        {
            FileOpener fo = new FileOpener(args[0]);
            krispCode = fo.getContents();
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println("You need to provide an input code file as the first command line argument");
        }


        //Writes compiled code to given file.
        try
        {
            CodeWriter codeWriter = new CodeWriter(args[1], krispCode);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println("You need to provide an output code file as the second command line argument");
        }
    }
}
