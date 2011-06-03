/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package com.sonar.sslr.dsl.internal.controlflow;

import com.sonar.sslr.dsl.adapter.LoopBlockAdapter;

public class Loop implements LoopBlockAdapter {

  private int numberOfLoop;
  private int pendingNumberOfLoop;

  public void add(Integer numberOfLoop) {
    this.numberOfLoop = numberOfLoop;
  }

  public boolean shouldExecuteLoopBlockIteration() {
    pendingNumberOfLoop--;
    return pendingNumberOfLoop > -1;
  }

  public void initLoopState() {
    pendingNumberOfLoop = numberOfLoop;
  }
}
