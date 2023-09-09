package org.dbnoobs.jsonparser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JsonParser {

    public HashMap<String, Object> parser(String json) {
        List<JsonToken> tokens = JsonLexer.lex(json);
        Queue<JsonToken> jsonTokenQueue = new LinkedList<>(tokens);
        HashMap<String, Object> result = parseObject(jsonTokenQueue, false);
        if(jsonTokenQueue.isEmpty()){
            return result;
        }else{
            throw new RuntimeException("Unexpected format");
        }
    }

    /**
     * Object Structure : { key1 : value1, key2 : value2, ... }
     * key - string
     * value - string, number, boolean, null, array, object
     * Array Structure: [comma seperated values]
     *
     * @param tokens
     * @param enforce
     */
    public HashMap<String, Object> parseObject(Queue<JsonToken> tokens, boolean enforce) {
        HashMap<String, Object> result = new HashMap<>();
        if(tokens.isEmpty()){
            if(enforce){
                throw new RuntimeException("Expected object but not found");
            }
            return result;
        }
        try {
            expectConstant(tokens, TokenType.CONSTANT, JsonConstants.JSON_LEFTBRACE, true);
            while(true){

                boolean isRightBrace = expectConstant(tokens, TokenType.CONSTANT, JsonConstants.JSON_COLON, false);
                if (isRightBrace) {
                    tokens.poll();
                    break;
                }


                String identifier = expectIdentifier(tokens);
                expectConstant(tokens, TokenType.CONSTANT, JsonConstants.JSON_COLON, true);

                boolean isLeftBrace = expectConstant(tokens, TokenType.CONSTANT, JsonConstants.JSON_LEFTBRACE, false);
                boolean isLeftBracket = expectConstant(tokens, TokenType.CONSTANT, JsonConstants.JSON_LEFTBRACKET, false);

                if(isLeftBrace){
                    result.put(identifier, parseObject(tokens, true));
                }else if(isLeftBracket){
                    result.put(identifier, parseArray(tokens));
                }else{
                    if(tokens.isEmpty()){
                        throw new RuntimeException("Expected a value token but not found");
                    }
                    JsonToken token = tokens.peek();
                    if(token == null || token.getToken() == null || TokenType.CONSTANT.equals(token.getTokenType())){
                        throw new RuntimeException("Expected a value token but not found");
                    }
                    tokens.poll();
                    if(TokenType.INT.equals(token.getTokenType())){
                        result.put(identifier, (Integer)token.getToken());
                    }else if(TokenType.LONG.equals(token.getTokenType())){
                        result.put(identifier, (Long)token.getToken());
                    }else if(TokenType.BIGINTEGER.equals(token.getTokenType())){
                        result.put(identifier, (BigInteger)token.getToken());
                    }else if(TokenType.BIGDECIMAL.equals(token.getTokenType())){
                        result.put(identifier, (BigDecimal)token.getToken());
                    }else if(TokenType.BOOLEAN.equals(token.getTokenType())){
                        result.put(identifier, (Boolean)token.getToken());
                    }else if(TokenType.NULL.equals(token.getTokenType())){
                        result.put(identifier, (String)token.getToken());
                    }else if(TokenType.STRING.equals(token.getTokenType())){
                        result.put(identifier, (String)token.getToken());
                    }else{
                        throw new RuntimeException("Expected a simple token but not found");
                    }

                }

                // expect Right brace or ( comma and object again)
                isRightBrace = expectConstant(tokens, TokenType.CONSTANT, JsonConstants.JSON_COLON, false);
                if (isRightBrace) {
                    tokens.poll();
                    break;
                } else {
                    expectConstant(tokens, TokenType.CONSTANT, JsonConstants.JSON_COMMA, true);
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }

    }

    private Object parseArray(Queue<JsonToken> tokens) {
        return new Object();
    }

    /**
     * Value can be of array type or another object or string or number or boolean or null
     * @param tokens
     * @return
     */

    private Object extractValue(Queue<JsonToken> tokens) {
        if(tokens.isEmpty()){
            throw new RuntimeException("Expected a value token but not found");
        }
        JsonToken token = tokens.poll();
        if(token == null || token.getToken() == null || TokenType.CONSTANT.equals(token.getTokenType())){
            throw new RuntimeException("Expected a value token but not found");
        }
        return token.getToken();
    }

    private String expectIdentifier(Queue<JsonToken> tokens) {
        if(tokens.isEmpty()){
            throw new RuntimeException("Expected identifier but Not Enough Tokens present");
        }
        JsonToken jsonToken = tokens.poll();
        if(jsonToken == null || jsonToken.getToken() == null || jsonToken.getTokenType() == null || !TokenType.STRING.equals(jsonToken.getTokenType())){
            throw new RuntimeException("Expected token of type identifier but not found");
        }
        return (String)jsonToken.getToken();
    }

    private boolean expectConstant(final Queue<JsonToken> tokens, final TokenType expectedTokentype,
                                   final Character expectedValue, final boolean enforce) {
        if(tokens.isEmpty()){
            throw new RuntimeException("Expected constant but not enough tokens found");
        }
        JsonToken jsonToken = tokens.peek();
        if(enforce){
            tokens.poll();
        }
        if (!((jsonToken != null && jsonToken.getToken() != null) && expectedTokentype.equals(jsonToken.getTokenType())
                && expectedValue.equals(jsonToken.getToken()))) {
            if (enforce) {
                throw new RuntimeException(String.format("Expected constant {} but not found", expectedValue));
            }
            return false;
        }
        return true;
    }
}
