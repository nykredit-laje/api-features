package dk.nykredit.api.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dk.nykredit.api.features.BaseLexer.Token;
import dk.nykredit.api.features.BaseLexer.TokenType;

/**
 * Parser (and lexer) for Sort API feature
 *  
 * @author LAJE
 */
/* package */ class SortParser {

    private static enum SortTokenType implements TokenType {
        PLUS("\\+"), MINUS("\\-"), WHITESPACE("[ ]+"), COMMA(","), IDENTIFIER("[a-zA-Z][a-zA-Z0-9]*");
        
        private final String pattern;

        private SortTokenType(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public String pattern() {
            return pattern;
        }
        
    }

    public static List<Sort> parse(String input) {
        List<Sort> output = new ArrayList<>();

        BaseLexer<SortTokenType> lexer = BaseLexer.newInstance(SortTokenType.class, input);
        Optional<Token<SortTokenType>> token = Optional.empty();
        while ((token = lexer.nextToken()).isPresent()) {
            switch (token.get().getTokenType()) {
                case IDENTIFIER:
                    String identifier = token.get().getValue();
                    Sort.Direction direction = Sort.Direction.ASC;
                    done: while ((token = lexer.nextToken()).isPresent()) {
                        switch (token.get().getTokenType()) {
                            case MINUS:
                                direction = Sort.Direction.DESC;
                                break done;
                            case PLUS:
                                direction = Sort.Direction.ASC;
                                break done;
                            case IDENTIFIER:
                                throw new IllegalArgumentException("Syntax error - found another identifier while parsing an identifier");
                            case COMMA:
                                lexer.pushToken(token);
                                break done;
                            default:
                                break;
                        }
                    }
                    output.add(new Sort(identifier, direction));
                    break;
                default:
                    // Ignoring WHITESPACE, COMMA, PLUS, MINUS
                    break;
            }
        }

        return output;
    }

}
