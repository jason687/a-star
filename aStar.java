import java.util.*;
import java.io.*;

public class aStar {
  private ArrayList<cell> open;
  private ArrayList<cell> closed;
  private cell[][] maze;

  private cell end;
  private cell start;

  public aStar () {
    open = new ArrayList<cell>();
    closed = new ArrayList<cell>();
  }

  public void scanText (String mazeFile) throws FileNotFoundException {
    File text = new File(mazeFile);
    Scanner scanLn;
    scanLn = new Scanner(text);

    String line = scanLn.nextLine();
    int countCol = line.length();
    int countRow = 1;
    while (scanLn.hasNextLine()) {
      countRow += 1;
      scanLn.nextLine();
    }

    maze = new cell[countRow][countCol];
    for (int i = 0; i < countRow; i++) {
      for (int j = 0; j < countCol; j++) {
        maze[i][j] = new cell(i, j);
      }
    }

    Scanner scan2 = new Scanner(text);
    int row = 0;
    while (scan2.hasNextLine()) {
      line = scan2.nextLine();
      for (int i = 0; i < countCol; i++) {
        maze[row][i].setType(line.charAt(i));
      }
      row += 1;
    }
  }

  public void algo () {
    boolean startFound = false;
    boolean endFound = false;
    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[0].length; j++) {
        if (maze[i][j].getType() == 'S') {
          maze[i][j].setParent(null);
          maze[i][j].setG(0);
          open.add(maze[i][j]);
          start = maze[i][j];
          startFound = true;
        }
        if (maze[i][j].getType() == 'E') {
          end = maze[i][j];
          endFound = true;
        }
        if (startFound && endFound) {
          i = maze.length;
          j = maze[0].length;
        }
      }
    }
    if (startFound && endFound) {
      open.get(0).setH(hCostCalc(open.get(0)));
      open.get(0).setF(open.get(0).getH() + open.get(0).getG());
    }
    boolean contLoop = true;
    while (contLoop) {
      contLoop = loop();
    }
    cell trail = end;
    while (trail.getG() != 0) {
      if (trail.getType() == ' ') {
        trail.setType('*');
      }
      trail = trail.getParent();
      //printMaze();
      // System.out.print(Text.CLEAR_SCREEN);
      Text.wait(100);
      System.out.println(toStringColor());
    }

  }

  public boolean loop () {
    ArrayList<cell> lowF = new ArrayList<cell>();
    lowF.add(open.get(0));
    for (int i = 1; i < open.size(); i++) {
      if (open.get(i).getF() < lowF.get(0).getF()) {
        lowF = new ArrayList<cell>();
        lowF.add(open.get(i));
      }
      if (open.get(i).getF() == lowF.get(0).getF()) {
        lowF.add(open.get(i));
      }
    }
    cell lowH;
    if (lowF.size() > 1) {
      lowH = lowF.get(0);
      for (int i = 1; i < lowF.size(); i++) {
        if (lowH.getH() > lowF.get(i).getH()) {
          lowH = lowF.get(i);
        }
      }
    } else {
      lowH = lowF.get(0);
    }
    if (lowH.getType() == 'E') {
      return false;
    }
    if (lowH.getRow() - 1 > 0) {
      if (maze[lowH.getRow() - 1][lowH.getCol()].getType() != '#' && !closed.contains(maze[lowH.getRow() - 1][lowH.getCol()])) {
        open.add(maze[lowH.getRow() - 1][lowH.getCol()]);
        maze[lowH.getRow() - 1][lowH.getCol()].setup(hCostCalc(maze[lowH.getRow() - 1][lowH.getCol()]), lowH);
      }
    }
    if (lowH.getRow() + 1 < maze.length) {
      if (maze[lowH.getRow() + 1][lowH.getCol()].getType() != '#' && !closed.contains(maze[lowH.getRow() + 1][lowH.getCol()])) {
        open.add(maze[lowH.getRow() + 1][lowH.getCol()]);
        maze[lowH.getRow() + 1][lowH.getCol()].setup(hCostCalc(maze[lowH.getRow() + 1][lowH.getCol()]), lowH);
      }
    }
    if (lowH.getCol() - 1 > 0) {
      if (maze[lowH.getRow()][lowH.getCol() - 1].getType() != '#' && !closed.contains(maze[lowH.getRow()][lowH.getCol() - 1])) {
        open.add(maze[lowH.getRow()][lowH.getCol() - 1]);
        maze[lowH.getRow()][lowH.getCol() - 1].setup(hCostCalc(maze[lowH.getRow()][lowH.getCol() - 1]), lowH);
      }
    }
    if (lowH.getCol() + 1 > 0) {
      if (maze[lowH.getRow()][lowH.getCol() + 1].getType() != '#' && !closed.contains(maze[lowH.getRow()][lowH.getCol() + 1])) {
        open.add(maze[lowH.getRow()][lowH.getCol() + 1]);
        maze[lowH.getRow()][lowH.getCol() + 1].setup(hCostCalc(maze[lowH.getRow()][lowH.getCol() + 1]), lowH);
      }
    }
    open.remove(lowH);
    closed.add(lowH);
    return true;
  }

  private int hCostCalc(cell a) {
    int x;
    int y;
    x = Math.abs(a.getCol() - end.getCol());
    y = Math.abs(a.getRow() - end.getRow());
    return x + y;
  }

  public void printMaze () {
    for (int i = 0; i < maze.length; i++) {
      for (int j = 0; j < maze[0].length; j++) {
        System.out.print(maze[i][j].getType());
      }
      System.out.println();
    }
  }

  public String toStringColor () {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < maze.length; i++) {
      for (int c = 0; c < maze[i].length; c++) {
        if(maze[i][c].getType()==' ')
          builder.append(" ");
        else if(maze[i][c].getType()=='#')
          builder.append(Text.color(Text.DARK)+"#");
        else if(maze[i][c].getType()=='E')
          builder.append(Text.color(Text.GREEN)+"E");
        else if(maze[i][c].getType()=='S')
          builder.append(Text.color(Text.RED)+"S");
        else if(maze[i][c].getType()=='*')
          builder.append(Text.color(Text.CYAN)+'*');
      }
      builder.append("\n"+Text.RESET);
    }
    return builder.toString()+"\n";
  }

  public static void main (String[] args) {
    aStar a = new aStar();
    try {
      a.scanText(args[0]);
      a.algo();
    }
    catch (FileNotFoundException err) {
      System.out.println("stop being bad");
    }
  }


}