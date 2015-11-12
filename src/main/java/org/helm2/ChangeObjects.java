/*--
 *
 * @(#) ChangeObjects.java
 *
 *
 */
package org.helm2;

import org.helm.notation2.parser.Notation.Annotation.AnnotationNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code ChangeObjects}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class ChangeObjects {

  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(ChangeObjects.class);

  public void addAnnotation(String str, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().addAnnotation(new AnnotationNotation(
        str));
  }

  public void removeAnnotation(String str, ContainerHELM2 containerhelm2) {

  }


}
