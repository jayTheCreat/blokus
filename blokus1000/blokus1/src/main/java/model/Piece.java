package model;

import java.util.Arrays;

public class Piece {
    private final BColor color;
    private int[][] shape;
    public static final int SIZE = 7;


    public Piece(BColor color, int[][] shape) {
        this.color = color;
        this.shape = shape;
    }
    public Piece(Piece other){
        color=other.getColor();
        shape=new int[other.getShape().length][other.getShape()[0].length];
        for (int i = 0; i < other.SIZE; i++) {
            shape[i] = Arrays.copyOf(other.getShape()[i], other.getShape()[i].length);
        }
    }
    public BColor getColor() {
        return color;
    }

    public int[][] getShape() {
        return shape;
    }

    public void rotateClockwise() {
        int[][] newShape = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newShape[j][SIZE - i - 1] = shape[i][j];
            }
        }
        shape=newShape;
    }
    public void rotateCounterClockwise() {
        int[][] newShape = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newShape[SIZE - j - 1][i] = shape[i][j];
            }
        }
        shape=newShape;
    }
    public void flipOver() {
        int[][] newShape = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                newShape[i][SIZE - j - 1] = shape[i][j];
            }
        }
        shape=newShape;
    }
    public int getPoints() {
        int points=0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (shape[i][j] == Shape.PIECE)
                    points++;
            }
        }
        return points;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Piece other = (Piece) obj;

        // Check if any rotation or flip results in a match
        for (int i = 0; i < 4; i++) {
            if (Arrays.deepEquals(this.shape, other.shape)) {
                return true;
            }
            other.rotateClockwise();
        }

        // Flip the piece and check if any rotation results in a match
        this.flipOver();
        for (int i = 0; i < 4; i++) {
            if (Arrays.deepEquals(this.shape, other.shape)) {
                return true;
            }
            other.rotateClockwise();
        }

        return false;
    }


    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color;
    }

}