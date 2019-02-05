/**
 * Token for a number attribute. Extends class Token. Holds additional information about the type of number.
 *
 * @author Michael Connor
 */
public class NumberToken extends Token
{
    private int number;

    /**
     * Initialises Token with id based on number of instances already active and sets the given key subtype attribute
     * to parameter
     *
     * @param number - the number refered to by this key token
     * @author Michael COnnor
     */
    public NumberToken(int number)
    {
        this.tokenId = instanceCount;
        instanceCount++;
        this.tokenType = "Num";
        this.number = number;
    }

    /**
     * Returns the number of this Token instance
     *
     * @return keyType - the key type of this Token instance
     * @author Michael Connor
     */
    public int getNumber() {return number;}
}
