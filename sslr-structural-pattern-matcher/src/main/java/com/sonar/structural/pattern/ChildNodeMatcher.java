/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package com.sonar.structural.pattern;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.dsl.Literal;

public final class ChildNodeMatcher extends CompositeMatcher {

  protected String rule;
  protected String tokenValue;

  public void setRule(String name) {
    rule = name;
  }

  public void setTokenValue(Literal tokenValue) {
    this.tokenValue = tokenValue.toString();
  }

  @Override
  public final AstNode match(AstNode node) {
    if (node.getTokenValue().equals(tokenValue)) {
      return getLeafNode(node);
    }
    node = matchChildren(node);
    if (node == null) {
      return null;
    }
    if (matcher != null && matcher.match(node) == null) {
      return null;
    }
    return node;
  }

  private AstNode getLeafNode(AstNode nextNode) {
    if (nextNode.hasChildren()) {
      return getLeafNode(nextNode.getFirstChild());
    }
    return nextNode;
  }

  public AstNode matchChildren(AstNode node) {
    if (node.hasChildren()) {
      for (AstNode child : node.getChildren()) {
        if (child.getName().equals(rule)) {
          return child;
        }
      }
      for (AstNode child : node.getChildren()) {
        return matchChildren(child);
      }
    }
    return null;
  }
}
