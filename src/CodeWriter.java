import java.io.*;
import java.util.*;

/**
 * Writes compiled intermediate code into the given file.
 *
 * @author Michael Connor
 */
public class CodeWriter
{
    /**
     * Constructor takes file location as input, opens the file, copies contents of intermediate code, then
     * closes the file.
     *
     * @author Michael Connor
     * @param location - String containing the location of the file to be written in
     * @param compiledCode - String to be written into the file
     */
    public CodeWriter(String location, LinkedList<String> compiledCode)
    {
        File f = new File(location);
        try
        {
            BufferedWriter fout = new BufferedWriter(new FileWriter(f));
            int size = compiledCode.size();
            for(int i = 1; i <= size; i++)
            {
                fout.write(compiledCode.remove());
                fout.newLine();
            }
            fout.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch(IOException e)
        {
            System.out.println("An error has occurred in writing the lines of the file");
        }
    }
}
