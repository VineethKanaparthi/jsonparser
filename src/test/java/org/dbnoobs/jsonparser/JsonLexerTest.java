package org.dbnoobs.jsonparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class JsonLexerTest {

    @Test
    public void Lex(){
        JsonLexer lexer = new JsonLexer();
        List<Object> tokens = lexer.lex("{\"foo\"   : [1, 2 , \n{\"bar\": 2}]}");
        System.out.println(tokens);
        List<Object> expected = Arrays.asList('{', "foo", ':', '[', 1,',',2,',','{', "bar", ':', 2, '}', ']', '}');
        Assertions.assertIterableEquals(expected, tokens);
    }

    @Test
    public void testLexNumber() {
        JsonLexer lexer = new JsonLexer();
        // digits pattern
        Assertions.assertEquals("123456", lexer.lexNumber("123456", 0));
        Assertions.assertEquals("+123456", lexer.lexNumber("+123456 ", 0));
        Assertions.assertEquals("-123456", lexer.lexNumber("-123456 ", 0));
        Assertions.assertNull(lexer.lexNumber("abc123", 0));
        Assertions.assertNull(lexer.lexNumber("", 0));

        // digits.[digits][e[+-]digits]
        Assertions.assertNull(lexer.lexNumber(" . ", 1));
        Assertions.assertEquals("123.", lexer.lexNumber("123.  ", 0));
        Assertions.assertEquals("123.e1", lexer.lexNumber("123.e1", 0));
        Assertions.assertEquals("123.e1", lexer.lexNumber(" 123.e1 ", 1));
        Assertions.assertEquals("123.e+1", lexer.lexNumber(" 123.e+1 ", 1));
        Assertions.assertEquals("123.e-1", lexer.lexNumber(" 123.e-1 ", 1));
        Assertions.assertNull(lexer.lexNumber(" 123.e ", 1));
        Assertions.assertNull(lexer.lexNumber(" 123.e- ", 1));
        Assertions.assertNull(lexer.lexNumber(" 123.e+ ", 1));
        Assertions.assertNull(lexer.lexNumber(" abc123.e+ ", 1));
        Assertions.assertNull(lexer.lexNumber(" 1.1e ", 1));
        Assertions.assertNull(lexer.lexNumber(" 1.1e- ", 1));
        Assertions.assertNull(lexer.lexNumber(" 1.1e+ ", 1));
        Assertions.assertEquals("123.456", lexer.lexNumber("123.456", 0));
        Assertions.assertEquals("123.456", lexer.lexNumber("123.456 ", 0));
        Assertions.assertEquals("123.1e1", lexer.lexNumber("123.1e1 ", 0));
        Assertions.assertEquals("123.1e+1", lexer.lexNumber("123.1e+1 ", 0));
        Assertions.assertEquals("123.45e+10", lexer.lexNumber("123.45e+10", 0));
        Assertions.assertEquals("0.123e-5", lexer.lexNumber("0.123e-5", 0));

        // [digits].digits[e[+-]digits]
        Assertions.assertNull(lexer.lexNumber(" . ", 1));
        Assertions.assertNull(lexer.lexNumber(" .e ", 1));
        Assertions.assertNull(lexer.lexNumber(" .e- ", 1));
        Assertions.assertNull(lexer.lexNumber(" .e+ ", 1));
        Assertions.assertNull(lexer.lexNumber(" .1e ", 1));
        Assertions.assertNull(lexer.lexNumber(" .1e- ", 1));
        Assertions.assertNull(lexer.lexNumber(" .1e+ ", 1));
        Assertions.assertEquals(".1", lexer.lexNumber(".1 ", 0));
        Assertions.assertEquals(".1e2", lexer.lexNumber(".1e2  ", 0));
        Assertions.assertEquals(".1e23", lexer.lexNumber(".1e23  ", 0));
        Assertions.assertEquals(".1e+1", lexer.lexNumber(" .1e+1 ", 1));
        Assertions.assertEquals(".1e-1", lexer.lexNumber(" .1e-1 ", 1));

        // digitse[+-]digits
        Assertions.assertNull(lexer.lexNumber("e", 0));
        Assertions.assertNull(lexer.lexNumber("e-", 0));
        Assertions.assertNull(lexer.lexNumber("e+", 0));
        Assertions.assertNull(lexer.lexNumber("1e", 0));
        Assertions.assertNull(lexer.lexNumber("1e-", 0));
        Assertions.assertNull(lexer.lexNumber("1e+", 0));
        Assertions.assertEquals("1e23", lexer.lexNumber("1e23  ", 0));
        Assertions.assertEquals("1e23", lexer.lexNumber(" 1e23  ", 1));
        Assertions.assertEquals("11e23", lexer.lexNumber(" 11e23  ", 1));

    }

    @Test
    void convertToNumber() {
        JsonLexer lexer = new JsonLexer();
        Assertions.assertInstanceOf(Long.class, lexer.convertToNumber("1e9"));
        Assertions.assertInstanceOf(Long.class, lexer.convertToNumber("1e18"));
        Assertions.assertInstanceOf(BigInteger.class, lexer.convertToNumber("1e25"));
        Assertions.assertInstanceOf(BigDecimal.class, lexer.convertToNumber("1e-9"));
        Assertions.assertInstanceOf(BigDecimal.class, lexer.convertToNumber("1e-100"));
        Assertions.assertInstanceOf(Integer.class, lexer.convertToNumber("123"));
        Assertions.assertInstanceOf(BigDecimal.class, lexer.convertToNumber("123.12"));
        Assertions.assertInstanceOf(Long.class, lexer.convertToNumber("123456789123456"));
        Assertions.assertInstanceOf(BigInteger.class, lexer.convertToNumber("123456789123456789123"));
        Assertions.assertInstanceOf(BigDecimal.class, lexer.convertToNumber("12345678912345678.9123"));
        Assertions.assertInstanceOf(BigDecimal.class, lexer.convertToNumber("1234567891234567812345678912345678123456789123456781234567891234567812345678912345678123456789123456781234567891234567812345678912345678.1234567891234567812345678912345678123456789123456781234567891234567812345678912345678123456789123456781234567891234567812345678912345678"));
    }

    @Test
    void lexBoolean() {
        JsonLexer lexer = new JsonLexer();
        Assertions.assertTrue(lexer.lexBoolean("true", 0));
        Assertions.assertTrue(lexer.lexBoolean("TRUE", 0));
        Assertions.assertTrue(lexer.lexBoolean("True", 0));
        Assertions.assertTrue(lexer.lexBoolean(" true", 1));
        Assertions.assertTrue(lexer.lexBoolean(" true ", 1));
        Assertions.assertFalse(lexer.lexBoolean("false", 0));
        Assertions.assertFalse(lexer.lexBoolean("FALSE", 0));
        Assertions.assertFalse(lexer.lexBoolean("False", 0));
        Assertions.assertFalse(lexer.lexBoolean(" false", 1));
        Assertions.assertFalse(lexer.lexBoolean(" false ", 1));
        Assertions.assertNull(lexer.lexBoolean("null", 0));
        Assertions.assertTrue(lexer.lexBoolean("trued", 0));
        Assertions.assertFalse(lexer.lexBoolean("falseee", 0));
    }

    @Test
    void lexNull() {
        JsonLexer lexer = new JsonLexer();
        Assertions.assertEquals("NULL", lexer.lexNull("null", 0));
        Assertions.assertEquals("NULL", lexer.lexNull(" null", 1));
        Assertions.assertEquals("NULL", lexer.lexNull(" null ", 1));
        Assertions.assertEquals("NULL", lexer.lexNull("nullll", 0));
        Assertions.assertNull(lexer.lexNull("nul", 0));
        Assertions.assertNull(lexer.lexNull(" nul", 1));
        Assertions.assertNull(lexer.lexNull(" anc", 1));
    }
}