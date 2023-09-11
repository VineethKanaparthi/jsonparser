package org.dbnoobs.jsonparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class JsonLexerTest {

    @Test
    public void Lex(){
        List<JsonToken> tokens = new JsonLexer().lex("{\"foo\"   : [1, +2 , \n{\"bar\": 2}]}");
        System.out.println(tokens);
        List<JsonToken> expected = Arrays.asList(
                JsonToken.getToken('{', TokenType.CONSTANT, new Location(1, 0)),
                JsonToken.getToken("foo", TokenType.STRING, new Location(1, 1)),
                JsonToken.getToken(':', TokenType.CONSTANT, new Location(1, 9)),
                JsonToken.getToken('[', TokenType.CONSTANT, new Location(1, 11)),
                JsonToken.getToken(1, TokenType.INT, new Location(1, 12)),
                JsonToken.getToken(',', TokenType.CONSTANT, new Location(1, 13)),
                JsonToken.getToken(2, TokenType.INT, new Location(1, 15)),
                JsonToken.getToken(',', TokenType.CONSTANT, new Location(1, 18)),
                JsonToken.getToken('{', TokenType.CONSTANT, new Location(2, 0)),
                JsonToken.getToken("bar", TokenType.STRING, new Location(2, 1)),
                JsonToken.getToken(':', TokenType.CONSTANT, new Location(2, 6)),
                JsonToken.getToken(2, TokenType.INT, new Location(2, 8)),
                JsonToken.getToken('}', TokenType.CONSTANT, new Location(2, 9)),
                JsonToken.getToken(']', TokenType.CONSTANT, new Location(2, 10)),
                JsonToken.getToken('}', TokenType.CONSTANT, new Location(2, 11))
        );
        Assertions.assertIterableEquals(expected, tokens);

    }

//    @Test
//    public void testLexComplicatedJson(){
//        System.out.println(JsonLexer.lex("    {\n" +
//                "    \"_id\": \"64fc15e816964298016ed4a4\",\n" +
//                "    \"index\": 0,\n" +
//                "    \"guid\": \"7f8f196f-24b6-4b62-a69a-26c911e450ff\",\n" +
//                "    \"isActive\": false,\n" +
//                "    \"balance\": \"$2,565.84\",\n" +
//                "    \"picture\": \"http://placehold.it/32x32\",\n" +
//                "    \"age\": 34,\n" +
//                "    \"eyeColor\": \"green\",\n" +
//                "    \"name\": \"Tabatha Rodgers\",\n" +
//                "    \"gender\": \"female\",\n" +
//                "    \"company\": \"ZOMBOID\",\n" +
//                "    \"email\": \"tabatharodgers@zomboid.com\",\n" +
//                "    \"phone\": \"+1 (818) 414-3938\",\n" +
//                "    \"address\": \"735 Conover Street, Edgar, Texas, 768\",\n" +
//                "    \"about\": \"Aliqua labore mollit sint non incididunt dolor elit cillum ex minim. Ullamco labore laboris commodo mollit commodo voluptate dolore. Tempor consequat consequat do qui est. Dolore commodo dolor occaecat Lorem et minim.\\r\\n\",\n" +
//                "    \"registered\": \"2018-06-06T06:28:45 +07:00\",\n" +
//                "    \"latitude\": -36.029072,\n" +
//                "    \"longitude\": -79.561037,\n" +
//                "    \"tags\": [\n" +
//                "      \"ipsum\",\n" +
//                "      \"ipsum\",\n" +
//                "      \"deserunt\",\n" +
//                "      \"et\",\n" +
//                "      \"enim\",\n" +
//                "      \"officia\",\n" +
//                "      \"irure\"\n" +
//                "    ],\n" +
//                "    \"friends\": [\n" +
//                "      {\n" +
//                "        \"id\": 0,\n" +
//                "        \"name\": \"Anthony Bowman\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"id\": 1,\n" +
//                "        \"name\": \"Buckley Valenzuela\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"id\": 2,\n" +
//                "        \"name\": \"Deborah Meyers\"\n" +
//                "      }\n" +
//                "    ],\n" +
//                "    \"greeting\": \"Hello, Tabatha Rodgers! You have 10 unread messages.\",\n" +
//                "    \"favoriteFruit\": \"banana\"\n" +
//                "  }"));
//    }

    @Test
    public void testLexString(){
        JsonToken expected = new JsonToken("Vineeth", TokenType.STRING, new Location(1, 0));
        JsonToken actual = new JsonLexer().lexString("\"Vineeth\"", new Cursor(0, new Location(1, 0)));
        Assertions.assertEquals(expected, actual);

    }
//    @Test
//    public void testLexNumber() {
//        // digits pattern
//        Assertions.assertEquals("123456", JsonLexer.lexNumber("123456", 0));
//        Assertions.assertEquals("+123456", JsonLexer.lexNumber("+123456 ", 0));
//        Assertions.assertEquals("-123456", JsonLexer.lexNumber("-123456 ", 0));
//        Assertions.assertNull(JsonLexer.lexNumber("abc123", 0));
//        Assertions.assertNull(JsonLexer.lexNumber("", 0));
//
//        // digits.[digits][e[+-]digits]
//        Assertions.assertNull(JsonLexer.lexNumber(" . ", 1));
//        Assertions.assertEquals("123.", JsonLexer.lexNumber("123.  ", 0));
//        Assertions.assertEquals("123.e1", JsonLexer.lexNumber("123.e1", 0));
//        Assertions.assertEquals("123.e1", JsonLexer.lexNumber(" 123.e1 ", 1));
//        Assertions.assertEquals("123.e+1", JsonLexer.lexNumber(" 123.e+1 ", 1));
//        Assertions.assertEquals("123.e-1", JsonLexer.lexNumber(" 123.e-1 ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" 123.e ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" 123.e- ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" 123.e+ ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" abc123.e+ ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" 1.1e ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" 1.1e- ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" 1.1e+ ", 1));
//        Assertions.assertEquals("123.456", JsonLexer.lexNumber("123.456", 0));
//        Assertions.assertEquals("123.456", JsonLexer.lexNumber("123.456 ", 0));
//        Assertions.assertEquals("123.1e1", JsonLexer.lexNumber("123.1e1 ", 0));
//        Assertions.assertEquals("123.1e+1", JsonLexer.lexNumber("123.1e+1 ", 0));
//        Assertions.assertEquals("123.45e+10", JsonLexer.lexNumber("123.45e+10", 0));
//        Assertions.assertEquals("0.123e-5", JsonLexer.lexNumber("0.123e-5", 0));
//
//        // [digits].digits[e[+-]digits]
//        Assertions.assertNull(JsonLexer.lexNumber(" . ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" .e ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" .e- ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" .e+ ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" .1e ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" .1e- ", 1));
//        Assertions.assertNull(JsonLexer.lexNumber(" .1e+ ", 1));
//        Assertions.assertEquals(".1", JsonLexer.lexNumber(".1 ", 0));
//        Assertions.assertEquals(".1e2", JsonLexer.lexNumber(".1e2  ", 0));
//        Assertions.assertEquals(".1e23", JsonLexer.lexNumber(".1e23  ", 0));
//        Assertions.assertEquals(".1e+1", JsonLexer.lexNumber(" .1e+1 ", 1));
//        Assertions.assertEquals(".1e-1", JsonLexer.lexNumber(" .1e-1 ", 1));
//
//        // digitse[+-]digits
//        Assertions.assertNull(JsonLexer.lexNumber("e", 0));
//        Assertions.assertNull(JsonLexer.lexNumber("e-", 0));
//        Assertions.assertNull(JsonLexer.lexNumber("e+", 0));
//        Assertions.assertNull(JsonLexer.lexNumber("1e", 0));
//        Assertions.assertNull(JsonLexer.lexNumber("1e-", 0));
//        Assertions.assertNull(JsonLexer.lexNumber("1e+", 0));
//        Assertions.assertEquals("1e23", JsonLexer.lexNumber("1e23  ", 0));
//        Assertions.assertEquals("1e23", JsonLexer.lexNumber(" 1e23  ", 1));
//        Assertions.assertEquals("11e23", JsonLexer.lexNumber(" 11e23  ", 1));
//
//    }
//
//    @Test
//    void convertToNumber() {
//        Assertions.assertEquals(JsonToken.getToken(1000000000L, TokenType.LONG), JsonLexer.convertToNumber("1e9"));
//        Assertions.assertEquals(JsonToken.getToken(1000000000000000000L, TokenType.LONG), JsonLexer.convertToNumber("1e18"));
//        Assertions.assertEquals(JsonToken.getToken(new BigInteger("10000000000000000000000000"), TokenType.BIGINTEGER), JsonLexer.convertToNumber("1e25"));
////        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("1e-9"));
////        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("1e-100"));
////        Assertions.assertInstanceOf(Integer.class, JsonLexer.convertToNumber("123"));
////        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("123.12"));
////        Assertions.assertInstanceOf(Long.class, JsonLexer.convertToNumber("123456789123456"));
////        Assertions.assertInstanceOf(BigInteger.class, JsonLexer.convertToNumber("123456789123456789123"));
////        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("12345678912345678.9123"));
////        Assertions.assertInstanceOf(BigDecimal.class, JsonLexer.convertToNumber("1234567891234567812345678912345678123456789123456781234567891234567812345678912345678123456789123456781234567891234567812345678912345678.1234567891234567812345678912345678123456789123456781234567891234567812345678912345678123456789123456781234567891234567812345678912345678"));
//    }

    @Test
    void lexBoolean() {
        JsonLexer jsonLexer = new JsonLexer();
        Assertions.assertTrue((Boolean)jsonLexer.lexBoolean("true", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertTrue((Boolean)jsonLexer.lexBoolean("trued", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertTrue((Boolean)jsonLexer.lexBoolean("TRUE", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertTrue((Boolean)jsonLexer.lexBoolean("True", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertTrue((Boolean)jsonLexer.lexBoolean(" true", new Cursor(1, new Location(1, 1))).getToken());
        Assertions.assertTrue((Boolean)jsonLexer.lexBoolean(" true ", new Cursor(1, new Location(1, 1))).getToken());


        Assertions.assertFalse((Boolean)jsonLexer.lexBoolean("false", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertFalse((Boolean)jsonLexer.lexBoolean("falseeee", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertFalse((Boolean)jsonLexer.lexBoolean("FALSE", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertFalse((Boolean)jsonLexer.lexBoolean("False", new Cursor(0, new Location(1, 0))).getToken());
        Assertions.assertFalse((Boolean)jsonLexer.lexBoolean(" false", new Cursor(1, new Location(1, 1))).getToken());
        Assertions.assertFalse((Boolean)jsonLexer.lexBoolean(" false ", new Cursor(1, new Location(1, 1))).getToken());

        Assertions.assertNull(jsonLexer.lexBoolean("null", new Cursor(0, new Location(1, 0))));

        Assertions.assertEquals(new Location(1, 1), jsonLexer.lexBoolean(" true ", new Cursor(1, new Location(1, 1))).getLocation());
        Assertions.assertEquals(new Location(1, 0), jsonLexer.lexBoolean("true ", new Cursor(0, new Location(1, 0))).getLocation());
        Assertions.assertEquals(new Location(1, 1), jsonLexer.lexBoolean(" false ", new Cursor(1, new Location(1, 1))).getLocation());
        Assertions.assertEquals(new Location(1, 0), jsonLexer.lexBoolean("false ", new Cursor(0, new Location(1, 0))).getLocation());
        Assertions.assertEquals(new Location(1, 0), jsonLexer.lexBoolean("falseee ", new Cursor(0, new Location(1, 0))).getLocation());

    }
//
    @Test
    void lexNull() {
        JsonLexer jsonLexer = new JsonLexer();
        Assertions.assertEquals(
                new JsonToken(
                        "NULL",
                        TokenType.NULL,
                        new Location(1, 0)
                ),
                jsonLexer.lexNull(
                        "null",
                        new Cursor(0, new Location(1, 0))
                )
        );
        Assertions.assertEquals(
                new JsonToken(
                        "NULL",
                        TokenType.NULL,
                        new Location(1, 1)
                ),
                jsonLexer.lexNull(
                        " null",
                        new Cursor(1, new Location(1, 1))
                )
        );
        Assertions.assertEquals(
                new JsonToken(
                        "NULL",
                        TokenType.NULL,
                        new Location(1, 1)
                ),
                jsonLexer.lexNull(
                        " null ",
                        new Cursor(1, new Location(1, 1))
                )
        );
        Assertions.assertEquals(
                new JsonToken(
                        "NULL",
                        TokenType.NULL,
                        new Location(1, 0)
                ),
                jsonLexer.lexNull(
                        "nullllll ",
                        new Cursor(0, new Location(1, 0))
                )
        );
        Assertions.assertNull(jsonLexer.lexNull("nul", new Cursor(0, new Location(1, 0))));
        Assertions.assertNull(jsonLexer.lexNull(" nul", new Cursor(1, new Location(1, 1))));
        Assertions.assertNull(jsonLexer.lexNull(" anc", new Cursor(1, new Location(1, 1))));
    }
}