package model;

public class Piece {
    private final BColor color;
    private int[][] shape;
    public static final int SIZE = 7;


    public Piece(BColor color, int[][] shape) {
        this.color = color;
        this.shape = shape;
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
}