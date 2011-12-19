/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */

package com.sonar.sslr.api;

public interface TokenType extends AstNodeType {

  String getName();

  String getValue();

  boolean hasToBeSkippedFromAst(AstNode node);

}
