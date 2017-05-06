package dk.nykredit.api.features;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link Sort}
 * 
 * @author LAJE
 */
public class SortTest {

    @Test
    public void testSingleAttributeStandardDirection() {
        List<Sort> list = Sort.parse("myAttribute");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("myAttribute", list.get(0).getAttribute());
        Assert.assertEquals(Sort.Direction.ASC, list.get(0).getDirection());
    }

    @Test
    public void testSingleAttributeAscDirection() {
        List<Sort> list = Sort.parse("myAttribute+");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("myAttribute", list.get(0).getAttribute());
        Assert.assertEquals(Sort.Direction.ASC, list.get(0).getDirection());
    }

    @Test
    public void testSingleAttributeDescDirection() {
        List<Sort> list = Sort.parse("myAttribute  -");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("myAttribute", list.get(0).getAttribute());
        Assert.assertEquals(Sort.Direction.DESC, list.get(0).getDirection());
    }

    @Test
    public void testMultipleAttributes() {
        List<Sort> list = Sort.parse("myAttribute1,myAttribute2+, MyAttribute3-  ");

        Assert.assertEquals(3, list.size());

        Assert.assertEquals("myAttribute1", list.get(0).getAttribute());
        Assert.assertEquals(Sort.Direction.ASC, list.get(0).getDirection());

        Assert.assertEquals("myAttribute2", list.get(1).getAttribute());
        Assert.assertEquals(Sort.Direction.ASC, list.get(1).getDirection());

        Assert.assertEquals("MyAttribute3", list.get(2).getAttribute());
        Assert.assertEquals(Sort.Direction.DESC, list.get(2).getDirection());
    }

}
