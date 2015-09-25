# sudoku

*A Java Sudoku Solver*

Born from my frustration of not being able to solve even a simple sudoku 
myself, I did this solver without looking at any existing algorithms. I
consider that I do not have to bother to solve a single Sudoku anymore
since I solved them all with this program. I used an operational research
approach to build the resolution algorithm.

## How To Start The Program

### From Source

From Eclipse, run the class `org.formix.sudoku.gui.SudokuSolver`.

### From The Executable Jar

From the command line, run `javaw -jar sudokusolver-1.0.0-jwd.jar`

## Usage

You can create your own Sudoku by entering your base numbers and then
CTRL-Clicking the cell you want to get fixed.

### Top Button Bar

#### Level DropDown

Allows selection of the level of difficulty of the downloaded grid.

#### Download

Gets a Sudoku from [websudoku.com](http://like.websudoku.com) of the 
selected level.

#### Generate

Generates a random sudoku with 26 fixed cells without regard of the selected 
level. My sudoku generation algorithm is crude and produce multi-solution
easy-to-resolve grids. It sucks.

#### Cancel

Stops the current execution.

### Left Button Bar

#### Load

Loads a previously saved sudoku grid.

#### Save

Saves the current sudoku grid.

#### Solve

Solves the current sudoku grid.

#### Clear

Clears all writable cells. Keep frozen cells in place.

#### Empty

Empties the whole grid.

### Log Messages

The bottom list displays messages generated while solving the grid.
