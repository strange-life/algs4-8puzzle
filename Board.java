import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int hamming;
    private final int manhattan;

    public Board(int[][] tiles) {
        int dimension = tiles.length;
        int hammingSum = 0;
        int manhattanSum = 0;

        this.tiles = new int[dimension][dimension];

        for (int row = 0; row < dimension; row += 1) {
            for (int col = 0; col < dimension; col += 1) {
                this.tiles[row][col] = tiles[row][col];
                if (tiles[row][col] == 0) continue;

                int current = tiles[row][col] - 1;
                int goal = row * dimension + col;
                if (current == goal) continue;

                hammingSum += 1;

                int vertical = Math.abs(row - current / dimension);
                int horizontal = Math.abs(col - current % dimension);
                manhattanSum += vertical + horizontal;
            }
        }

        hamming = hammingSum;
        manhattan = manhattanSum;
    }

    public static void main(String[] args) {
        int[][] tiles = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board board = new Board(tiles);

        StdOut.println(board);
        StdOut.printf("Dimension: %d\n", board.dimension());
        StdOut.printf("Is goal: %s\n", board.isGoal());
        StdOut.printf("Hamming: %d\n", board.hamming());
        StdOut.printf("Manhattan: %d\n", board.manhattan());
        StdOut.printf("Same tiles board equals: %s\n", board.equals(new Board(tiles)));

        StdOut.println("Neighbors:");
        for (Board item : board.neighbors()) {
            StdOut.println(item);
        }

        StdOut.println("Twin:");
        StdOut.println(board.twin());
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (this.getClass() != y.getClass()) return false;

        Board board = (Board) y;

        return Arrays.deepEquals(tiles, board.tiles);
    }

    // string representation of this board
    public String toString() {
        int dimension = dimension();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dimension).append('\n');

        for (int row = 0; row < dimension; row += 1) {
            for (int col = 0; col < dimension; col += 1) {
                stringBuilder.append(tiles[row][col]);
                if (col < dimension - 1) stringBuilder.append(' ');
            }

            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        int dimension = dimension();
        int blankRow = -1, blankCol = -1;
        for (int row = 0; row < dimension; row += 1) {
            for (int col = 0; col < dimension; col += 1) {
                if (tiles[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                    break;
                }
            }
        }

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] direction : directions) {
            int targetRow = blankRow + direction[0], targetCol = blankCol + direction[1];

            if (isValid(targetRow, targetCol)) {
                neighbors.add(new Board(swap(blankRow, blankCol, targetRow, targetCol)));
            }
        }

        return neighbors;
    }

    private boolean isValid(int row, int col) {
        int dimension = dimension();

        return row >= 0 && row < dimension && col >= 0 && col < dimension;
    }

    private int[][] swap(int sourceRow, int sourceCol, int targetRow, int targetCol) {
        int dimension = dimension();
        int[][] newTiles = new int[dimension][dimension];

        for (int row = 0; row < dimension; row += 1) {
            System.arraycopy(tiles[row], 0, newTiles[row], 0, dimension);
        }

        int temp = newTiles[sourceRow][sourceCol];
        newTiles[sourceRow][sourceCol] = newTiles[targetRow][targetCol];
        newTiles[targetRow][targetCol] = temp;

        return newTiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int dimension = dimension();
        Board board = null;

        for (int i = 0; i < dimension * dimension - 1; i += 1) {
            int x = i / dimension, y = i % dimension;
            int xx = (i + 1) / dimension, yy = (i + 1) % dimension;

            if (tiles[x][y] != 0 && tiles[xx][yy] != 0) {
                board = new Board(swap(x, y, xx, yy));
            }
        }

        return board;
    }
}
