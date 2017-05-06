package dk.nykredit.api.features;

import java.util.List;

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

    public Sort(String attribute) {
        this.attribute = attribute;
        this.direction = Direction.ASC;
    }

    public Sort(String attribute, Direction direction) {
        this.attribute = attribute;
        this.direction = direction;
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
        return SortParser.parse(input);
    }

}
