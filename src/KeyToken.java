/**
 * Token for a key attribute such as "if", "while", "goto", "assign", "+ - x /", "; ( ) { }", "and", "or", "not",
 * "equals". Extends class Token. Holds additional information about the type of key.
 *
 * @author Michael Connor
 */
public class KeyToken extends Token
{
    private String keyType;

    /**
     * Initialises Token with id based on number of instances already active and sets the given key subtype attribute
     * to parameter
     *
     * @param keyType - the type of key refered to by this key token
     * @author Michael COnnor
     */
    public KeyToken(String keyType)
    {
        this.tokenId = instanceCount;
        instanceCount++;
        this.tokenType = "Key";
        this.keyType = keyType;
    }

    /**
     * Returns the ky type of this Token instance
     *
     * @return keyType - the key type of this Token instance
     * @author Michael Connor
     */
    public String getKeyType() {return keyType;}
}
