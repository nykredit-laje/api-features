package dk.nykredit.api.features;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple RexExp based lexer for API feature token set. 
 * In most cases the lexer just ignores (skips) unkown characters.
 * 
 * <p>A {@link Lexer} instance is not thread-safe and cannot be reused</p>
 *   
 * @author LAJE
 */
/* package */ class Lexer {
    private final Pattern pattern;
    private final Matcher matcher;

    public static enum TokenType {
        DATE("[0-9][0-9][0-9][0-9]-(0[0-9]|1[0-2])-(0[1-9]|([12][0-9]|3[01]))"), TIME("([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]"),
        NUMBER("-?[0-9]+"), PLUS("\\+"), MINUS("\\-"), WHITESPACE("[ ]+"), COMMA(","), IDENTIFIER("[a-zA-Z][a-zA-Z0-9]+"), SLASH("/");

        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }

    public static class Token {
        public TokenType tokenType;
        public String value;

        public Token(TokenType tokenType, String value) {
            super();
            this.tokenType = tokenType;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", tokenType.name(), value);
        }
    }

    private Lexer(String regex, String input) {
        this.pattern = Pattern.compile(regex);
        this.matcher = pattern.matcher(input);
    }

    public static Lexer newInstance(String input) {
        StringBuffer sb = new StringBuffer(64);
        for (TokenType tokenType : TokenType.values())
            sb.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        return new Lexer(sb.substring(1), input);
    }

    public Optional<Token> nextToken() {
        if (matcher.find()) {
            for (TokenType tokenType : TokenType.values()) {
                String value = matcher.group(tokenType.name());
                if (value != null) {
                    return Optional.of(new Token(tokenType, value));
                }
            }
            throw new IllegalArgumentException("Syntax Error near position " + matcher.end());
        }
        return Optional.empty();
    }

}
