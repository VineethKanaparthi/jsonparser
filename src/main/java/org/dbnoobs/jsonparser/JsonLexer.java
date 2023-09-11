package org.dbnoobs.jsonparser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonLexer {

    public List<JsonToken> lex(String json) {
        int len = json.length();
        Cursor cursor = new Cursor(0, new Location(1, 0));
        List<JsonToken> tokens = new ArrayList<>();
        List<LexerFunction> lexers = Arrays.asList(this::lexString, this::lexNumeric, this::lexBoolean, this::lexNull,
                this::lexWhiteSpace, this::lexSyntax);

        while(cursor.getPointer() < len){
            boolean tokenFound = false;
            for(LexerFunction lexerFunction: lexers){
                JsonToken token = lexerFunction.apply(json, cursor);
                if(token != null){
                    if(!TokenType.WHITESPACE.equals(token.getTokenType())){
                        tokens.add(token);
                    }
                    tokenFound = true;
                    break;
                }
            }
            if(!tokenFound){
                Object lastToken = (tokens.size() > 0)?tokens.get(tokens.size()-1).getToken(): "";
                throw new RuntimeException(String.format("Unable to lex after token %s, at line: %d col: %d", lastToken, cursor.getLocation().getLine(), cursor.getLocation().getCol()));
            }
        }
        return tokens;
    }

    private JsonToken lexSyntax(String json, Cursor cursor) {
        for(char c: JsonConstants.JSON_SYNTAX){
            if(c == json.charAt(cursor.getPointer())){
                Location tokenLocation = new Location(cursor.getLocation());
                cursor.increment();
                return new JsonToken(c, TokenType.CONSTANT, tokenLocation);
            }
        }
        return null;
    }

    private JsonToken lexWhiteSpace(String json, Cursor cursor) {
        for(char c: JsonConstants.JSON_WHITESPACE){
            if(c == json.charAt(cursor.getPointer())){
                Location tokenLocation = new Location(cursor.getLocation());
                if(c == '\n'){
                    cursor.incrementLine();
                }else{
                    cursor.increment();
                }
                return new JsonToken(c, TokenType.WHITESPACE, tokenLocation);
            }
        }
        return null;
    }

    public JsonToken lexString(final String json, Cursor inputCursor){
        Cursor cursor = new Cursor(inputCursor);
        if(json == null || json.isEmpty() || json.charAt(cursor.getPointer()) != JsonConstants.JSON_QUOTE) {
            return null;
        }
        // skip the quotes
        cursor.increment();
        // start of string

        while(cursor.getPointer() < json.length()){
            if(json.charAt(cursor.getPointer()) == JsonConstants.JSON_QUOTE){
                Location tokenLocation = new Location(inputCursor.getLocation());
                String value = json.substring(inputCursor.getPointer()+1, cursor.getPointer());
                cursor.increment();
                inputCursor.modify(cursor);
                return new JsonToken(value, TokenType.STRING, tokenLocation);
            }
            cursor.increment();
        }
        throw new RuntimeException(String.format("Expected end of quotes for token starting at: %d", inputCursor.getPointer()));
    }

    /**
     * postgres documentation defines number as
     * digits
     * digits.[digits][e[+-]digits]
     * [digits].digits[e[+-]digits]
     * digitse[+-]digits
     * <p>
     *
     * At least one digit should be present
     * At least one digit should be present before an exponent marker
     * At least one digit should follow an exponent marker after an optional sign
     *
     * @param json - Input json string
     * @param inputCursor - index, @Location of the string to start lexing
     * @return token(substring of json string) that is a number defined by postgres doc
     */
    public JsonToken lexNumeric(final String json, Cursor inputCursor){
        if(json == null || json.isEmpty()){
            return null;
        }
        Cursor cursor = new Cursor(inputCursor);
        char firstChar = json.charAt(cursor.getPointer());
        // skip the first character if it's a sign
        if(firstChar == '+' || firstChar == '-'){
            cursor.increment();
        }
        boolean periodExists = false;
        boolean exponentExists = false;
        boolean atLeastOneDigitExists = false;

        while(cursor.getPointer() < json.length()){
            char c = json.charAt(cursor.getPointer());
            boolean isDigit = Character.isDigit(c);
            boolean isExpMarker = (c=='e');
            boolean isPeriod = (c=='.');

            if(isDigit){
                atLeastOneDigitExists = true;
            }else if(isPeriod){
                if(periodExists){
                   return null;
                }
                periodExists = true;
            }else if(isExpMarker && !exponentExists){
                exponentExists = true;
                // at least one digit should be before exponent
                if(!atLeastOneDigitExists){
                    return null;
                }
                // at least 1 digit should follow after exponent
                cursor.increment();
                // if next is a sign skip it
                if(cursor.getPointer() < json.length()){
                    char next = json.charAt(cursor.getPointer());
                    if(next == '+' || next == '-'){
                        cursor.increment();
                    }
                }else{
                    return null;
                }
                // if next is not a digit return null
                if(cursor.getPointer() >= json.length() || !Character.isDigit(json.charAt(cursor.getPointer()))){
                        return null;
                }
            }else{
               break;
            }
            cursor.increment();
        }
        if(!atLeastOneDigitExists){
            return null;
        }

        String token = json.substring(inputCursor.getPointer(), cursor.getPointer());
        Location tokenLocation = new Location(inputCursor.getLocation());
        inputCursor.modify(cursor);
        try{
            if(periodExists){
                return JsonToken.getToken(new BigDecimal(token), TokenType.BIGDECIMAL, tokenLocation) ;
            }else if(exponentExists) {
                try{
                    return JsonToken.getToken(new BigDecimal(token).longValueExact(), TokenType.LONG, tokenLocation);
                }catch (Exception e1){
                    try{
                        return JsonToken.getToken(new BigDecimal(token).toBigIntegerExact(), TokenType.BIGINTEGER, tokenLocation);
                    }catch (Exception e2){
                        return JsonToken.getToken(new BigDecimal(token), TokenType.BIGDECIMAL, tokenLocation);
                    }
                }
            }else{
                try{
                    return JsonToken.getToken(Integer.parseInt(token), TokenType.INT, tokenLocation);
                }catch (Exception e1){
                    try{
                        return JsonToken.getToken(Long.parseLong(token), TokenType.LONG, tokenLocation);
                    }catch (Exception e2){
                        return JsonToken.getToken(new BigInteger(token), TokenType.BIGINTEGER, tokenLocation);
                    }
                }
            }
        }catch (Exception e3){
            return null;
        }
    }

    public JsonToken lexBoolean(final String json, Cursor cursor){
        int i = cursor.getPointer();
        if( i + 3 < json.length() && ("true".equals(json.substring(i, i+4).toLowerCase()))){
            Location tokenLocation = new Location(cursor.getLocation());
            cursor.increment(4);
            return JsonToken.getToken(Boolean.TRUE, TokenType.BOOLEAN, tokenLocation);
        }
        if(i + 4 < json.length() && ("false".equals(json.substring(i, i+5).toLowerCase()))){
            Location tokenLocation = new Location(cursor.getLocation());
            cursor.increment(5);
            return JsonToken.getToken(Boolean.FALSE, TokenType.BOOLEAN, tokenLocation);
        }
        return null;
    }

    public JsonToken lexNull(final String json, Cursor cursor){
        int i = cursor.getPointer();
        if(i + 3 < json.length() && "null".equalsIgnoreCase(json.substring(i, i+4))){
            Location tokenLocation = new Location(cursor.getLocation());
            cursor.increment(i+4);
            return JsonToken.getToken("NULL", TokenType.NULL, tokenLocation);
        }
        return null;
    }
}
