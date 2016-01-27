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

import java.io.IOException;

import org.helm.notation.MonomerFactory;
import org.helm.notation.model.Monomer;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.grouping.GroupingNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroup;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupElement;
import org.helm.notation2.parser.notation.polymer.MonomerNotationList;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;

/**
 * ChangeObjects, class to provide simple methods to change the ContainerHELM2
 *
 * @author hecht
 */
public final class ChangeObjects {

  /**
   * method to add an annotation at a specific position of the ContainerHELM2
   *
   * @param notation new annotation
   * @param position position of the new annotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void addAnnotation(final AnnotationNotation notation, final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().add(position, notation);
  }

  /**
   * method to change an annotation at the position of the ContainerHELM2
   *
   * @param notation new changed annotation
   * @param position position of the changed annotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void changeAnnotation(final AnnotationNotation notation, final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().set(position, notation);
  }

  /**
   * method to delete the annotation at the specific position of the
   * ContainerHELM2
   *
   * @param position position of the to be deleted annotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void deleteAnnotation(final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().remove(position);
  }

  /**
   * method to delete all annotations of the ContainerHELM2
   *
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void deleteAllAnnotations(final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfAnnotations().clear();
  }

  /**
   * method to add a new connection at the position of the ContainerHELM2
   *
   * @param notation new ConnectionNotation
   * @param position position of the new ConnectionNotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void addConnection(final ConnectionNotation notation, final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().add(position, notation);
  }

  /**
   * method to change a connection at the position of the ContainerHELM2
   *
   * @param position position of the changed Connection
   * @param notation to be changed ConnectionNotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void changeConnection(final int position, final ConnectionNotation notation, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().set(position, notation);
  }

  /**
   * method to delete the connection at the specific position of the
   * ContainerHELM2
   *
   * @param position of the to deleted connection
   * @param containerhelm2 input containerHELM2
   */
  protected static void deleteConnection(final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().remove(position);
  }

  /**
   * method to add an annotation to a connection of the ContainerHELM2
   *
   * @param position position of the connection
   * @param annotation new annotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void addAnnotationToConnection(final int position, final String annotation, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().get(position).setAnnotation(annotation);
  }

  /**
   * method to delete all connections of the ContainerHELM2
   *
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void deleteAllConnections(final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfConnections().clear();
  }

  /**
   * method to add a group to the grouping section of the ContainerHELM2
   *
   * @param notation new group
   * @param position position of the new group
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void addGroup(final GroupingNotation notation, final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().add(position, notation);
  }

  /**
   * method to change a group of the ContainerHELM2
   *
   * @param notation new group
   * @param position position of the to be changed group
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void changeGroup(final GroupingNotation notation, final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().set(position, notation);
  }

  /**
   * method to delete a group at a specific position of the ContainerHELM2
   *
   * @param position position of the to be deleted group
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void deleteGroup(final int position, final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().remove(position);
  }

  /**
   * method to delete all groups of the ContainerHELM2
   *
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void deleteAllGroups(final ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfGroupings().clear();
  }

  /**
   * method to add an annotation to a PolymerNotation
   *
   * @param polymer PolymerNotation
   * @param annotation new annotation
   * @return PolymerNotation with the annotation
   */
  protected static PolymerNotation addAnnotationToPolymer(final PolymerNotation polymer, final String annotation) {
    if (polymer.getAnnotation() != null) {
      return new PolymerNotation(polymer.getPolymerID(), polymer.getPolymerElements(), polymer.getAnnotation() + " | " + annotation);
    }
    return new PolymerNotation(polymer.getPolymerID(), polymer.getPolymerElements(), annotation);
  }

  /**
   * method to remove a current annotation of a PolymerNotation
   *
   * @param polymer PolymerNotation
   * @return PolymerNotation with no annotation
   */
  protected static PolymerNotation removeAnnotationOfPolmyer(PolymerNotation polymer) {
    return new PolymerNotation(polymer.getPolymerID(), polymer.getPolymerElements(), null);
  }

