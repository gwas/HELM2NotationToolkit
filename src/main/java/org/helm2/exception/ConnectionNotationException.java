
package org.helm2.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code ConnectionNotationException}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class ConnectionNotationException extends Exception {

  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(ConnectionNotationException.class);

  public ConnectionNotationException(String message) {
    super(message);
  }

}
