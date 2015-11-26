/*--
 *
 * @(#) ParserException.java
 *
 *
 */
package org.helm.notation2.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code ParserException}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class ParserException extends Exception {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(ParserException.class);

  public ParserException(String message) {
    super(message);
  }
}
