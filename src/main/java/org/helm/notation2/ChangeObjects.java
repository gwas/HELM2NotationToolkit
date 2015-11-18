/**
 * *****************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *****************************************************************************
 */
package org.helm.notation2;

import org.helm.notation2.parser.Notation.Annotation.AnnotationNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChangeObjects
 * 
 * @author hecht
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
