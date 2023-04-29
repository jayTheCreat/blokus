package view;

import controller.MenuController;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

public class MenuView extends BorderPane{
    private Button startButton, optionsButton, exitButton;
    private VBox buttonPanel;
    private ChoiceBox numOfBots;
    private ChoiceBox numOfPlayers;
    private Label labelPlayers, labelBots;
    private MenuController controller;
    public MenuView(Stage primaryStage) {
        labelPlayers = new Label("number of players: ");
        labelPlayers.setVisible(false);
        labelBots = new Label("number of bots: ");
        labelBots.setVisible(false);

        numOfPlayers = new ChoiceBox(FXCollections.observableArrayList(1,2,3,4));
        numOfBots = new ChoiceBox();

        numOfPlayers.setOnAction(e->controller.numOfPlayerAction());
        numOfPlayers.setVisible(false);

        numOfBots.setOnAction(e->controller.newGame(primaryStage));
        numOfBots.setVisible(false);

        startButton = new Button("Start Game");
        startButton.setOnMouseClicked(e->controller.startBtnAction());

        optionsButton = new Button("Options");

        exitButton = new Button("Exit");
        exitButton.setOnMouseClicked(e->controller.close(primaryStage));

        Image image = new Image(new File("src/main/resources/images/menuPic.jpg").toURI().toString());
        buttonPanel = new VBox(new HBox(startButton, labelPlayers,numOfPlayers, labelBots,numOfBots),optionsButton,exitButton);
        setLeft(buttonPanel);
        setBackground(new Background(new BackgroundImage(
                image,
                BackgroundRepeat.REPEAT, // repeat horizontally
                BackgroundRepeat.REPEAT, // repeat vertically
                BackgroundPosition.CENTER, // position in the top-left corner
                BackgroundSize.DEFAULT // use the default size
        )));
        Scene scene = new Scene(this,500,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void setController(MenuController menuController) {
        this.controller=menuController;
    }
    public ChoiceBox getNumOfBots() {
        return numOfBots;
    }

    public ChoiceBox getNumOfPlayers() {
        return numOfPlayers;
    }

    public Label getLabelPlayers() {
        return labelPlayers;
    }

    public Label getLabelBots() {
        return labelBots;
    }
}

