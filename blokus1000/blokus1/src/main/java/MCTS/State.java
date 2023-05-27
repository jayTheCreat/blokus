package MCTS;

import model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class State{
    private final List<Player> players;
    private final Board board;
    private Piece currentPiece;
    private Player currentPlayer;
    private GameState gameState;
    public State(Game game){
        players=new ArrayList<>();
        for (Player player : game.getPlayers()) {
            players.add(new Player(player));
        }
        board = new Board(game.getBoard());
        currentPiece=game.getCurrentPiece();
        currentPlayer=new Player(game.getCurrentPlayer());
        // find index of current player in other players list
        int currentPlayerIndex = game.getPlayers().indexOf(game.getCurrentPlayer());
        // set current player to the corresponding player in the new players list
        currentPlayer = players.get(currentPlayerIndex);
        gameState=game.getState();

    }
    public State(State other){
        players=new ArrayList<>();
        for (Player player : other.getPlayers()) {
            players.add(new Player(player));
        }
        board = new Board(other.getBoard());
        currentPiece=other.getCurrentPiece();
        currentPlayer = new Player(other.getCurrentPlayer());
        // find index of current player in other players list
        int currentPlayerIndex = other.getPlayers().indexOf(other.getCurrentPlayer());
        // set current player to the corresponding player in the new players list
        currentPlayer = players.get(currentPlayerIndex);
        gameState=other.getGameState();

    }
    public boolean isGameOver() {
        if (players.stream().filter(Player::isPassed).count()==players.size()){
            gameState = GameState.END;
            return true;
        } else {
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
    }

public Move getRandomUnexploredMove() {
    List<Move> validMoves = getValidMoves(currentPlayer);

    // Filter and sort the moves
    List<Move> validPlacements = validMoves.stream()
            .filter(move -> {
                try {
                    return board.isValidPiecePlacement(move.getPiece(), move.getSquare(), currentPlayer);
                } catch (GameException e) {
                    return false;
                }
            })
            .sorted(Comparator.comparing(Move::getScore).reversed())
            .collect(Collectors.toList());

    if (validPlacements.isEmpty()) {
        return null; // no valid moves available
    }
    int max=validPlacements.get(0).getScore();
    List<Move>best=validPlacements.stream().filter(move -> move.getScore()==max).collect(Collectors.toList());
    return best.get((int) (Math.random()*best.size()));
}

    public List<Move> getValidMoves(Player player) {
        List<Move> moves = new ArrayList<>();
        for (Piece piece : player.getPiecesLeft()) {
            for (int i = 0; i < 4; i++) { // 4 rotations
                piece.rotateClockwise();
                for (boolean flip : new boolean[]{false, true}) { // flip or not
                    if (flip) {
                        piece.flipOver();
                    }
                    getValidMovesForPos(piece, player, moves);
                }
            }
        }
        return moves;
    }

    public void getValidMovesForPos(Piece piece, Player player, List<Move> validMoves) {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                BColor color = board.getBoard()[i][j].getColor();
                try {
                    if ((player.isFirstPlay() && color == BColor.WHITE) || (board.getBoard()[i][j].getColorValue().get(color) != Shape.PIECE)) {
                        Square square = new Square(i, j, color);
                        try {
                            if (board.isValidPiecePlacement(piece, square, player)) {
                                Move move = new Move(piece, square);
                                if (!validMoves.contains(move)) {
                                    validMoves.add(move);
                                }
                            }
                        } catch (GameException e) {
                            // Handle the exception if needed
                        }
                    }
                } catch (NullPointerException e) {
                    // Handle the exception if needed
                }
            }
        }
    }


    public void playPiece(Move move) {
        try {
            board.isValidPiecePlacement(move.getPiece(), move.getSquare(), currentPlayer);
        }catch (GameException ignored){
            System.out.println("ojojoijojoo");
        }
        board.placePiece(move.getPiece(), move.getSquare(), currentPlayer);
        currentPlayer.playPiece(move.getPiece());
        currentPlayer.setScore();
    }
}
