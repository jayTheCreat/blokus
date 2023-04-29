package MCTS;

import MCTS.Mcts;
import MCTS.State;
import model.*;

import java.util.List;
import java.util.Random;

public class AI extends Player {

    private Random random;
    private Mcts mcts;
    public AI(BColor color) {
        super(color);
        this.random = new Random();
        this.mcts = new Mcts();
    }

    public Mcts.Node mctsPlay(Game game) {
        State state = new State(game);
        // Play the selected move.
        Mcts.Node node=mcts.chooseMove(state);
        if (node==null)
            setPassed(true);
        return node;
    }
    public void randomPlay(Board board){
        List<Square> posiblesMoves;
        Piece piece;
        int c=0;
        do {
            piece = getPiecesLeft().get(random.nextInt(getPiecesLeft().size()));
            posiblesMoves = board.getValidMoves(piece,this);
            c++;
        }while (posiblesMoves.isEmpty()&&c<5);
        if (posiblesMoves.isEmpty()||c>5)
            setPassed(true);
        else {
            Square pos = posiblesMoves.get(random.nextInt(posiblesMoves.size()));
            board.placePiece(piece, pos, this);
            playPiece(piece);
        }

    }
    @Override
    public String getType(){
        return "AI";
    }
}
