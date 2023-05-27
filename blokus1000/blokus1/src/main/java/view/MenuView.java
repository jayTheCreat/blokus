package view;

import controller.MenuController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView extends BorderPane{
    private final Stage primaryStage;
    private Button startButton, helpButton, exitButton;
    private VBox buttonPanel;
    private ChoiceBox numOfBots;
    private ChoiceBox numOfPlayers;
    private Label labelPlayers, labelBots;
    private MenuController controller;
    public MenuView(Stage primaryStage) {
        this.primaryStage = primaryStage;
//        labelPlayers = new Label("number of players: ");
//        labelPlayers.setVisible(false);
//        labelBots = new Label("number of bots: ");
//        labelBots.setVisible(false);
//
//        numOfPlayers = new ChoiceBox(FXCollections.observableArrayList(1,2,3,4));
//        numOfBots = new ChoiceBox();
//
//        numOfPlayers.setOnAction(e->controller.numOfPlayerAction());
//        numOfPlayers.setVisible(false);
//
//        numOfBots.setOnAction(e->controller.newGame(primaryStage));
//        numOfBots.setVisible(false);
//
//        startButton = new Button("Start Game");
//        startButton.setOnMouseClicked(e->controller.startBtnAction());
//
//        optionsButton = new Button("Options");
//
//        exitButton = new Button("Exit");
//        exitButton.setOnMouseClicked(e->controller.close(primaryStage));
//
//        Image image = new Image(new File("src/main/resources/images/menuPic.jpg").toURI().toString());
//        buttonPanel = new VBox(new HBox(startButton, labelPlayers,numOfPlayers, labelBots,numOfBots),optionsButton,exitButton);
//        setLeft(buttonPanel);
//        setBackground(new Background(new BackgroundImage(
//                image,
//                BackgroundRepeat.REPEAT, // repeat horizontally
//                BackgroundRepeat.REPEAT, // repeat vertically
//                BackgroundPosition.CENTER, // position in the top-left corner
//                BackgroundSize.DEFAULT // use the default size
//        )));
//        Scene scene = new Scene(this,500,400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
        labelPlayers = new Label("Number of Players:");
        labelPlayers.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        labelPlayers.setVisible(false);

        labelBots = new Label("Number of Bots:");
        labelBots.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        labelBots.setVisible(false);

        numOfPlayers = new ChoiceBox(FXCollections.observableArrayList(1, 2, 3, 4));
        numOfPlayers.setStyle("-fx-font-size: 14px;");
        numOfPlayers.setOnAction(e -> controller.numOfPlayerAction());
        numOfPlayers.setVisible(false);

        numOfBots = new ChoiceBox();
        numOfBots.setStyle("-fx-font-size: 14px;");
        numOfBots.setOnAction(e -> controller.newGame(primaryStage));
        numOfBots.setVisible(false);

        startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 18px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setOnMouseClicked(e -> controller.startBtnAction());

        helpButton = new Button("Help");
        helpButton.setStyle("-fx-font-size: 18px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        helpButton.setOnMouseClicked(e -> openHelpPage());

        exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #F44336; -fx-text-fill: white;");
        exitButton.setOnMouseClicked(e -> controller.close(primaryStage));

        VBox buttonPanel = new VBox(startButton, new HBox(labelPlayers, numOfPlayers), new HBox(labelBots, numOfBots), helpButton, exitButton);
        buttonPanel.setSpacing(10);
        buttonPanel.setStyle("-fx-padding: 20px; -fx-alignment: center; -fx-background-color: #333333;");

        StackPane stackPane = new StackPane(buttonPanel);
        stackPane.setStyle("-fx-background-color: #ECEFF1;");

        Scene scene = new Scene(stackPane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu");
        primaryStage.show();

    }
    public static void openHelpPage() {
        Stage helpStage = new Stage();
        helpStage.setTitle("Help");

        TextArea helpText = new TextArea();
        helpText.setEditable(false);
        helpText.setWrapText(true);
        helpText.setText("Game Rules:\n\n" +
                "1. The game is played on a square board.\n" +
                "2. Each player has a set of unique pieces.\n" +
                "3. Players take turns placing their pieces on the board.\n" +
                "4. Pieces can only be placed if they touch a corner of a piece already on the board.\n" +
                "5. Pieces cannot overlap or extend beyond the board boundaries.\n" +
                "6. The game ends when no player can make a valid move.\n" +
                "7. The player with the highest score wins.\n\n" +
                "Enjoy playing Blokus!");

        VBox helpLayout = new VBox(helpText);
        helpLayout.setPadding(new Insets(10));
        helpLayout.setStyle("-fx-background-color: #F5F5F5;");

        Scene helpScene = new Scene(helpLayout);
        helpStage.setScene(helpScene);
        helpStage.show();
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

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

