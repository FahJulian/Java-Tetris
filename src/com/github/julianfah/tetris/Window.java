package com.github.julianfah.tetris;

import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Window extends JFrame 
{
  private static final long serialVersionUID = 2255469318620343984L;

  private final int contentWidth;
  private final int contentHeight;

  /**
   * Constructs a initially visible JFrame
   * 
   * @param title  The title of the JFrame
   * @param width  The width of the JFrames {@code contentPane}
   * @param height The height of the JFrames {@code contentPane}
   */
  public Window(String title, int width, int height) 
  {
    super(title);
    this.contentWidth = width;
    this.contentHeight = height;
    setSize(width, height);

    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);

    resize();
  }

  @Override
  public void paint(Graphics g) 
  {
    // Make color painting fancier
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    super.paint(g2d);
  }

  /**
   * Add to the frames height to make the JFrames contentPane height match the
   * {@link #contentHeight}. Should be called in Constructor.
   */
  private void resize() 
  {
    // JFrame needs to be visible to access its contentPane
    if (!isVisible())
      setVisible(true);
    int extraHeight = contentHeight - getContentPane().getSize().height;
    setSize(contentWidth, contentHeight + extraHeight);
  }
}
