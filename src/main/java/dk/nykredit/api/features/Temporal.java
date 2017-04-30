package dk.nykredit.api.features;

import java.time.LocalDateTime;
import java.util.Optional;

import dk.nykredit.api.features.Lexer.Token;

/**
 * API feature temporal
 *   
 * @author LAJE
 */
public abstract class Temporal {

    public static class Interval extends Temporal {
        private LocalDateTime start;
        private LocalDateTime end;
    }

    public static class Period extends Temporal {
        private java.time.Period period;
    }

    public static final Temporal parse(String input) {
        return new Temporal.Parser().parse(input);
    }

    /**
     * Parser of temporal input string
     *  
     * @author LAJE
     */
    private static class Parser {

        private Temporal parse(String input) {
            Lexer lexer = Lexer.newInstance(input);
            Optional<Token> optToken = Optional.empty();
            while ((optToken = lexer.nextToken()).isPresent()) {
                Token token = optToken.get();
                switch (token.tokenType) {
                    case DATE:
                        break;
                    case IDENTIFIER:
                        continue;
                    case TIME:
                        break;
                    case SLASH:
                        break;
                    default:
                        throw new IllegalArgumentException("Syntax error - token not allowed " + token.tokenType);
                }
            }

            return null;
        }

    }

}
