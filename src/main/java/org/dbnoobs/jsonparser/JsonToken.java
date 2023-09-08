package org.dbnoobs.jsonparser;

import java.util.Objects;

public class JsonToken {
    private TokenType tokenType;
    private Object token;

    public JsonToken(Object token, TokenType tokenType) {
        this.tokenType = tokenType;
        this.token = token;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public static JsonToken getToken(Object token, TokenType tokenType){
        return new JsonToken(token, tokenType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonToken jsonToken = (JsonToken) o;
        return tokenType == jsonToken.tokenType && Objects.equals(token, jsonToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenType, token);
    }
}
