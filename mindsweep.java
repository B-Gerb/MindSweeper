// PLEASE PLAY A GAME FULLY THROUGH UNTIL THE END
// THERE IS A FUN ANIMATION THAT WE SPENT A VERY VERY VERY VERY 
// VERY VERY VERY LONG TIME CODING, ENJOY PLAYING THE GAME :)!
// imports
import java.util.ArrayList;
import java.util.Arrays;

import tester.*;
import javalib.impworld.*;

import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;


//main game world
class MindWorld extends World {
  ArrayList<Cell> allCells = new ArrayList<Cell>(); // holds cells
  int rows; // number of rows
  int cols; // number of columns
  boolean gameOver; // indicates if the game is over
  double time; // keeps track of time
  double score; // keeps track of player score
  boolean mainMenu; // indicates if main menu is being displayed
  int tickTT; // timer for transition
  int waiter; // keeps track of time intervals
  boolean displayScores; // indicates if scores are being displayed
  ArrayList<Double> allScores = new ArrayList<Double>(); // stores all scores

  // resets game state
  void gameWipe() {
    this.allCells = new ArrayList<Cell>(); // holds cells
    this.rows = 0;
    this.cols = 0;
    this.gameOver = false;
    this.time = 0;
    this.score = 0;
    this.tickTT = 0;
    this.waiter = 0;
  }

  // checks if the player has won
  boolean winCheck() {
    for (int i = 0; i < this.allCells.size(); ++i) {
      if (allCells.get(i).represent != -1 && !allCells.get(i).clicked) {
        return false;
      }
    }
    return true;
  }

  // default constructor
  MindWorld() {
    this.allCells = new ArrayList<Cell>(); // holds cells
    this.rows = 0;
    this.cols = 0;
    this.gameOver = false;
    this.mainMenu = true;

    this.time = 0;
    this.score = 0;
    this.tickTT = 0;
    this.waiter = 0;
  }

  // constructor for starting the game with specific parameters
  MindWorld(int rows, int cols, ArrayList<Integer> bombs, boolean preClick) {
    this.displayScores = false;

    this.gameOver = false;
    this.mainMenu = false;
    this.time = 0;
    this.score = 0;
    this.waiter = 0;
    this.tickTT = 0;
    // for manual testing
    if (rows * cols != bombs.size()) {
      throw new IllegalArgumentException("null sizes " + bombs.size());
    }
    this.rows = rows;
    this.cols = cols;
    // new cell and corresponding bomb value
    this.cellCreation(rows, cols, bombs);
    this.addneighbors();
  }

