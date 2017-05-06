package dk.nykredit.api.features;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base lexer implementation using Java RegExp group features
 *  
 * @param T Enum type 
 * @author LAJE
 */
/* package */ class BaseLexer<T extends Enum<T>> {
    private final Pattern pattern;
    private final Matcher matcher;
    private final EnumSet<T> enumSet;
    private final Queue<Optional<Token<T>>> queue = new LinkedList<>(); // Used as FIFO stack

    private BaseLexer(EnumSet<T> enumSet, String regex, String input) {
        this.enumSet = enumSet;
        this.pattern = Pattern.compile(regex);
        this.matcher = pattern.matcher(input);
    }

    public Optional<Token<T>> nextToken() {
        if (!queue.isEmpty()) {
            return queue.remove();
        }
        if (matcher.find()) {
            for (T tokenType : enumSet) {
                String value = matcher.group(tokenType.name());
                if (value != null) {
                    return Optional.of(new Token<T>(tokenType, value));
                }
            }
            throw new IllegalArgumentException("Syntax Error near position " + matcher.end());
        }
        return Optional.empty();
    }

    public void pushToken(Optional<Token<T>> token) {
        queue.add(token);
    }

    public static <T extends Enum<T> & TokenType> BaseLexer<T> newInstance(Class<T> enumType, String input) {
        StringBuffer sb = new StringBuffer(64);
        EnumSet<T> enumSet = EnumSet.allOf(enumType);
        for (T eType : enumSet)
            sb.append(String.format("|(?<%s>%s)", eType.name(), eType.pattern()));
        return new BaseLexer<T>(enumSet, sb.substring(1), input);
    }


    /**
     * TokenType interface all Lexer Token types must implement
     * 
     * @author LAJE
     */
    public static interface TokenType {
        String pattern();
    }

    /**
     * Generic lexer token (parsed)
     *  
     * @author LAJE
     */
    public static class Token<T> {
        private T tokenType;
        private String value;

        public Token(T tokenType, String value) {
            super();
            this.tokenType = tokenType;
            this.value = value;
        }

        public T getTokenType() {
            return tokenType;
        }

        public String getValue() {
            return value;
        }
    }

}
