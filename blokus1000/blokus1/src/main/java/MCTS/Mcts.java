package MCTS;

import model.BColor;
import model.GameException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Mcts {
    private static final double EXPLORATION_CONSTANT = Math.sqrt(2);
    private static final int TIME_SIMULATION = 3;

    public Mcts() {}

//    public Node chooseMove(State state) {
//        // Create root node for MCTS tree
//        Node rootNode = new Node(new State(state), null, null);
//        // Perform MCTS simulations to choose move
//
////        for (int i = 0; i < SIMULATION_ROUNDS; i++) {
////            //Selection
//////            t = System.currentTimeMillis();
////            Node selectedNode = select(rootNode);
//////            System.out.println("selection time:" + (System.currentTimeMillis() - t));
////            //Expansion
//////            t = System.currentTimeMillis();
////            Node expandedNode = expand(selectedNode);
//////            System.out.println("expandtion time:" + (System.currentTimeMillis() - t));
////            //Simulation
////            double result = simulate(expandedNode);
//////            System.out.println("simulation time:" + (System.currentTimeMillis() - t));
//////            System.out.println("pppp:"+result);
////            //BackPropagation
////            backpropagate(expandedNode, result);
//////            System.out.println("backProagation time:" + (System.currentTimeMillis() - t));
////        }
//        long t = System.currentTimeMillis()/1000;
//        while (System.currentTimeMillis() / 1000 - t < TIME_SIMULATION){
//            //Selection
//            Node selectedNode = select(rootNode);
////            System.out.println("selection time:" + (System.currentTimeMillis() - t));
//            //Expansion
//            Node expandedNode = expand(selectedNode);
////            System.out.println("expandtion time:" + (System.currentTimeMillis() - t));
//            //Simulation
//            double result = simulate(expandedNode);
////            System.out.println("simulation time:" + (System.currentTimeMillis() - t));
////            System.out.println("pppp:"+result);
//            //BackPropagation
//            backpropagate(expandedNode, result);
////            System.out.println("backProagation time:" + (System.currentTimeMillis() - t));
//        }
//        //Select best move based on tree statistics
////        Node bestChild = rootNode.children.stream().max(Comparator.comparingDouble(child ->
////                child.value / child.visits + EXPLORATION_CONSTANT * Math.sqrt(Math.log(rootNode.visits) / child.visits)
////        )).orElse(null);
//        Node bestChild = rootNode.selectChild();
////        printTree(rootNode, "");
//
//        if (bestChild != null) {
//            System.out.println("rg");
//            return bestChild;
//        } else {
//            System.out.println("fv");
//            return null;
//        }
//    }
public Node chooseMove(State state) {
    // Create root node for MCTS tree
    Node rootNode = new Node(new State(state), null, null);

    // Set the end time
    long endTime = System.currentTimeMillis() + TIME_SIMULATION * 1000;
    while (System.currentTimeMillis() < endTime) {
        //Selection
        Node selectedNode = select(rootNode);
        //Expansion
        Node expandedNode = expand(selectedNode);
        //Simulation
        double result = simulate(expandedNode);
        //BackPropagation
        backpropagate(expandedNode, result);

    }

    // Select the best move based on tree statistics
    Node bestChild = rootNode.selectChild();

    if (bestChild != null) {
        System.out.println("Selected move: " + bestChild.move);
        return bestChild;
    } else {
        System.out.println("No move selected.");
        return null;
    }
}

    private Node select(Node node) {
        while (!node.isLeaf()) {
            node = node.selectChild();
        }
        return node;
    }
//    private Node expand(Node node) {
//        State newGame = new State(node.state);
//
//        Move move = newGame.getRandomUnexploredMove();
//        if (move == null) {
//            return node;
//        }
//        newGame.playPiece(move);
////        newGame.printBoard();
//        Node childNode = new Node(newGame, node, move);
//        node.children.add(childNode);
//        return childNode;
//    }
    private Node expand(Node node) {
        if (node.state.isGameOver()) {
            return node;
        }

        List<Move> validMoves = node.state.getValidMoves(node.state.getCurrentPlayer());

        // Filter and sort the moves
        List<Move> filteredMoves = validMoves.stream()
                .filter(move -> {
                    try {
                        return node.state.getBoard().isValidPiecePlacement(move.getPiece(), move.getSquare(), node.state.getCurrentPlayer());
                    } catch (GameException e) {
                        return false;
                    }
                })
                .sorted(Comparator.comparing(Move::getScore).reversed())
                .collect(Collectors.toList());

        int numMoves = filteredMoves.size();
        if (numMoves == 0) {
            return node;
        }

        int numChildren = Math.min(5, numMoves);

        for (int i = 0; i < numChildren; i++) {
            Move move = filteredMoves.get(i);
            State newGame = new State(node.state);
            newGame.playPiece(move);
            Node childNode = new Node(newGame, node, move);
            node.children.add(childNode);
        }

        if (numChildren > 0) {
            int randomIndex = (int) (Math.random() * numChildren);
            return node.children.get(randomIndex);
        } else {
            return node;
        }
    }

    private double simulate(Node node) {
        State simGame = new State(node.state);
        simGame.advanceTurn();
        // Play random moves until the game is over
        while (!simGame.isGameOver()) {
            Move move = simGame.getRandomUnexploredMove();
            if (move != null) {
                simGame.playPiece(move);
            } else {
                simGame.getCurrentPlayer().setPassed(true);
            }
            simGame.advanceTurn();
        }
        // Return result of the game
        BColor winner = simGame.getWinner();
        if (winner == node.state.getCurrentPlayer().getColor()) {
            return 1.0;
        } else if (winner == BColor.WHITE) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

    private void backpropagate(Node node, double result) {
        Node tempNode = node;
        while (tempNode != null) {
            tempNode.visits++;
            tempNode.value += result;
            tempNode = tempNode.parent;
        }
    }


//    private void printTree(Node node, String prefix) {
//        System.out.println(prefix + "+-- State: " + node.state);
//        System.out.println(prefix + "|   Visits: " + node.visits);
//        System.out.println(prefix + "|   Value: " + node.value);
//        System.out.println(prefix + "|   Move: " + node.move);
//        for (int i = 0; i < node.children.size(); i++) {
//            Node child = node.children.get(i);
//            String childPrefix = prefix + (i == node.children.size() - 1 ? "    " : "|   ");
//            printTree(child, childPrefix);
//        }
//    }
    public static class Node {
        private final State state;
        private Node parent;
        private final List<Node> children;
        private final Move move;
        private int visits;
        private double value;
        public Node(State state, Node parent, Move move) {
            this.state = state;
            this.parent = parent;
            this.move = move;
            this.children = new ArrayList<>();
            this.visits = 0;
            this.value = 0;
        }
        public boolean isLeaf() {
            return children.isEmpty();
        }
        public Node selectChild() {
            return children.stream().max(Comparator.comparingDouble(child ->
                    child.value / child.visits + EXPLORATION_CONSTANT * Math.sqrt(Math.log(visits) / child.visits)
            )).orElse(null);
        }
        public Move getMove() {
            return move;
        }
    }
}
