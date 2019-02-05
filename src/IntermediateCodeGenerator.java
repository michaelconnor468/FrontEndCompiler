import java.util.*;

public class IntermediateCodeGenerator
{
    private LinkedList<LinkedList<Token>> tokenCode;
    private LinkedList<Token> tokenLine;
    private LinkedList<String> assemblyCode;
    private Token currentToken;
    private Token lookaheadToken;
    private int assemblyLine = 0;
    private int currentLine = 0;

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
        if(tokenCode.isEmpty())
        {
            System.err.println("Error: Nothing to compile.");
            System.exit(-1);
        }
        tokenLine = tokenCode.pop();
        if(tokenLine.size() < 2)
        {
            System.err.println("Syntax Error");
            System.exit(-1);
        }
        currentToken = tokenLine.pop();
        lookaheadToken = tokenLine.pop();
    }

    /**
     * Generates a linked list of lines of assembly code for the given tokenCode list. Converts token list into a
     * series of MIPS microprocessor like assembly instructions
     *
     * @return assemblyCode - a linked list of compiled assembly code
     */
    public LinkedList<String> generateAssembly()
    {

    }
}
