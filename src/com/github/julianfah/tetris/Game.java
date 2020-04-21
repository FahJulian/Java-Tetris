package com.github.julianfah.tetris;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Game implements Runnable 
{
  private static final int FPS;
  private static final int FRAMETIME;
  private static final String WINDOW_TITLE;
  private static final int CONTENT_WIDTH;
  private static final int CONTENT_HEIGHT;
  public static final int GRID_PADDING;

  private boolean running;
  private Window window;
  private Grid grid;

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
    grid = new Grid(CONTENT_WIDTH - 2 * GRID_PADDING, CONTENT_HEIGHT - 2 * GRID_PADDING, GRID_PADDING);

    window.add(grid);
    window.addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e)
      {
        switch (e.getKeyCode())
        {
          case KeyEvent.VK_DOWN:
            grid.setAccelerated(true);
            break;
        }
      }

      @Override 
      public void keyReleased(KeyEvent e) 
      {
        switch (e.getKeyCode())
        {
          case KeyEvent.VK_DOWN:
            grid.setAccelerated(false);
            break;
          case KeyEvent.VK_LEFT:
            grid.setHorizonalDir(Direction.LEFT);
            break;
          case KeyEvent.VK_RIGHT:
            grid.setHorizonalDir(Direction.RIGHT);
            break;
        }
      }

      @Deprecated @Override public void keyTyped(KeyEvent e) {}
    });
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

      update();
      render();

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

  private void update() 
  {
    grid.update();
  }

  private void render()
  {
    grid.render();
  }

  public static void main(String[] args) 
  {
    new Game();
  }
}
