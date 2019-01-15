import java.io.*;

/**
 * Opens a given file and returns a string consisting of the entire files contents. Prints to the console
 * if the file cannot be located.
 *
 * @author Michael Connor
 */
public class FileOpener
{
    // Holds the contents of the opened file.
    private String filecontents;

    /**
     * Constructor takes file location as input, opens the file, copies contents to filelocation global String, then
     * closes the file.
     *
     * @author Michael Connor
     * @param location - String containing the location of the file to be opened
     */
    public FileOpener(String location)
    {
        File f = new File(location);
        try
        {
            BufferedReader fin = new BufferedReader(new FileReader(f));
            String line = fin.readLine();
            while(line != null)
            {
                filecontents = filecontents + line;
                line = fin.readLine();
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch(IOException e)
        {
            System.out.println("An error has occurred in reading the lines of the file");
        }
    }

    public String getContents()
    {
        return filecontents;
    }
}