  /**
   * method to add a new MonomerNotation to a PolymerNotation
   *
   * @param position position of the new MonomerNotation
   * @param polymer PolymerNotation
   * @param monomerNotation new MonomerNotation
   * @return PolymerNotation with the new MonomerNotation
   */
  protected static PolymerNotation addMonomerNotation(int position, PolymerNotation polymer, MonomerNotation monomerNotation) {
    polymer.getPolymerElements().getListOfElements().add(position, monomerNotation);
    return polymer;
  }

  protected static void changeMonomerNotation(int position, PolymerNotation polymer, MonomerNotation not) {
    polymer.getPolymerElements().getListOfElements().set(position, not);
  }

  /**
   * method to delete a MonomerNotation at a specific position of the
   * PolymerNotation
   *
   * @param position position of the to be deleted MonomerNotation
   * @param polymer PolymerNotation
   * @throws NotationException if the generated PolymerNotation has no elements
   *           after deleting the MonomerNotation
   */
  protected static void deleteMonomerNotation(int position, PolymerNotation polymer) throws NotationException {
    MonomerNotation monomerNotation = polymer.getPolymerElements().getListOfElements().get(position);
    if (polymer.getPolymerElements().getListOfElements().size() == 1) {
      throw new NotationException(monomerNotation.toString() + " can't be removed. Polymer has to have at least one Monomer Notation");
    }
    polymer.getPolymerElements().getListOfElements().remove(monomerNotation);
  }

  /**
   * method to add an annotation to a MonomerNotation
   *
   * @param polymer PolymerNotation
   * @param position position of the monomerNotation
   * @param annotation new annotation
   */
  protected static void addAnnotationToMonomerNotation(PolymerNotation polymer, int position, String annotation) {
    polymer.getPolymerElements().getListOfElements().get(position).setAnnotation(annotation);
  }

  /**
   * method to set the count of a MonomerNotation
   *
   * @param polymer PolymerNotation
   * @param position position of the MonomerNotation
   * @param count new count of the MonomerNotation
   */
  protected static void addCountToMonomerNotation(PolymerNotation polymer, int position, String count) {
    polymer.getPolymerElements().getListOfElements().get(position).setCount(count);
  }

  /**
   * method to delete the annotation of a MonomerNotation
   *
   * @param polymer PolymerNotation
   * @param position position of the MonomerNotation
   */
  protected static void deleteAnnotationFromMonomerNotation(PolymerNotation polymer, int position) {
    polymer.getPolymerElements().getListOfElements().get(position).setAnnotation(null);
  }

  /**
   * method to set the count of a MonomerNotation to default (=1)
   *
   * @param polymer PolymerNotation
   * @param position position of the MonomerNotation
   */
  protected static void setCountToDefault(PolymerNotation polymer, int position) {
    polymer.getPolymerElements().getListOfElements().get(position).setCount("1");
  }

  /**
   * method to change the PolymerNotation at a specific position of the
   * ContainerHELM2
   *
   * @param position position of the PolymerNotation
   * @param polymer new PolymerNotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void changePolymerNotation(int position, PolymerNotation polymer, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfPolymers().set(position, polymer);
  }

  /**
   * method to delete the PolymerNotation at a specific position of the
   * ContainerHELM2
   *
   * @param position position of the PolymerNotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void deletePolymerNotation(int position, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfPolymers().remove(position);
  }

  /**
   * method to add a PolymerNotation at a specific position of the
   * ContainerHELM2
   *
   * @param position position of the PolymerNotation
   * @param polymer new PolymerNotation
   * @param containerhelm2 input ContainerHELM2
   */
  protected static void addPolymerNotation(int position, PolymerNotation polymer, ContainerHELM2 containerhelm2) {
    containerhelm2.getHELM2Notation().getListOfPolymers().add(position, polymer);
  }

