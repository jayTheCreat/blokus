package model;

import java.util.ArrayList;
import java.util.List;

public class Board{
    private static int size;
    private Square[][] board;

    public Board(int size) {
        Board.size = size;
        this.board = new Square[size][size];
        for (int i = 0; i <size; i++) {
            for (int j = 0; j <size; j++) {
                this.board[i][j]=new Square(i,j,BColor.WHITE);
            }
        }
    }
    public Board(Board other) {
        this.board = new Square[other.getSize()][other.getSize()];
        for (int i = 0; i < other.getSize(); i++) {
            for (int j = 0; j < other.getSize(); j++) {
                this.board[i][j] = new Square(other.board[i][j]);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Square[][] getBoard() {
        return board;
    }

    public boolean isValidPiecePlacement(Piece piece, Square centerPiece, Player player) {
        try {
            if (centerPiece.getColor() != BColor.WHITE && centerPiece.getColor() != player.getColor() && centerPiece.getValue() == Shape.PIECE) {
                throw new GameException("Invalid placement - square already occupied by another player's piece");
            }
        } catch (NullPointerException e) {
            // Ignore null pointer exception if centerPiece is null
        }

        int[][] shape = piece.getShape();
        int row = centerPiece.getX();
        int col = centerPiece.getY();

        boolean isCorner = false;
        boolean isAdjacent = false;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == Shape.PIECE) {// non-empty square in the piece
                    int r = i - 3 + row;
                    int c = j - 3 + col;
                    if (checkIsCorner(r, c)) {
                        isCorner = true;
                    }
                    if (r < 0 || r >= size || c < 0 || c >= size) {
                        // position out of bounds
                        throw new GameException("Invalid placement - position out of bounds");
                    }
                    if (board[r][c].getColor() != BColor.WHITE && board[r][c].getColor() != player.getColor() &&  board[r][c].getValue() == Shape.PIECE) {
                        // square already occupied by another player's piece
                        throw new GameException("Invalid placement - square already occupied by another player's piece");
                    }
                    try {
                        if (board[r][c].getColorValue().get(player.getColor()) == Shape.CORNER) {
                            isAdjacent = true;
                        } else if (board[r][c].getColorValue().get(player.getColor()) == Shape.SIDE) {
                            throw new GameException("Invalid placement - piece touching another player's piece side");
                        }
                    } catch (NullPointerException e) {
                        // Ignore null pointer exception if getColorValue() returns null
                    }
                }
            }
        }

        if (player.isFirstPlay()) {
            if (isCorner) {
                // Valid corner placement for first play
                return true;
            } else {
                throw new GameException("Invalid placement for first play - not on corner");
            }
        }

        if (isAdjacent) {
            // Valid placement
            return true;
        } else {
            throw new GameException("Invalid placement - piece needs to touch another player's piece corner");
        }
    }

    public static boolean checkIsCorner(int x, int y) {
        if ((x == 0 && y == 0) || (x == size-1 && y == size-1)||
                (x == size-1 && y == 0)|| (x == 0 && y == size-1))
            return true;
        return false;
    }
    public static boolean checkIfInBound(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size)
            return false;
        return true;
    }

    public void placePiece(Piece piece, Square square, Player player) {
        int[][] shape = piece.getShape();
        int row = square.getX();
        int col = square.getY();
        for (int i = 0; i < shape.length; i++) {
            int r = i - 3 + row;
            for (int j = 0; j < shape[i].length; j++) {
                int c = j - 3 + col;
                if (!checkIfInBound(r,c) || shape[i][j] == 0)
                    continue;
               // non-empty square in the piece
                if (board[r][c].getValue()!=Shape.PIECE){
                    board[r][c].setColor(player.getColor());
                    board[r][c].setValue(shape[i][j]);
                    if (board[r][c].getColorValue().get(player.getColor())==null||board[r][c].getColorValue().get(player.getColor()) != Shape.SIDE)
                        board[r][c].addColorValue(player.getColor(),shape[i][j]);
                }
            }
        }
    }

    public List<Square> getValidMoves(Piece piece, Player player) {
        List<Square>moves = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Square square = new Square(i,j,player.getColor());
                try{
                    if (isValidPiecePlacement(piece,square,player))
                        moves.add(square);
                }catch (GameException e){}
            }
        }
        return moves;
    }
    public void resetBoard() {
        for (int i = 0; i <size; i++) {
            for (int j = 0; j <size; j++) {
                this.board[i][j].resetSquare();
            }
        }
    }
}