  // adds cells to array list
  void cellCreation(int rows, int cols, ArrayList<Integer> bombs) {
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        allCells.add(new Cell(bombs.remove(0)));

      }
    }
  }

  // initializes world with random bomb count
  MindWorld(int rows, int cols, int bombs) {
    this.displayScores = false;

    this.gameOver = false;
    this.time = 0;
    this.score = 0;
    this.waiter = 0;
    this.tickTT = 0;
    this.mainMenu = false;

    // random testing
    this.rows = rows;
    this.cols = cols;
    // generate random order of bomb positions
    ArrayList<Integer> bomborder = this.bombCreation(this.rows, this.cols, bombs, new Random());
    this.cellCreation(rows, cols, bomborder);

    this.addneighbors();
  }

  // constructor for starting the game with a specific seed
  MindWorld(int rows, int cols, int bombs, Random seed) {
    this.displayScores = false;
    this.gameOver = false;
    this.time = 0;
    this.score = 0;
    this.rows = rows;
    this.cols = cols;
    this.tickTT = 0;
    this.waiter = 0;
    this.mainMenu = false;

    // generate a random order of bomb positions given seed
    ArrayList<Integer> bomborder = this.bombCreation(this.rows, this.cols, bombs, seed);
    this.cellCreation(rows, cols, bomborder);

    this.addneighbors(); // add new neighbor
  }

  // generate random order of bomb positions
  ArrayList<Integer> bombCreation(int row, int col, int count, Random seed) {
    this.gameOver = false;
    this.time = 0;
    this.score = 0;
    ArrayList<Integer> order = new ArrayList<Integer>(); // holds bomb order
    for (int i = 0; i < row * col; ++i) {
      order.add(0);
    }
    // generate bomb positions until given count is reached
    while (count > 0) {
      int spot = seed.nextInt(row * col);
      if (order.get(spot) != -1) {
        order.set(spot, -1);
        count--;
      }
    }
    return order;

  }

  // handles keyboard events
  public void onKeyEvent(String key) {
    if (this.gameOver && key.equals("enter")) {
      this.mainMenu = true;
    }
    if (this.displayScores && key.equals("escape")) {
      this.displayScores = false;
      this.mainMenu = true;
    }
  }

  // addes neighbors for each cell
  void addneighbors() {
    // loop through each cell in grid
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        // calc the current cells val in the grid
        int curVal = i * this.cols + j;

        // add neighbors to current cell based on its grid pos
        if (i + 1 < this.rows && j + 1 < this.cols) {
          this.allCells.get(curVal).addCell(this.allCells.get(curVal + 1 + this.cols));
        }
        if (i + 1 < this.rows) {
          this.allCells.get(curVal).addCell(this.allCells.get(curVal + this.cols));
          if (j - 1 >= 0) {
            this.allCells.get(curVal).addCell(this.allCells.get(curVal - 1 + this.cols));
          }

        }
        if (j + 1 < this.cols) {
          this.allCells.get(curVal).addCell(this.allCells.get(curVal + 1));
          if (i - 1 >= 0) {
            this.allCells.get(curVal).addCell(this.allCells.get(curVal + 1 - this.cols));
          }
        }
        if (i - 1 >= 0) {
          this.allCells.get(curVal).addCell(this.allCells.get(curVal - this.cols));
        }
        if (j - 1 >= 0) {
          this.allCells.get(curVal).addCell(this.allCells.get(curVal - 1));
        }
        if (j - 1 >= 0 && i - 1 >= 0) {
          this.allCells.get(curVal).addCell(this.allCells.get(curVal - 1 - this.cols));
        }

      }
    }
  }

  // current world state
  public WorldScene makeScene() {
    // if the game is ongoing
    if (this.tickTT > 0) {
      WorldScene finalDraw = new WorldScene(1000, 1000);
      // display win message and score
      finalDraw.placeImageXY(new TextImage("You Win! Score: " + String.valueOf(this.score), 20,
          new Color(130, 10, 80)), 100, 20);
      // loop through each cell
      for (int i = this.rows - 1; i >= 0; --i) {
        for (int j = this.cols - 1; j >= 0; --j) {
          int curVal = i * this.cols + j;

          int totalAmt = this.rows * this.cols;
          if (totalAmt >= tickTT - 50 + curVal) {
            Posn spot = new Posn(
                Math.toIntExact(Math
                    .round((((totalAmt - curVal) - (tickTT - 50)) * (((this.rows - i) / 30.0) * 26))
                        + i * 26)),
                Math.toIntExact(Math.round(((j * 26) + (((totalAmt - curVal) - (tickTT - 50))
                    * (((this.rows / 2) - j) / 30.0) * 26)))));
            if (spot.x < this.rows * 26) {
              finalDraw.placeImageXY(allCells.get(curVal).draw(26, i, j, new Random()), 50 + spot.y,
                  50 + spot.x);
            }
          }
          else {
            finalDraw.placeImageXY(allCells.get(curVal).draw(26, i, j), 50 + (j * 26),
                50 + (i * 26));
          }
        }
      }

      return finalDraw;
    }

    // if displayScores is true
    if (this.displayScores) {
      WorldScene finalDraw = new WorldScene(1000, 1000);
      // display scores
      finalDraw.placeImageXY(
          new TextImage("Scores Below: (press escape return to menu)", 20, Color.black), 400, 30);
      OverlayImage easy = new OverlayImage(new EmptyImage(), new EmptyImage());
      for (int i = 0; i < this.allScores.size() && i < 10; ++i) {
        if (i == 0) {
          easy = new OverlayImage(
              new TextImage(String.valueOf(this.allScores.get(i)), 30, Color.black),
              new RectangleImage(200, 60, OutlineMode.SOLID, new Color(227, 169, 14)));
        }
        else {
          if (i == 1) {
            easy = new OverlayImage(
                new TextImage(String.valueOf(this.allScores.get(i)), 30, Color.black),
                new RectangleImage(200, 60, OutlineMode.SOLID, new Color(201, 199, 192)));
          }
          else {
            if (i == 2) {
              easy = new OverlayImage(
                  new TextImage(String.valueOf(this.allScores.get(i)), 30, Color.black),
                  new RectangleImage(200, 60, OutlineMode.SOLID, new Color(205, 127, 50)));
            }
            else {
              easy = new OverlayImage(
                  new TextImage(String.valueOf(this.allScores.get(i)), 30, Color.black),
                  new EmptyImage());
            }

          }
        }
        finalDraw.placeImageXY(easy, 400, 100 + 50 * i);

      }
      return finalDraw;
    }
    /*
     * if (this.custom) { WorldScene finalDraw = new WorldScene(1000, 1000);
     * OverlayImage instr = new OverlayImage( new
     * TextImage("Click Escape Exit, Click Boxes to Enter numbers", 25,
     * Color.black), new RectangleImage(800, 30, OutlineMode.SOLID, Color.GREEN));
     * OverlayImage instr1 = new OverlayImage( new
     * TextImage("Only numbers allow amount of mines roughly 20% of rowsXcols", 25,
     * Color.black), new RectangleImage(800, 30, OutlineMode.SOLID, Color.GREEN));
     * OverlayImage medium = new OverlayImage(new TextImage("Rows:", 30,
     * Color.black), new RectangleImage(200, 60, OutlineMode.SOLID, Color.YELLOW));
     * OverlayImage hard = new OverlayImage(new TextImage("Cols:", 30, Color.black),
     * new RectangleImage(200, 60, OutlineMode.SOLID, Color.RED)); OverlayImage
     * fullCustom = new OverlayImage(new TextImage("Seed:", 30, Color.black), new
     * RectangleImage(200, 60, OutlineMode.SOLID, new Color(192, 192, 192)));
     * finalDraw.placeImageXY(instr, 500, 50); finalDraw.placeImageXY(instr1, 500,
     * 80);
     * 
     * finalDraw.placeImageXY(medium, 300, 200); finalDraw.placeImageXY(hard, 300,
     * 300); finalDraw.placeImageXY(fullCustom, 300, 400); return finalDraw;
     * 
     * }
     */

    // menu interface
    if (this.mainMenu) {
      WorldScene finalDraw = new WorldScene(1000, 1000);
      OverlayImage easy = new OverlayImage(new TextImage("Easy Mode", 30, Color.black),
          new RectangleImage(200, 60, OutlineMode.SOLID, Color.GREEN));
      OverlayImage medium = new OverlayImage(new TextImage("Medium Mode", 30, Color.black),
          new RectangleImage(200, 60, OutlineMode.SOLID, Color.YELLOW));
      OverlayImage hard = new OverlayImage(new TextImage("Hard Mode", 30, Color.black),
          new RectangleImage(200, 60, OutlineMode.SOLID, Color.RED));

      OverlayImage scores = new OverlayImage(new TextImage("Scores", 30, Color.black),
          new RectangleImage(200, 60, OutlineMode.SOLID, new Color(192, 192, 192)));
      finalDraw.placeImageXY(easy, 300, 100);
      finalDraw.placeImageXY(medium, 300, 200);
      finalDraw.placeImageXY(hard, 300, 300);
      finalDraw.placeImageXY(scores, 300, 500);

      return finalDraw;

    }

    /*
     * AboveImage col = new AboveImage(new EmptyImage()); for (int j = 0; j <
     * this.rows; ++j) { BesideImage row = new BesideImage(new EmptyImage());
     * 
     * for (int i = 0; i < this.cols; ++i) { int curVal = i * this.cols + j; row =
     * new BesideImage(row, allCells.get(curVal).draw(25, j, i)); } col = new
     * AboveImage(col, row); } WorldScene finalDraw = new WorldScene(25 * this.cols,
     * 25 * this.rows);
     * 
     * finalDraw.placeImageXY(col, 25 * this.cols / 2 + 1, 25 * this.rows / 2 + 1);
     * return finalDraw;
     */

    // constructs final scene
    WorldScene finalDraw = new WorldScene(1000, 1000);
    // converts time to string format
    String timer = String.valueOf(this.time);
    // creates timer with conditions
    if (timer.length() < 3) {
      timer += "0";
    }
    if (timer.indexOf(".") == 1) {
      timer = timer.substring(0, 3);
    }
    if (timer.length() > 4) {
      timer = timer.substring(0, timer.length() - 1);
    }

    // if game is not over, places images for time and score on scene
    if (!this.gameOver) {
      finalDraw.placeImageXY(new TextImage("Time: " + timer, 20, Color.black), 50, 30);
      finalDraw.placeImageXY(new TextImage("Score: " + String.valueOf(this.score), 20, Color.black),
          500, 30);
    }

    // iterates over the cells and places their images on the scene
    for (int i = 0; i < this.rows; ++i) {
      for (int j = 0; j < this.cols; ++j) {
        int curVal = i * this.cols + j;

        finalDraw.placeImageXY(allCells.get(curVal).draw(26, i, j), 50 + (j * 26), 50 + (i * 26));
      }
    }

    // if games over, displays final score and 'play again' message
    if (gameOver) {
      finalDraw.placeImageXY(
          new TextImage("You Loose Final Score: " + String.valueOf(this.score), 30, Color.black),
          400, this.cols * 13);
      finalDraw.placeImageXY(new TextImage(" press Enter to play again", 30, Color.black), 400,
          this.cols * 13 + 30);
    }

    return finalDraw; // returns final scene
  }

  // updates game state on each tick
  public void onTick() {
    if (!mainMenu) {
      this.waiter += 1; // increments waiter
      this.time = waiter / 50.0; // updates time
    }

    if (this.tickTT > 0) {
      this.tickTT -= 3;
      if (this.tickTT <= 0) {
        this.mainMenu = true;
        this.gameWipe(); // resets game
      }
    }

  }

  // menu options
  void menuOpt(Posn pos, String buttonName) {
    /*
     * finalDraw.placeImageXY(easy, 300, 100); finalDraw.placeImageXY(medium, 300,
     * 200); finalDraw.placeImageXY(hard, 300, 300);
     * finalDraw.placeImageXY(fullCustom, 300, 400);
     */

    // checks mouse pos and click press
    if (pos.x >= 200 && pos.x <= 400) {
      // handles easy mode
      if (pos.y >= 70 && pos.y <= 130 && buttonName.equals("LeftButton")) {
        this.gameWipe();
        this.rows = 10;
        this.cols = 8;
        ArrayList<Integer> bomborder = this.bombCreation(this.rows, this.cols, 24, new Random());
        this.cellCreation(rows, cols, bomborder);
        this.addneighbors();
        this.mainMenu = false;
      }
      // handles medium mode
      if (pos.y >= 170 && pos.y <= 230 && buttonName.equals("LeftButton")) {

        this.gameWipe();
        this.rows = 20;
        this.cols = 11;
        ArrayList<Integer> bomborder = this.bombCreation(this.rows, this.cols, 40, new Random());
        this.cellCreation(rows, cols, bomborder);
        this.addneighbors();
        this.mainMenu = false;
      }
      // handles hard mode
      if (pos.y >= 270 && pos.y <= 330 && buttonName.equals("LeftButton")) {
        this.gameWipe();
        this.rows = 30;
        this.cols = 16;
        ArrayList<Integer> bomborder = this.bombCreation(this.rows, this.cols, 99, new Random());
        this.cellCreation(rows, cols, bomborder);
        this.addneighbors();
        this.mainMenu = false;
      }
      // handles score display
      if (pos.y >= 470 && pos.y <= 530 && buttonName.equals("LeftButton")) {
        this.gameWipe();
        this.displayScores = true;
      }

    }
  }

  // handles mouse click events
  public void onMouseClicked(Posn pos, String buttonName) {
    if (this.mainMenu) {

      this.menuOpt(pos, buttonName);
      return;
    }
    int xrow = (pos.x - 37) / 26;

    int yrow = (pos.y - 37) / 26;

    int spot = yrow * cols + xrow;
    // determines if clicked
    if (!this.gameOver && spot >= 0 && spot <= this.allCells.size() && xrow < this.cols
        && yrow < this.rows) {
      // checks if the left button clicked on a bomb cell
      if (buttonName.equals("LeftButton") && this.allCells.get(spot).represent == -1) {
        this.fullBomb();
        this.gameOver = true;
        return;
      }
      // handles right-click to flag/unflag cells
      if (buttonName.equals("RightButton") && !this.allCells.get(spot).clicked) {
        if (this.allCells.get(spot).flag) {
          this.allCells.get(spot).flag = false;
        }
        else {
          this.allCells.get(spot).flag = true;
        }
      }
      // handles left-click
      if (buttonName.equals("LeftButton") && !this.allCells.get(spot).flag) {
        // reveals cell, initiates flood
        if (!this.allCells.get(spot).clicked) {
          if (time < 1) {
            this.score += 8.5;
          }
          else {
            this.score += Math.round(10 / this.time);
          }
        }
        // marks cell as clicked
        this.allCells.get(spot).clicked = true;
        // initiate flood if cell represents empty
        if (this.allCells.get(spot).represent == 0) {
          this.allCells.get(spot).flood();
        }
        // checks for win condition and initiates game reset
        if (this.winCheck() && this.tickTT == 0) {
          this.allScores.add(score);
          this.tickTT = this.rows * this.cols + 50;
        }

      }

    }
  }

  public void fullBomb() {
    // reveals all bomb cells
    for (Cell cellSearch : this.allCells) {
      if (cellSearch.represent == -1) {
        cellSearch.clicked = true;
        cellSearch.flag = false;
      }
    }
  }
}

