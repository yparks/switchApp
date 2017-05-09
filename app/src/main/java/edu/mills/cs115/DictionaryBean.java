package edu.mills.cs115;

/**
 * @author Ying Parks
 * Dictionary Objec
 *
 */

public class DictionaryBean {
  private String term;

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  private String definition;
}
