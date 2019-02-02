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
     * using the krispCode the class was initialized with. Also separates parentheses, brackets and end of line
     * characters.
     *
     * @return returns a pre-parsed list where every string separated by a space has been isolated
     * @author Michael Connor
     */
    public LinkedList<LinkedList<String>> getPreParsedList()
    {
        LinkedList<String> lineList = new LinkedList<>();
        for (String temp : krispCode)
        {
            StringBuilder tempsb = new StringBuilder(temp);
            while((tempsb.length() != 0)&&(tempsb.charAt(0) != '\n'))
            {
                if((tempsb.charAt(0) == '(') || (tempsb.charAt(0) == ')') || (tempsb.charAt(0) == '}') ||
                        (tempsb.charAt(0) == '{') || (tempsb.charAt(0) == ';'))
                {
                    if(stringBuffer.toString().length() != 0)
                    {
                        lineList.add(stringBuffer.toString());
                        stringBuffer = new StringBuilder();
                    }
                    stringBuffer.append(tempsb.charAt(0));
                    tempsb.deleteCharAt(0);
                    lineList.add(stringBuffer.toString());
                    stringBuffer = new StringBuilder();
                }
                else if(tempsb.charAt(0) != ' ')
                {
                    stringBuffer.append(tempsb.charAt(0));
                    tempsb.deleteCharAt(0);
                    if((stringBuffer.length() != 0)&&(temp.length() == 0))
                    {
                        lineList.add(stringBuffer.toString());
                        stringBuffer = new StringBuilder();
                    }
                }
                else
                {
                    if(stringBuffer.toString().length() != 0)
                    {
                        lineList.add(stringBuffer.toString());
                        stringBuffer = new StringBuilder();
                    }
                    tempsb.deleteCharAt(0);
                }
            }
            if(stringBuffer.toString().length() != 0)
            {
                lineList.add(stringBuffer.toString());
                stringBuffer = new StringBuilder();
            }
            separatedCode.add(lineList);
            lineList = new LinkedList<>();
        }
        return separatedCode;
    }
}
