package controller;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;
import view.BlokusView;
import view.MenuView;

import java.util.ArrayList;
import java.util.List;

public class MenuController {
    private MenuView menuView;

    public MenuController(MenuView menuView) {
        this.menuView = menuView;
        menuView.setController(this);
    }
    public void numOfPlayerAction() {
        int selectedNumOfPlayers = (int) menuView.getNumOfPlayers().getSelectionModel().getSelectedItem();
        if (selectedNumOfPlayers == 1) {
            menuView.getNumOfBots().setValue(0);
            newGame(menuView.getPrimaryStage());
        } else {
            List<Integer> botOptions = new ArrayList<>();
            for (int i = 0; i < selectedNumOfPlayers; i++) {
                botOptions.add(i);
            }
            menuView.getNumOfBots().setItems(FXCollections.observableArrayList(botOptions));

            menuView.getNumOfBots().setVisible(true);
            menuView.getLabelBots().setVisible(true);
        }
    }


    public void newGame(Stage primaryStage){
        int numOfPlayers = (int) menuView.getNumOfPlayers().getValue();
        int numOfBots = (int) menuView.getNumOfBots().getValue();
        Game game = new Game(numOfPlayers,numOfBots); //GameInterface
        for (int i = 0; i < numOfBots; i++) {
            game.addBot();
        }
        BlokusView blokusView = new BlokusView(game);
        BlokusController blokusController = new BlokusController(game,blokusView);
        game.addObserver(blokusView);
        blokusView.setStage(primaryStage);
        Scene scene = new Scene(blokusView);
        primaryStage.setScene(scene);

        primaryStage.sizeToScene(); // resize stage to new scene dimensions
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void startBtnAction() {
        menuView.getNumOfPlayers().setVisible(true);
        menuView.getLabelPlayers().setVisible(true);
    }

    public void close(Stage primaryStage) {
        primaryStage.close();
    }
}