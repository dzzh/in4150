package nl.tudelft.in4150.da2.message;

import nl.tudelft.in4150.da2.Token;

public class TokenMessage extends Message {

    private Token token;

    public TokenMessage(String srcUrl, int srcId, Token token){
        super(srcUrl, srcId);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
