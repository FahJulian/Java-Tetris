package com.github.fahjulian.tetris;

import com.github.fahjulian.tetris.util.Clock;
import com.github.fahjulian.tetris.util.Direction;
import com.github.fahjulian.tetris.gameobject.Tile;
import com.github.fahjulian.tetris.gameobject.Block;

import java.util.ArrayList;

import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class Grid extends JLabel 
{
  private static final long serialVersionUID = 7393460942206999569L;

  public static final int BLOCKSIZE;
  public static final int ROWS;
  public static final int COLS;
  private static final Point STARTING_POS;
  /** The amount of rows the current tile moves down normally */
  private static final int NORMAL_SPEED;
  /** The amount of rows the current tile moves down when down arrow button is pressed */
  private static final int ACCELERATED_SPEED;

  private final int width;
  private final int height;
  private final int padding;
  private final Block[][] blocks;
  private final Game game;
  private final CollisionManager collisionManager;

  private boolean accelerated;
  private Clock movingClock;
  private int tileRotation;
  private Point tilePos;
  private Tile currentTile;
  private Tile nextTile;
  private Direction horizontalDir;

  static 
  {
    BLOCKSIZE = 25;
    ROWS = 20;
    COLS = 12;
    STARTING_POS = new Point((COLS / 2 - 2) * BLOCKSIZE + Game.PADDING, Game.PADDING);
    NORMAL_SPEED = 1;
    ACCELERATED_SPEED = 15;
  }

  /**
   * Constructs a new Tetris Grid
   * @param width Width of the grid
   * @param height Height of the grid
   * @param padding Spacing to the border of the window in all 4 directions
   */
  public Grid(Game game, int width, int height, int padding) 
  {
    this.game = game;
    this.width = width;
    this.height = height;
    this.padding = padding;
    this.blocks = new Block[ROWS][COLS];
    this.currentTile = Tile.randomTile();
    this.nextTile = Tile.randomTile();
    this.tileRotation = 0;
    this.movingClock = new Clock();
    this.accelerated = false;
    this.tilePos = (Point) STARTING_POS.clone();
    this.collisionManager = new CollisionManager(this);
  }

  public void render()
  {
    repaint();
  }

  public void update()
  {
    // If after this update the game is lost, let the Game instance know
    if (collisionManager.hasPlayerLost())
    {
      game.gameover();
      return;
    }

    if (currentTile == null) return;

    // Move down the current tile if enough time has passed from the last time
    // it has been moved down.
    int speed = accelerated ? ACCELERATED_SPEED : NORMAL_SPEED;
    if (movingClock.peekDuration() > (1000 / speed))
    {
      if (collisionManager.canTileMoveDown()) tilePos.y += BLOCKSIZE;
      else spawnNewTile();
      movingClock.reset();
    }


    // Move Tile horizontally if the right or left arrow button has been pressed.
    if (horizontalDir != null)
    {
      if (collisionManager.canTileMoveHorizontically())
      {
        tilePos.x += (horizontalDir == Direction.RIGHT) ? BLOCKSIZE : -BLOCKSIZE;
        horizontalDir = null;
      } 
      else if (collisionManager.hasTileHitBlock())
        spawnNewTile();
    }

    int clearedRows = 0;
    for (int row = 0; row < ROWS; row++)
      if (isRowComplete(row)) 
      {
        eraseRow(row);
        clearedRows++;
      }
    game.updateScore(clearedRows);
  }
  
  @Override
  protected void paintComponent(Graphics g) 
  {
    super.paintComponent(g);

    // Background
    g.setColor(Color.WHITE);
    g.fillRect(padding, padding, width, height);
    
    // Grid lines
    g.setColor(Color.LIGHT_GRAY);
    for (int row = 0; row < ROWS + 1; row++)
      g.fillRect(0 + padding, row * BLOCKSIZE + padding, width, 1);
    for (int col = 0; col < COLS + 1; col++)
      g.fillRect(col * BLOCKSIZE + padding, 0 + padding, 1, height);
    
    // Current tile
    if (currentTile != null)
    for (Block block: getCurrentTileBlocks())
      block.render(g);
      
    // Static blocks
    for (Block block: getBlocks())
      block.render(g);
  }

  private void spawnNewTile()
  {
    for (Block block: getCurrentTileBlocks())
      blocks[(block.getY() - padding) / BLOCKSIZE][(block.getX() - padding) / BLOCKSIZE] = block;

    currentTile = nextTile;
    nextTile = Tile.randomTile();
    tilePos = (Point) STARTING_POS.clone();
    tileRotation = 0;
    movingClock.reset();
  }

  public void setHorizonalDir(Direction dir) 
  {
    if (dir == Direction.RIGHT || dir == Direction.LEFT) horizontalDir = dir;
  }

  public void setAccelerated(boolean accelerated) 
  {
    this.accelerated = accelerated;
  }

  public Direction getHorizontalDir()
  {
    return horizontalDir;
  }

  public Tile getCurrentTile()
  {
    return currentTile;
  }
  
  public void rotateTile()
  {
    if (collisionManager.canTileRotate())
      tileRotation = tileRotation < 3 ? tileRotation + 1 : 0;
  }

  public ArrayList<Block> getCurrentTileBlocks()
  {
    return currentTile.toBlockArray(tileRotation, tilePos);
  }

  /**
   * Get a {@code Rectangle} of the grid in the {@code Window}
   * @return A {@code Rectangle} of the grid's {@code JPanel}
   */
  @Override
  public Rectangle getBounds()
  {
    return new Rectangle(padding, padding, width, height);
  }

  /**
   * Get all the current blocks in the grid
   * @return {@code ArrayList} of all the current static blocks
   */
  public ArrayList<Block> getBlocks() 
  {
    ArrayList<Block> blocks = new ArrayList<Block>();
    for (Block[] row: this.blocks)
      for (Block block: row)
        if (block != null) blocks.add(block);
    return blocks;
  }

  public Tile getNextTile()
  {
    return nextTile;
  }

  public int getTileRotation()
  {
    return tileRotation;
  }

  public Point getTilePos()
  {
    return tilePos;
  }

  private boolean isRowComplete(int row)
  {
    for (Block block: blocks[row])
      if (block == null)
        return false;
    return true;
  }

  private void eraseRow(int row)
  {
    // blocks[row] = new Block[COLS];

    // Drop all other rows down
    while (row > 0)
    {
      blocks[row] = blocks[row - 1];
      row--;

      for (Block block: blocks[row])
        if (block != null) block.setY(block.getY() + BLOCKSIZE);
    }
    blocks[0] = new Block[COLS];
  }
}


