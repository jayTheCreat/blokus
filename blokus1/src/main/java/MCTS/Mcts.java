package MCTS;

import model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Mcts {
    private static final double EXPLORATION_CONSTANT = Math.sqrt(2);
    private static final int SIMULATION_ROUNDS = 1500;

    public Mcts() {
    }

    public Node chooseMove(State state) {
        int begin = State.debugCounter;
        // Create root node for MCTS tree
        Node rootNode = new Node(new State(state), null, null);
        // Perform MCTS simulations to choose move
        long t;
        for (int i = 0; i < SIMULATION_ROUNDS; i++) {
            //Selection
            t = System.currentTimeMillis();
            Node selectedNode = select(rootNode);
//            System.out.println("selection time:" + (System.currentTimeMillis() - t));
            //Expansion
            t = System.currentTimeMillis();
            Node expandedNode = expand(selectedNode);
//            System.out.println("expandtion time:" + (System.currentTimeMillis() - t));
            //Simulation
            t = System.currentTimeMillis();
            double result = simulate(expandedNode);
//            System.out.println("simulation time:" + (System.currentTimeMillis() - t));
            //BackPropagation
            t = System.currentTimeMillis();
            backpropagate(expandedNode, result);
//            System.out.println("backProagation time:" + (System.currentTimeMillis() - t));

        }
        // Select best move based on tree statistics
        Node bestChild = rootNode.children.stream().max(Comparator.comparingDouble(child ->
                (double) child.value / child.visits + EXPLORATION_CONSTANT * Math.sqrt(Math.log(rootNode.visits) / child.visits)
        )).orElse(null);

//        System.out.println("delta = " + (State.debugCounter - begin));
        State.debugCounter = 0;

        if (bestChild != null) {
            return bestChild;
        } else {
            return null;
        }
    }

    private Node select(Node node) {
        while (!node.isLeaf()) {
            node = node.selectChild();
        }
        return node;
    }

    private Node expand(Node node) {
        Duration deltaTime = Duration.ZERO;
        Instant time0 = Instant.now();

        State newGame = new State(node.state);
        Move move = newGame.getRandomUnexploredMove();
        if (move == null) {
            return null;
        }
        Instant time1 = Instant.now();

        newGame.playPiece(move);
        Instant time2 = Instant.now();
        newGame.advanceTurn();
        Node childNode = new Node(newGame, node, move);
        node.children.add(childNode);

//        System.out.println("============\nDuration = " + Duration.between(time0, time1));
//        time0 = time1;
//        time1 = time2;
//        System.out.println("Duration = " + Duration.between(time0, time1));
        return childNode;
    }

    private double simulate(Node node) {
        if (node == null) return Integer.MIN_VALUE;
        State simGame = new State(node.state);
        // Play random moves until game is over
        while (simGame.getGameState()==GameState.MIDDLE) {
            Move move = simGame.getRandomUnexploredMove();
            if (move != null) {
                simGame.playPiece(move);
                //simGame.printBoard();
            }
            else
                simGame.isGameOver();
        }
        //Return result of game
        BColor winner = simGame.getWinner();
        if (winner==node.state.getCurrentPlayer().getColor()) {
            return 1;
        } else if (winner == BColor.WHITE) {
            return  0.5;
        } else {
            return  0;
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


    public static class Node {
        private final State state;
        private final Node parent;
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
                    (double) child.value / child.visits + EXPLORATION_CONSTANT * Math.sqrt(Math.log(visits) / child.visits)
            )).orElseThrow(IllegalStateException::new);
        }
        public Move getMove() {
            return move;
        }


    }
}