//represents a single cell 
class Cell {
  boolean flag; // indicates if cell is flagged
  int represent; // will be -1 if a bomb
  boolean clicked; // indicates if cell has been clicked
  ArrayList<Cell> neighbor = new ArrayList<Cell>(); // stores neighboring cells

  // constructors
  Cell(int represent) {
    this.represent = represent;
  }

  Cell(int represent, boolean clicked) {
    this.represent = represent;
    this.clicked = clicked;
  }

  Cell(boolean flag, int represent, boolean clicked) {
    this.flag = flag;
    this.represent = represent;
    this.clicked = clicked;

  }

  // adds neighboring cell
  void addCell(Cell add) {
    if (this.represent == -1 && add.represent != -1) {
      add.represent += 1;
    }
    this.neighbor.add(add);
    // add.addCellBack(this);
  }

  // adds neighboring cell (reverse direction)
  void addCellBack(Cell add) {
    if (add.represent == -1 && this.represent != -1) {
      this.represent += 1;
    }
    this.neighbor.add(add);
  }

  // performs flood-fill
  void flood() {
    for (int i = 0; i < this.neighbor.size(); ++i) {
      if (this.neighbor.get(i).represent == 0 && !this.neighbor.get(i).clicked
          && !this.neighbor.get(i).flag) {
        this.neighbor.get(i).clicked = true;
        this.neighbor.get(i).flood();

      }
      if (this.neighbor.get(i).represent > 0 && !this.neighbor.get(i).clicked
          && !this.neighbor.get(i).flag) {
        this.neighbor.get(i).clicked = true;
      }
    }
  }

