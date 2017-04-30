package dk.nykredit.api.features;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import dk.nykredit.api.features.Lexer.Token;
import dk.nykredit.api.features.Lexer.TokenType;

/**
 * Test of {@link Lexer} 
 * 
 * @author LAJE
 */
public class LexerTest {

    @Test
    public void testDate() {
        Optional<Token> token = Lexer.newInstance("1000-10-10").nextToken();
        Assert.assertTrue(token.isPresent());
        Assert.assertTrue(token.get().tokenType == Lexer.TokenType.DATE);
        Assert.assertEquals("1000-10-10", token.get().value);
    }

    @Test
    public void testTime() {
        Optional<Token> token = Lexer.newInstance("23:59:17").nextToken();
        Assert.assertTrue(token.isPresent());
        Assert.assertTrue(token.get().tokenType == Lexer.TokenType.TIME);
        Assert.assertEquals("23:59:17", token.get().value);
    }

    @Test
    public void testNumber() {
        Optional<Token> token = Lexer.newInstance("012").nextToken();
        Assert.assertTrue(token.isPresent());
        Assert.assertTrue(token.get().tokenType == Lexer.TokenType.NUMBER);
        Assert.assertEquals("012", token.get().value);
    }

    @Test
    public void testSimpleSequence() {
        Lexer lexer = Lexer.newInstance("12,-2, hans+, my1Attribute12-");
        assertToken(lexer.nextToken().get(), TokenType.NUMBER, "12");
        assertToken(lexer.nextToken().get(), TokenType.COMMA, ",");
        assertToken(lexer.nextToken().get(), TokenType.NUMBER, "-2");
        assertToken(lexer.nextToken().get(), TokenType.COMMA, ",");
        assertToken(lexer.nextToken().get(), TokenType.WHITESPACE, " ");
        assertToken(lexer.nextToken().get(), TokenType.IDENTIFIER, "hans");
        assertToken(lexer.nextToken().get(), TokenType.PLUS, "+");
        assertToken(lexer.nextToken().get(), TokenType.COMMA, ",");
        assertToken(lexer.nextToken().get(), TokenType.WHITESPACE, " ");
        assertToken(lexer.nextToken().get(), TokenType.IDENTIFIER, "my1Attribute12");
        assertToken(lexer.nextToken().get(), TokenType.MINUS, "-");
        Assert.assertFalse(lexer.nextToken().isPresent());
    }

    private static final void assertToken(Token token, TokenType shouldHaveType, String shouldHaveValue) {
        if (token.tokenType != shouldHaveType) {
            throw new AssertionError(String.format("Token was expected to have type %s but it was %s", shouldHaveType, token.tokenType));
        }
        if (!token.value.equals(shouldHaveValue)) {
            throw new AssertionError(String.format("Token was expected to have value %s but it was %s", shouldHaveValue, token.value));
        }
    }

}
