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
    private int vregCount = 0;
    private int tregCount = 0;
    private final String returnReg = "%rtr";
    private Hashtable<String, Integer> identifierHash;

    /**
     * Initializes local variable tokenCode to linked list of tokens given to the code generator as an argument.
     *
     * @param tokenCode - a LinkedList of tokens to be compiled
     * @author Michael COnnor
     */
    public IntermediateCodeGenerator(LinkedList<LinkedList<Token>> tokenCode)
    {
        assemblyCode = new LinkedList<>();
        identifierHash = new Hashtable<>();
        this.tokenCode = tokenCode;
        if(tokenCode.isEmpty())
        {
            System.err.println("Error: Nothing to compile.");
            System.exit(-1);
        }
    }

    /**
     * Generates a linked list of lines of assembly code for the given tokenCode list. Converts token list into a
     * series of MIPS microprocessor like assembly instructions
     *
     * @return assemblyCode - a linked list of compiled assembly code
     */
    public LinkedList<String> generateAssembly()
    {
        while(!tokenCode.isEmpty())
        {
            tokenLine = tokenCode.pop();
            if(tokenLine.size() < 1)
            {
                System.err.println("Syntax Error");
                System.exit(-1);
            }
            currentToken = tokenLine.pop();
            if(currentToken.getTokenType().equals("End"))
            {
                break;
            }
            else if(tokenLine.size() < 1)
            {
                System.err.println("Syntax Error");
                System.exit(-1);
            }
            lookaheadToken = tokenLine.pop();
            if (currentToken.getTokenType().equals("Id"))
            {
                parseAssign();
            }
        }
        return assemblyCode;
    }

    /**
     * Used to parse a statement type within the context free grammar of the following form,
     * stm -> asgn stm | branch stm
     * stm -> asgn | branch
     *
     * @author Michael Connor
     */
    private void parseStatement()
    {

    }

    /**
     * Used to parse a assign type within the context free grammar of the following form,
     * asgn -> identifier assign expr;
     * asgn -> identifier assign bool;
     *
     * @author Michael Connor
     */
    private void parseAssign()
    {
        IdentifierToken tempToken = (IdentifierToken) this.currentToken;
        String assignVariable = tempToken.getIdentifier();
        int assignVariableReg;
        if(identifierHash.containsKey(assignVariable))
        {
            assignVariableReg = identifierHash.get(assignVariable);
        }
        else
        {
            identifierHash.put(assignVariable, vregCount);
            assignVariableReg = vregCount;
            vregCount++;
        }
        if((!lookaheadToken.getTokenType().equals("Key"))||(tokenLine.isEmpty()))
        {
            System.out.println("Syntax Error");
            System.exit(-1);
        }
        currentToken = lookaheadToken;
        lookaheadToken = tokenLine.pop();
        KeyToken testToken = (KeyToken)currentToken;
        String lookaheadType = lookaheadToken.getTokenType();
        if((!testToken.getKeyType().equals("assign"))||(!(lookaheadType.equals("Num")||lookaheadType.equals("Id"))))
        {
            System.out.println("Syntax Error");
            System.exit(-1);
        }
        else if(tokenLine.isEmpty())
        {
            if(lookaheadType.equals("Num"))
            {
                NumberToken numToken = (NumberToken)lookaheadToken;
                assemblyCode.add("movc  &v" + Integer.toString(assignVariableReg) + ", @" +
                        Integer.toString(numToken.getNumber()));
                assemblyLine++;
            }
            else
            {
                IdentifierToken idToken = (IdentifierToken)lookaheadToken;
                if(!identifierHash.containsKey(idToken.getIdentifier()))
                {
                    System.out.println("Syntax Error: Declare" + ((IdentifierToken) lookaheadToken).getIdentifier() +
                            "before using it");
                    System.exit(-1);
                }
                assemblyCode.add("movr  &v" + Integer.toString(assignVariableReg) + ", $v" +
                        Integer.toString(identifierHash.get(idToken.getIdentifier())));
                assemblyLine++;
            }
        }
        else
        {
            currentToken = lookaheadToken;
            lookaheadToken = tokenLine.pop();
            if(!lookaheadToken.getTokenType().equals("Key"))
            {
                System.out.println("Syntax Error");
                System.exit(-1);
            }
            KeyToken keyToken = (KeyToken)lookaheadToken;
            if(Token.isBoolOperator(keyToken.getKeyType()))
            {
                parseBool();
                assemblyCode.add("movr &v" + Integer.toString(assignVariableReg) + ", $"+ returnReg );
                assemblyLine++;
            }
            parseExpression();
            assemblyCode.add("movr &v" + Integer.toString(assignVariableReg) + ", $"+ returnReg );
            assemblyLine++;
        }
    }

    /**
     * Used to parse an expression type within the context free grammar of the following form,
     * expr -> number [+-] expr | identifier [+-] expr
     * expr -> (expr) | term
     *
     * @author Michael Connor
     */
    private void parseExpression()
    {

    }

    /**
     * Used to parse a boolean expression type within the context free grammar of the following form,
     * bool -> bool [and,or,nand,nor] bool
     * bool -> [number, identifier] [>,<,eq] [number, identifier]
     * bool -> (bool)
     *
     * @author Michael Connor
     */
    private void parseBool()
    {

    }
}
