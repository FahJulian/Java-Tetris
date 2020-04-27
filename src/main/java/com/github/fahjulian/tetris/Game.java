package com.github.fahjulian.tetris;

import com.github.fahjulian.tetris.ui.Window;
import com.github.fahjulian.tetris.util.Direction;
import com.github.fahjulian.tetris.util.Database;
import com.github.fahjulian.tetris.util.GameState;

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
  public static final int HUD_GRID_WIDTH;
  public static final int HUD_GRID_HEIGHT;

  private boolean running;
  private Window window;
  private Grid grid;
  private HUD hud;
  private int highscore;
  private int score;
  private int gameoverScore;
  private int totalClearedRows;
  private GameState state;

  static 
  {
    FPS = 60;
    FRAMETIME = 1000 / FPS;
    WINDOW_TITLE = "Tetris";
    PADDING = 50;
    GRID_WIDTH = Grid.BLOCKSIZE * Grid.COLS;
    GRID_HEIGHT = Grid.BLOCKSIZE * Grid.ROWS;
    HUD_GRID_WIDTH = Grid.BLOCKSIZE * 4;
    HUD_GRID_HEIGHT = Grid.BLOCKSIZE * 4;
    CONTENT_WIDTH = GRID_WIDTH + HUD_GRID_WIDTH + 3 * PADDING + 2;
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
    state = GameState.GAMEOVER;
    restart();
  }

  /**
   * Update the score for the amount of cleared rows using the official tetris score system.
   * @param cleared_rows The amount of rows that have been cleared at one time
   */
  public void updateScore(int clearedRows)
  {
    int points = 0;
    switch (clearedRows)
    {
      case 1: points = 40; break;
      case 2: points = 100; break;
      case 3: points = 300; break;
      case 4: points = 1200; break;
      default: return;
    }

    points *= totalClearedRows / 10 + 1;
    totalClearedRows += clearedRows;
    score += points;

    if (score > highscore) 
    {
      highscore = score;
      Database.saveHighscore(highscore);
    }
  }

  public int getScore()
  { 
    return score;
  }

  public int getHighscore()
  {
    return highscore;
  }

  public GameState getState()
  {
    return state;
  }

  public int scoreOnGameover()
  {
    return this.gameoverScore;
  }

  private void init() 
  {
    Database.init();
    score = 0;
    highscore = Database.getHighscore();
    totalClearedRows = 0;
    state = GameState.NEW_GAME;

    window = new Window(WINDOW_TITLE, CONTENT_WIDTH, CONTENT_HEIGHT);
    grid = new Grid(this, GRID_WIDTH, GRID_HEIGHT, PADDING);
    hud = new HUD(this, HUD_GRID_WIDTH, HUD_GRID_HEIGHT, PADDING);

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
          case KeyEvent.VK_ENTER:
            state = GameState.INGAME;
            break;
          case KeyEvent.VK_ESCAPE:
            if (state == GameState.INGAME)
              state = GameState.PAUSED;
            else if (state == GameState.PAUSED)
              state = GameState.INGAME;
            break;
        }
      }

      @Deprecated @Override 
      public void keyTyped(KeyEvent e) {}
    });

    window.setVisible(true);
    hud.setTile(grid.getNextTile());
  }

  private void start() 
  {
    running = true;
    this.state = GameState.NEW_GAME;
    gameloop();
  }

  private void gameloop() 
  {
    while (running) 
    {
      long start = System.nanoTime();

      if (state == GameState.INGAME)
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

  private void restart()
  {
    try 
    {
      Thread.sleep(500);
    } 
    catch (InterruptedException e) 
    {
      e.printStackTrace();
    }
    reset();
  }

  private void reset()
  {
    grid.reset();
    state = GameState.GAMEOVER;
    this.gameoverScore = score;
    this.score = 0;
    this.totalClearedRows = 0;
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
