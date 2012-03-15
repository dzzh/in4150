package nl.tudelft.in4150.da2.message;

import nl.tudelft.in4150.da2.Token;

public class TokenMessage extends Message {

    private Token token;

    public TokenMessage(int id, String srcUrl, int srcId, int destId){
        super(id, srcUrl, srcId, destId);
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
