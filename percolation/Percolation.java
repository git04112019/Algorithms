/* *****************************************************************************
 *  Name: Zhe Cai
 *  Date: 2018/12/25
 *  Description: Algorithms-part 1 Assignment 1
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid; // open = True, blocked = False;
    private final int n; // row, col = n
    private final WeightedQuickUnionUF uf, ufNoBottom;
    private int numOfOpenSites = 0;
    /**
     * Constructs a n by n grid, with all sites blocked
     * Setup the UF and add two virtual nodes on the top and bottom
     * Make sure the top node is unioned with the first row nodes, so does the bottom node and the last row nodes
     * @param n the num of rows and columns in grid[][]
     * */
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        grid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }

        this.n = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2); // Two more virtual nodes on the top and the bottom
        this.ufNoBottom = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 1; i <= n; i++) {
            this.uf.union(0, i); // Union the first row and the top
            this.uf.union(n * n + 1, n * n + 1 - i); // Union the last row and the bottom
            this.ufNoBottom.union(0, i);
        }
    }

    /**
     * Given the site (i, j), return the index in the grid
     * @param row the row of the site
     * @param col the col of the site
     * */
    private int site2index(int row, int col) {
        validate(row, col);
        return (row - 1) * this.n + col;
    }

    /**
     * Given the site (i, j), validate if the site is in the grid
     * @param row the row of the site
     * @param col the col of the site
     * */
    private boolean validate(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            throw new IllegalArgumentException("row " + row + " col " + col + " is out of the grid.");
        }
        else return true;
    }

    /**
     *  Open site (row, col) if it's not open already and union it with adjacent open sites
     *  @param row the row of the site
     *  @param col the col of the site
     *  */
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            this.numOfOpenSites += 1;
        }
        grid[row - 1][col - 1] = true;
        int indexOfThisSite = site2index(row, col);

        // union adjacent open nodes
        if (row > 1 && isOpen(row - 1, col)) {
            this.uf.union(site2index(row - 1, col), indexOfThisSite);
            this.ufNoBottom.union(site2index(row - 1, col), indexOfThisSite);
        }

        if (row < this.n && isOpen(row + 1, col)) {
            this.uf.union(site2index(row + 1, col), indexOfThisSite);
            this.ufNoBottom.union(site2index(row + 1, col), indexOfThisSite);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            this.uf.union(site2index(row, col - 1), indexOfThisSite);
            this.ufNoBottom.union(site2index(row, col - 1), indexOfThisSite);
        }

        if (col < this.n && isOpen(row, col + 1)) {
            this.uf.union(site2index(row, col + 1), indexOfThisSite);
            this.ufNoBottom.union(site2index(row, col + 1), indexOfThisSite);
        }
    }

    /**
     * Given the site (i, j), check if it's open
     * @param row the row of the site
     * @param col the col of the site
     * */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    /**
     * Given the site (i, j), check if it's full (connected to the top node)
     * @param row the row of the site
     * @param col the col of the site
     * */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int indexOfThisSite = site2index(row, col);
        return isOpen(row, col) && this.ufNoBottom.connected(0, indexOfThisSite);
    }

    /**
     * Return the number of open sites in the grid
     * */
    public int numberOfOpenSites() {
        return this.numOfOpenSites;
    }

    /**
     * Check if the system percolates
     * */
    public boolean percolates() {
        if (this.n == 1) return isOpen(1, 1);
        return this.uf.connected(0, this.n * this.n + 1);
    }

}
