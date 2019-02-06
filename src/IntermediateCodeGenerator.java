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
    private int branchCount;

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
            currentLine++;
            if(tokenLine.size() < 1)
            {
                System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            currentToken = tokenLine.pop();
            if(currentToken.getTokenType().equals("End") ||
                    (currentToken.getTokenType().equals("Key")&&((KeyToken)currentToken).getKeyType().equals("}")))
            {
                break;
            }
            else if((tokenLine.size() < 1)&&(!currentToken.getTokenType().equals("Key")))
            {
                System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            lookaheadToken = tokenLine.pop();
            if (currentToken.getTokenType().equals("Id"))
            {
                parseAssign();
                continue;
            }
            KeyToken keyToken;
            if(!currentToken.getTokenType().equals("Key"))
            {
                System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            keyToken = (KeyToken)currentToken;
            if(keyToken.getKeyType().equals("if"))
            {
                if((!lookaheadToken.getTokenType().equals("Key"))&&(!((KeyToken)lookaheadToken).getKeyType().equals("(")))
                {
                    System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                else if(tokenLine.size() < 2)
                {
                    System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                currentToken = tokenLine.pop();
                lookaheadToken = tokenLine.pop();
                parseBool();
                if((!currentToken.getTokenType().equals("Key"))&&(!((KeyToken)currentToken).getKeyType().equals(")")))
                {
                    System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyLine++;
                int currentBranch = branchCount;
                assemblyCode.add("brze $"+returnReg+", @BRANCH"+Integer.toString(branchCount));
                branchCount++;
                if((!lookaheadToken.getTokenType().equals("Key"))&&(!((KeyToken)lookaheadToken).getKeyType().equals("{")))
                {
                    System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                if(tokenCode.isEmpty())
                {
                    System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                generateAssembly();
                if((!currentToken.getTokenType().equals("Key"))&&(!((KeyToken)currentToken).getKeyType().equals("}")))
                {
                    System.err.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyCode.add("");
                assemblyCode.add("BRANCH"+Integer.toString(currentBranch)+":");
                assemblyLine += 2;
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
            System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
            System.exit(-1);
        }
        currentToken = lookaheadToken;
        lookaheadToken = tokenLine.pop();
        KeyToken testToken = (KeyToken)currentToken;
        String lookaheadType = lookaheadToken.getTokenType();
        if((!testToken.getKeyType().equals("assign"))||(!(lookaheadType.equals("Num")||lookaheadType.equals("Id"))))
        {
            System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
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
                    System.out.println("Syntax Error: Declare variable " +
                            "before using it, Line " + Integer.toString(currentLine));
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
                System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            Stack<Token> tokenStack = new Stack<>();
            LinkedList<Token> tempTokenLine = new LinkedList<>();
            for(Token token : tokenLine)
            {
                tokenStack.push(token);
            }
            for(Token token : tokenStack)
            {
                tempTokenLine.add(token);
            }
            tokenLine = tempTokenLine;
            parseExpression();
            assemblyCode.add("movr &v" + Integer.toString(assignVariableReg) + ", $" + returnReg);
            assemblyLine++;
        }
    }

    /**
     * Used to parse an expression type within the context free grammar of the following form,
     * expr -> number [+-*\] expr | identifier [+-*\] expr
     *
     * @author Michael Connor
     */
    private void parseExpression()
    {
        Token firstToken = currentToken;
        NumberToken numToken = null;
        IdentifierToken idToken = null;
        boolean isNum = false;
        if((!firstToken.getTokenType().equals("Num"))&&(!firstToken.getTokenType().equals("Id")))
        {
            System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
            System.exit(-1);
        }
        if(firstToken.getTokenType().equals("Num"))
        {
            isNum = true;
            numToken = (NumberToken)firstToken;
        }
        else
        {
            idToken = (IdentifierToken)firstToken;
        }
        KeyToken keyToken = (KeyToken) lookaheadToken;
        currentToken = lookaheadToken;
        if(tokenLine.isEmpty())
        {
            System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
            System.exit(-1);
        }
        lookaheadToken = tokenLine.pop();
        if(lookaheadToken.getTokenType().equals("Num"))
        {
            NumberToken num1Token = (NumberToken)lookaheadToken;
            assemblyCode.add("movc &"+returnReg+", @"+Integer.toString(num1Token.getNumber()));
            assemblyLine++;
        }
        else
        {
            IdentifierToken id1Token = (IdentifierToken)lookaheadToken;
            if(!identifierHash.containsKey(id1Token.getIdentifier()))
            {
                System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            assemblyCode.add("movr &"+returnReg+", $v"+identifierHash.get(id1Token.getIdentifier()));
            assemblyLine++;
        }
        if(tokenLine.isEmpty())
        {
            if(keyToken.getKeyType().equals("+"))
            {
                if(isNum)
                {
                    assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                    assemblyCode.add("addr &"+returnReg+", $t0, $"+returnReg);
                    assemblyLine++;
                    assemblyLine++;
                }
                else
                {
                    if(!identifierHash.containsKey(idToken.getIdentifier()))
                    {
                        System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                        System.exit(-1);
                    }
                    assemblyCode.add("addr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                            ", $"+returnReg);
                    assemblyLine++;
                }
            }
            else if(keyToken.getKeyType().equals("-"))
            {
                if(isNum)
                {
                    assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                    assemblyCode.add("subr &"+returnReg+", $t0, $"+returnReg);
                    assemblyLine++;
                    assemblyLine++;
                }
                else
                {
                    if(!identifierHash.containsKey(idToken.getIdentifier()))
                    {
                        System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                        System.exit(-1);
                    }
                    assemblyCode.add("subr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                            ", $"+returnReg);
                    assemblyLine++;
                }
            }
            else if(keyToken.getKeyType().equals("*"))
            {
                if(isNum)
                {
                    assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                    assemblyCode.add("mulr &"+returnReg+", $t0, $"+returnReg);
                    assemblyLine++;
                    assemblyLine++;
                }
                else
                {
                    if(!identifierHash.containsKey(idToken.getIdentifier()))
                    {
                        System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                        System.exit(-1);
                    }
                    assemblyCode.add("mulr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                            ", $"+returnReg);
                    assemblyLine++;
                }
            }
            else if(keyToken.getKeyType().equals("/"))
            {
                if(isNum)
                {
                    assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                    assemblyCode.add("divr &"+returnReg+", $t0, $"+returnReg);
                    assemblyLine++;
                    assemblyLine++;
                }
                else
                {
                    if(!identifierHash.containsKey(idToken.getIdentifier()))
                    {
                        System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                        System.exit(-1);
                    }
                    assemblyCode.add("divr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                            ", $"+returnReg);
                    assemblyLine++;
                }
            }
            else
            {
                System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            return;
        }
        currentToken = lookaheadToken;
        lookaheadToken = tokenLine.pop();
        parseExpression();
        if(keyToken.getKeyType().equals("+"))
        {
            if(isNum)
            {
                assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                assemblyCode.add("addr &"+returnReg+", $t0, $"+returnReg);
                assemblyLine++;
                assemblyLine++;
            }
            else
            {
                if(!identifierHash.containsKey(idToken.getIdentifier()))
                {
                    System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyCode.add("addr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                        ", $"+returnReg);
                assemblyLine++;
            }
        }
        else if(keyToken.getKeyType().equals("-"))
        {
            if(isNum)
            {
                assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                assemblyCode.add("subr &"+returnReg+", $t0, $"+returnReg);
                assemblyLine++;
                assemblyLine++;
            }
            else
            {
                if(!identifierHash.containsKey(idToken.getIdentifier()))
                {
                    System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyCode.add("subr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                        ", $"+returnReg);
                assemblyLine++;
            }
        }
        else if(keyToken.getKeyType().equals("*"))
        {
            if(isNum)
            {
                assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                assemblyCode.add("mulr &"+returnReg+", $t0, $"+returnReg);
                assemblyLine++;
                assemblyLine++;
            }
            else
            {
                if(!identifierHash.containsKey(idToken.getIdentifier()))
                {
                    System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyCode.add("mulr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                        ", $"+returnReg);
                assemblyLine++;
            }
        }
        else if(keyToken.getKeyType().equals("/"))
        {
            if(isNum)
            {
                assemblyCode.add("movc &t0, @" + Integer.toString(numToken.getNumber()));
                assemblyCode.add("divr &"+returnReg+", $t0, $"+returnReg);
                assemblyLine++;
                assemblyLine++;
            }
            else
            {
                if(!identifierHash.containsKey(idToken.getIdentifier()))
                {
                    System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyCode.add("divr &"+returnReg+", $v" + identifierHash.get(idToken.getIdentifier())+
                        ", $"+returnReg);
                assemblyLine++;
            }
        }
        else
        {
            System.out.println("Syntax Error, Line " + Integer.toString(currentLine));
            System.exit(-1);
        }
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
        NumberToken numToken;
        KeyToken keyToken;
        IdentifierToken idToken;
        if(currentToken.getTokenType().equals("Key"))
        {
            keyToken = (KeyToken)currentToken;
            if(!keyToken.getKeyType().equals("("))
            {
                System.out.println("Syntax Error, Line " +
                        Integer.toString(currentLine));
                System.exit(-1);
            }
            else if(tokenLine.isEmpty())
            {
                System.out.println("Syntax Error, Line " +
                        Integer.toString(currentLine));
                System.exit(-1);
            }
            currentToken = lookaheadToken;
            lookaheadToken = tokenLine.pop();
            parseBool();
            if(!currentToken.getTokenType().equals("Key"))
            {
                System.out.println("Syntax Error, Line " +
                        Integer.toString(currentLine));
                System.exit(-1);
            }
            keyToken = (KeyToken)currentToken;
            if(!keyToken.getKeyType().equals(")"))
            {
                System.out.println("Syntax Error, Line " +
                        Integer.toString(currentLine));
                System.exit(-1);
            }
            if(lookaheadToken.getTokenType().equals("Key"))
            {
                keyToken = (KeyToken)lookaheadToken;
                if(Token.isBoolOperator(keyToken.getKeyType()))
                {
                    if(tokenLine.isEmpty())
                    {
                        System.out.println("Syntax Error, Line " +
                                Integer.toString(currentLine));
                        System.exit(-1);
                    }
                    currentToken = tokenLine.pop();
                    if(tokenLine.isEmpty())
                    {
                        System.out.println("Syntax Error, Line " +
                                Integer.toString(currentLine));
                        System.exit(-1);
                    }
                    lookaheadToken = tokenLine.pop();
                    assemblyCode.add("spush $" + returnReg);
                    assemblyLine++;
                    parseBool();
                    assemblyCode.add("spop &t0");
                    assemblyLine++;
                    assemblyCode.add("movr &t1 ,$" + returnReg);
                    assemblyLine++;
                    if(keyToken.getKeyType().equals("="))
                    {
                        assemblyCode.add("leqr &" + returnReg + " ,$t0 , $t1");
                        assemblyLine++;
                    }
                    else if(keyToken.getKeyType().equals("<"))
                    {
                        assemblyCode.add("lltr &" + returnReg + " ,$t0 , $t1");
                        assemblyLine++;
                    }
                    else if(keyToken.getKeyType().equals(">"))
                    {
                        assemblyCode.add("lgtr &" + returnReg + " ,$t0 , $t1");
                        assemblyLine++;
                    }
                    else if(keyToken.getKeyType().equals("and"))
                    {
                        assemblyCode.add("land &" + returnReg + " ,$t0 , $t1");
                        assemblyLine++;
                    }
                    else if(keyToken.getKeyType().equals("or"))
                    {
                        assemblyCode.add("lor &" + returnReg + " ,$t0 , $t1");
                        assemblyLine++;
                    }
                    else if(keyToken.getKeyType().equals("nand"))
                    {
                        assemblyCode.add("lndn &" + returnReg + " ,$t0 , $t1");
                        assemblyLine++;
                    }
                    else if(keyToken.getKeyType().equals("nor"))
                    {
                        assemblyCode.add("lnor &" + returnReg + " ,$t0 , $t1");
                        assemblyLine++;
                    }
                    if(!tokenLine.isEmpty())
                    {
                        currentToken = tokenLine.pop();
                        if(!tokenLine.isEmpty())
                        {
                            lookaheadToken = tokenLine.pop();
                        }
                    }
                }
                else
                {
                    System.out.println("Syntax Error, Line " +
                            Integer.toString(currentLine));
                    System.exit(-1);
                }
            }
        }
        else if(currentToken.getTokenType().equals("Num")||currentToken.getTokenType().equals("Id"))
        {
            if(currentToken.getTokenType().equals("Num"))
            {
                numToken = (NumberToken)currentToken;
                assemblyCode.add("movc &t0 ,@" + Integer.toString(numToken.getNumber()));
                assemblyLine++;
            }
            else
            {
                idToken = (IdentifierToken)currentToken;
                if(!identifierHash.containsKey(idToken.getIdentifier()))
                {
                    System.out.println("Syntax Error Assign Variable Before Use, Line " +
                            Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyCode.add("movr &t0 ,$" + identifierHash.get(idToken.getIdentifier()));
                assemblyLine++;
            }
            keyToken = (KeyToken) lookaheadToken;
            if(!Token.isBoolOperator(keyToken.getKeyType()))
            {
                System.out.println("Syntax Error Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            currentToken = lookaheadToken;
            lookaheadToken = tokenLine.pop();
            if(!(lookaheadToken.getTokenType().equals("Num")||lookaheadToken.getTokenType().equals("Id")))
            {
                System.out.println("Syntax Error Line " + Integer.toString(currentLine));
                System.exit(-1);
            }
            if(lookaheadToken.getTokenType().equals("Num"))
            {
                numToken = (NumberToken)lookaheadToken;
                assemblyCode.add("movc &t1 ,@" + Integer.toString(numToken.getNumber()));
                assemblyLine++;
            }
            else
            {
                idToken = (IdentifierToken)lookaheadToken;
                if(!identifierHash.containsKey(idToken.getIdentifier()))
                {
                    System.out.println("Syntax Error Assign Variable Before Use, Line " +
                            Integer.toString(currentLine));
                    System.exit(-1);
                }
                assemblyCode.add("movr &t1 ,$" + identifierHash.get(idToken.getIdentifier()));
                assemblyLine++;
            }
            keyToken = (KeyToken)currentToken;
            if(keyToken.getKeyType().equals("="))
            {
                assemblyCode.add("leqr &" + returnReg + " ,$t0 , $t1");
                assemblyLine++;
            }
            else if(keyToken.getKeyType().equals("<"))
            {
                assemblyCode.add("lltr &" + returnReg + " ,$t0 , $t1");
                assemblyLine++;
            }
            else if(keyToken.getKeyType().equals(">"))
            {
                assemblyCode.add("lgtr &" + returnReg + " ,$t0 , $t1");
                assemblyLine++;
            }
            else if(keyToken.getKeyType().equals("and"))
            {
                assemblyCode.add("land &" + returnReg + " ,$t0 , $t1");
                assemblyLine++;
            }
            else if(keyToken.getKeyType().equals("or"))
            {
                assemblyCode.add("lor &" + returnReg + " ,$t0 , $t1");
                assemblyLine++;
            }
            else if(keyToken.getKeyType().equals("nand"))
            {
                assemblyCode.add("lndn &" + returnReg + " ,$t0 , $t1");
                assemblyLine++;
            }
            else if(keyToken.getKeyType().equals("nor"))
            {
                assemblyCode.add("lnor &" + returnReg + " ,$t0 , $t1");
                assemblyLine++;
            }
            if(!tokenLine.isEmpty())
            {
                currentToken = tokenLine.pop();
                if(!tokenLine.isEmpty())
                {
                    lookaheadToken = tokenLine.pop();
                }
            }
        }
        else
        {
            System.out.println("Syntax Error Line " + Integer.toString(currentLine));
            System.exit(-1);
        }
    }
}
