package net.remgant.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jdr
 * Date: 12/20/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SquareArray2<E> implements TwoDimensionArray<E> {
    E[][] array;

    public SquareArray2(Class<E> c,E[][] array) {
        E[] a =  (E[]) Array.newInstance(c,array.length);
        this.array = (E[][]) Array.newInstance(a.getClass(),array.length);
        for (int i=0; i<array.length; i++) {
            this.array[i] = (E[]) Array.newInstance(c,array[i].length);
            for (int j=0; j<array[i].length; j++) {
                this.array[i][j] = array[i][j];
            }
        }
    }
    public void put(int x, int y, E e) {
        array[x][y] = e;
    }

    public E get(int x, int y) {
        return array[x][y];
    }

    public List<E> getRow(int y) {
        return Collections.unmodifiableList(Arrays.asList(array[y]));
    }

    public List<E> getColumn(int x) {
        List<E> list = new ArrayList<E>(array[0].length);
        for (int i=0; i<array[0].length; i++)
            list.add(array[i][x]);
        return Collections.unmodifiableList(list);
    }

    public void setRow(int y, E[] e) {
        for (int i=0; i<array.length; i++)
            array[y][i] = e[i];
    }

    public void setColumn(int x, E[] e) {
        for (int i=0; i<array[0].length; i++)
                   array[i][x] = e[i];    }

    public int getWidth() {
        return array.length;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getHeight() {
        return array[0].length;
    }
}
