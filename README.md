# sudoku

*A Java Sudoku Solver*

Born from my frustration of not being able to solve even a simple sudoku 
myself, I did this solver without looking at any existing algorithms. I
consider that I do not have to bother to solve a single sudoku anymore
since I solved them all with this program.

[Version 1.0.0](https://github.com/formix/sudoku/releases/tag/1.0.0)

![Sudoku Solver](https://cloud.githubusercontent.com/assets/8600014/10090085/137b2aae-62f9-11e5-8ae3-cda2ca52b1b7.png)

It takes between 2 and 10 seconds to solve an evil level sudoku. Sometimes, 
the resolver get stuck in a local optimum and will take up to 30 seconds 
or more to resolve. In these rare cases, the output message list (bottom 
section) may becomes too big and past 10000 messages, it slows down the 
whole application. This is not a core solver algorithm issue but a UI 
problem.

## How To Start The Program

### From Source

From Eclipse, run the class `org.formix.sudoku.gui.SudokuSolver`.

### From The Executable Jar

From the command line, run `javaw -jar sudokusolver-1.0.0-jwd.jar`

## Usage

You can create your own sudoku by entering your base numbers and then
CTRL-Clicking the cell you want to freeze.

### Top Button Bar

#### Level DropDown

Allows selection of the level of difficulty for the downloaded grid.

#### Download

Gets a sudoku grid from [websudoku.com](http://like.websudoku.com) of the 
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

Empties the whole grid and the message log.

### Message Log

The bottom list displays messages generated while solving the grid.
