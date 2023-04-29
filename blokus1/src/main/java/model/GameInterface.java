package model;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface GameInterface {
    int getBoardSize();
    List<Player> getPlayers();
    Player getCurrentPlayer();
    Board getBoard();
    Piece getCurrentPiece();
    GameState getState();
    void playPiece(Piece piece, int row, int col);
    void nextTurn();
    String getWinners();
}
