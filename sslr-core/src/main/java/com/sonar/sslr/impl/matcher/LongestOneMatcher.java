/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */

package com.sonar.sslr.impl.matcher;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.ParsingState;
import com.sonar.sslr.impl.BacktrackingEvent;

public class LongestOneMatcher extends StatelessMatcher {

	protected LongestOneMatcher(Matcher... matchers) {
  	super(matchers);
  }

  public AstNode matchWorker(ParsingState parsingState) {
  	Matcher longestMatcher = null;
  	int longestMatchIndex = -1;
  	
    for (Matcher matcher : super.children) {
    	int matcherIndex = matcher.matchToIndex(parsingState);
      if (matcherIndex >= 0) {
        /* This matcher could parse the input [as well], but for longer than the current longest matcher? */
      	if (matcherIndex > longestMatchIndex) {
      		/* Yes! */
      		longestMatcher = matcher;
      		longestMatchIndex = matcherIndex;
      	}
      }
    }
    
    if (longestMatcher != null) {
    	return longestMatcher.match(parsingState);
    }
    
    throw BacktrackingEvent.create();
  }
  
  @Override
  public String toString() {
  	return "longestOne";
  }

}
