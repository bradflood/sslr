/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */

package com.sonar.sslr.impl.matcher;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.BacktrackingEvent;
import com.sonar.sslr.impl.ParsingState;

public final class AdjacentMatcher extends StatelessMatcher {

  protected AdjacentMatcher(Matcher matcher) {
    super(matcher);
  }

  @Override
  protected AstNode matchWorker(ParsingState parsingState) {
    int index = parsingState.lexerIndex;
    Token nextToken = parsingState.peekToken(index, this);
    Token previousToken = parsingState.readToken(index - 1);
    if (nextToken.getColumn() <= previousToken.getColumn() + previousToken.getValue().length()
        && nextToken.getLine() == previousToken.getLine()) {
      AstNode node = new AstNode(null, "adjacentMatcher", nextToken);
      node.addChild(super.children[0].match(parsingState));
      return node;
    } else {
      throw BacktrackingEvent.create();
    }
  }

  @Override
  public final String toString() {
    return "adjacent";
  }

}
