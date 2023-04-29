package view;

import controller.BlokusController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class BlokusView extends BorderPane implements Observer {
    private static final int SQUARE_SIZE = 33;
    private final GridPane gridBoard = new GridPane();
    private final VBox pieceBoard = new VBox();
    private final Button passButton;
    private final MenuBar menuBar;
    private Label currentPlayerLabel;
    private Label currentScoreLabel;
    private Label remainingPiecesLabel;
    private HBox controlsBox;
    private GameInterface game;
    private BlokusController controller;
    private Stage stage;


    public BlokusView(GameInterface game) {
        this.game = game;
        menuBar = new MenuBar();
        createMenu();
        // Create the board
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                Pane square = new Pane();
                square.setMinSize(SQUARE_SIZE,SQUARE_SIZE);
                square.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                int finalRow = row;
                int finalCol = col;
                square.setOnMouseEntered(e-> controller.movePiece(e,finalRow,finalCol));

                square.setOnMouseExited(e-> controller.movePiece(e,finalRow,finalCol));

                square.setOnMouseClicked(e->controller.playPiece(finalRow, finalCol));
                gridBoard.add(square,col,row);
            }
            gridBoard.setGridLinesVisible(true);
        }
        //Create the pieces for each player
        for (Player player:game.getPlayers()){
            playerPieceView(player);
        }

        //Create the labels
        currentPlayerLabel = new Label("Current Player: " + game.getCurrentPlayer().getColor());
        currentScoreLabel = new Label("Score: " + game.getCurrentPlayer().getScore());
        remainingPiecesLabel = new Label("Remaining Pieces: " + game.getCurrentPlayer().getPiecesLeft().size());

        passButton = new Button("Pass/Forfeit");
        passButton.setOnMouseClicked(e->controller.passButton());

        Button printBoard = new Button("Print Board");
        printBoard.setOnMouseClicked(e->controller.printButton());

        Button crotate = new Button("Clock_rotate");
        crotate.setOnMouseClicked(e->controller.rotateClockwise());

        Button antrotate = new Button("anti_clock_rotate");
        antrotate.setOnMouseClicked(e->controller.rotateCounterClockwise());

        Button flip = new Button("flip");
        flip.setOnMouseClicked(e->controller.flipPiece());

        Button end = new Button("end");
        end.setOnMouseClicked(e->controller.endGame());

        Button playAi = new Button("play AI");
        playAi.setOnMouseClicked(e->controller.playAI());

        controlsBox = new HBox(10, currentPlayerLabel, currentScoreLabel, remainingPiecesLabel,passButton,printBoard,crotate,antrotate,flip,end,playAi);
        controlsBox.setPadding(new Insets(10));
        controlsBox.setAlignment(javafx.geometry.Pos.CENTER);
        controlsBox.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
        setBottom(controlsBox);
        setCenter(gridBoard);
        setRight(pieceBoard);
        setBackground(new Background(new BackgroundFill(Color.AZURE, null, null)));
    }

    private void createMenu() {
        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        // Create menu items
        MenuItem newGameMenuItem = new MenuItem("New Game");
        newGameMenuItem.setOnAction(e->controller.newGame());
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e->controller.exitToMenu(stage));

//        MenuItem saveItem = new MenuItem("Save");
//        saveItem.setOnAction(event -> controller.saveGame(stage));
//
//        MenuItem loadItem = new MenuItem("Load");
//        loadItem.setOnAction(event -> controller.loadGame(stage));

        // Add menu items to file menu
        fileMenu.getItems().addAll(newGameMenuItem, exitMenuItem);

        // Add menus to menu bar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        // Add menu bar to top of border pane
        setTop(menuBar);
    }

    public void playerPieceView(Player player){
        // Creating the pieces
        FlowPane fp=new FlowPane();
        for (Piece piece:new ArrayList<>(player.getPiecesLeft())){
            //Creating The Piece
            GridPane grid = createPiece(piece);
            if (player.getColor()==game.getCurrentPlayer().getColor()){
                //make the current player pop
                grid.setBackground(new Background(new BackgroundFill(Color.WHEAT, null, null)));
            }
            if (player.isPassed()) {
                grid.setBackground(new Background(new BackgroundFill(Color.AZURE, null, null)));
                grid.setOpacity(0.35);
            }
            //highlighting the piece selected
            if (piece==game.getCurrentPiece()){
                grid.setOpacity(0.35);
            }
            grid.setOnMouseClicked(e->{
                controller.selectPiece(piece);
                controller.possibleMoves(player,piece);});
            fp.getChildren().add(grid);
        }
        VBox.setMargin(fp, new Insets(0, 0, 10, 10));
        //add the player pieces stock to the pieces board
        pieceBoard.getChildren().add(fp);
    }
    private GridPane createPiece(Piece piece){
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        for(int i = 0; i < Piece.SIZE; i++) {
            for(int j = 0; j < Piece.SIZE; j++) {
                Pane pane = new Pane();
                pane.setMinSize(7.1,7.1);
                if (piece.getShape()[i][j]==Shape.PIECE){
                    pane.setBackground(new Background(new BackgroundFill(Color.valueOf(piece.getColor().getColor()), null, null)));
                }
                grid.add(pane, j, i);
            }
        }
        return grid;
    }


    public static Node getNode(int row, int col, GridPane gridPane) {
        ObservableList<Node> childrens = gridPane.getChildren();
        Node result = null;
        for (Node node : childrens) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == col) {
                result = node;
                break;
            }
        }
        return result;
    }

    public GridPane getGridBoard() {
        return gridBoard;
    }

    public void setController(BlokusController controller) {
        this.controller = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void reloadBoard(){
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                if (game.getBoard().getBoard()[row][col].getColor() != BColor.WHITE && game.getBoard().getBoard()[row][col].getValue() == Shape.PIECE)
                    ((Pane) getNode(row, col,gridBoard)).setBackground(new Background(new BackgroundFill(Color.valueOf(game.getBoard().getBoard()[row][col].getColor().getColor()), null, null)));
                else {
                    ((Pane) getNode(row, col,gridBoard)).setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
                }
            }
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        pieceBoard.getChildren().clear();
        for (Player player : game.getPlayers()) {
            playerPieceView(player);
        }
        reloadBoard();
        if(game.getState()!= GameState.END) {
            currentPlayerLabel.setText("Current Player: " + game.getCurrentPlayer().getColor());
            currentScoreLabel.setText("Score: " + game.getCurrentPlayer().getScore());
            remainingPiecesLabel.setText("Remaining Pieces: " + game.getCurrentPlayer().getPiecesLeft().size());
        }
        else
            JOptionPane.showMessageDialog(null, game.getWinners(), "Score Board", JOptionPane.INFORMATION_MESSAGE);


    }

}
