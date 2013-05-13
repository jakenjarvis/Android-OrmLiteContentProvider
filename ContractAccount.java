package com.example;

public final class Person {
  private String firstName;
  private String lastName;
  /**
   * Returns the person's full name.
   */
  public String getName() {
    return firstName + " " + lastName;;
  }
}
