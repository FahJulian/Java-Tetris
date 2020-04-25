package com.github.fahjulian.tetris;

import com.github.fahjulian.tetris.ui.Window;
import com.github.fahjulian.tetris.util.Direction;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;

public class Game implements Runnable 
{
  private static final int FPS;
  private static final int FRAMETIME;
  private static final String WINDOW_TITLE;
  private static final int CONTENT_WIDTH;
  private static final int CONTENT_HEIGHT;
  public static final int PADDING;
  public static final int GRID_WIDTH;
  public static final int GRID_HEIGHT;
  public static final int HUD_WIDTH;
  public static final int HUD_HEIGHT;

  private boolean running;
  private Window window;
  private Grid grid;
  private HUD hud;

  static 
  {
    FPS = 60;
    FRAMETIME = 1000 / FPS;
    WINDOW_TITLE = "Tetris";
    PADDING = 50;
    GRID_WIDTH = Grid.BLOCKSIZE * Grid.COLS;
    GRID_HEIGHT = Grid.BLOCKSIZE * Grid.ROWS;
    HUD_WIDTH = Grid.BLOCKSIZE * 4;
    HUD_HEIGHT = Grid.BLOCKSIZE * 4;
    CONTENT_WIDTH = GRID_WIDTH + HUD_WIDTH + 3 * PADDING + 2;
    CONTENT_HEIGHT = GRID_HEIGHT + 2 * PADDING + 1;
  }

  private Game() 
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

  /**
   * Stops the game. Should be called by game members when the player looses.
   */
  public void gameover()
  {
    running = false;
  }

  private void init() 
  {
    window = new Window(WINDOW_TITLE, CONTENT_WIDTH, CONTENT_HEIGHT);
    grid = new Grid(this, GRID_WIDTH, GRID_HEIGHT, PADDING);
    hud = new HUD(HUD_WIDTH, HUD_HEIGHT, PADDING);

    window.add(grid, BorderLayout.CENTER);
    window.add(hud, BorderLayout.EAST);
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
          case KeyEvent.VK_UP:
            grid.rotateTile();
            break;
          case KeyEvent.VK_LEFT:
            grid.setHorizonalDir(Direction.LEFT);
            break;
          case KeyEvent.VK_RIGHT:
            grid.setHorizonalDir(Direction.RIGHT);
            break;
        }
      }

      @Deprecated @Override 
      public void keyTyped(KeyEvent e) {}
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
    quit();
  }

  private void update() 
  {
    grid.update();

    hud.setTile(grid.getNextTile());
  }

  private void render()
  {
    grid.render();
    hud.render();
  }

  private void quit()
  {
    System.exit(1);
  }

  public static void main(String[] args) 
  {
    new Game();
  }
}
