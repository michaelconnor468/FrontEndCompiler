/**
 * Abstract class meant to represent a lexeme token which is outputted during lexical parsing
 * @author MichaelConnor
 */
public abstract class Token
{
    private int tokenId;

    /**
     * Returns the Id of this token
     * @return tokenId - integer value representing the token id
     */
    public int getId()
    {
        return this.tokenId;
    }
}
