package org.dbnoobs.jsonparser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class JsonLexer {

    public List<Object> lex(String json) {
        int len = json.length();
        int i = 0;
        List<Object> tokens = new ArrayList<>();
        while(i < len){
            String token = null;
            token = lexString(json, i);
            if(token != null) {
                i += (token.length()+2);
                tokens.add(token);
                continue;
            }
            token = lexNumber(json, i);
            Object extractedNumber = convertToNumber(token);
            if(extractedNumber != null) {
                i += (token.length());
                tokens.add(extractedNumber);
                continue;
            }
            Boolean extractedBoolean = lexBoolean(json, i);
            if(extractedBoolean != null) {
                i += ((extractedBoolean)?4:5);
                tokens.add(extractedBoolean);
                continue;
            }
            token = lexNull(json, i);
            if(token != null) {
                i += (token.length());
                tokens.add(token);
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
                    tokens.add(c);
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

    public Object convertToNumber(String token) {
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
                return new BigDecimal(token);
            }else if(exponentExists) {
                try{
                    return new BigDecimal(token).longValueExact();
                }catch (Exception e1){
                    try{
                        return new BigDecimal(token).toBigIntegerExact();
                    }catch (Exception e2){
                            return new BigDecimal(token);
                    }
                }
            }else{
                try{
                    return Integer.parseInt(token);
                }catch (Exception e1){
                    try{
                        return Long.parseLong(token);
                    }catch (Exception e2){
                        return new BigInteger(token);
                    }
                }
            }
        }catch (Exception e3){
            return null;
        }
    }

    private String lexString(final String json, int i){
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
     *
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
    public String lexNumber(final String json, int i){
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

    public Boolean lexBoolean(final String json, int i){
        if(i + 3 < json.length() && ("true".equals(json.substring(i, i+4).toLowerCase()))){
            return Boolean.TRUE;
        }
        if(i + 4 < json.length() && ("false".equals(json.substring(i, i+5).toLowerCase()))){
            return Boolean.FALSE;
        }
        return null;
    }

    public String lexNull(final String json, int i){
        if(i + 3 < json.length() && "null".equalsIgnoreCase(json.substring(i, i+4))){
            return "NULL";
        }
        return null;
    }
}
