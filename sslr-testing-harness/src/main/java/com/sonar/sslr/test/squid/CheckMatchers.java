/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package com.sonar.sslr.test.squid;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.hamcrest.Matcher;
import org.sonar.squid.api.CheckMessage;
import org.sonar.squid.api.SourceFile;
import org.sonar.squid.measures.MetricDef;

import com.google.common.collect.Lists;

/**
 * Utility class used for testing checks without having to deploy them on a Sonar instance.
 * 
 */
public final class CheckMatchers {

  private static CheckMatchers singleInstance = new CheckMatchers();

  private static ThreadLocal<List<CheckMessage>> currentMessagesUnderTest = new ThreadLocal<List<CheckMessage>>();

  private static ThreadLocal<Iterator<CheckMessage>> currentMessagesIterator = new ThreadLocal<Iterator<CheckMessage>>();

  private static ThreadLocal<SourceFile> currentSourceFile = new ThreadLocal<SourceFile>();

  /*
   * Comparator for CheckMessage that orders them by line number, and if line number are equals, then by alphabetic order for the message.
   */
  private static Comparator<CheckMessage> checkMessageComparator = new Comparator<CheckMessage>() {

    public int compare(CheckMessage m1, CheckMessage m2) {
      int lineDifference = m1.getLine() - m2.getLine();
      if (lineDifference == 0) {
        return m1.getDefaultMessage().compareTo(m2.getDefaultMessage());
      } else {
        return lineDifference;
      }
    }
  };

  private CheckMatchers() {
  }

  private static CheckMatchers getInstance() {
    return singleInstance;
  }

  /**
   * Sets the current source file. <br/>
   * <br/>
   * To verify the results, some convenient methods are provided. For instance:
   * 
   * <pre>
   * {@code}
   * setCurrentSourceFile(mySourceFile);
   * assertOnlyOneViolation().atLine(5).withMessage(&quot;Violation here!&quot;);
   * </pre>
   * 
   * @see #assertOnlyOneViolation()
   * @see #assertViolation()
   * 
   * @param sourceFile
   *          the analyzed file on which to perform the assertions
   */
  public static void setCurrentSourceFile(SourceFile sourceFile) {
    currentSourceFile.set(sourceFile);
    currentMessagesUnderTest.set(Lists.newArrayList(sourceFile.getCheckMessages()));
    Collections.sort(currentMessagesUnderTest.get(), checkMessageComparator);
    currentMessagesIterator.set(currentMessagesUnderTest.get().iterator());
  }

  /**
   * Verifies that there's only one violation generated by the last call to #setCurrentSourceFile(...) methods.<br/>
   * <br/>
   * This method returns a Violation object that can be used to verify some more assertions.
   * 
   * @return the first and only violation message - if any.
   */
  public static Violation assertOnlyOneViolation() {
    assertThat(currentMessagesUnderTest.get().size(), is(1));
    return new Violation(currentMessagesIterator.get().next());
  }

  /**
   * Verifies that there's the expected number of violations.
   */
  public static void assertNumberOfViolations(int expectedNumberOfViolations) {
    assertThat(currentMessagesUnderTest.get().size(), is(expectedNumberOfViolations));
  }

  /**
   * Verifies that there isn't any violation generated by the last call to #setCurrentSourceFile(...) methods.
   * 
   * @return the first and only violation message - if any.
   */
  public static void assertNoViolation() {
    assertThat(currentMessagesUnderTest.get().size(), is(0));
  }

  /**
   * Return the value of the provided metric generated by the last call to #setCurrentSourceFile(...) methods.
   */
  public static double getDouble(MetricDef metric) {
    return currentSourceFile.get().getDouble(metric);
  }

  /**
   * Return the value of the provided metric generated by the last call to #setCurrentSourceFile(...) methods.
   */
  public static int getInt(MetricDef metric) {
    return currentSourceFile.get().getInt(metric);
  }

  /**
   * Verifies that there's one more violation left to check since the last call to #setCurrentSourceFile(...) methods.<br/>
   * <br/>
   * This method returns a Violation object that can be used to verify some more assertions. For instance:
   * 
   * <pre>
   * {@code}
   * assertViolation().atLine(5).withMessage(&quot;Violation here!&quot;);
   * assertViolation().atLine(8).withMessage(&quot;Other violation here!&quot;);
   * </pre>
   * 
   * <b>Important</b>: the calls to {@link #assertViolation()} must be done in the following order:
   * <ol>
   * <li>line increasing order</li>
   * <li>alphabetical increasing order (for violations on a same line)</li>
   * </ol>
   * 
   * @return the next violation message - if any.
   */
  public static Violation assertViolation() {
    assertTrue("There's no more violation.", currentMessagesIterator.get().hasNext());
    return new Violation(currentMessagesIterator.get().next());
  }

  /**
   * Utility class used to ease the unit test development of checks
   */
  public static final class Violation {

    private final CheckMessage checkMessage;

    private Violation(CheckMessage checkMessage) {
      this.checkMessage = checkMessage;
    }

    /**
     * Checks that the current violation occurs at line #lineNumber in the code. If this is not the case, the assertion fails.
     * 
     * @param lineNumber
     *          the number where the violation should occur
     * @return the current violation
     */
    public Violation atLine(int lineNumber) {
      assertThat(checkMessage.getLine(), is(lineNumber));
      return this;
    }

    /**
     * Checks that the current violation message is the same as the given message. If this is not the case, the assertion fails.
     * 
     * @param message
     *          the message that the violation should contain
     * @return the current violation
     */
    public Violation withMessage(String message) {
      assertThat(checkMessage.getText(Locale.getDefault()), is(message));
      return this;
    }

    /**
     * Checks that the current violation message matches the given String matcher. If this is not the case, the assertion fails.
     * 
     * @param message
     *          the String matcher that violation message should match
     * @return the current violation
     */
    public Violation withMessage(Matcher<String> matcher) {
      assertThat(checkMessage.getText(Locale.getDefault()), matcher);
      return this;
    }
  }

}