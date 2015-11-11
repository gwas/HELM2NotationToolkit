/*--
 *
 * @(#) AttachmentException.java
 *
 *
 */
package org.helm2.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code AttachmentException}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class AttachmentException extends Exception {

  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(AttachmentException.class);

  public AttachmentException(String message) {
    super(message);
  }

}
