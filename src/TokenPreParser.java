import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.util.*;

/**
 * Takes in a linked list of krisp code and isolates each individual string in another linked list to make things
 * easier for the token parser.
 *
 * @author michael
 */
public class TokenPreParser
{
    LinkedList<String> krispCode;
    LinkedList<LinkedList<String>> separatedCode;
    StringBuilder stringBuffer;
    /**
     * Initialises local variable krispCode to passed in linked list ready to be parsed. Initialises stringBuffer and
     * seperatedCode linked list.
     *
     * @param krispCode - LinkedList ready to be parsed.
     * @author Michael COnnor
     */
    public TokenPreParser(LinkedList<String> krispCode)
    {
        this.krispCode = krispCode;
        stringBuffer = new StringBuilder();
        separatedCode = new LinkedList<>();
    }

    /**
     * Returns the pre-parsed linked list in which every string separated by a space has been isolated. Performs this
     * using the krispCode the class was initialized with.
     *
     * @return returns a pre-parsed list where every string separated by a space has been isolated
     * @author Michael Connor
     */
    public LinkedList<LinkedList<String>> getPreParsedList()
    {
        int currentChar = 0;
        for (String temp : krispCode)
        {
            StringBuilder tempsb = new StringBuilder(temp);
            if(tempsb.charAt(0) != ' ')
            {
                //stringBuffer.append(tempsb.charAt(currentChar));
                currentChar++;
            }
            else
            {
                //String tempString = stringBuffer.toString();
                currentChar++;
            }
            currentChar = 0;
        }
    }
}
