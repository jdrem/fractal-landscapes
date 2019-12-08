package net.remgant.util;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;


public class SquareArray2Test {
    @Test
    public void constructorTest() {
        Integer input[][] = {{0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}};
        SquareArray2<Integer> array = new SquareArray2<Integer>(Integer.class, input);
        assertEquals(0, (int) array.get(0, 0));
        assertEquals(1, (int) array.get(0, 1));
        assertEquals(2, (int) array.get(0, 2));

        assertEquals(3, (int) array.get(1, 0));
        assertEquals(4, (int) array.get(1, 1));
        assertEquals(5, (int) array.get(1, 2));

        assertEquals(6, (int) array.get(2, 0));
        assertEquals(7, (int) array.get(2, 1));
        assertEquals(8, (int) array.get(2, 2));

        List<Integer> row0 = array.getRow(0);
        assertEquals(0, (int) row0.get(0));
        assertEquals(1, (int) row0.get(1));
        assertEquals(2, (int) row0.get(2));

        List<Integer> col0 = array.getColumn(0);
        assertEquals(0, (int) col0.get(0));
        assertEquals(3, (int) col0.get(1));
        assertEquals(6, (int) col0.get(2));
    }
}