  protected static void replaceMonomer(ContainerHELM2 containerhelm2, String polymerType, String existingMonomerID, String newMonomerID) throws NotationException, IOException {
    for (int i = 0; i < containerhelm2.getHELM2Notation().getListOfPolymers().size(); i++) {
      if (containerhelm2.getHELM2Notation().getListOfPolymers().get(i).getPolymerID().getType().equals(polymerType)) {
        for (int j = 0; j < containerhelm2.getHELM2Notation().getListOfPolymers().get(i).getPolymerElements().getListOfElements().size(); j++) {
          MonomerNotation monomerNotation =
              replaceMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers().get(i).getPolymerElements().getListOfElements().get(j), existingMonomerID, newMonomerID);
          if (monomerNotation != null) {
            containerhelm2.getHELM2Notation().getListOfPolymers().get(i).getPolymerElements().getListOfElements().set(j, monomerNotation);
          }
        }
      }
    }

  }

  private static MonomerNotation replaceMonomerNotation(MonomerNotation monomerNotation, String existingMonomerID, String newMonomerID) throws NotationException, IOException {
    if (newMonomerID.length() > 1) {
      newMonomerID = "[" + newMonomerID + "]";
    }
    boolean hasChanged = false;

    if (monomerNotation instanceof MonomerNotationUnitRNA) {
      StringBuilder sb = new StringBuilder();
      for (MonomerNotationUnit element : ((MonomerNotationUnitRNA) monomerNotation).getContents()) {
        if (element.getID().equals(existingMonomerID)) {
          hasChanged = true;
          sb.append(newMonomerID);
        } else {
          if (MonomerFactory.getInstance().getMonomerStore().getMonomer("RNA", element.getID()).getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
            sb.append("(" + element.getID() + ")");
          } else {
            sb.append(element.getID());
          }
        }
      }
      if (hasChanged) {
        MonomerNotationUnitRNA newObject = new MonomerNotationUnitRNA(sb.toString(), monomerNotation.getType());
        newObject.setCount(monomerNotation.getCount());
        if (monomerNotation.isAnnotationTrue()) {
          newObject.setAnnotation(monomerNotation.getAnnotation());
        }
      }

    } else if (monomerNotation instanceof MonomerNotationUnit) {
      if (monomerNotation.getID().equals(existingMonomerID)) {
        return produceMonomerNotationUnitWithOtherID(monomerNotation, newMonomerID);
      }
    } else if (monomerNotation instanceof MonomerNotationList) {
      StringBuilder sb = new StringBuilder();
      for (MonomerNotation element : ((MonomerNotationList) monomerNotation).getListofMonomerUnits()) {
        if (element.getID().equals(existingMonomerID)) {
          hasChanged = true;
          sb.append(newMonomerID);
        } else {
          if (MonomerFactory.getInstance().getMonomerStore().getMonomer("RNA", element.getID()).getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
            sb.append("(" + element.getID() + ")");
          } else {
            sb.append(element.getID());
          }
        }
      }
      if (hasChanged) {
        MonomerNotationUnitRNA newObject = new MonomerNotationUnitRNA(sb.toString(), monomerNotation.getType());
        newObject.setCount(monomerNotation.getCount());
        if (monomerNotation.isAnnotationTrue()) {
          newObject.setAnnotation(monomerNotation.getAnnotation());
        }
      }

    } else if (monomerNotation instanceof MonomerNotationGroup) {
      for (int i = 0; i < ((MonomerNotationGroup) monomerNotation).getListOfElements().size(); i++) {

        if (((MonomerNotationGroup) monomerNotation).getListOfElements().get(i).getMonomerNotation().getID().equals(monomerNotation.getID().equals(existingMonomerID))) {
          hasChanged = true;
          MonomerNotationGroupElement oldElement = ((MonomerNotationGroup) monomerNotation).getListOfElements().get(i);
          oldElement.setMonomerNotation(produceMonomerNotationUnitWithOtherID(monomerNotation, newMonomerID));
          ((MonomerNotationGroup) monomerNotation).getListOfElements().set(i, oldElement);
        }
      }
    } else {
      throw new NotationException("Unknown MonomerNotation Type " + monomerNotation.getClass());
    }

    if (hasChanged) {
      return monomerNotation;
    }
    return null;
  }

  private static MonomerNotationUnit produceMonomerNotationUnitWithOtherID(MonomerNotation monomerNotation, String newID) throws NotationException, IOException {
    MonomerNotationUnit result = new MonomerNotationUnit(newID, monomerNotation.getType());
    if (monomerNotation.isAnnotationTrue()) {
      result.setAnnotation(monomerNotation.getAnnotation());
    }
    result.setCount(monomerNotation.getCount());
    return result;
  }

}
