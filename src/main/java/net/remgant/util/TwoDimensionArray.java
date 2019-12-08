package net.remgant.util;

import java.util.List;

public interface TwoDimensionArray<E> {
    public void put(int x, int y, E e);
    public E get(int x, int y);
    public List<E> getRow(int y);
    public List<E> getColumn(int x);
    public void setRow(int y,E[] e);
    public void setColumn(int x,E[] e);
    public int getWidth();
    public int getHeight();
}
