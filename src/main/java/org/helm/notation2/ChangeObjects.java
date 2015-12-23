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

import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.grouping.GroupingNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;

/**
 * ChangeObjects
 * 
 * @author hecht
 */
public final class ChangeObjects {


  protected void addAnnotation(AnnotationNotation notation, int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().add(position, notation);
  }

  protected void changeAnnotation(AnnotationNotation notation, int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().set(position, notation);
  }

  protected void deleteAnnotation(int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().remove(position);
  }

  protected void deleteAllAnnotations(ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().clear();
  }

  protected void addConnection(ConnectionNotation notation, int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().add(position, notation);
  }

  protected void changeConnection(int position, ConnectionNotation notation, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().set(position, notation);
  }

  protected void deleteConnection(int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().remove(position);
  }

  protected void addAnnotationToConnection(int position, String annotation, ContainerHELM2 containerhelm2){
    containerhelm2.getHELM2Notation().getListOfConnections().get(position).setAnnotation(annotation);
  }
  protected void deleteAllConnections(ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().clear();
  }

  protected void addGroup(GroupingNotation notation, int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().add(position, notation);
  }

  protected void changeGroup(GroupingNotation notation, int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().set(position, notation);
  }

  protected void deleteGroup(int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().remove(position);
  }

  protected void deleteAllGroups(ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().clear();
  }



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
   * method to remove a current annotation of a polymernotation
   * 
   * @param not
   * @param annotation
   * @return
   */
  public static PolymerNotation removeAnnotationOfPolmyer(PolymerNotation not) {
    return new PolymerNotation(not.getPolymerID(), not.getPolymerElements(), null);
  }





  protected static PolymerNotation addMonomerNotation(int position, PolymerNotation polymer, MonomerNotation not) {
    polymer.getPolymerElements().getListOfElements().add(position, not);
    return polymer;
  }

  protected static void changeMonomerNotation(int position, PolymerNotation polymer, MonomerNotation not) {
    polymer.getPolymerElements().getListOfElements().set(position, not);
  }

  protected static void deleteMonomerNotation(int position, PolymerNotation polymer) throws NotationException {
    MonomerNotation monomerNotation = polymer.getPolymerElements().getListOfElements().get(position);
    if (polymer.getPolymerElements().getListOfElements().size() == 1) {
      throw new NotationException(monomerNotation.toString() + " can't be removed. Polymer has to have at least one Monomer Notation");
    }
    polymer.getPolymerElements().getListOfElements().remove(monomerNotation);
    if (polymer.getPolymerElements().getListOfElements().size() == 0) {

    }
  }

  protected void addAnnotationToMonomerNotation(PolymerNotation polymer, int position, String annotation) {
    polymer.getPolymerElements().getListOfElements().get(position).setAnnotation(annotation);
  }

  protected void addCountToMonomerNotation(PolymerNotation polymer, int position, String count) {
    polymer.getPolymerElements().getListOfElements().get(position).setCount(count);
  }

  protected void deleteAnnotationFromMonomerNotation(PolymerNotation polymer, int position) {
    polymer.getPolymerElements().getListOfElements().get(position).setAnnotation(null);
  }

  protected void setCountToDefault(PolymerNotation polymer, int position) {
    polymer.getPolymerElements().getListOfElements().get(position).setCount("1");
  }

  protected static void changePolymerNotation(int position, PolymerNotation polymer, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfPolymers().set(position, polymer);
  }

  protected static void deletePolymerNotation(int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfPolymers().remove(position);
  }

  protected static void addPolymerNotation(int position, PolymerNotation polymer, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfPolymers().add(position, polymer);
  }



}

