import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Solver {
    //private ArrayList<Board> roots;
    //private ArrayList<Board> twinRoots;
    //private ArrayList<Board> twinSeen = new ArrayList<Board>();
    private Stack<Board> solution;
    private Stack<Board> twinSolution;
    private int moves = -1;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Argument cannot be null");
        SearchNode initialNode = new SearchNode(initial, null, 0);
        Board twinBoard = initial.twin();
        SearchNode twinInitialNode = new SearchNode(twinBoard, null, 0);

        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        minPQ.insert(initialNode);
        twinPQ.insert(twinInitialNode);

        //roots = new ArrayList<Board>();
        //twinRoots = new ArrayList<Board>();
        //ArrayList<Board> seen = new ArrayList<Board>();
        solution = new Stack<Board>();
        twinSolution = new Stack<Board>();

        while (true) {
            if (!minPQ.isEmpty()) {
                SearchNode min = minPQ.delMin();
                if (min.board.isGoal()) {
                    this.moves = min.moves;
                    while (min != null) {
                        solution.push(min.board);
                        min = min.prev;
                    }
                    return;
                } else {
                    for (Board board : min.board.neighbors()) {
                        if (min.prev != null && board.equals(min.prev.board)) {
                            continue;
                        }
                        minPQ.insert(new SearchNode(board, min, min.moves + 1));
                    }
                }
            }

            if (!twinPQ.isEmpty()) {
                SearchNode twinMin = twinPQ.delMin();
                if (twinMin.board.isGoal()) { //rem this
                    this.moves = twinMin.moves; // rem this
                    while (twinMin != null) { //rem this whole three line segment
                        twinSolution.push(twinMin.board);
                        twinMin = twinMin.prev;
                    }
                    /*this.moves = -1; //de-commnet this three line segment
                    solution = null;
                    return; */
                } else {
                    for (Board board : twinMin.board.neighbors()) {
                        if (twinMin.prev != null && board.equals(twinMin.prev.board)) {
                            continue;
                        }
                        twinPQ.insert(new SearchNode(board, twinMin, twinMin.moves + 1));
                    }
                }
            }
            /*SearchNode min = minPQ.delMin();
            roots.add(min.board);
            seen.add(min.board);
            if (min.board.isGoal()) { break; }
            for (Board board : min.board.neighbors()) {
                if (!seen.contains(board)) {
                    seen.add(board);
                    minPQ.insert(new SearchNode(board));
                }
            } */

            /*SearchNode twinMin = twinPQ.delMin();
            twinRoots.add(twinMin.board);
            twinSeen.add(twinMin.board);
            if (twinMin.board.isGoal()) break;
            for (Board boards : twinMin.board.neighbors()) {
                if (!twinSeen.contains(boards)) {
                    twinSeen.add(boards);
                    twinPQ.insert(new SearchNode(boards));
                }
            } */
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        //return this.initial.manhattan() % 2 == 0;
        //return true;
        return this.moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable())
            return solution;
        else
            return null;
        //Iterable<Board> ret = roots.size() > twinRoots.size() ? twinRoots : roots;
        //return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int priority;
        private final int moves;
        private final SearchNode prev;

        private SearchNode(Board board, SearchNode prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            int hamming = this.board.hamming();
            int manhattan = this.board.manhattan();
            this.priority = moves + board.manhattan();
        }

        @Override
        public String toString() {
            return board.toString();
        }

        public int compareTo(SearchNode node) {
            return Integer.compare(this.priority, node.priority);
        }
    }

    /*private class manhattanOrder implements Comparator<SearchNode>  {
        public int compare(SearchNode node1, SearchNode node2) {
            int b1Manhattan = node1.priority;
            int b2Manhattan = node2.priority;
            return Integer.compare(b1Manhattan, b2Manhattan);
        }
    } */

    // test client (see below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
             for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
