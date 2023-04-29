package model;


import java.util.*;

public class Player {
    private static final int BONUS = 15;
    private BColor color;
    private List<Piece> piecesLeft;
    private Set<Piece> piecesPlayed;
    private int score;
    private boolean firstPlay;
    private boolean passed;

    public Player(BColor color) {
        this.color = color;
        this.piecesLeft = new ArrayList<>();
        this.piecesPlayed = new HashSet<>();
        this.score = 0;
        this.firstPlay = true;
        Arrays.stream(Shape.values()).forEach(shape->piecesLeft.add(new Piece(color,shape.getShape())));
//        for (int i = 0; i < 5; i++) {
//            piecesLeft.add(new Piece(color,Shape.values()[i]));
//        }
        this.passed = false;
    }

    public Player(Player other) {
        this.color=other.getColor();
        this.piecesLeft=new ArrayList<>(other.piecesLeft);
        this.piecesPlayed=new HashSet<>(other.piecesPlayed);
        this.score=other.getScore();
        this.firstPlay=other.isFirstPlay();
        this.passed=other.isPassed();
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

    public Set<Piece> getPiecesPlayed() {
        return piecesPlayed;
    }

    public int getScore() {
        return score;
    }
    public boolean isFirstPlay() { return firstPlay; }
    public void setFirstPlay(boolean firstPlay) {
        this.firstPlay = firstPlay;
    }

    //    public void setScore(int points) {
//        score += points;
//        if(!hasPiecesLeft()) {
//            for (Piece piece : piecesPlayed) {
//                score -= piece.getShape().getPoints();
//            }
//            if (points==1)
//                score+=BONUS;
//        }
//    }
    public void setScore() {
        int score = 0;
        boolean allPiecesPlayed = true;
//        Piece lastPiecePlayed = null;
//        for (Piece piece : piecesPlayed) {
//            lastPiecePlayed = piece;
//        }
//        if (lastPiecePlayed != null && lastPiecePlayed.getShape().getPoints() == 1) {
//            score += 5;
//        }
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
        return true;
    }
    public String getType(){
        return "Human";
    }

    public void resetPlayer() {
        this.piecesLeft = new ArrayList<>();
        this.piecesPlayed.clear();
        this.score = 0;
        this.firstPlay = true;
        Arrays.stream(Shape.values()).forEach(shape->piecesLeft.add(new Piece(color,shape.getShape())));
//        for (int i = 0; i < 5; i++) {
//            piecesLeft.add(new Piece(color,Shape.values()[i]));
//        }
        this.passed = false;
    }
}
