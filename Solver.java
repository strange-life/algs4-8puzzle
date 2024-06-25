import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private final boolean isSolvable;
    private final int moves;
    private final Iterable<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> pq = new MinPQ<>();

        pq.insert(new Node(initial, false));
        pq.insert(new Node(initial.twin(), true));

        Node node;
        Board board;
        do {
            node = pq.delMin();
            board = node.board;

            for (Board b : board.neighbors()) {
                if (node.parent == null || !b.equals(node.parent.board)) {
                    pq.insert(new Node(b, node));
                }
            }
        } while (!board.isGoal());

        if (node.isTwin) {
            isSolvable = false;
            moves = -1;
            solution = null;
            return;
        }

        ArrayList<Board> boards = new ArrayList<>();
        do {
            boards.add(node.board);
            node = node.parent;
        } while (node != null);
        Collections.reverse(boards);

        isSolvable = true;
        moves = boards.size() - 1;
        solution = boards;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private static class Node implements Comparable<Node> {
        private final Node parent;
        private final Board board;
        private final boolean isTwin;
        private final int moves;
        private final int distance;
        private final int priority;

        public Node(Board board, boolean isTwin) {
            parent = null;
            this.board = board;
            this.isTwin = isTwin;
            moves = 0;
            distance = board.manhattan();
            priority = moves + distance;
        }

        public Node(Board board, Node parent) {
            this.parent = parent;
            this.board = board;
            isTwin = parent.isTwin;
            moves = parent.moves + 1;
            distance = board.manhattan();
            priority = moves + distance;
        }

        public int compareTo(Node node) {
            if (priority == node.priority) {
                return Integer.compare(distance, node.distance);
            }

            return Integer.compare(priority, node.priority);
        }

        public int hashCode() {
            return board.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (this == obj) return true;
            if (this.getClass() != obj.getClass()) return false;

            Node that = (Node) obj;

            return board.equals(that.board);
        }
    }
}
