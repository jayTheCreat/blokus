package model;


import java.util.*;

public class Player {
    private static final int BONUS = 15;
    private BColor color;
    private List<Piece> piecesLeft;
    private List<Piece> piecesPlayed;
    private int score;
    private boolean firstPlay;
    private boolean passed;
    private boolean isBot;

    public Player(BColor color) {
        this.color = color;
        this.piecesLeft = new ArrayList<>();
        this.piecesPlayed = new ArrayList<>();
        this.firstPlay = true;
        Arrays.stream(Shape.values()).forEach(shape->piecesLeft.add(new Piece(color,shape.getShape())));
        setScore();
        this.passed = false;
        this.isBot = false;
    }

    public Player(Player other) {
        this.color = other.getColor();
        this.piecesLeft = new ArrayList<>();
        for (Piece piece : other.getPiecesLeft()) {
            this.piecesLeft.add(new Piece(piece));
        }
        this.piecesPlayed = new ArrayList<>(other.getPiecesPlayed());
        this.score = other.getScore();
        this.firstPlay = other.isFirstPlay();
        this.passed = other.isPassed();
        this.isBot = other.isBot();
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isPassed() {
        return passed;
    }

    public BColor getColor() {
        return color;
    }

    public List<Piece> getPiecesLeft() {
        return piecesLeft;
    }

    public List<Piece> getPiecesPlayed() {
        return piecesPlayed;
    }

    public int getScore() {
        return score;
    }
    public boolean isFirstPlay() { return firstPlay; }
    public void setFirstPlay(boolean firstPlay) {
        this.firstPlay = firstPlay;
    }

    public void setScore() {
        int score = 0;
        boolean allPiecesPlayed = true;
        if (!piecesPlayed.isEmpty() && piecesPlayed.get(piecesPlayed.size()-1).getPoints() == 1 && passed) {
            score += 5;
        }
        // Deduct points for unplayed pieces
        for (Piece piece : piecesLeft) {
            allPiecesPlayed = false;
            score -= piece.getPoints();
        }
        // Add bonus points for playing all pieces
        if (allPiecesPlayed) {
            score += BONUS;
        }
        this.score = score;
    }


    public boolean hasPiecesLeft() {
        return !piecesLeft.isEmpty();
    }

    public boolean hasPiece(Piece piece) {
        return piecesLeft.contains(piece);
    }

    public boolean playPiece(Piece piece) {
        if (!hasPiece(piece)) {
            return false;
        }
        piecesLeft.remove(piece);
        piecesPlayed.add(piece);
        setScore();
        if (firstPlay)
            setFirstPlay(false);
        if (piecesLeft.isEmpty())
            setPassed(true);
        return true;
    }
    public String getType(){
        return "Human";
    }

    public void resetPlayer() {
        this.piecesLeft = new ArrayList<>();
        this.piecesPlayed.clear();
        this.firstPlay = true;
        Arrays.stream(Shape.values()).forEach(shape->piecesLeft.add(new Piece(color,shape.getShape())));
        setScore();
        this.passed = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        Player other = (Player) obj;
        return this.color == other.color;
    }
}
