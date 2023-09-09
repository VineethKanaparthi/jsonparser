package org.dbnoobs.jsonparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class JsonLexerTest {

    @Test
    public void Lex(){
        List<JsonToken> tokens = JsonLexer.lex("{\"foo\"   : [1, +2 , \n{\"bar\": 2}]}");
        System.out.println(tokens);
        List<JsonToken> expected = Arrays.asList(
                JsonToken.getToken('{', TokenType.CONSTANT),
                JsonToken.getToken("foo", TokenType.STRING),
                JsonToken.getToken(':', TokenType.CONSTANT),
                JsonToken.getToken('[', TokenType.CONSTANT),
                JsonToken.getToken(1, TokenType.INT),
                JsonToken.getToken(',', TokenType.CONSTANT),
                JsonToken.getToken(2, TokenType.INT),
                JsonToken.getToken(',', TokenType.CONSTANT),
                JsonToken.getToken('{', TokenType.CONSTANT),
                JsonToken.getToken("bar", TokenType.STRING),
                JsonToken.getToken(':', TokenType.CONSTANT),
                JsonToken.getToken(2, TokenType.INT),
                JsonToken.getToken('}', TokenType.CONSTANT),
                JsonToken.getToken(']', TokenType.CONSTANT),
                JsonToken.getToken('}', TokenType.CONSTANT)
        );
        Assertions.assertIterableEquals(expected, tokens);

    }

    @Test
    public void testLexComplicatedJson(){
        System.out.println(JsonLexer.lex("    {\n" +
                "    \"_id\": \"64fc15e816964298016ed4a4\",\n" +
                "    \"index\": 0,\n" +
                "    \"guid\": \"7f8f196f-24b6-4b62-a69a-26c911e450ff\",\n" +
                "    \"isActive\": false,\n" +
                "    \"balance\": \"$2,565.84\",\n" +
                "    \"picture\": \"http://placehold.it/32x32\",\n" +
                "    \"age\": 34,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Tabatha Rodgers\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"ZOMBOID\",\n" +
                "    \"email\": \"tabatharodgers@zomboid.com\",\n" +
                "    \"phone\": \"+1 (818) 414-3938\",\n" +
                "    \"address\": \"735 Conover Street, Edgar, Texas, 768\",\n" +
                "    \"about\": \"Aliqua labore mollit sint non incididunt dolor elit cillum ex minim. Ullamco labore laboris commodo mollit commodo voluptate dolore. Tempor consequat consequat do qui est. Dolore commodo dolor occaecat Lorem et minim.\\r\\n\",\n" +
                "    \"registered\": \"2018-06-06T06:28:45 +07:00\",\n" +
                "    \"latitude\": -36.029072,\n" +
                "    \"longitude\": -79.561037,\n" +
                "    \"tags\": [\n" +
                "      \"ipsum\",\n" +
                "      \"ipsum\",\n" +
                "      \"deserunt\",\n" +
                "      \"et\",\n" +
                "      \"enim\",\n" +
                "      \"officia\",\n" +
                "      \"irure\"\n" +
                "    ],\n" +
                "    \"friends\": [\n" +
                "      {\n" +
                "        \"id\": 0,\n" +
                "        \"name\": \"Anthony Bowman\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Buckley Valenzuela\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"Deborah Meyers\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"greeting\": \"Hello, Tabatha Rodgers! You have 10 unread messages.\",\n" +
                "    \"favoriteFruit\": \"banana\"\n" +
                "  }"));
    }

    @Test
    public void testLexNumber() {
        // digits pattern
        Assertions.assertEquals("123456", JsonLexer.lexNumber("123456", 0));
        Assertions.assertEquals("+123456", JsonLexer.lexNumber("+123456 ", 0));
        Assertions.assertEquals("-123456", JsonLexer.lexNumber("-123456 ", 0));
        Assertions.assertNull(JsonLexer.lexNumber("abc123", 0));
        Assertions.assertNull(JsonLexer.lexNumber("", 0));

        // digits.[digits][e[+-]digits]
        Assertions.assertNull(JsonLexer.lexNumber(" . ", 1));
        Assertions.assertEquals("123.", JsonLexer.lexNumber("123.  ", 0));
        Assertions.assertEquals("123.e1", JsonLexer.lexNumber("123.e1", 0));
        Assertions.assertEquals("123.e1", JsonLexer.lexNumber(" 123.e1 ", 1));
        Assertions.assertEquals("123.e+1", JsonLexer.lexNumber(" 123.e+1 ", 1));
        Assertions.assertEquals("123.e-1", JsonLexer.lexNumber(" 123.e-1 ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" 123.e ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" 123.e- ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" 123.e+ ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" abc123.e+ ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" 1.1e ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" 1.1e- ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" 1.1e+ ", 1));
        Assertions.assertEquals("123.456", JsonLexer.lexNumber("123.456", 0));
        Assertions.assertEquals("123.456", JsonLexer.lexNumber("123.456 ", 0));
        Assertions.assertEquals("123.1e1", JsonLexer.lexNumber("123.1e1 ", 0));
        Assertions.assertEquals("123.1e+1", JsonLexer.lexNumber("123.1e+1 ", 0));
        Assertions.assertEquals("123.45e+10", JsonLexer.lexNumber("123.45e+10", 0));
        Assertions.assertEquals("0.123e-5", JsonLexer.lexNumber("0.123e-5", 0));

        // [digits].digits[e[+-]digits]
        Assertions.assertNull(JsonLexer.lexNumber(" . ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" .e ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" .e- ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" .e+ ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" .1e ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" .1e- ", 1));
        Assertions.assertNull(JsonLexer.lexNumber(" .1e+ ", 1));
        Assertions.assertEquals(".1", JsonLexer.lexNumber(".1 ", 0));
        Assertions.assertEquals(".1e2", JsonLexer.lexNumber(".1e2  ", 0));
        Assertions.assertEquals(".1e23", JsonLexer.lexNumber(".1e23  ", 0));
        Assertions.assertEquals(".1e+1", JsonLexer.lexNumber(" .1e+1 ", 1));
        Assertions.assertEquals(".1e-1", JsonLexer.lexNumber(" .1e-1 ", 1));

        // digitse[+-]digits
        Assertions.assertNull(JsonLexer.lexNumber("e", 0));
        Assertions.assertNull(JsonLexer.lexNumber("e-", 0));
        Assertions.assertNull(JsonLexer.lexNumber("e+", 0));
        Assertions.assertNull(JsonLexer.lexNumber("1e", 0));
        Assertions.assertNull(JsonLexer.lexNumber("1e-", 0));
        Assertions.assertNull(JsonLexer.lexNumber("1e+", 0));
        Assertions.assertEquals("1e23", JsonLexer.lexNumber("1e23  ", 0));
        Assertions.assertEquals("1e23", JsonLexer.lexNumber(" 1e23  ", 1));
        Assertions.assertEquals("11e23", JsonLexer.lexNumber(" 11e23  ", 1));

    }

    @Test
    void convertToNumber() {
        Assertions.assertEquals(JsonToken.getToken(1000000000L, TokenType.LONG), JsonLexer.convertToNumber("1e9"));
        Assertions.assertEquals(JsonToken.getToken(1000000000000000000L, TokenType.LONG), JsonLexer.convertToNumber("1e18"));
        Assertions.assertEquals(JsonToken.getToken(new BigInteger("10000000000000000000000000"), TokenType.BIGINTEGER), JsonLexer.convertToNumber("1e25"));
//        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("1e-9"));
//        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("1e-100"));
//        Assertions.assertInstanceOf(Integer.class, JsonLexer.convertToNumber("123"));
//        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("123.12"));
//        Assertions.assertInstanceOf(Long.class, JsonLexer.convertToNumber("123456789123456"));
//        Assertions.assertInstanceOf(BigInteger.class, JsonLexer.convertToNumber("123456789123456789123"));
//        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("12345678912345678.9123"));
//        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("1234567891234567812345678912345678123456789123456781234567891234567812345678912345678123456789123456781234567891234567812345678912345678.1234567891234567812345678912345678123456789123456781234567891234567812345678912345678123456789123456781234567891234567812345678912345678"));
    }

    @Test
    void lexBoolean() {
        Assertions.assertTrue(JsonLexer.lexBoolean("true", 0));
        Assertions.assertTrue(JsonLexer.lexBoolean("TRUE", 0));
        Assertions.assertTrue(JsonLexer.lexBoolean("True", 0));
        Assertions.assertTrue(JsonLexer.lexBoolean(" true", 1));
        Assertions.assertTrue(JsonLexer.lexBoolean(" true ", 1));
        Assertions.assertFalse(JsonLexer.lexBoolean("false", 0));
        Assertions.assertFalse(JsonLexer.lexBoolean("FALSE", 0));
        Assertions.assertFalse(JsonLexer.lexBoolean("False", 0));
        Assertions.assertFalse(JsonLexer.lexBoolean(" false", 1));
        Assertions.assertFalse(JsonLexer.lexBoolean(" false ", 1));
        Assertions.assertNull(JsonLexer.lexBoolean("null", 0));
        Assertions.assertTrue(JsonLexer.lexBoolean("trued", 0));
        Assertions.assertFalse(JsonLexer.lexBoolean("falseee", 0));
    }

    @Test
    void lexNull() {
        Assertions.assertEquals("NULL", JsonLexer.lexNull("null", 0));
        Assertions.assertEquals("NULL", JsonLexer.lexNull(" null", 1));
        Assertions.assertEquals("NULL", JsonLexer.lexNull(" null ", 1));
        Assertions.assertEquals("NULL", JsonLexer.lexNull("nullll", 0));
        Assertions.assertNull(JsonLexer.lexNull("nul", 0));
        Assertions.assertNull(JsonLexer.lexNull(" nul", 1));
        Assertions.assertNull(JsonLexer.lexNull(" anc", 1));
    }
}