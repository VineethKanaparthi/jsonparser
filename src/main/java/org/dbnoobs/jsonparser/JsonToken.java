package org.dbnoobs.jsonparser;

import java.util.Objects;

public class JsonToken {

    private Object token;

    private TokenType tokenType;
    private Location location;

    public JsonToken(Object token, TokenType tokenType, Location location) {
        this.token = token;
        this.tokenType = tokenType;
        this.location = location;
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

    public static JsonToken getToken(Object token, TokenType tokenType, Location location){
        return new JsonToken(token, tokenType, location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonToken jsonToken = (JsonToken) o;
        return Objects.equals(token, jsonToken.token) && tokenType == jsonToken.tokenType && Objects.equals(location, jsonToken.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tokenType, location);
    }

    @Override
    public String toString() {
        return "JsonToken{" +
                "token=" + token +
                ", tokenType=" + tokenType +
                ", location=" + location +
                '}';
    }
}
