package com.github.fahjulian.tetris.util;

public class Clock {
  private long start;

  public Clock() 
  {
    start = System.nanoTime();
  }

  /**
   * Get duration since last time without reseting the timer.
   * @return Duration since last request in millis
   */
  public int peekDuration()
  {
    return (int)((System.nanoTime() - start) / 1e6);
  }

  /**
   * Get duration since last time and reset the timer.
   * @return Duration since last request in millis
   */
  public int getDuration()
  {
    long now = System.nanoTime();
    int duration = (int)((now - start) / 1e6);
    start = now;
    return duration;
  }

  /**
   * Reset the timer.
   */
  public void reset()
  {
    start = System.nanoTime();
  }
}
