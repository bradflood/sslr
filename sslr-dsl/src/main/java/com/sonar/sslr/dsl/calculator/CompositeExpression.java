/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package com.sonar.sslr.dsl.calculator;

public abstract class CompositeExpression implements AbstractExpression {

  protected AbstractExpression firstExp;
  protected AbstractExpression secondExp;

  public void add(AbstractExpression exp) {
    if (firstExp == null) {
      firstExp = exp;
    }
    secondExp = exp;
  }
}
