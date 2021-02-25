package com.vietthang;

import java.util.*;


/*
    The Maze Makers is a publisher of puzzle books. One of their most popular series is maze books.
    They have a program that generates rectangular two-dimensional mazes like the one shown in Figure 1.
    The rules for these mazes are: (1) A maze has exactly two exterior cell walls missing, opening to two distinct terminal cells, (2) starting from any one cell, all other cells are reachable, (3) between any two cells in the maze there is exactly one simple path.
    Formally, a path is a sequence of cells where each cell and its successor on the path share an edge without a wall. A simple path is a path that never repeats a cell.
    The Maze Maker program uses hexadecimal digits to encode the walls and passages of a maze.
    For each cell in the maze there is a corresponding hex digit. As shown in Figure 2, the 1’s and 0’s in the 4 digit binary representation of a hex digit correspond to the walls (1’s) and passages (0’s) for each cell in the maze.
    For example, the binary encoding for the hex digit B is 1011. Starting at the top of the cell and moving clockwise around it, this digit represents a cell with a wall at the top, a passage to the right and walls at the bottom and to the left.
    A path between two maze cells successively moves one cell up, down, left or right, going through passages only.

    ### Input
        The input consists of the descriptions of T candidate mazes (1⩽T⩽120)

        Each maze description will start with two integers, H and W, indicating the height and width of the maze, respectively, such that 1⩽H⩽50 and 2⩽W⩽50.
        Following this first line will be H rows of hexadecimal digits, with each row consisting of W digits.

        The input is terminated with a line displaying a pair of zeros.
    ### Output
    For each candidate maze, the program should output the first one of the following statements that applies:
        NO SOLUTION
        UNREACHABLE CELL
        MULTIPLE PATHS
        MAZE OK
        The classification statements are defined formally as follows:
        NO SOLUTION - There is no path through the interior of the maze between the two exterior openings.
        UNREACHABLE CELL - There is at least one cell in the maze that is not reachable by following passageways from either of the openings in the exterior walls of the maze.
        MULTIPLE PATHS - There exists a pair of cells in the maze that have more than one simple path between them. Two simple paths are considered to be distinct if any part of the paths differ.
        MAZE OK - None of the above problems exist.
        Note well that for the second case given in the following examples, there is no path between the start and finish and there is an unreachable cell; the correct output should simply be NO SOLUTION, because that error message is listed first in the above list. Similarly, in the fourth example given, UNREACHABLE CELL is reported because that error has priority over the multiple paths.

    ### Sample Input 1
        6 7
        9A8C98E
        2E5753C
        980A496
        553C53C
        53C75D5
        3E3E363
        3 3
        F9A
        D3E
        3AC
        1 8
        3AAA8AAE
        6 3
        9AC
        3C5
        A24
        9A6
        5BC
        3C7
        5 4
        8A8E
        592C
        5186
        161C
        3A63
        5 4
        8AAE
        59AC
        5386
        1E1C
        3A63
        0 0
    ### Sample Output 1
        MAZE OK
        NO SOLUTION
        MAZE OK
        UNREACHABLE CELL
        MULTIPLE PATHS
        MULTIPLE PATHS
 */
public class MazeMakers {
    static final String SPACE = " ";

    public static void main(String[] args) {

        // input
        Scanner in = new Scanner(System.in);
        Maze maze = null;
        int rowCount = 0;
        // map by row and col.
        Map<Integer, Map<Integer, Cell>> cellMap = new HashMap<>();
        while (in.hasNextLine()) {
            String input = in.nextLine();
            if (input.equalsIgnoreCase("0 0")) {
                checkMaze(maze);
                break;
            }
            if (input.contains(SPACE)) { // create new Maze.
                if (maze != null) {
                    checkMaze(maze);
                }
                String[] numbers = input.split(SPACE);
                int height = Integer.parseInt(numbers[0]), width = Integer.parseInt(numbers[1]);
                maze = new Maze(height, width);
                rowCount = 0;
            } else {
                // input cell data of maze:
                char[] data = input.toCharArray();
                for (int i = 0; i < data.length; i++) {
                    Cell currentCell = new Cell();
                    currentCell.cellData = convertToBinaryString(data[i]);
                    currentCell.rowIndex = rowCount;
                    currentCell.colIndex = i;
                    currentCell.neighbours = new ArrayList<>();
                    // currentCell.cellLabel = data[i] + "(" + rowCount + "-" + i + " )";
                    if (!cellMap.containsKey(rowCount)) {
                        cellMap.put(rowCount, new HashMap<>());
                    }
                    cellMap.get(rowCount).put(currentCell.colIndex, currentCell);
                    if (currentCell.colIndex == 0 && hasNoWallOfDirection(currentCell.cellData, Direction.LEFT)) {
                        if (maze.inDoorCell == null) {
                            maze.inDoorCell = currentCell;
                        } else {
                            maze.outDoorCell = currentCell;
                        }
                    }
                    if (currentCell.colIndex == maze.width - 1 && hasNoWallOfDirection(currentCell.cellData, Direction.RIGHT)) {
                        if (maze.inDoorCell == null) {
                            maze.inDoorCell = currentCell;
                        } else {
                            maze.outDoorCell = currentCell;
                        }
                    }
                    if (currentCell.rowIndex == 0 && hasNoWallOfDirection(currentCell.cellData, Direction.UP)) {
                        if (maze.inDoorCell == null) {
                            maze.inDoorCell = currentCell;
                        } else {
                            maze.outDoorCell = currentCell;
                        }
                    }
                    if (currentCell.rowIndex == maze.height - 1 && hasNoWallOfDirection(currentCell.cellData, Direction.DOWN)) {
                        if (maze.inDoorCell == null) {
                            maze.inDoorCell = currentCell;
                        } else {
                            maze.outDoorCell = currentCell;
                        }
                    }


                    if (currentCell.colIndex >= 1) {
                        // update neighbour of relevant cells:
                        Cell leftCell = cellMap.get(rowCount).get(currentCell.colIndex - 1);

                        // check has wall between 2 currentCell or not:
                        if (hasNoWallOfDirection(currentCell.cellData, Direction.LEFT)) {
                            leftCell.neighbours.add(currentCell);
                            // update current currentCell:
                            currentCell.neighbours.add(leftCell);
                        }
                    }
                    if (rowCount > 0) {
                        Cell topCell = cellMap.get(rowCount - 1).get(currentCell.colIndex);
                        // check has wall between 2 currentCell or not:
                        if (hasNoWallOfDirection(currentCell.cellData, Direction.UP)) {
                            topCell.neighbours.add(currentCell);
                            // update current currentCell:
                            currentCell.neighbours.add(topCell);
                        }
                    }
                }
                rowCount++;
            }
        }

    }

