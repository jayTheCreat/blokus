package MCTS;

import model.Piece;
import model.Square;

import java.util.Objects;

public class Move {
    private Piece piece;
    private Square square;

    public Move(Piece piece, Square square) {
        this.piece = piece;
        this.square = square;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getSquare() {
        return square;
    }
    public int getScore(){return piece.getPoints();}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(piece, move.piece) && Objects.equals(square, move.square);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, square);
    }
}