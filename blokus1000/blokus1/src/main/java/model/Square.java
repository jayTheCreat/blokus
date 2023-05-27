package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Square {
    private int x;
    private int y;
    private int value;
    private BColor color;
    private Map<BColor,Integer> colorValue;

    public Square(int x, int y, BColor color) {
        this.x = x;
        this.y = y;
        this.value = 0;
        this.color = color;
        colorValue = new HashMap<>();
        this.colorValue.put(color,value);
    }

    public Square(Square other) {
        this.x = other.getX();
        this.y = other.getY();
        this.value = other.getValue();
        this.color = other.getColor();
        this.colorValue = new HashMap<>(other.getColorValue());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addColorValue(BColor bColor, int value) {
        this.colorValue.put(bColor,value);
    }

    public Map<BColor, Integer> getColorValue() {
        return colorValue;
    }

    public BColor getColor() {
        return color;
    }

    public void setColor(BColor color) {
        this.color = color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void resetSquare() {
        this.value = 0;
        this.color = BColor.WHITE;
        this.colorValue.clear();
        this.colorValue.put(color,value);
    }

    @Override
    public String toString() {
        return "Square{" +
                "x=" + x +
                ", y=" + y +
                ", value=" + value +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return x == square.x && y == square.y && value == square.value && color == square.color && Objects.equals(colorValue, square.colorValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, value, color, colorValue);
    }
}

