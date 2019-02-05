import java.util.*;

/**
 * Generates a LinkedList of tokens from a LinkedList of Krisp code
 *
 * @author Michael Connor
 */
public class TokenGenerator
{
    private LinkedList<LinkedList<String>> preParsedCode;
    private LinkedList<LinkedList<Token>> tokenCode;

    /**
     * Uses the linked list of pre parsed string code to generate a list of the individual tokens for intermediate
     * code generation. Also fills in the symbol list when parsing identifier tokens to help with intermediate code
     * generation and error analysis
     *
     * @param preParsedCode - a linked list of linked lists of strings
     * @param hashTable - the symbol list for compilation
     * @author Michael Connor
     */
    public TokenGenerator(LinkedList<LinkedList<String>> preParsedCode, Hashtable hashTable)
    {
        this.preParsedCode = preParsedCode;
        this.tokenCode = new LinkedList<>();
    }

    /**
     * Uses the given preparsed code to generate a linked list of token objects which is stored in the tokenCode local
     * variable
     *
     * @author Michael Connor
     */
    public void generateTokenList()
    {
        for(LinkedList<String> line: preParsedCode)
        {

        }
    }

    /**
     * Returns the processed list of tokens
     *
     * @return tokenCode - processed list of tokens
     * @author Michael Connor
     */
    public LinkedList<LinkedList<Token>> getTokenList()
    {
        return tokenCode;
    }
}
