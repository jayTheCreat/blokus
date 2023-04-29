package MCTS;

import model.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class State{
    private final List<Player> players;
    private final Board board;
    private Piece currentPiece;
    private Player currentPlayer;
    private GameState gameState;
    public static int debugCounter =0;
    private Map<Piece, List<Move>> validMovesCache = new HashMap<>();

    public State(Game game){
        players=new ArrayList<>();
        for (Player player : game.getPlayers()) {
            players.add(new Player(player));
        }
        board = new Board(game.getBoard());
        currentPiece=game.getCurrentPiece();
        currentPlayer=game.getCurrentPlayer();
        gameState=game.getState();
        for (Piece piece:currentPlayer.getPiecesLeft()){
            validMovesCache.put(piece,getValidMoves(piece,currentPlayer));
        }

    }
    public State(State other){
        debugCounter++;
//        System.out.println(debugCounter);
        players=new ArrayList<>();
        for (Player player : other.getPlayers()) {
            players.add(new Player(player));
        }
        board = new Board(other.getBoard());
        currentPiece=other.getCurrentPiece();
        currentPlayer=new Player(other.getCurrentPlayer());
        gameState=other.getGameState();
        for (Piece piece:currentPlayer.getPiecesLeft()){
            validMovesCache.put(piece,getValidMoves(piece,currentPlayer));
        }
    }
    public List<Move>movelist(){
        List<Piece> pieces = currentPlayer.getPiecesLeft();
        List<Move> moves=new ArrayList<>();
        for (Piece piece: pieces){
            moves.addAll(getValidMoves(piece, currentPlayer));
        }
        return moves;
    }
    public boolean isGameOver() {
        int c = 0;
        for (Player player : players) {
            if (!player.hasPiecesLeft() || player.isPassed()||movelist().size()==0) {
                c++;
            }
        }
        if (c==players.size()){
            gameState = GameState.END;
            return true;
        }
        else {
            gameState = GameState.MIDDLE;
            return false;
        }
    }
    public BColor getWinner() {
        // Check if the game is still ongoing
        if (getGameState() != GameState.END) {
            return BColor.WHITE;
        }
        // Determine the winner based on the scores
        int maxScore = Integer.MIN_VALUE;
        BColor winner = BColor.WHITE;
        for (Player player : players) {
            int score = player.getScore();
            System.out.println(score);
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
    public GameState getGameState() {
        return gameState;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Piece getCurrentPiece() {
        return currentPiece;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void printBoard(){
        System.out.println("Current board state:");
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
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
    public Board getBoard() {
        return board;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean playerValidator(Player player){
        return player.hasPiecesLeft() && !player.isPassed();
    }
    public void advanceTurn() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        while (nextPlayerIndex != currentPlayerIndex) {
            Player nextPlayer = players.get(nextPlayerIndex);
            if (playerValidator(nextPlayer)) {
                currentPlayer = nextPlayer;
                return;
            }
            nextPlayerIndex = (nextPlayerIndex + 1) % players.size();
        }
        if (players.stream().filter(this::playerValidator).count() == 1) {
            currentPlayer = playerValidator(currentPlayer) ? currentPlayer : null;
        }
        for (Piece piece:currentPlayer.getPiecesLeft()){
            validMovesCache.put(piece,getValidMoves(piece,currentPlayer));
        }
    }
    public Move getRandomUnexploredMove() {
        List<Piece> pieces = currentPlayer.getPiecesLeft();
        Piece piece = pieces.get((int) (Math.random() * pieces.size()));
        List<Move> moves = validMovesCache.get(piece);
        if (moves==null||moves.isEmpty()) {
            return null;
        }
        return moves.get((int) (Math.random() * moves.size()));
    }
    public List<Move> getValidMoves(Piece piece, Player player) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < 4; i++) { // 4 rotations
            for (boolean flip : new boolean[]{false, true}) { // flip or not
                piece.rotateClockwise();
                if (flip) {
                    piece.flipOver();
                }
                moves.addAll(getValidMoves2(piece,player));
            }
        }
        return moves;
    }
//        for (int i = 0; i < board.getSize(); i++) {
//            for (int j = 0; j < board.getSize(); j++) {
//                if (!(board.getBoard()[i][j].getColor() != BColor.WHITE && board.getBoard()[i][j].getColor() != player.getColor() &&  board.getBoard()[i][j].getValue() == Shape.PIECE)) {
//                    Square square = new Square(i, j, player.getColor());
//                    try {
//                        if (board.isValidPiecePlacement(piece, square, player))
//                            moves.add(new Move(piece, square));
//                    } catch (GameException ignored) {
//                    }
//                }
//            }
//        }
//        return moves;
//    }

    public List<Move> getValidMoves2(Piece piece, Player player) {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                BColor color = board.getBoard()[i][j].getColor();
                int value = board.getBoard()[i][j].getValue();
                if (color == BColor.WHITE || (color == player.getColor() && value == Shape.CORNER)) {
                    Square square = new Square(i, j, player.getColor());
                    try {
                        if (board.isValidPiecePlacement(piece, square, player)) {
                            validMoves.add(new Move(piece, square));
                        }
                    } catch (GameException ignored) {
                    }
                }
            }
        }
        return validMoves;
    }

    public void playPiece(Move move) {
        try {
            board.isValidPiecePlacement(move.getPiece(), move.getSquare(), currentPlayer);
        }catch (GameException ignored){}
        board.placePiece(move.getPiece(), move.getSquare(), currentPlayer);
        currentPlayer.playPiece(move.getPiece());
        currentPlayer.setScore();
        advanceTurn();
    }
}
