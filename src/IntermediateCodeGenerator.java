import java.util.*;

public class IntermediateCodeGenerator
{
    LinkedList<LinkedList<Token>> tokenCode;
    LinkedList<String> assemblyCode;
    int assemblyLine = 0;

    /**
     * Initializes local variable tokenCode to linked list of tokens given to the code generator as an argument.
     *
     * @param tokenCode - a LinkedList of tokens to be compiled
     * @author Michael COnnor
     */
    public IntermediateCodeGenerator(LinkedList<LinkedList<Token>> tokenCode)
    {
        assemblyCode = new LinkedList<>();
        this.tokenCode = tokenCode;
    }

    /**
     * Generates a linked list of lines of assembly code for the given tokenCode list. Converts token list into a
     * series of MIPS microprocessor like assembly instructions
     *
     * @return assemblyCode - a linked list of compiled assembly code
     */
    public LinkedList<String> generateAssembly()
    {
        int currentLine = 0;
        Stack<Integer> gotoStack = new Stack<>();
        Stack<Integer> branchLineStack = new Stack<>();
        for(LinkedList<Token> line: tokenCode)
        {
            currentLine++;
            for(Token token: line)
            {

            }
        }
        return assemblyCode;
    }
}
