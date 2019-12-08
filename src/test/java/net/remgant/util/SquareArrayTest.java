package net.remgant.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SquareArrayTest {

    @Test
    public void integerConstructorTest() {
        SquareArray<Integer> array = new SquareArray<Integer>(3);
        array.put(0, 0, 0);
        array.put(0, 1, 1);
        array.put(0, 2, 2);

        array.put(1, 0, 3);
        array.put(1, 1, 4);
        array.put(1, 2, 5);

        array.put(2, 0, 6);
        array.put(2, 1, 7);
        array.put(2, 2, 8);


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

    @Test
    public void arrayConstructorTest() {
        Integer input[][] = {{0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}};
        SquareArray<Integer> array = new SquareArray<Integer>(input);
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
