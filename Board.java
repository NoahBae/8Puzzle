import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
public class Board {
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[][] board;
    private int blankI;
    private int blankJ;
    public Board(int[][] tiles) {
        this.board = tiles;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] == 0) {
                    blankI = i;
                    blankJ = j;
                    break;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(board.length).append("\n");
        for (int[] array : this.board) {
            for (int integer : array) {
                stringBuilder.append(integer).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return this.board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        int key = 1;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] != key++ && this.board[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                int num = this.board[i][j];
                if (num != 0) {
                    int valueI = (num - 1) / dimension();
                    int valueY = (num - 1) % dimension();
                    count = count + Math.abs(i - valueI) + Math.abs(j - valueY);
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        if (y == this) return false;
        Board otherBoard = (Board) y;
        return Arrays.deepEquals(otherBoard.board, this.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> list = new Stack<Board>();
        if (blankI == dimension()-1) {
            if (blankJ == dimension()-1) {
                list.push(new Board(exch(deepCopy(), blankI - 1, blankJ, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ - 1, blankI, blankJ)));
            } else if (blankJ == 0) {
                list.push(new Board(exch(deepCopy(), blankI - 1, blankJ, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ + 1, blankI, blankJ)));
            } else {
                list.push(new Board(exch(deepCopy(), blankI - 1, blankJ, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ + 1, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ - 1, blankI, blankJ)));
            }
        } else if (blankI == 0) {
            if (blankJ == dimension()-1) {
                list.push(new Board(exch(deepCopy(), blankI + 1, blankJ, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ - 1, blankI, blankJ)));
            } else if (blankJ == 0) {
                list.push(new Board(exch(deepCopy(), blankI + 1, blankJ, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ + 1, blankI, blankJ)));
            } else {
                list.push(new Board(exch(deepCopy(), blankI + 1, blankJ, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ + 1, blankI, blankJ)));
                list.push(new Board(exch(deepCopy(), blankI, blankJ - 1, blankI, blankJ)));
            }
        } else if (blankJ == dimension() - 1) {
            list.push(new Board(exch(deepCopy(), blankI + 1, blankJ, blankI, blankJ)));
            list.push(new Board(exch(deepCopy(), blankI-1, blankJ, blankI, blankJ)));
            list.push(new Board(exch(deepCopy(), blankI, blankJ - 1, blankI, blankJ)));
        } else if (blankJ == 0) {
            list.push(new Board(exch(deepCopy(), blankI + 1, blankJ, blankI, blankJ)));
            list.push(new Board(exch(deepCopy(), blankI-1, blankJ, blankI, blankJ)));
            list.push(new Board(exch(deepCopy(), blankI, blankJ + 1, blankI, blankJ)));
        } else {
            list.push(new Board(exch(deepCopy(), blankI-1, blankJ, blankI, blankJ)));
            list.push(new Board(exch(deepCopy(), blankI+1, blankJ, blankI, blankJ)));
            list.push(new Board(exch(deepCopy(), blankI, blankJ-1, blankI, blankJ)));
            list.push(new Board(exch(deepCopy(), blankI, blankJ+1, blankI, blankJ)));
        }
        return list;
    }

    public int[][] exch(int[][] clone, int i, int j, int index1, int index2) {
        int temp = clone[i][j];
        clone[i][j] = clone[index1][index2];
        clone[index1][index2] = temp;
        return clone;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(deepCopy());
        // exchange
        if ((blankI == 0 && blankJ == 0) || (blankI == 0 && blankJ == 1)) {
            int temp = twin.board[1][0];
            twin.board[1][0] = twin.board[1][1];
            twin.board[1][1] = temp;
        } else {
            int temp = twin.board[0][0];
            twin.board[0][0] = twin.board[0][1];
            twin.board[0][1] = temp;
        }
        return twin;
    }

    public int[][] deepCopy() {
        int[][] result = new int[dimension()][];
        for (int r = 0; r < dimension(); r++) {
            result[r] = this.board[r].clone();
        }
        return result;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board = new Board(array);
        StdOut.println(board.isGoal());
        StdOut.println("Hamming: " + board.hamming());
        StdOut.println("\n" + board.toString());
        StdOut.println("Manhattan: " + board.manhattan());
        for (Board boards : board.neighbors()) StdOut.println(boards);
        StdOut.println("i"+board);
        StdOut.println(board.twin());
        StdOut.println(board.isGoal());
    }
}

/*import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    // moves made so far to reach the board
    // private int moves;
    // save manhattan distances
    // private int manhattan = -1;
    private int[][] blocks;
    private int dimension;
    // record blank position
    private int blankI;
    private int blankJ;

    public Board(int[][] blocks) {
        if (blocks == null)
            dimension = 0;
        else
            dimension = blocks[0].length;
        this.blocks = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    blankI = i;
                    blankJ = j;
                }
            }
        }
        // initial value manhattan = -1
        // manhattan = manhattan();
        // this.blocks = blocks.clone();
    } // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public int dimension() {
        return dimension;
    } // board dimension n

    public int hamming() {
        int count = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int num = blocks[i][j];
                if (num != 0) { // do not count the blank square
                    if (i == dimension - 1 && j == dimension - 1) {
                        int ref = 0; // last block should be blank
                        if (num != ref)
                            count++;
                    } else {
                        int ref = dimension * i + j + 1;
                        if (num != ref)
                            count++;
                    }
                }
            }
        }
        return count;
    } // number of blocks out of place

    public int manhattan() {
        // if (manhattan != -1) return manhattan;
        int distance = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int num = blocks[i][j];
                if (num != 0) {
                    int refI = (num - 1) / dimension;
                    int refJ = (num - 1) % dimension;
                    distance = distance + Math.abs(i - refI) + Math.abs(j - refJ);
                }
            }
        }
        // manhattan = distance;
        // return manhattan;
        return distance;
    } // sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        return manhattan() == 0;
    } // is this board the goal board?

    public Board twin() {
        // Copy current Board
        Board twin = new Board(blocks);
        // exchange
        if ((blankI == 0 && blankJ == 0) || (blankI == 0 && blankJ == 1)) {
            int temp = twin.blocks[1][0];
            twin.blocks[1][0] = twin.blocks[1][1];
            twin.blocks[1][1] = temp;
        } else {
            int temp = twin.blocks[0][0];
            twin.blocks[0][0] = twin.blocks[0][1];
            twin.blocks[0][1] = temp;
        }
        return twin;
    } // a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.blocks, that.blocks);
    } // does this board equal y?

    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        if (blankI - 1 >= 0) {
            Board neighbor = new Board(blocks);
            // move one step
            neighbor.blocks[blankI][blankJ] = blocks[blankI - 1][blankJ];
            neighbor.blocks[blankI - 1][blankJ] = 0;
            // update neighbor's blank point
            neighbor.blankI--;
            // add neighbor to stack
            neighbors.push(neighbor);
        }
        if (blankI + 1 < dimension) {
            Board neighbor = new Board(blocks);
            // move one step
            neighbor.blocks[blankI][blankJ] = blocks[blankI + 1][blankJ];
            neighbor.blocks[blankI + 1][blankJ] = 0;
            // update neighbor's blank point
            neighbor.blankI++;
            // add neighbor to stack
            neighbors.push(neighbor);
        }
        if (blankJ - 1 >= 0) {
            Board neighbor = new Board(blocks);
            // move one step
            neighbor.blocks[blankI][blankJ] = blocks[blankI][blankJ - 1];
            neighbor.blocks[blankI][blankJ - 1] = 0;
            // update neighbor's blank point
            neighbor.blankJ--;
            // add neighbor to stack
            neighbors.push(neighbor);
        }
        if (blankJ + 1 < dimension) {
            Board neighbor = new Board(blocks);
            // move one step
            neighbor.blocks[blankI][blankJ] = blocks[blankI][blankJ + 1];
            neighbor.blocks[blankI][blankJ + 1] = 0;
            // update neighbor's blank point
            neighbor.blankJ++;
            // add neighbor to stack
            neighbors.push(neighbor);
        }
        return neighbors;
    } // all neighboring boards

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    } // string representation of this board (in the output format specified below)

    public static void main(String[] args) {
        System.out.println("hello world.");
    } // unit tests (not graded)
} */
