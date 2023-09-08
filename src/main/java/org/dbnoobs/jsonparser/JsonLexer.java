package org.dbnoobs.jsonparser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class JsonLexer {

    public static List<JsonToken> lex(String json) {
        int len = json.length();
        int i = 0;
        List<JsonToken> tokens = new ArrayList<>();
        while(i < len){
            String token = lexString(json, i);
            if(token != null) {
                i += (token.length()+2);
                tokens.add(JsonToken.getToken(token, TokenType.STRING));
                continue;
            }
            token = lexNumber(json, i);
            JsonToken extractedNumber = convertToNumber(token);
            if(extractedNumber != null) {
                i += (token.length());
                tokens.add(extractedNumber);
                continue;
            }
            Boolean extractedBoolean = lexBoolean(json, i);
            if(extractedBoolean != null) {
                i += ((extractedBoolean)?4:5);
                tokens.add(JsonToken.getToken(extractedBoolean, TokenType.BOOLEAN));
                continue;
            }
            token = lexNull(json, i);
            if(token != null) {
                i += (token.length());
                tokens.add(JsonToken.getToken(token, TokenType.NULL));
                continue;
            }

            boolean isWhiteSpace = false;

            for(char c: JsonConstants.JSON_WHITESPACE){
                if(c == json.charAt(i)){
                    isWhiteSpace = true;
                    break;
                }
            }

            if(isWhiteSpace){
                i++;
                continue;
            }

            boolean isJsonSyntax = false;

            for(char c: JsonConstants.JSON_SYNTAX){
                if(c == json.charAt(i)){
                    isJsonSyntax = true;
                    tokens.add(JsonToken.getToken(c, TokenType.CONSTANT));
                    break;
                }
            }

            if(isJsonSyntax){
                i++;
                continue;
            }
            throw new RuntimeException("Token unrecognized in json string at index: " + i);
        }
        return tokens;
    }

    public static JsonToken convertToNumber(String token) {
        if(token == null || token.isEmpty()){
            return null;
        }
        boolean periodExists = false;
        boolean exponentExists = false;
        for(int i=0;i<token.length();i++){
            if(token.charAt(i) == '.'){
                periodExists = true;
                continue;
            }
            if(token.charAt(i) == 'e'){
                exponentExists = true;
            }
        }
        try{
            if(periodExists){
                return JsonToken.getToken(new BigDecimal(token),TokenType.BIGDECIMAL) ;
            }else if(exponentExists) {
                try{
                    return JsonToken.getToken(new BigDecimal(token).longValueExact(), TokenType.LONG);
                }catch (Exception e1){
                    try{
                        return JsonToken.getToken(new BigDecimal(token).toBigIntegerExact(), TokenType.BIGINTEGER);
                    }catch (Exception e2){
                            return JsonToken.getToken(new BigDecimal(token), TokenType.BIGDECIMAL);
                    }
                }
            }else{
                try{
                    return JsonToken.getToken(Integer.parseInt(token), TokenType.INT);
                }catch (Exception e1){
                    try{
                        return JsonToken.getToken(Long.parseLong(token), TokenType.LONG);
                    }catch (Exception e2){
                        return JsonToken.getToken(new BigInteger(token), TokenType.BIGINTEGER);
                    }
                }
            }
        }catch (Exception e3){
            return null;
        }
    }

    private static String lexString(final String json, int i){
        if(json == null || json.isEmpty() || json.charAt(i) != JsonConstants.JSON_QUOTE) {
            return null;
        }
        i++;
        int j = i;
        while(j < json.length()){
            if(json.charAt(j) == JsonConstants.JSON_QUOTE){
                return json.substring(i, j);
            }
            j++;
        }
        throw new RuntimeException("Expected end of quotes");

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
     * @param i - index of the string to start lexing
     * @return token(substring of json string) that is a number defined by postgres doc
     */
    public static String lexNumber(final String json, int i){
        if(json == null || json.isEmpty()){
            return null;
        }
        int j = i;
        char firstChar = json.charAt(j);
        // skip the first character if it's a sign
        if(firstChar == '+' || firstChar == '-'){
            j++;
        }
        boolean periodExists = false;
        boolean exponentExists = false;
        boolean atLeastOneDigitExists = false;

        while(j < json.length()){
            char c = json.charAt(j);
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
                j++;
                // if next is a sign skip it
                if(j < json.length()){
                    char next = json.charAt(j);
                    if(next == '+' || next == '-'){
                        j++;
                    }
                }else{
                    return null;
                }
                // if next is not a digit return null
                if(j >= json.length() || !Character.isDigit(json.charAt(j))){
                        return null;
                }
            }else{
               break;
            }
            j++;
        }
        if(!atLeastOneDigitExists){
            return null;
        }
        return json.substring(i, j);
    }

    public static Boolean lexBoolean(final String json, int i){
        if(i + 3 < json.length() && ("true".equals(json.substring(i, i+4).toLowerCase()))){
            return Boolean.TRUE;
        }
        if(i + 4 < json.length() && ("false".equals(json.substring(i, i+5).toLowerCase()))){
            return Boolean.FALSE;
        }
        return null;
    }

    public static String lexNull(final String json, int i){
        if(i + 3 < json.length() && "null".equalsIgnoreCase(json.substring(i, i+4))){
            return "NULL";
        }
        return null;
    }
}
