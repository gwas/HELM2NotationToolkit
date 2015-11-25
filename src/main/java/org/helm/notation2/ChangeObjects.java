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

import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.grouping.GroupingNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;

/**
 * ChangeObjects
 * 
 * @author hecht
 */
public final class ChangeObjects {

  public void addAnnotation(String str, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().addAnnotation(new AnnotationNotation(
        str));
  }

  public void removeAnnotation(String str, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().addAnnotation(null);
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


  /**
   * method to add an annotation to a PolymerNotation
   * 
   * @param not
   * @param annotation
   * @return
   */
  public static PolymerNotation addAnnotationToPolymer(PolymerNotation not, String annotation) {
    if (not.getAnnotation() != null) {
      return new PolymerNotation(not.getPolymerID(), not.getPolymerElements(), not.getAnnotation() + " | " + annotation);
    }
    return new PolymerNotation(not.getPolymerID(), not.getPolymerElements(), annotation);
  }


  /**
   * method to change a current annotation to the new annotation
   * 
   * @param not
   * @param annotation
   * @return
   */
  public static PolymerNotation changeAnnotationToPolmyer(PolymerNotation not, String annotation){
    return new PolymerNotation(not.getPolymerID(), not.getPolymerElements(), annotation);
  }

  /**
   * method to remove a current annotation of a polymernotation
   * 
   * @param not
   * @param annotation
   * @return
   */
  public static PolymerNotation removeAnnotationOfPolmyer(PolymerNotation not) {
    return new PolymerNotation(not.getPolymerID(), not.getPolymerElements(), null);
  }

  /**
   * method to add a Group Notation to the ContainerHELM2
   * 
   * @param helm2container
   * @param not
   */
  public static void addGroupNotation(ContainerHELM2 helm2container, GroupingNotation not) {
    helm2container.getHELM2Notation().getListOfGroupings().add(not);
  }

  /* method to change a group */
  /* method to remove a group */
  /* method to remove all groups */

  public static void removeAllGroups(ContainerHELM2 helm2container) {

    HELM2Notation notation = new HELM2Notation();
  }


}

