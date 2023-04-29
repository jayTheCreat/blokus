package model;

import MCTS.AI;
import MCTS.Move;

import java.util.*;
import java.util.List;

public class Game extends Observable implements GameInterface{
    private static final int L_SIZE=20;
    private static final int S_SIZE=14;
    private Board board;
    private List<Player> players;
    private Player currentPlayer;
    private Piece currentPiece;
    private GameState state;
    private int numOfBots;

    public Game(int numOfPlayers, int numOfBots) {
        this.numOfBots = numOfBots;
        if (numOfPlayers > 2)
            this.board = new Board(L_SIZE);
        else
            this.board = new Board(S_SIZE);

        this.players = new ArrayList<>();
        for(int i = 0; i < numOfPlayers-numOfBots; i++) {
            BColor color = BColor.values()[i];
            players.add(new Player(color));
        }

        for (int i = 0; i < numOfBots; i++) {
            BColor color = BColor.values()[i+numOfPlayers-numOfBots];
            players.add(new AI(color));
        }

        this.currentPlayer = players.get(0);
        this.state = GameState.START;
    }
//    private void createBots(int numOfBots) {
//
//    }

    @Override
    public int getBoardSize() {
        return board.getSize();
    }
    @Override
    public Board getBoard() {
        return board;
    }
    @Override
    public List<Player> getPlayers() {
        return players;
    }
    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    @Override
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    @Override
    public GameState getState() {
        return state;
    }

    public String getWinners() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Scores:\n");
        players.forEach(e-> stringBuilder.append("player ").append(e.getColor().getColor()).append(" : ")
                .append(e.getScore()).append("\n"));
        return stringBuilder.toString();

    }

    public void setCurrentPiece(Piece currentPiece) {
        if (currentPiece != null && currentPlayer.getColor() != currentPiece.getColor()) {
            throw new GameException(currentPlayer.getColor().getColor() + " turn to play!");
        }
        else
            this.currentPiece = currentPiece;
        notifyView();
    }

    public void nextTurn() {
        isGameOver();
        if (state == GameState.MIDDLE){
            this.currentPlayer = getNextPlayer();
//            ((AI)currentPlayer).randomPlay(board);
            //notifyView();
        }
    }


    public boolean playerValidator(Player player){
        return player.hasPiecesLeft() && !player.isPassed();
    }
    public Player getNextPlayer() {

        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        while (nextPlayerIndex != currentPlayerIndex) {
            Player nextPlayer = players.get(nextPlayerIndex);
            if (playerValidator(nextPlayer)) {
                return nextPlayer;
            }
            nextPlayerIndex = (nextPlayerIndex + 1) % players.size();
        }
        if (players.stream().filter(player ->playerValidator(player)).count() == 1) {
            return playerValidator(currentPlayer) ? currentPlayer : null;
        }
        return null;
    }


    public void isGameOver() {
        int c = 0;
        for (Player player : players) {
            if (!player.hasPiecesLeft() || player.isPassed()) {
                c++;
            }
        }
        if (c==players.size()){
            state = GameState.END;
        }
        else {
            state = GameState.MIDDLE;
        }
    }
    //todo: int row,col to square
    @Override
    public void playPiece(Piece piece, int row, int col) {
        if (piece == null) {
            throw new GameException("No Piece Was Selected");
        }
        board.isValidPiecePlacement(piece, board.getBoard()[row][col], currentPlayer);
        board.placePiece(piece, board.getBoard()[row][col], currentPlayer);
        currentPlayer.playPiece(piece);
        currentPlayer.setScore();
        currentPiece = null;
        //isGameOver();
        //nextTurn();
        //notifyView();
        //return true;
    }


    public void passTurn() {
        currentPlayer.setPassed(true);
        nextTurn();
    }
    public void printBoard(){
        System.out.println("Current board state:");
        for (int row = 0; row < getBoardSize(); row++) {
            for (int col = 0; col < getBoardSize(); col++) {
                if (board.getBoard()[row][col].getColor() == BColor.WHITE||board.getBoard()[row][col].getValue()!=3) {
                    System.out.print("_ ");
                } else {
                    System.out.print(board.getBoard()[row][col].getColor().toString().charAt(0) + " ");
                }

//                System.out.print(board.getBoard()[row][col].getValue() + " ");

            }
            System.out.println();
        }
    }
    public void notifyView() {
        setChanged();
        notifyObservers();
    }

    public String endGame() {
        state = GameState.END;
        return getWinners();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void newGame() {
        board.resetBoard();
        for (Player player: players){
            player.resetPlayer();
        }
        currentPlayer=players.get(0);
        state = GameState.START;
        notifyView();
    }

    public void playPiece(Move move) {
        playPiece(move.getPiece(), move.getSquare().getX(), move.getSquare().getY());
    }

//    public void saveGameState(File file) throws IOException{
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
//            oos.writeObject(this);
//        }
//    }
//
//    public Game loadGameState(File file) throws IOException, ClassNotFoundException {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
//            Game loadedGame = (Game) ois.readObject();
//            // copy the state of the loaded game to the current game object
//            this.players = loadedGame.players;
//            this.board = loadedGame.board;
//            this.currentPlayer = loadedGame.currentPlayer;
//            this.currentPiece = loadedGame.currentPiece;
//            this.state = loadedGame.state;
//            notifyView();
//        }
//        return this;
//    }


}
