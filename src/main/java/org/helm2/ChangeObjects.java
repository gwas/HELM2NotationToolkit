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

  /* method to change monomer */

  public void changeMonomerNotationUnit() {

  }

  /* method to remove monomer */

  /* method to add monomer */

  /* method to add annotation to monomer */

  /* method to remove annotion from monomer */

  /* method to set count to monomer */



  /* method to remove connection */
  /* method to remove all connections */
  /* method to add connection */

  /* method to change connection */

  /* method to add polymer */
  /* method to remove polymer */

  /* method to add annotation to polymer */

  /* method to change annotation to polymer */

  /* method to remove annotaiton from polymer */

  /* method to add a group */
  /* method to change a group */
  /* method to remove a group */
  /* method to remove all groups */


}
