package com.github.fahjulian.tetris.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database 
{
  public static void init()
  {
    Connection c = null;
    Statement stmt = null;

    try
    {
      Class.forName("org.sqlite.JDBC");
      // Class.forName("org.xerial.sqlite-jdbc");
      c = DriverManager.getConnection("jdbc:sqlite:./.tetris.sqlite");
      c.setAutoCommit(false);
      stmt = c.createStatement();

      stmt = c.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS TETRIS " + 
                    "(ID INT PRIMARY KEY    NOT NULL," + 
                    " HIGHSCORE      INT    NOT NULL);";
      stmt.executeUpdate(sql);
      c.commit();
      
      ResultSet rs = stmt.executeQuery("SELECT * FROM TETRIS");
      // Check if it is a new game
      if (!rs.next())
      {
        sql = "INSERT INTO TETRIS (ID, HIGHSCORE) VALUES (0, 0);";
        stmt.executeUpdate(sql);
        c.commit();
      }

      rs.close();
      stmt.close();
      c.close();
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void saveHighscore(int highscore)
  {
    Connection c = null;
    Statement stmt = null;

    try
    {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:./.tetris.sqlite");
        c.setAutoCommit(false);
        stmt = c.createStatement();

        String sql = String.format("UPDATE TETRIS set HIGHSCORE = %d where ID=0;", highscore);
        stmt.executeUpdate(sql);
        c.commit();

        stmt.close();
        c.close();
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static int getHighscore()
  {
    Connection c = null;
    Statement stmt = null;
    int highscore = 0;

    try
    {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:./.tetris.sqlite");
      c.setAutoCommit(false);
      stmt = c.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT * FROM TETRIS");
      rs.next();
      highscore = rs.getInt("HIGHSCORE");
      
      rs.close();
      stmt.close();
      c.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      highscore = -1;
    }

    return highscore;
  }
}
