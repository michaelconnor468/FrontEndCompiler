/**
 * Abstract class meant to represent a lexeme token which is outputted during lexical parsing
 * @author Michael Connor
 */
public static class Token
{
    protected int tokenId;
    // Can be set to - Key, Num, Id, Op
    protected String tokenType;
    protected static int instanceCount = 0;

    /**
     * Returns the Id of this token
     *
     * @return tokenId - integer value representing the token id
     * @author Michael Connor
     */
    public int getId()
    {
        return this.tokenId;
    }

    /**
     * Returns the type of this token instance
     *
     * @return tokenType - the type of Token instance
     * @author Michael Connor
     */
    public String getTokenType() { return this.type; }

    /**
     * Returns true if the given string is a key, false otherwise.
     *
     * @param str - the string to compare with key values
     * @return key - true if key false if not
     * @author Michael Connor
     */
    public boolean isKey(String str)
    {
        boolean key = false;
        if((str.equals("if")) ||  (str.equals("goto")) || (str.equals("while")) || (str.equals("assign")) ||
                (str.equals("x")) || (str.equals("+")) || (str.equals("-")) || (str.equals("/")) ||
                (str.equals("and")) || (str.equals("or")) || (str.equals("not")) || (str.equals("=")) ||
                (str.equals(">")) || (str.equals("<")) || (str.equals(";")) || (str.equals("(")) ||
                (str.equals(")")) || (str.equals("{")) || (str.equals("}")))
        {
            key = true;
        }
        return key;
    }
}
