package dk.nykredit.api.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dk.nykredit.api.features.Lexer.Token;

/**
 * API feature Sort
 *  
 * @author LAJE
 */
public class Sort {

    private String attribute = null;
    private Direction direction = Direction.ASC;

    public static enum Direction {
        ASC, DESC
    }

    private Sort(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getAttribute(), getDirection());
    }

    public static final List<Sort> parse(String input) {
        return new SortParser().parse(input);
    }

    /**
     * Parser of Sort 
     *  
     * @author LAJE
     */
    /* package */ static class SortParser {

        private List<Sort> sortList = new ArrayList<>();
        private boolean stateParsingSort = false;
        private Sort sort = null;

        private void pushIdentifier(String attribute) {
            if (stateParsingSort) {
                throw new IllegalStateException("Syntax error - please separate attributes by a comma");
            }
            stateParsingSort = true;
            sort = new Sort(attribute);
        }

        private void pushPlus() {
            if (!stateParsingSort) {
                throw new IllegalStateException("Syntax error - plus and minus can only be set after an attribute identifier");
            }
            sort.direction = Sort.Direction.ASC;
        }

        private void pushMinus() {
            if (!stateParsingSort) {
                throw new IllegalStateException("Syntax error - plus and minus can only be set after an attribute identifier");
            }
            sort.direction = Sort.Direction.DESC;
        }

        private void pushNewState() {
            if (!stateParsingSort) {
                throw new IllegalStateException("Syntax error - attribute definition missing before comma");
            }
            sortList.add(sort);
            stateParsingSort = false;
            sort = null;
        }

        public final List<Sort> parse(String input) {
            Lexer lexer = Lexer.newInstance(input);

            Optional<Token> optToken = Optional.empty();

            while ((optToken = lexer.nextToken()).isPresent()) {
                Token token = optToken.get();
                switch (token.tokenType) {
                    case WHITESPACE:
                        continue; // Whitespace is ignored
                    case COMMA:
                        pushNewState();
                        break;
                    case IDENTIFIER:
                        pushIdentifier(token.value);
                        break;
                    case PLUS:
                        pushPlus();
                        break;
                    case MINUS:
                        pushMinus();
                        break;
                    default:
                        throw new IllegalArgumentException("Syntax error - unsupported token " + token);
                }
            }

            if (stateParsingSort) {
                pushNewState();
            }

            return sortList;
        }

    }

}
