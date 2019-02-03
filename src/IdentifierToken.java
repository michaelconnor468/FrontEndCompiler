/**
 * Token for a identifier attribute such as "abc1", "tempNum". Extends class Token. Holds
 * additional information about the type of identifier.
 *
 * @author Michael Connor
 */
public class IdentifierToken extends Token
{
    private String identifier;

    /**
     * Initialises Token with id based on number of instances already active and sets the given identifier subtype
     * attributes to parameter
     *
     * @param identifier - the identifier refered to by this key token
     * @author Michael COnnor
     */
    public IdentifierToken(String identifier)
    {
        this.tokenId = instanceCount;
        instanceCount++;
        this.tokenType = "Id";
        this.identifier = identifier;
    }

    /**
     * Returns the identifier type of this Token instance
     *
     * @return keyType - the identifier type of this Token instance
     * @author Michael Connor
     */
    public String getIdentifier() {return identifier}
}