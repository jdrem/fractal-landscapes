package net.remgant.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jdr
 * Date: 12/20/13
 * Time: 6:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class SquareArray<E> implements TwoDimensionArray<E> {
    List<List<E>> rows;
    List<List<E>> columns;

    public SquareArray(int size) {
        rows = new ArrayList<List<E>>(size);
        columns = new ArrayList<List<E>>(size);
        for (int i = 0; i < size; i++) {
            rows.add(new ArrayList<E>(size));
            columns.add(new ArrayList<E>(size));
            for (int j=0; j<size; j++) {
                rows.get(i).add(null);
                columns.get(i).add(null);
            }
        }
    }

    public SquareArray(E[][] e) {
        this(e.length);
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < e[i].length; j++) {
                rows.get(i).set(j,e[i][j]);
                columns.get(j).set(i,e[i][j]);
            }
        }
    }

    public void put(int x, int y, E e) {
        rows.get(x).set(y,e);
        columns.get(y).set(x, e);
    }

    public E get(int x, int y) {
        return rows.get(x).get(y);
    }

    public List<E> getRow(int y) {
        return rows.get(y);
    }

    public List<E> getColumn(int x) {
        return columns.get(x);
    }

    public void setRow(int y, E[] e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setColumn(int x, E[] e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getWidth() {
        return columns.size();
    }

    public int getHeight() {
        return rows.size();
    }
}
