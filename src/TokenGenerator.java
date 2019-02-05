import java.util.*;
import java.util.regex.*;

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
     * code generation.
     *
     * @param preParsedCode - a linked list of linked lists of strings
     * @author Michael Connor
     */
    public TokenGenerator(LinkedList<LinkedList<String>> preParsedCode)
    {
        this.preParsedCode = preParsedCode;
        this.tokenCode = new LinkedList<>();
    }

    /**
     * Uses the given preparsed code to generate a linked list of token objects which is stored in the tokenCode local
     * variable. Returns tokenCode linked list.
     *
     * @return tokenCode - a linked list of individual tokens for intermediate code generation
     * @author Michael Connor
     */
    public LinkedList<LinkedList<Token>> generateTokenList()
    {
        LinkedList<Token> tokenLine;
        Pattern numRegex = Pattern.compile("[0-9]+");
        Matcher numMatcher;
        Pattern idRegex = Pattern.compile("(^[a-zA-Z]+)([0-9]*[a-zA-Z]*)*");
        Matcher idMatcher;
        for(LinkedList<String> line: preParsedCode)
        {
            tokenLine = new LinkedList<>();
            for(String word: line)
            {
                numMatcher = numRegex.matcher(word);
                idMatcher = idRegex.matcher(word);
                if(Token.isKey(word))
                {
                    tokenLine.add(new KeyToken(word));
                }
                else if(numMatcher.matches())
                {
                    tokenLine.add(new NumberToken(Integer.parseInt(word)));
                }
                else if(idMatcher.matches())
                {
                    tokenLine.add(new IdentifierToken(word));
                }
                else
                {
                    System.err.println("Syntax error. Tokens must start with a character and can contain characters" +
                            "and numbers only");
                }
            }
            tokenCode.add(tokenLine);
        }
        LinkedList<Token> tempTokenLine = new LinkedList<Token>();
        tempTokenLine.add(new EndToken());
        tokenCode.add(tempTokenLine);
        return tokenCode;
    }
}
