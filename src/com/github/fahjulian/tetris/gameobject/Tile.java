package com.github.fahjulian.tetris.gameobject;

import com.github.fahjulian.tetris.Grid;

import java.util.ArrayList;
import java.util.Random;

import java.awt.Color;
import java.awt.Point;

public enum Tile 
{
  CUBE(Color.YELLOW, new Boolean[][][] { 
    { { false, true,  true,  false }, 
      { false, true,  true,  false }, 
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  true,  false }, 
      { false, true,  true,  false }, 
      { false, false, false, false },
      { false, false, false, false } },
       
    { { false, true,  true,  false }, 
      { false, true,  true,  false }, 
      { false, false, false, false },
      { false, false, false, false } },
 
    { { false, true,  true,  false }, 
      { false, true,  true,  false }, 
      { false, false, false, false },
      { false, false, false, false } },
  }),

  T(Color.MAGENTA, new Boolean[][][] { 
    { { true,  true,  true,  false }, 
      { false, true,  false, false }, 
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  false, false },
      { true,  true,  false, false },
      { false, true,  false, false },
      { false, false, false, false } },

    { { false, true,  false, false },
      { true,  true,  true,  false },
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  false, false },
      { false, true,  true,  false },
      { false, true,  false, false },
      { false, false, false, false } },
  }),
  I(Color.CYAN, new Boolean[][][] { 
    { { false, true,  false, false }, 
      { false, true,  false, false }, 
      { false, true,  false, false },
      { false, true,  false, false } },

    { { false, false, false, false },
      { true,  true,  true,  true  },
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  false, false },
      { false, true,  false, false },
      { false, true,  false, false },
      { false, true,  false, false } },

    { { false, false, false, false },
      { true,  true,  true,  true  },
      { false, false, false, false },
      { false, false, false, false } },
  }),
  J(Color.BLUE, new Boolean[][][] { 
    { { false, false, true,  false }, 
      { false, false, true,  false }, 
      { false, true,  true,  false },
      { false, false, false, false } },

    { { true,  false, false, false },
      { true,  true,  true,  false },
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  true,  false },
      { false, true,  false, false },
      { false, true,  false, false },
      { false, false, false, false } },

    { { true,  true,  true,  false },
      { false, false, true,  false },
      { false, false, false, false },
      { false, false, false, false } },
  }),
  L(new Color(255, 108, 0) /* Orange */, new Boolean[][][] { 
    { { false, true,  false, false }, 
      { false, true,  false, false }, 
      { false, true,  true,  false },
      { false, false, false, false } },

    { { true,  true,  true,  false },
      { true,  false, false, false },
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  true,  false },
      { false, false, true,  false },
      { false, false, true,  false },
      { false, false, false, false } },

    { { false, false, false, true  },
      { false, true,  true,  true  },
      { false, false, false, false },
      { false, false, false, false } },
  }),
  S(Color.GREEN, new Boolean[][][] { 
    { { false, true,  true,  false }, 
      { true,  true,  false, false }, 
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  false, false },
      { false, true,  true,  false },
      { false, false, true,  false },
      { false, false, false, false } },

    { { false, true,  true,  false },
      { true,  true,  false, false },
      { false, false, false, false },
      { false, false, false, false } },

    { { false, true,  false, false },
      { false, true,  true,  false },
      { false, false, true,  false },
      { false, false, false, false } },

  }),
  Z(Color.RED, new Boolean[][][] {
    { { true,  true,  false, false }, 
      { false, true,  true, false },
      { false, false, false, false }, 
      { false, false, false, false } },

    { { false, false, true,  false },
      { false, true,  true,  false },
      { false, true,  false, false },
      { false, false, false, false } },

    { { true,  true,  false, false }, 
      { false, true,  true, false },
      { false, false, false, false }, 
      { false, false, false, false } },

    { { false, false, true,  false },
      { false, true,  true,  false },
      { false, true,  false, false },
      { false, false, false, false } },
  });

  private static ArrayList<Tile> allTiles;
  public final Color color;
  /** The 2-Dimensional shapes of the tile for every rotation */
  private final Boolean[][][] shapes;

  private Tile(Color color, Boolean[][][] shapes) 
  {
    this.shapes = shapes;
    this.color = color;
    register(this);
  }

  private static void register(Tile tile)
  {
    if (allTiles == null) allTiles = new ArrayList<Tile>();
    allTiles.add(tile);
  }

  /**
   * Selects a random entry from the {@code ArrayList} of registered Tiles
   * @return A random Tile
   */
  public static Tile randomTile()
  {
    int index = new Random().nextInt(allTiles.size());
    return allTiles.get(index);
  }

  /**
   * Construct a {@code Block} {@code ArrayList} representating the Tile on a {@code Grid}.
   * @param rotation The rotation state of the tile to get the blocks for (between 0 and 3)
   * @param pos Coordinates of the top-left position of the tile on the {@code Window}.
   * @return {@code ArrayList}<{@code Block}> representating the Tile
   */
  public ArrayList<Block> toBlockArray(int rotation, Point pos)
  {
    if (rotation < 0 || rotation > 3) throw new Error("Rotation must be between 0 and 3");
    ArrayList<Block> blocks = new ArrayList<Block>(5);
    for (int row = 0; row < 4; row++)
      for (int col = 0; col < 4; col++)
        if (shapes[rotation][row][col]) {
          Point blockPos = new Point(pos.x + col * Grid.BLOCKSIZE, pos.y + row * Grid.BLOCKSIZE);
          blocks.add(new Block(blockPos, Grid.BLOCKSIZE, color));
        }
    return blocks;
  }
}
