/**
 * Token for an end attribute. Extends class Token.
 *
 * @author Michael Connor
 */
public class EndToken extends Token
{
    /**
     * Initialises Token with id based on number of instances already active
     *
     * @author Michael COnnor
     */
    public EndToken()
    {
        this.tokenId = instanceCount;
        instanceCount++;
        this.tokenType = "End";
    }

    @Override
    public String toString() {
        return "<end>";
    }
}