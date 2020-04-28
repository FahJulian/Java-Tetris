package com.github.fahjulian.tetris.util;

import java.io.File;
import java.io.IOException;

import java.awt.Font;
import java.awt.FontFormatException;

public class FontLoader {
  public static Font loadTTF(String ttfPath, int style, int fontSize)
  { 
    try 
    {
      System.out.println(new File(ttfPath).getAbsolutePath());
      return Font.createFonts(new File(ttfPath))[0].deriveFont(style, fontSize);
    } 
    catch(IOException | FontFormatException e)
    {
      e.printStackTrace();
      return null;
    }
  }
}
