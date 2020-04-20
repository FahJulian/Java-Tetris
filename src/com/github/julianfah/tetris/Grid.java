package com.github.julianfah.tetris;

import java.util.ArrayList;

import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

public class Grid extends JLabel 
{
  private static final long serialVersionUID = 7393460942206999569L;

  public static final int BLOCKSIZE;
  public static final int ROWS;
  public static final int COLS;

  private final int width;
  private final int height;
  private final int padding;
  private ArrayList<Block> currentTile;

  static 
  {
    BLOCKSIZE = 25;
    ROWS = 20;
    COLS = 15;
  }

  public Grid(int width, int height, int padding) 
  {
    this.width = width;
    this.height = height;
    this.padding = padding;
    this.currentTile = tileToBlockArray(new Point(padding, padding), Tile.CUBE);
  }

  @Override
  protected void paintComponent(Graphics g) 
  {
    // Background
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);

    // Grid lines
    g.setColor(Color.LIGHT_GRAY);
    for (int row = 0; row < ROWS + 1; row++)
      g.fillRect(0 + padding, row * BLOCKSIZE + padding, width - 2 * padding, 1);
    for (int col = 0; col < COLS + 1; col++)
      g.fillRect(col * BLOCKSIZE + padding, 0 + padding, 1, height - 2 * padding);

    // Current tile
    for (Block block: currentTile)
      block.render(g);
  }

  /**
   * Construct a {@code Block} ArrayList representating a Tetris tile from a pattern
   * in the given file
   * @param pos Coordinates of the top-left position of the Tile
   * @param Tile {@code Tile} Instance to create the block array off of
   */
  private static ArrayList<Block> tileToBlockArray(Point pos, Tile tile) 
  {
    ArrayList<Block> blocks = new ArrayList<Block>(5);
    for (int row = 0; row < 4; row++)
      for (int col = 0; col < 4; col++) 
        if (tile.shape[row][col]) 
          blocks.add(new Block( new Point(pos.x + col * BLOCKSIZE, pos.y + row * BLOCKSIZE), BLOCKSIZE, tile.color ));

    return blocks;
  }
}

class Block 
{
  private Point pos;
  private final int size;
  private final Color color;

  /**
   * Construct a new Tetris Block (A single block, not a whole tile)
   * @param pos Coordinates of the top-left corner
   * @param size Size of the block
   * @param color Color of the block
   */
  public Block(Point pos, int size, Color color)
  {
    this.pos = pos;
    this.size = size;
    this.color = color;
  }

  public void render(Graphics g) 
  {
    // Draw outline in darker color
    g.setColor(color.darker());
    g.fillRect(pos.x, pos.y, size, size);
    // Draw whole in brigther color
    g.setColor(color.brighter());
    g.fillRect(pos.x + 3, pos.y + 3, size - 6, size - 6);
  }

  public void setPos(Point pos) 
  {
    this.pos = pos;
  }

  public void setX(int x)
  {
    this.pos.x = x;
  }

  public void setY(int y) 
  {
    this.pos.y = y;
  }
}

enum Tile 
{
  CUBE(Color.YELLOW, new Boolean[][] { 
        { true,  true,  false, false }, 
        { true,  true,  false, false }, 
        { false, false, false, false },
        { false, false, false, false } 
      }),
  T(Color.MAGENTA, new Boolean[][] { 
        { true,  true,  true,  false }, 
        { false, true,  false, false }, 
        { false, false, false, false },
        { false, false, false, false }}),
  I(Color.CYAN, new Boolean[][] { 
        { false, true,  false, false }, 
        { false, true,  false, false }, 
        { false, true,  false, false },
        { false, true,  false, false } 
      }),
  J(Color.BLUE, new Boolean[][] { 
        { false, false, true,  false }, 
        { false, false, true,  false }, 
        { false, false, true,  false },
        { false, true,  true,  false }
      }),
  L(Color.ORANGE, new Boolean[][] { 
        { false, true,  false, false }, 
        { false, true,  false, false }, 
        { false, true,  false, false },
        { false, true,  true,  false } 
      }),
  S(Color.GREEN, new Boolean[][] { 
        { false, true,  true,  false }, 
        { true,  true,  false, false }, 
        { false, false, false, false },
        { false, false, false, false }
      }),
  Z(Color.RED, new Boolean[][] {
        { true,  true,  false, false }, 
        { false, true,  true, false },
        { false, false, false, false }, 
        { false, false, false, false } 
      });

  public final Color color;
  public final Boolean[][] shape;

  private Tile(Color color, Boolean[][] shape) 
  {
    this.shape = shape;
    this.color = color;
  }
}
