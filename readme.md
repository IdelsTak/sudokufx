# sudokufx

Application starts with the `Sudoku` class, which it initializes with 81 integers.  

These must all be between 0 and 9, where 0 means that the cell is empty.  It then calls the **solve()** method and sits back and waits until the answer pops out a millisecond later.  

#### Some notes about the code.

- Each cell on the game board holds an `EnumSet` of all possible values for that cell.  
- When it reaches an `EnumSet` of size 1, it has found a solution for that cell. 
- If it ends up with size 0, then there is no solution for the  game.  
- It alternatively goes through sieving the numbers that are on the board and searching for possible answers.  
- It keeps on going until it cannot reduce any more numbers, at which point there will be several answers to the puzzle.  

The application could be expanded by making nondeterministic choices and continuing. 