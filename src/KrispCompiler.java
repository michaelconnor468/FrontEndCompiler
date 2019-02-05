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
     * requested file. First argument is input, second is output.
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

        Hashtable<String, String> idHashTable = new Hashtable<String, String>();
        TokenPreParser preParser = new TokenPreParser(krispCode);
        LinkedList<LinkedList<String>> preParsedCode = preParser.getPreParsedList();

        TokenGenerator tokenGenerator = new TokenGenerator(preParsedCode);
        LinkedList<LinkedList<Token>> tokenCode = tokenGenerator.generateTokenList();

        IntermediateCodeGenerator intermediateCodeGenerator = new IntermediateCodeGenerator(tokenCode);
        LinkedList<String> intermediateCode = intermediateCodeGenerator.generateAssembly();
        //Writes compiled code to given file

        try
        {
            CodeWriter codeWriter = new CodeWriter(args[1], intermediateCode);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println("You need to provide an output code file as the second command line argument");
        }

        //TokenGenerator tg = new TokenGenerator(preParsedCode);
    }
}
