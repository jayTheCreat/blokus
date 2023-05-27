package controller;

import MCTS.Mcts;
import MCTS.State;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;
import view.BlokusView;
import view.MenuView;

import javax.swing.*;
import java.util.List;

public class BlokusController  {
    private Game game;
    private BlokusView blokusView;
    private Mcts mcts;
    public BlokusController(Game game, BlokusView blokusView) {
        this.game = game;
        this.blokusView = blokusView;
        blokusView.setController(this);
        this.mcts = new Mcts();
    }
    public void passButton() {
        if (game.getState()==GameState.END)
            return;
        try {
            game.passTurn();
        }
        catch (GameException e){
            endGame();
        }
        playAI();
    }
    public void printButton(){game.printBoard();}
    public void selectPiece(Piece piece){
        if (game.getState()==GameState.END)
            return;
        try {
            game.setCurrentPiece(piece);
        }
        catch (GameException e){
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
    public void possibleMoves(Player player, Piece piece){
        if (game.getState()==GameState.END)
            return;
        if (player.getColor()!=game.getCurrentPlayer().getColor())
            return;
        List<Square>possibleMoves=game.getBoard().getValidMoves(piece,player);
        for (Square square:possibleMoves){
            ((Pane) BlokusView.getNode(square.getX(), square.getY(), blokusView.getGridBoard())).setBackground(new Background(new BackgroundFill(
                    Color.TURQUOISE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
        }
    }
    public void playPiece(int row,int col){
        if (game.getState()==GameState.END)
            return;
        try {
            game.playPiece(game.getCurrentPiece(),row,col);
            game.nextTurn();
        }
        catch (GameException e){
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
        playAI();
    }
    public void movePiece(MouseEvent e,int row, int col) {
        if (game.getState()==GameState.END)
            return;
        if (game.getCurrentPiece()==null)
            return;
        Color color = (e.getEventType() == MouseEvent.MOUSE_ENTERED? Color.GRAY: Color.TRANSPARENT);
        Color opColor = (e.getEventType() == MouseEvent.MOUSE_ENTERED? Color.TRANSPARENT: Color.GRAY);

        for (int i = 0; i < game.getCurrentPiece().getShape().length; i++) {
            int r = i - 3 + row;
            for (int j = 0; j < game.getCurrentPiece().getShape()[i].length; j++) {
                int c = j - 3 + col;
                if (game.getCurrentPiece().getShape()[i][j]==Shape.PIECE) {
                    try{
                        Pane temp = ((Pane) BlokusView.getNode(r, c, blokusView.getGridBoard()));
                        if (temp.getBackground().getFills().get(0).getFill() == opColor)
                            temp.setBackground(new Background(new BackgroundFill(
                                    color,
                                    CornerRadii.EMPTY,
                                    Insets.EMPTY
                            )));
                    } catch(NullPointerException | IndexOutOfBoundsException ex) {}
                }
            }
        }
    }
    public void rotateClockwise() {
        if (game.getState()==GameState.END)
            return;
        game.getCurrentPiece().rotateClockwise();
        game.notifyView();
        possibleMoves(game.getCurrentPlayer(),game.getCurrentPiece());
    }
    public void rotateCounterClockwise() {
        if (game.getState()==GameState.END)
            return;
        game.getCurrentPiece().rotateCounterClockwise();
        game.notifyView();
        possibleMoves(game.getCurrentPlayer(),game.getCurrentPiece());

    }
    public void flipPiece() {
        if (game.getState()==GameState.END)
            return;
        game.getCurrentPiece().flipOver();
        game.notifyView();
        possibleMoves(game.getCurrentPlayer(),game.getCurrentPiece());

    }
    public void endGame() {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"New Game", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, game.endGame(), "Score Board", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            Platform.runLater(() -> {
                if (choice == 0) {
                    // New Game option selected
                    newGame();
                } else if (choice == 1) {
                    // Exit option selected
                    exitToMenu();
                }
            });
        });
    }

    public void playAI() {
        if (game.getState() != GameState.END && game.getCurrentPlayer().isBot()) {
            // Disable GUI events
            Platform.runLater(() -> {
                blokusView.setDisable(true);
            });

            Thread thread = new Thread(() -> {
                Mcts.Node node = mcts.chooseMove(new State(game));
                System.out.println(node);
                Platform.runLater(() -> {
                    synchronized (game) {
                        try {
                            System.out.println(node);
                            if (node != null)
                                game.playPiece(node.getMove());
                            else
                                game.getCurrentPlayer().setPassed(true);
                            game.nextTurn();
                            // game.notifyView();
                            if (game.getState() != GameState.END && game.getCurrentPlayer().isBot())
                                playAI();
                        } catch (GameException e) {
                            System.out.println(9090);
                            System.out.println(node.getMove().getSquare());
                            for (int i = 0; i < 7; i++) {
                                for (int j = 0; j < 7; j++) {
                                    System.out.print(node.getMove().getPiece().getShape()[i][j]);
                                }
                                System.out.println();
                            }

                        } finally {
                            // Enable GUI events when AI is done
                            Platform.runLater(() -> {
                                blokusView.setDisable(false);
                            });
                        }
                    }
                });
            });
            thread.start();
        }
    }


    public void newGame() {
        game.newGame();
    }
    public void exitToMenu() {
        Stage stage = blokusView.getStage();
        stage.close();
        MenuView menuView = new MenuView(stage);
        MenuController menuController = new MenuController(menuView);
    }
}

