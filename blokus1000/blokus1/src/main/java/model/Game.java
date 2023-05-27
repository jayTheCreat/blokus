package model;

import MCTS.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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
        for(int i = 0; i < numOfPlayers; i++) {
            BColor color = BColor.values()[i];
            players.add(new Player(color));
        }
        this.currentPlayer = players.get(0);
        this.state = GameState.START;
    }
    public void addBot() {
        currentPlayer.setBot(true);
        currentPlayer = getNextPlayer();
    }

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
        BColor winner = getWinner();
        if (winner!=BColor.WHITE)
            stringBuilder.append(winner+" Won:\n");
        else
            stringBuilder.append("No Winner/Tie:\n");
        stringBuilder.append("Scores:\n");
        players.forEach(e-> stringBuilder.append("player ").append(e.getColor().getColor()).append(" : ")
                .append(e.getScore()).append("\n"));
        return stringBuilder.toString();
    }
    public BColor getWinner() {
        // Check if the game is still ongoing
        if (state != GameState.END) {
            return BColor.WHITE;
        }
        // Determine the winner based on the scores
        int maxScore = Integer.MIN_VALUE;
        BColor winner = BColor.WHITE;
        for (Player player : players) {
            int score = player.getScore();
            if (score > maxScore) {
                maxScore = score;
                winner = player.getColor();
            } else if (score == maxScore) {
                // In case of a tie, return null
                winner = BColor.WHITE;
            }
        }
        return winner;
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
        this.currentPlayer = getNextPlayer();
        if (currentPlayer==null)
             isGameOver();
        notifyView();
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
        if (players.stream().filter(Player::isPassed).count()==players.size()){
            state = GameState.END;
         }
        else {
            state = GameState.MIDDLE;
         }
    }
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
        notifyView();
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
        currentPlayer = players.stream()
                .filter(player -> !player.isBot())
                .findFirst()
                .orElse(null);
        state = GameState.START;
        notifyView();
    }

    public void playPiece(Move move) {
        playPiece(move.getPiece(), move.getSquare().getX(), move.getSquare().getY());
    }

}
