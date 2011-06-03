/*
 * Copyright (C) 2010 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package com.sonar.sslr.dsl.helloworld;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;

import org.junit.Test;

import com.sonar.sslr.dsl.DslRunner;

public class HelloWorldTest {

  StringBuilder output = new StringBuilder();

  @Test
  public void shouldGetHelloWorld() throws URISyntaxException {
    DslRunner.builder(new HelloWorldDsl(), "print \"hello world!\"").inject(output).build().execute();
    assertThat(output.toString(), is("\"hello world!\""));
  }

  @Test
  public void shouldGetHelloFreddy() throws URISyntaxException {
    DslRunner.builder(new HelloWorldDsl(), "print \"hello freddy!\"").inject(output).build().execute();
    assertThat(output.toString(), is("\"hello freddy!\""));
  }
}