  // draws cell
  OverlayImage draw(int size, int row, int col, Random rand) {
    Color text = new Color(255, 255, 255);
    // color scheme
    if (this.represent == 1) {
      text = new Color(25, 23, 150);
    }
    if (this.represent == 2) {
      text = new Color(13, 122, 116);
    }
    if (this.represent == 3) {
      text = new Color(128, 98, 10);
    }
    if (this.represent == 4) {
      text = new Color(156, 35, 14);
    }
    if (this.represent == 5) {
      text = new Color(77, 5, 9);
    }
    if (this.represent == 6) {
      text = new Color(39, 11, 69);
    }
    if (this.represent == 7) {
      text = new Color(138, 4, 91);
    }
    if (this.represent == 8) {
      text = new Color(0, 0, 0);
    }
    Color shade = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    if (this.clicked && this.represent == -1) {
      return new OverlayImage(new CircleImage(size / 4, OutlineMode.SOLID, Color.red),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }
    // returns flag when flagged
    if (this.flag) {
      return new OverlayImage(
          new OverlayOffsetImage(
              new RectangleImage(10, 4, OutlineMode.SOLID, new Color(214, 46, 10)), 5, -10,
              new OverlayOffsetImage(new RectangleImage(3, 23, OutlineMode.SOLID, Color.RED), 5, -6,
                  new TriangleImage(new Posn(0, 0), new Posn(0, 15), new Posn(12, 8),
                      OutlineMode.SOLID, Color.RED))),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }
    // returns number of neighboring bombs
    if (this.clicked && this.represent > 0) {
      return new OverlayImage(
          new TextImage(String.valueOf(this.represent), 20, FontStyle.BOLD, text),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }
    // returns empty image if cell is clicked
    if (this.clicked) {
      return new OverlayImage(new EmptyImage(),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }

    return new OverlayImage(new EmptyImage(),
        new RectangleImage(size, size, OutlineMode.SOLID, shade));
  }

  // draws cell
  OverlayImage draw(int size, int row, int col) {
    boolean dark;
    Color shade;
    Color text = new Color(255, 255, 255);
    // color scheme
    if (this.represent == 1) {
      text = new Color(25, 23, 150);
    }
    if (this.represent == 2) {
      text = new Color(13, 122, 116);
    }
    if (this.represent == 3) {
      text = new Color(128, 98, 10);
    }
    if (this.represent == 4) {
      text = new Color(156, 35, 14);
    }
    if (this.represent == 5) {
      text = new Color(77, 5, 9);
    }
    if (this.represent == 6) {
      text = new Color(39, 11, 69);
    }
    if (this.represent == 7) {
      text = new Color(138, 4, 91);
    }
    if (this.represent == 8) {
      text = new Color(0, 0, 0);
    }

    if (row % 2 == 0) {
      if (col % 2 == 0) {
        dark = false;
      }
      else {
        dark = true;
      }
    }
    else {
      if (col % 2 == 0) {
        dark = true;
      }
      else {
        dark = false;
      }
    }
    // sets background color based on if cell is dark or light
    if (dark) {
      shade = new Color(6, 102, 8);
    }
    else {
      shade = new Color(10, 209, 14);
    }

    // returns bomb if cell is clicked and contains a bomb
    if (this.clicked && this.represent == -1) {
      return new OverlayImage(new CircleImage(size / 4, OutlineMode.SOLID, Color.red),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }
    // returns flag when flagged
    if (this.flag) {
      return new OverlayImage(
          new OverlayOffsetImage(
              new RectangleImage(10, 4, OutlineMode.SOLID, new Color(214, 46, 10)), 5, -10,
              new OverlayOffsetImage(new RectangleImage(3, 23, OutlineMode.SOLID, Color.RED), 5, -6,
                  new TriangleImage(new Posn(0, 0), new Posn(0, 15), new Posn(12, 8),
                      OutlineMode.SOLID, Color.RED))),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }
    // returns number of neighboring bombs
    if (this.clicked && this.represent > 0) {
      return new OverlayImage(
          new TextImage(String.valueOf(this.represent), 20, FontStyle.BOLD, text),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }
    // returns empty image if cell is clicked
    if (this.clicked) {
      return new OverlayImage(new EmptyImage(),
          new RectangleImage(size, size, OutlineMode.SOLID, shade));
    }
    if (dark) {
      shade = new Color(196, 207, 196);
    }
    else {
      shade = new Color(92, 92, 92);
    }
    return new OverlayImage(new EmptyImage(),
        new RectangleImage(size, size, OutlineMode.SOLID, shade));
  }

}

//examples class
class Examples {
  World unStart;
  World world1;
  World world2;
  World world3;
  World world4;
  World world11;
  World simp;
  MindWorld world12;
  MindWorld world13;
  MindWorld world14;
  MindWorld worldShow;
  Cell cell1;
  Cell cell2;
  Cell cell3;
  Cell cell4;
  Cell cell5;
  Cell cell6;
  Cell cell7;
  Cell cell8;
  Cell cell9;
  Cell cellConstruct1;
  Cell cellConstruct2;
  // initializes example worlds

  void Init() {
    this.unStart = new MindWorld();
    worldShow = new MindWorld(30, 16, new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, -1, -1,
        -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, -1, 0, 0, 0, -1, 0, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1,
        0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1,
        -1, -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1,
        -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1, 0, 0,
        0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, 0,
        -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1,
        -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0,
        0, 0, 0, -1, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1,
        0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0,
        0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, -1,
        0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0,
        0, -1, 0, -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1,
        -1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1,
        0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1,
        0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1,
        -1, -1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, 0,
        0, 0, -1, 0, 0, 0, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, 0,
        0, -1, 0, 0, -1, -1, -1, 0, 0, 0, -1, 0, 0, 0, -1, -1, -1, -1)), true);
    world11 = new MindWorld(3, 3, new ArrayList<>(Arrays.asList(-1, -1, -1, -1, 0, -1, -1, -1, -1)),
        true);

    world12 = new MindWorld(3, 3, 9, new Random(5));
    this.simp = new MindWorld(30, 30, 0, new Random(5));

    world12 = new MindWorld(3, 3, 0, new Random(5));
    world13 = new MindWorld(3, 3, 4, new Random(5));

    world1 = new MindWorld(30, 16, 99, new Random(5));
    world2 = new MindWorld(4, 4, 5, new Random(5));
    world3 = new MindWorld(5, 4, 5, new Random(5));
    world4 = new MindWorld(4, 5, 5, new Random(5));
    cell1 = new Cell(0);
    cell2 = new Cell(-1);
    cell3 = new Cell(-1);
    cell4 = new Cell(-1);
    cell5 = new Cell(-1);
    cell6 = new Cell(0);
    cell7 = new Cell(0);
    cell8 = new Cell(-1);
    cell9 = new Cell(1);

    cellConstruct1 = new Cell(1, true);
    cellConstruct2 = new Cell(true, 1, false);

  }

  //tests running game

  void testBigBang(Tester t) {
    Init();
    unStart.bigBang(1000, 1000, .005);
  }

  // tests MindWorld constructor

  void testConstructor(Tester t) {
    Init();
    t.checkExpect(world12.bombCreation(3, 3, 9, new Random(5)),
        new ArrayList<>(Arrays.asList(-1, -1, -1, -1, -1, -1, -1, -1, -1)));
    t.checkExpect(world12.bombCreation(3, 3, 0, new Random(5)),
        new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0)));
    t.checkExpect(world12.bombCreation(3, 3, 3, new Random(5)),
        new ArrayList<>(Arrays.asList(0, -1, 0, 0, 0, -1, 0, 0, -1)));

  }

  // tests MindWorld Addneighbors

  void testAddNeighbors(Tester t) {
    Init();
    cell1.addCell(cell2);
    t.checkExpect(cell1.neighbor, new ArrayList<Cell>(Arrays.asList(cell2)));
    t.checkExpect(cell1.represent, 0);
    cell1.addCell(cell3);
    cell1.addCell(cell4);
    t.checkExpect(cell1.neighbor, new ArrayList<Cell>(Arrays.asList(cell2, cell3, cell4)));
    t.checkExpect(cell1.represent, 0);
    cell1.addCell(cell6);
    t.checkExpect(cell1.neighbor, new ArrayList<Cell>(Arrays.asList(cell2, cell3, cell4, cell6)));
    t.checkExpect(cell1.represent, 0);
    cell2.addCell(cell1);
    t.checkExpect(cell1.represent, 1);
    Init();
    cell2.addCell(cell1);
    cell2.addCell(cell1);
    cell2.addCell(cell1);
    cell2.addCell(cell1);
    t.checkExpect(cell2.neighbor, new ArrayList<Cell>(Arrays.asList(cell1, cell1, cell1, cell1)));
    t.checkExpect(cell2.represent, -1);
    t.checkExpect(cell1.represent, 4);

  }
  // tests MindWorld makeScene

  void testMakeScene(Tester t) {
    Init();
    WorldScene scene1 = world11.makeScene();
    t.checkExpect(scene1.width, 1000);
    t.checkExpect(scene1.height, 1000);

    WorldScene scene2 = world1.makeScene();
    t.checkExpect(scene2.width, 1000);
    t.checkExpect(scene2.height, 1000);
  }

  void testHelpers(Tester t) { // tests all helpers
    Init();
    int passed = 0;
    boolean test1 = world12.winCheck();
    boolean test2 = world12.winCheck();
    Init();
    world12.gameOver = true;
    world12.onKeyEvent("enter");
    if (world12.mainMenu) {
      passed += 1;
    }
    t.checkExpect(passed, 1);

    if (!test1) {
      passed += 2;
    }
    t.checkExpect(passed, 3);
    Init();
    world12.gameWipe();
    world13.gameWipe();
    t.checkExpect(world12, world13);
    Init();
    world12.onTick();
    t.checkExpect(this.world12.waiter, 1);
    world13.onTick();
    world13.onTick();
    world13.onTick();
    world13.onTick();
    world13.onTick();
    t.checkExpect(this.world13.waiter, 5);
    for (int i = 0; i < 50; ++i) {
      world13.onTick();
      t.checkExpect(this.world13.waiter, 6 + i);

    }
    t.checkExpect(world13.time, 1.1);
    if (!worldShow.winCheck()) {
      passed++;
    }
    t.checkExpect(passed, 4);

  }

  // tests draw
  void testDraw(Tester t) {
    Init();
    t.checkExpect(new Cell(-1).draw(25, 0, 0), new OverlayImage(new EmptyImage(),
        new RectangleImage(25, 25, OutlineMode.SOLID, new Color(92, 92, 92))));

    t.checkExpect(new Cell(1, true).draw(25, 1, 0),
        new OverlayImage(
            new TextImage(String.valueOf(1), 20, FontStyle.BOLD, new Color(25, 23, 150)),
            new RectangleImage(25, 25, OutlineMode.SOLID, new Color(6, 102, 8))));

  }
  /*
   * // tests for drawing world for human eye boolean testDrawWords(Tester t) {
   * Init(); WorldCanvas c1 = new WorldCanvas(418, 782); return
   * c1.drawScene(worldShow.makeScene()) && c1.show(); }
   */

}
