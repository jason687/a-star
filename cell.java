public class cell {
  int g;
  int h;
  int f;
  int row;
  int col;
  char type;
  cell parent;

  public cell (int X, int Y) {
    row = X;
    col = Y;
  }

  public void setup (int hCost, cell parentCell) {
    g = parentCell.getG() + 1;
    h = hCost;
    f = g + h;
    parent = parentCell;
  }

  public int getG() {
    return g;
  }

  public int getH() {
    return h;
  }

  public int getF () {
    return f;
  }

  public char getType () {
    return type;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public cell getParent() {
    return parent;
  }

  public void setType (char a) {
    type = a;
  }

  public void setG (int newG) {
    g = newG;
  }

  public void setH (int newH) {
    h = newH;
  }

  public void setF (int newF) {
    f = newF;
  }

  public void setParent (cell newParent) {
    parent = newParent;
  }

}