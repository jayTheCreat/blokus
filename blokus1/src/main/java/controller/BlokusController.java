package controller;

import MCTS.AI;
import MCTS.Mcts;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;
import model.Shape;
import view.BlokusView;
import view.MenuView;

import javax.swing.*;
import java.util.List;

public class BlokusController  {

    private Game game;
    private BlokusView blokusView;

    public BlokusController(Game game, BlokusView blokusView) {
        this.game = game;
        this.blokusView = blokusView;
        blokusView.setController(this);
    }
    public void passButton() {
        try {
            game.passTurn();
            game.notifyView();
        }
        catch (GameException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Score Board", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void printButton(){game.printBoard();}
    public void selectPiece(Piece piece){
        try {
            game.setCurrentPiece(piece);
        }
        catch (GameException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
    }
    public void possibleMoves(Player player, Piece piece){
//        if (player.isFirstPlay()) {
//            for (int row = 0; row < game.getBoardSize(); row++) {
//                for (int col = 0; col < game.getBoardSize(); col++) {
//                    if (Board.checkIsCorner(row,col) && game.getBoard().getBoard()[row][col].getColor() == BColor.WHITE) {
//                        ((Pane)BlokusView.getNode(row,col,blokusView.getGridBoard())).setBackground(new Background(new BackgroundFill(
//                                Color.TURQUOISE,
//                                CornerRadii.EMPTY,
//                                Insets.EMPTY
//                        )));
//                    }
//                }
//            }
//        }
//        else {
//            for (int row = 0; row < game.getBoardSize(); row++) {
//                for (int col = 0; col < game.getBoardSize(); col++) {
//                    if (((Pane)BlokusView.getNode(row,col,blokusView.getGridBoard())).getBackground().getFills().get(0).getFill() == Color.TRANSPARENT && game.getBoard().getBoard()[row][col].getColorValue().get(piece.getColor())!=null && game.getBoard().getBoard()[row][col].getColorValue().get(piece.getColor()) == Shape.CORNER) {
//                        ((Pane) BlokusView.getNode(row, col, blokusView.getGridBoard())).setBackground(new Background(new BackgroundFill(
//                                Color.TURQUOISE,
//                                CornerRadii.EMPTY,
//                                Insets.EMPTY
//                        )));
//                    }
//                }
//            }
//        }
        List<Square>possibleMoves=game.getBoard().getValidMoves(piece,player);
        for (Square square:possibleMoves){
            ((Pane) BlokusView.getNode(square.getX(), square.getY(), blokusView.getGridBoard())).setBackground(new Background(new BackgroundFill(
                    Color.TURQUOISE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )));
        }
    }
    //    public void playPiece(int row,int col){
//        try {
//            game.playPiece(game.getCurrentPiece(),row,col);
//            game.nextTurn();
//            if (game.getCurrentPlayer().getType().equals("AI")) {
//                //((AI) currentPlayer).randomPlay(board);
//                AI ai = ((AI)game.getCurrentPlayer());
////                Thread thread = new Thread(()-> {
////                    //Platform.runLater(()->{
////                        try {
////                            ai.mctsPlay(game);
////                        }catch (GameException e){}
////                   // });
////                });
////                thread.start();
////                game.nextTurn();
//            }
//
//        }
//        catch (GameException e){
//            System.out.println(e.getMessage());
//            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//    }
    public void playPiece(int row,int col){
//    try {
//        game.playPiece(game.getCurrentPiece(),row,col);
//        game.nextTurn();
//    } catch (GameException e) {
//        System.out.println(e.getMessage());
//        JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//    }
        try {
            game.playPiece(game.getCurrentPiece(),row,col);
            game.nextTurn();
            game.notifyView();
//        if (game.getCurrentPlayer().getType().equals("AI")) {
//            AI ai = ((AI)game.getCurrentPlayer());
//            Thread thread = new Thread(() -> {
//                try {
//                    Mcts.Node node=ai.mctsPlay(game);
//                    Platform.runLater(() -> {
//                        try {
//                            game.playPiece(node.getPiece(), node.getMove().getX(), node.getMove().getY());
//                            game.nextTurn();
//                        } catch (GameException e) {
//                            System.out.println(e.getMessage());
//                            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                        }
//                    });
//                } catch (GameException e) {
//                    Platform.runLater(() -> {
//                        System.out.println(e.getMessage());
//                        JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                    });
//                }
//            });
//            thread.start();
//        }
        }
        catch (GameException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }



    public void movePiece(MouseEvent e,int row, int col) {
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
                        if (((Pane)BlokusView.getNode(r,c,blokusView.getGridBoard())).getBackground().getFills().get(0).getFill() == opColor)
                            ((Pane)BlokusView.getNode(r,c,blokusView.getGridBoard())).setBackground(new Background(new BackgroundFill(
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
        game.getCurrentPiece().rotateClockwise();
        game.notifyView();
        possibleMoves(game.getCurrentPlayer(),game.getCurrentPiece());
    }

    public void rotateCounterClockwise() {
        game.getCurrentPiece().rotateCounterClockwise();
        game.notifyView();
        possibleMoves(game.getCurrentPlayer(),game.getCurrentPiece());

    }

    public void flipPiece() {
        game.getCurrentPiece().flipOver();
        game.notifyView();
        possibleMoves(game.getCurrentPlayer(),game.getCurrentPiece());

    }

    public void endGame() {
        JOptionPane.showMessageDialog(null, game.endGame(), "Score Board", JOptionPane.INFORMATION_MESSAGE);

    }

    public void playAI() {
        System.out.println(game.getCurrentPlayer().getType());
        if (game.getCurrentPlayer().getType().equals("AI")) {
//        if (game.getCurrentPlayer() instanceof AI) {
            AI ai = ((AI) game.getCurrentPlayer());
            Thread thread = new Thread(() -> {
                Mcts.Node node = ai.mctsPlay(game);
                Platform.runLater(() -> {
                    try {
                        if (node!=null)
                            game.playPiece(node.getMove());
                        game.nextTurn();
                        game.notifyView();
                    } catch (GameException e) {}
                });
            });
            thread.start();
        }
    }
    public void nextp(){
//        game.notifyView();
//        game.nextTurn();
//        game.notifyView();


    }

    public void newGame() {
        game.newGame();
    }

    public void exitToMenu(Stage stage) {
        stage.close();
        MenuView menuView = new MenuView(stage);
        MenuController menuController = new MenuController(menuView);
    }

//    public void loadGame(Stage stage) {
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Load Game");
//        File file = fileChooser.showOpenDialog(stage);
//        if (file != null) {
//            try {
//                game = game.loadGameState(file);
//                game.notifyView();
//                // update the UI with the new game state
//            } catch (IOException | ClassNotFoundException e) {
//                // handle exception
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    public void saveGame(Stage stage) {
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Game");
//        File file = fileChooser.showSaveDialog(stage);
//        if (file != null) {
//            try {
//                game.saveGameState(file);
//            } catch (IOException e) {
//                // handle exception
//            }
//        }
//
//    }
}

