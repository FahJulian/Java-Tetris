package com.github.julianfah.tetris;

public class Game implements Runnable 
{
  private static final int FPS;
  private static final int FRAMETIME;
  private static final String WINDOW_TITLE;
  private static final int CONTENT_WIDTH;
  private static final int CONTENT_HEIGHT;
  private static final int GRID_PADDING;

  private Window window;
  private Grid grid;
  private boolean running;

  static 
  {
    FPS = 60;
    FRAMETIME = 1000 / FPS;
    WINDOW_TITLE = "Tetris";
    GRID_PADDING = 50;
    CONTENT_WIDTH = Grid.BLOCKSIZE * Grid.COLS + 2 * GRID_PADDING + 1;
    CONTENT_HEIGHT = Grid.BLOCKSIZE * Grid.ROWS + 2 * GRID_PADDING + 1;
  }

  public Game() 
  {
    new Thread(this).start();
  }

  /**
   * Run the gameloop. Can be interrupted by setting {@link #running} to false.
   */
  @Override
  public void run() 
  {
    init();
    start();
  }

  private void init() 
  {
    window = new Window(WINDOW_TITLE, CONTENT_WIDTH, CONTENT_HEIGHT);
    grid = new Grid(CONTENT_WIDTH, CONTENT_HEIGHT, GRID_PADDING);

    window.add(grid);
  }

  private void start() 
  {
    running = true;
    gameloop();
  }

  private void gameloop() 
  {
    while (running) 
    {
      long start = System.nanoTime();

      // Cap FPS by extending frametime if neccesary
      int delta = (int) (System.nanoTime() - start) / (int) 1e6;
      if (delta < FRAMETIME) 
      {
        try 
        {
          Thread.sleep(FRAMETIME - delta);
        } 
        catch (InterruptedException e) 
        {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) 
  {
    new Game();
  }
}