class CollisionManager
{
  public static final int HIT_WALL;
  public static final int HIT_BLOCK;

  private Grid grid;
  private int failReason; 

  static 
  {
    HIT_WALL = 1;
    HIT_BLOCK = 2;
  }

  /**
   * Constructs a new Collision manager for a tetris grid
   */
  public CollisionManager(Grid grid)
  {
    this.grid = grid; 
  }

  /**
   * Check if the grid's current tile can move in it's current horizontal direction
   * @return Whether or not it can move
   */
  public boolean canTileMoveHorizontically()
  { 
    if (grid.getHorizontalDir() == null || grid.getCurrentTile() == null) return false;

    int velX = (grid.getHorizontalDir() == Direction.RIGHT) ? Grid.BLOCKSIZE : -Grid.BLOCKSIZE;
    for (Block movingBlock: grid.getCurrentTileBlocks())
    {
      // Check if Tile would collide with any of the grids blocks
      Rectangle movedBounds = movingBlock.getBounds();
      movedBounds.translate(velX, 0);
      for (Block staticBlock: grid.getBlocks())
        if (movedBounds.intersects(staticBlock.getBounds())) 
        {
          failReason = HIT_BLOCK;
          return false;
          }

      // Check if Tile would go outside of the grids bounds
      if ((grid.getHorizontalDir() == Direction.RIGHT && movedBounds.getMaxX() > grid.getBounds().getMaxX()) ||
        (grid.getHorizontalDir()  == Direction.LEFT && movedBounds.getMinX() < grid.getBounds().getMinX()))
      {
        failReason = HIT_WALL;
        return false;
      }
    }

    return true;
  }

  /**
   * Check if the rotated version of the {@code #grid}'s current tile would exceed grid bounds or collide with another block
   * @return Whether or not it is allowed for the current tile to rotate
   */
  public boolean canTileRotate()
  {
    if (grid.getCurrentTile() == null) return false;

    int rotation = grid.getTileRotation() < 3 ? grid.getTileRotation() + 1 : 0;
    ArrayList<Block> rotatedBlocks = grid.getCurrentTile().toBlockArray(rotation, grid.getTilePos());

    for (Block rotatedBlock: rotatedBlocks)
    {
      if (rotatedBlock.getBounds().getMaxX() > grid.getBounds().getMaxX() || 
          rotatedBlock.getBounds().getMinX() < grid.getBounds().getMinX() ||
          rotatedBlock.getBounds().getMaxY() > grid.getBounds().getMaxY())
            return false;
      for (Block staticBlock: grid.getBlocks())
        if (rotatedBlock.getPos().equals(staticBlock.getPos()))
          return false;
    }

    return true;
  }

  /**
   * Check if the game is lost by checking for blocks above the grid height
   * @return Whether or not the game is lost
   */
  public boolean hasPlayerLost()
  {
    for (Block staticBlock: grid.getBlocks())
      if (staticBlock.getBounds().getMinY() <= grid.getBounds().getMinY())
        return true;
      
    return false;
  }

  /**
   * Check if the last time the tile has failed to move horizontically it was because it would have hit another block
   * @return Whether or not the last fail reason was another block
   */
  public boolean hasTileHitBlock()
  {
    return failReason == HIT_BLOCK;
  }

  /**
   * Check if the grid's current tile can move down.
   * @return Whether or not the tile can move down
   */
  public boolean canTileMoveDown()
  {
    for (Block movingBlock: grid.getCurrentTileBlocks())
    {
      Rectangle movedBounds = movingBlock.getBounds();
      movedBounds.translate(0, Grid.BLOCKSIZE);
      for (Block staticBlock: grid.getBlocks())
        if (movedBounds.intersects(staticBlock.getBounds()))
          return false;
      
      if (movedBounds.getMaxY() > grid.getBounds().getMaxY())
        return false;
    }

    return true;
  }
}
