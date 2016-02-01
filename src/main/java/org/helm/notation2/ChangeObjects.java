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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.MonomerStore;
import org.helm.notation.model.Monomer;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.grouping.GroupingNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroup;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupElement;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupMixture;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupOr;
import org.helm.notation2.parser.notation.polymer.MonomerNotationList;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;

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

  /**
   * method to change the MonomerNotation on the specific position
   *
   * @param position position of the changed MonomerNotation
   * @param polymer PolymerNotation
   * @param not changed MonomerNotation
   */
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

  /**
   * method to replace the MonomerID with the new MonomerID for a given polymer
   * type
   *
   * @param containerhelm2
   * @param polymerType String of the polymer type
   * @param existingMonomerID old MonomerID
   * @param newMonomerID new MonomerID
   * @throws NotationException
   * @throws IOException
   * @throws JDOMException
   * @throws MonomerException
   */
  protected static void replaceMonomer(ContainerHELM2 containerhelm2, String polymerType, String existingMonomerID, String newMonomerID) throws NotationException, IOException, JDOMException,
      MonomerException {
    validateMonomerReplacement(polymerType, existingMonomerID, newMonomerID);
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

  /**
   * method to replace the MonomerNotation having the MonomerID with the new
   * MonomerID
   *
   * @param monomerNotation
   * @param existingMonomerID
   * @param newMonomerID
   * @return MonomerNotation, if it had the old MonomerID, null otherwise
   * @throws NotationException
   * @throws IOException
   * @throws JDOMException
   */
  private static MonomerNotation replaceMonomerNotation(MonomerNotation monomerNotation, String existingMonomerID, String newMonomerID) throws NotationException, IOException, JDOMException {
    /* Nucleotide */
    if (monomerNotation instanceof MonomerNotationUnitRNA) {
      List<String> result = generateIDForNucleotide(((MonomerNotationUnitRNA) monomerNotation), existingMonomerID, newMonomerID);
      if (result.get(1) != null) {
        MonomerNotationUnitRNA newObject = new MonomerNotationUnitRNA(result.get(0), monomerNotation.getType());
        newObject.setCount(monomerNotation.getCount());
        if (monomerNotation.isAnnotationTrue()) {
          newObject.setAnnotation(monomerNotation.getAnnotation());
        }
        return newObject;
      }
    } else if (monomerNotation instanceof MonomerNotationUnit) {
      /* Simple MonomerNotationUnit */
      if (monomerNotation.getID().equals(existingMonomerID)) {
        return produceMonomerNotationUnitWithOtherID(monomerNotation, newMonomerID);
      }
    } else if (monomerNotation instanceof MonomerNotationList) {
      /* MonomerNotationList */
      monomerNotation = replaceMonomerNotationList(((MonomerNotationList) monomerNotation), existingMonomerID, newMonomerID);
      if (monomerNotation != null) {
        return monomerNotation;
      }
    } else if (monomerNotation instanceof MonomerNotationGroup) {
      /* MonomerNotatationGroup */
      System.out.println(monomerNotation.getID());
      monomerNotation = replaceMonomerNotationGroup(((MonomerNotationGroup) monomerNotation), existingMonomerID, newMonomerID);
      if (monomerNotation != null) {
        return monomerNotation;
      }
    } else {
      throw new NotationException("Unknown MonomerNotation Type " + monomerNotation.getClass());
    }

    return null;

  }

  /**
   * method to replace the MonomerNotationGroup having the MonomerID with the
   * new MonomerID
   *
   * @param monomerNotation
   * @param existingMonomerID
   * @param newMonomerID
   * @return MonomerNotationGroup, if it had the old MonomerID, null otherwise
   * @throws NotationException
   * @throws IOException
   * @throws JDOMException
   */
  private static MonomerNotationGroup replaceMonomerNotationGroup(MonomerNotationGroup monomerNotation, String existingMonomerID, String newMonomerID) throws NotationException, IOException,
      JDOMException {
    MonomerNotationGroup newObject = null;
    boolean hasChanged = false;
    StringBuilder sb = new StringBuilder();
    String id = "";
    for (MonomerNotationGroupElement object : monomerNotation.getListOfElements()) {
      System.out.println(object.getMonomerNotation().getID());
      System.out.println(object.getValue());
      if (object.getMonomerNotation().getID().equals(existingMonomerID)) {
        hasChanged = true;
        id = generateGroupElement(newMonomerID, object.getValue());
      } else {
        id = generateGroupElement(object.getMonomerNotation().getID(), object.getValue());
      }
      if (monomerNotation instanceof MonomerNotationGroupOr) {
        sb.append(id + ",");
      } else {
        sb.append(id + "+");
      }

    }
    if (hasChanged) {
      sb.setLength(sb.length() - 1);
      System.out.println(sb.toString());
      if (monomerNotation instanceof MonomerNotationGroupOr) {
        newObject = new MonomerNotationGroupOr(sb.toString(), monomerNotation.getType());
      } else {
        newObject = new MonomerNotationGroupMixture(sb.toString(), monomerNotation.getType());
      }
    }
    return newObject;
  }

  /**
   * method to replace the MonomerNotationUnit having the MonomerID with the new
   * MonomerID
   *
   * @param monomerNotation
   * @param newID
   * @return MonomerNotationUnit
   * @throws NotationException
   * @throws IOException
   */
  private static MonomerNotationUnit produceMonomerNotationUnitWithOtherID(MonomerNotation monomerNotation, String newID) throws NotationException, IOException {
    MonomerNotationUnit result = new MonomerNotationUnit(newID, monomerNotation.getType());
    if (monomerNotation.isAnnotationTrue()) {
      result.setAnnotation(monomerNotation.getAnnotation());
    }
    result.setCount(monomerNotation.getCount());
    return result;
  }

  /**
   * method to replace the MonomerNotationList having the MonomerID with the new
   * MonomerID
   *
   * @param object
   * @param existingMonomerID
   * @param newMonomerID
   * @return MonomerNotationList, if it had the old MonomerID, null otherwise
   * @throws NotationException
   * @throws IOException
   * @throws JDOMException
   */
  private static MonomerNotationList replaceMonomerNotationList(MonomerNotationList object, String existingMonomerID, String newMonomerID) throws NotationException, IOException, JDOMException {
    MonomerNotationList newObject = null;
    boolean hasChanged = false;
    StringBuilder sb = new StringBuilder();
    String id = "";
    for (MonomerNotation element : object.getListofMonomerUnits()) {
      if (element instanceof MonomerNotationUnitRNA) {
        List<String> result = generateIDForNucleotide(((MonomerNotationUnitRNA) element), existingMonomerID, newMonomerID);
        id = result.get(0);
        if (result.get(1) != null) {
          hasChanged = true;
        }
      } else {
        if (element.getID().equals(existingMonomerID)) {
          hasChanged = true;
          id = generateIDMonomerNotation(newMonomerID, element.getCount(), element.getAnnotation());
        } else {
          id = generateIDMonomerNotation(element.getID(), element.getCount(), element.getAnnotation());
        }

      }
      sb.append(id + ".");
    }
    if (hasChanged) {
      sb.setLength(sb.length() - 1);
      newObject = new MonomerNotationList(sb.toString(), object.getType());
      newObject.setCount(object.getCount());
      if (object.isAnnotationTrue()) {
        newObject.setAnnotation(object.getAnnotation());

      }
    }

    return newObject;

  }

  /**
   * method to generate the MonomerNotation in String format
   *
   * @param id id of the MonomerNotation
   * @param count Count of the MonomerNotation
   * @param annotation Annotation of the MonomerNotation
   * @return MonomerNotation in String format
   */
  private static String generateIDMonomerNotation(String id, String count, String annotation) {
    if (id.length() > 1) {
      id = "[" + id + "]";
    }
    String result = id;
    try {
      if (!(Integer.parseInt(count) == 1)) {
        result += "'" + count + "'";
      }
    } catch (NumberFormatException e) {
      result += "'" + count + "'";
    }
    if (annotation != null) {
      result += "\"" + annotation + "\"";
    }
    return result;
  }

  /**
   * method to generate the MonomerNotation for a RNA
   *
   * @param id id of the MonomerNotation
   * @param count Count of the MonomerNotation
   * @param annotation Annotation of the MonomerNotation
   * @return MonomerNotation in String format
   * @throws MonomerLoadingException
   */
  private static String generateIDRNA(String id, String count, String annotation) throws MonomerLoadingException {
    String result = id;
    if (id.length() > 1) {
      result = "[" + id + "]";
    }
    if (MonomerFactory.getInstance().getMonomerStore().getMonomer("RNA", id).getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
      result = "(" + result + ")";
    }

    try {
      if (!(Integer.parseInt(count) == 1)) {
        result += "'" + count + "'";
      }
    } catch (NumberFormatException e) {
      result += "'" + count + "'";
    }
    if (annotation != null) {
      result += "\"" + annotation + "\"";
    }
    return result;
  }

  /**
   * method to replace the MonomerNotationUnitRNA having the MonomerID with the
   * new MonomerID
   *
   * @param object MonomerNotationUnitRNA
   * @param existingMonomerID
   * @param newMonomerID
   * @return List of MonomerNotationUnitRNA in String format and the information
   *         if the MonomerNotationUnitRNA has to be changed
   * @throws MonomerLoadingException
   */
  private static List<String> generateIDForNucleotide(MonomerNotationUnitRNA object, String existingMonomerID, String newMonomerID) throws MonomerLoadingException {
    List<String> result = new ArrayList<String>();
    String hasChanged = null;
    StringBuilder sb = new StringBuilder();
    String id = "";
    for (MonomerNotation element : object.getContents()) {
      if (element.getID().equals(existingMonomerID)) {
        hasChanged = "";
        id = generateIDRNA(newMonomerID, element.getCount(), element.getAnnotation());
      } else {
        id = generateIDRNA(element.getID(), element.getCount(), element.getAnnotation());
      }
      sb.append(id);
    }
    try {
      if (!(Integer.parseInt(object.getCount()) == 1)) {
        sb.append("'" + object.getCount() + "'");
      }
    } catch (NumberFormatException e) {
      sb.append("'" + object.getCount() + "'");
    }
    if (object.getAnnotation() != null) {
      sb.append("\"" + object.getAnnotation() + "\"");
    }
    result.add(sb.toString());
    result.add(hasChanged);
    return result;
  }

  /**
   * method to generate the MonomerNotationGroupElement in String format
   *
   * @param id
   * @param list
   * @return MonomerNotationGroupElement in String format
   */
  private static String generateGroupElement(String id, List<Double> list) {
    StringBuilder sb = new StringBuilder();

    if (list.size() == 1) {
      if (list.get(0) == -1.0) {
        sb.append(id + ":");
        sb.append("?" + "-");
      } else if (list.get(0) == 1.0) {
        sb.append(id + ":");
      } else {
        sb.append(id + ":" + list.get(0) + "-");
      }
    } else {
      sb.append(id + ":");
      for (Double item : list) {
        sb.append(item + "-");
      }
    }

    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  protected static boolean validateMonomerReplacement(String polymerType,
      String existingMonomerID, String newMonomerID) throws MonomerException, IOException,
          JDOMException, NotationException {

    if (null == polymerType || polymerType.length() == 0) {
      throw new NotationException(
          "Polymer type is required for monomer replacement");
    }

    if (null == existingMonomerID || existingMonomerID.length() == 0) {
      throw new NotationException(
          "Existing monomer ID is required for monomer replacement");
    }

    if (null == newMonomerID || newMonomerID.length() == 0) {
      throw new NotationException(
          "New monomer ID is required for monomer replacement");
    }
    Map<String, Monomer> monomers = MonomerFactory.getInstance().getMonomerStore().getMonomers(polymerType);

    if (null == monomers || monomers.size() == 0) {
      throw new NotationException("Unknown polymer type [" + polymerType
          + "] found");
    }

    if (!monomers.containsKey(existingMonomerID)) {
      throw new NotationException("Existing monomer ID ["
          + existingMonomerID + "] is invalid in polymer type "
          + polymerType);
    }

    if (!monomers.containsKey(newMonomerID)) {
      throw new NotationException("New monomer ID [" + newMonomerID
          + "] is invalid in polymer type " + polymerType);
    }

    Monomer existingMonomer = monomers.get(existingMonomerID);
    Monomer newMonomer = monomers.get(newMonomerID);

    if (polymerType.equals(Monomer.NUCLIEC_ACID_POLYMER_TYPE)) {
      if (existingMonomer.getMonomerType().equals(newMonomer.getMonomerType())) {
        if (existingMonomer.getMonomerType().equals(Monomer.BACKBONE_MOMONER_TYPE)) {
          if (!existingMonomer.getNaturalAnalog().equals(newMonomer.getNaturalAnalog())) {
            throw new NotationException(
                "Existing monomer natural analog ["
                    + existingMonomer.getNaturalAnalog()
                    + "] and new monomer natural analog ["
                    + newMonomer.getNaturalAnalog()
                    + "] are different");
          }
        }
      } else {
        throw new NotationException("Existing monomer type ["
            + existingMonomer.getMonomerType()
            + "] and new monomer type ["
            + newMonomer.getMonomerType() + "] are different");
      }
    }

    if (!newMonomer.attachmentEquals(existingMonomer)) {
      throw new NotationException("Existing monomer attachment ["
          + existingMonomer.getAttachmentListString()
          + "] and new monomer attachement ["
          + newMonomer.getAttachmentListString() + "] are different");
    }

    return true;
  }
}
