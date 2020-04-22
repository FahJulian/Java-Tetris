package com.github.julianfah.tetris.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Block 
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

  public int getX()
  {
    return pos.x;
  }

  public int getY()
  {
    return pos.y;
  }

  public Point getPos() 
  {
    return pos;
  }

  public Rectangle getBounds()
  {
    return new Rectangle(pos.x, pos.y, size, size);
  }
}
