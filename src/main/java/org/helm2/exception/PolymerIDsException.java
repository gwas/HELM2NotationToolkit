/*--
 *
 * @(#) PolymerIDsException.java
 *
 *
 */
package org.helm2.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code PolymerIDsException}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class PolymerIDsException extends Exception {

  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(PolymerIDsException.class);

  public PolymerIDsException(String message) {
    super(message);
  }

}