    private static boolean hasNoWallOfDirection(String currentCellData, Direction direction) {
        if (direction.equals(Direction.LEFT)) {
            if (currentCellData.charAt(3) == '0') {
                return true;
            }
        }
        if (direction.equals(Direction.UP)) {
            if (currentCellData.charAt(0) == '0') {
                return true;
            }
        }
        if (direction.equals(Direction.RIGHT)) {
            if (currentCellData.charAt(1) == '0') {
                return true;
            }
        }
        if (direction.equals(Direction.DOWN)) {
            if (currentCellData.charAt(2) == '0') {
                return true;
            }
        }
        return false;
    }

    private static void checkMaze(Maze maze) {
        investigateMaze(maze);
        if (!maze.hasSolution) {
            System.out.println("NO SOLUTION");
            return;
        }
        if (maze.hasUnreachableCell) {
            System.out.println("UNREACHABLE CELL");
            return;
        }
        if (maze.hasMultiplePath) {
            System.out.println("MULTIPLE PATHS");
            return;
        }
        System.out.println("MAZE OK");
    }

    private static void investigateMaze(Maze maze) {
        List<Cell> traveledCell = new ArrayList<>();
        traveledCell.add(maze.inDoorCell);
        List<Cell> currentPathCell = new ArrayList<>();
        currentPathCell.add(maze.inDoorCell);
        checkPath(maze.inDoorCell, maze, traveledCell, currentPathCell, null);

        if (traveledCell.size() < maze.height * maze.width) {
            maze.hasUnreachableCell = true;
        }
    }

    private static void checkPath(Cell startCell, Maze maze, List<Cell> traveledCell, List<Cell> currentPathCell, Cell previousCell) {
        for (int i = 0; i < startCell.neighbours.size(); i++) {
            Cell cell = startCell.neighbours.get(i);
            if (cell.equals(previousCell)) {
                continue;
            }
            if (currentPathCell.contains(cell)) {
                maze.hasMultiplePath = true;
            } else {
                currentPathCell.add(cell);
            }
            // System.out.println("checking cell "+ cell.cellLabel);
            if (traveledCell.contains(cell)) {
                // if(traveledCell.get())
                // System.out.println(" already traveled "+ cell.cellLabel);
                continue;
            }
            if (cell.neighbours.contains(maze.outDoorCell)) {
                maze.hasSolution = true;
            }
            traveledCell.add(cell);
            if (cell.neighbours.size() > 1) {
                checkPath(cell, maze, traveledCell, currentPathCell, startCell);
            } else {
                currentPathCell.remove(cell);
            }
            if (cell.neighbours.size() == 1) {
                currentPathCell.remove(cell);
            }
        }
    }

    private static String convertToBinaryString(char input) {
        int hex = Integer.parseInt(String.valueOf(input), 16);
        String result = Integer.toBinaryString(hex);
        if (result.length() == 2) {
            result = "00" + result;
        } else if (result.length() == 3) {
            result = "0" + result;
        } else if (result.length() == 1) {
            result = "000" + result;
        }
        return result;
    }

    public static class Maze {
        int height;
        int width;
        Cell inDoorCell;
        Cell outDoorCell;
        boolean hasSolution = false;
        boolean hasUnreachableCell = false;
        boolean hasMultiplePath = false;

        public Maze(int height, int width) {
            this.height = height;
            this.width = width;
        }

    }

    public static class Cell {
        private String cellData;
        private int rowIndex;
        private int colIndex;
        private List<Cell> neighbours;
    }

    public enum Direction {
        LEFT, UP, RIGHT, DOWN
    }
}
