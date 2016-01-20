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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.NotationException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.PermutationAndExpansion;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.exceptionparser.HELM1ConverterException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HELM1 class to transform a HELM2 into HELM1; this is only possible in the
 * case of no HELM2 features
 *
 * @author hecht
 */
public final class HELM1Utils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(HELM1Utils.class);

  /**
   * Default constructor.
   */
  private HELM1Utils() {

  }

  /**
   * method to reproduce a standard HELM in HELM1 - Format
   *
   * @param helm2notation HELM2Notation
   * @return standard HELM1
   * @throws HELM1FormatException if HELM2 features are there
   * @throws NotationException if the notation objects can not be built
   * @throws CTKException
   * @throws MonomerLoadingException
   * @throws ValidationException if a smiles in the HELMNotation is not valid
   */
  public static String getStandard(HELM2Notation helm2notation) throws HELM1FormatException, NotationException, MonomerLoadingException, CTKException, ValidationException {
    try {
      String firstSection = setStandardHELMFirstSection(helm2notation);
      List<String> ListOfSecondAndThirdSection = setStandardHELMSecondSectionAndThirdSection(helm2notation.getListOfConnections());
      String fourthSection = setStandardHELMFourthSection(helm2notation.getListOfAnnotations());
      return firstSection + "$" + ListOfSecondAndThirdSection.get(0) + "$" + ListOfSecondAndThirdSection.get(1) + "$" + fourthSection + "$V2.0";
    } catch (HELM1ConverterException e) {
      throw new HELM1FormatException(e.getMessage());
    }
  }

  /**
   * method to transform the first section (polymer section) to HELM1 - Format
   *
   * @param helm2notation HELM2Notation
   * @return first section of standard HELM
   * @throws HELM1ConverterException if HELM2 features were there
   * @throws MonomerLoadingException
   * @throws NotationException
   * @throws CTKException
   * @throws ValidationException
   * @throws HELM1FormatException
   */
  private static String setStandardHELMFirstSection(HELM2Notation helm2notation) throws HELM1ConverterException, MonomerLoadingException, NotationException, CTKException, HELM1FormatException,
      ValidationException {
    StringBuilder notation = new StringBuilder();

    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      String id = polymer.getPolymerID().getID();
      String elementsToHELM = polymer.getPolymerElements().toHELM();
      Map<String, String> adHocList = findAdHocMonomers(elementsToHELM, polymer.getPolymerID().getType());
      Map<String, String> convert = convertAdHocMonomersIntoSMILES(adHocList);
      for (Map.Entry<String, String> e : convert.entrySet()) {
        elementsToHELM = elementsToHELM.replaceAll(e.getKey(), e.getValue());
      }
      notation.append(id + "{" + elementsToHELM + "}" + "|");
    }

    notation.setLength(notation.length() - 1);
    return notation.toString();
  }

  /**
   * method to transform the second and third section (connection + group
   * section) to HELM1-Format
   *
   * @param connections List of ConnectionNotation
   * @return second and third section in String format
   * @throws HELM1ConverterException if it can not be downcasted to HELM1, HELM2
   *           features were there
   */
  private static List<String> setStandardHELMSecondSectionAndThirdSection(List<ConnectionNotation> connections) throws HELM1ConverterException {
    List<String> result = new ArrayList<String>();
    StringBuilder notationSecond = new StringBuilder();
    StringBuilder notationThird = new StringBuilder();
    for (ConnectionNotation connectionNotation : connections) {
      /* pairs will be not shown */
      if (!(connectionNotation.toHELM().equals(""))) {
        notationSecond.append(connectionNotation.toHELM() + "|");
      } else {
        notationThird.append(connectionNotation.toHELM2() + "|");
      }
    }
    if (notationSecond.length() > 1) {
      notationSecond.setLength(notationSecond.length() - 1);
    }
    if (notationThird.length() > 1) {
      notationThird.setLength(notationThird.length() - 1);
    }

    result.add(notationSecond.toString());
    result.add(notationThird.toString());
    return result;
  }

  /**
   * method to transform the fourth section into HELM1-Format
   *
   * @param annotations List of AnnotationNotation
   * @return the fourth section of an Standard HELM
   */
  private static String setStandardHELMFourthSection(List<AnnotationNotation> annotations) {
    StringBuilder sb = new StringBuilder();

    for (AnnotationNotation annotation : annotations) {
      sb.append(annotation.toHELM2() + "|");
    }
    if (sb.length() > 1) {
      sb.setLength(sb.length() - 1);
    }
    return sb.toString();
  }

  /**
   * method to generate from a helm2notation a valid canonical HELM1
   *
   * @param helm2notation input HELM2Notation
   * @return canonical HELM
   * @throws HELM1FormatException if HELM2 features are there
   * @throws NotationException if the notation objects can not be built
   */
  public static String getCanonical(HELM2Notation helm2notation) throws HELM1FormatException, NotationException {
    Map<String, String> convertsortedIdstoIds;
    try {
      Object[] temp = setCanonicalHELMFirstSection(helm2notation);
      convertsortedIdstoIds = (Map<String, String>) temp[0];
      String firstSection = (String) temp[1];
      String secondSection = setCanonicalHELMSecondSection(convertsortedIdstoIds, helm2notation.getListOfConnections());
      return firstSection + "$" + secondSection + "$" + "" + "$" + "" + "$V2.0";
    } catch (ClassNotFoundException | IOException | HELM1ConverterException | ValidationException e) {
      LOG.error("Canonical HELM 1 can not be generated due to HELM2 features");
      throw new HELM1FormatException("Canonical HELM 1 can not be generated due to HELM2 features " + e.getMessage() + e.getCause());
    }
  }

  /**
   * method to convert the first section into canonical first section
   *
   * @param helm2notation HELM2Notation
   * @return an Object containing in the first place a Map of converted ids and
   *         in the second place the firstSection
   * @throws IOException
   * @throws HELM1ConverterException if there were HELM2 features in the
   *           HELMNotation
   * @throws HELM1FormatException if the adHocMonomers can not be found
   * @throws ClassNotFoundException
   * @throws ValidationException if a smiles as monomer is not valid
   */
  private static Object[] setCanonicalHELMFirstSection(HELM2Notation helm2notation) throws HELM1ConverterException, HELM1FormatException, ClassNotFoundException, IOException, ValidationException {
    Map<String, String> idLabelMap = new HashMap<String, String>();
    Map<String, List<String>> labelIdMap = new TreeMap<String, List<String>>();

    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      String id = polymer.getPolymerID().getID();
      String elementsToHELM = polymer.getPolymerElements().toHELM();
      Map<String, String> adHocList = findAdHocMonomers(elementsToHELM, polymer.getPolymerID().getType());
      Map<String, String> convert = convertAdHocMonomersIntoSMILES(adHocList);
      for (Map.Entry<String, String> e : convert.entrySet()) {
        elementsToHELM = elementsToHELM.replaceAll(e.getKey(), e.getValue());
      }

      idLabelMap.put(id, elementsToHELM);
      if (labelIdMap.containsKey(elementsToHELM)) {
        List<String> l = labelIdMap.get(elementsToHELM);
        l.add(id);
      } else {
        List<String> l = new ArrayList<String>();
        l.add(id);
        labelIdMap.put(elementsToHELM, l);
      }
    }

    Set<String> sortedLabelSet = labelIdMap.keySet();
    List<List<String[]>> lol = new ArrayList<List<String[]>>();
    for (String key : sortedLabelSet) {
      List<String> value = labelIdMap.get(key);
      List<String[]> al = new ArrayList<String[]>();
      al.add(value.toArray(new String[0]));
      PermutationAndExpansion.expand(lol, al);
    }

    List<List<String>> nodeIdPermutations = PermutationAndExpansion.linearize(lol);
    List<String> notationList = new ArrayList<String>();
    Map<String, String> convertsortedIdstoIds = new HashMap<String, String>();
    for (List<String> sortedIdList : nodeIdPermutations) {

      String notation = "";
      int countCHEM = 0;
      int countPEPTIDE = 0;
      int countRNA = 0;
      for (String id : sortedIdList) {

        int count = 0;
        if (helm2notation.getPolymerNotation(id).getPolymerID().getType().equals("PEPTIDE")) {
          countPEPTIDE += 1;
          count = countPEPTIDE;
        }
        if (helm2notation.getPolymerNotation(id).getPolymerID().getType().equals("CHEM")) {
          countCHEM += 1;
          count = countCHEM;
        }
        if (helm2notation.getPolymerNotation(id).getPolymerID().getType().equals("RNA")) {
          countRNA += 1;
          count = countRNA;
        }

        notation = notation + helm2notation.getPolymerNotation(id).getPolymerID().getType() + count + "{" + helm2notation.getPolymerNotation(id).toHELM() + "}|";
        convertsortedIdstoIds.put(helm2notation.getPolymerNotation(id).getPolymerID().getID(), helm2notation.getPolymerNotation(id).getPolymerID().getType() + count);

      }

      notation = notation.substring(0, notation.length() - 1);
      notationList.add(notation);
    }

    Collections.sort(notationList);
    return new Object[] {convertsortedIdstoIds, notationList.get(0)};
  }

  /**
   * method to generate a canonical HELM 1 connection section
   *
   * @param convertsortedIdstoIds Map of old ids with the equivalent new ids
   * @return second section of HELM
   * @throws HELM1ConverterException
   */
  private static String setCanonicalHELMSecondSection(Map<String, String> convertsortedIdstoIds, List<ConnectionNotation> connectionNotations) throws HELM1ConverterException {
    StringBuilder notation = new StringBuilder();
    for (ConnectionNotation connectionNotation : connectionNotations) {
      /* canonicalize connection */
      /* change the id's of the polymers to the sorted ids */
      List<String> connections = new ArrayList<String>();
      String source = connectionNotation.getSourceId().getID();
      String target = connectionNotation.getTargetId().getID();

      /* pairs will be not shown */
      if (!(connectionNotation.toHELM().equals(""))) {
        connections.add(convertConnection(connectionNotation.toHELM(), source, target, convertsortedIdstoIds));
        connections.add(convertConnection(connectionNotation.toReverseHELM(), source, target, convertsortedIdstoIds));
        Collections.sort(connections);
        notation.append(connections.get(0) + "|");
      }
    }
    if (notation.length() > 1) {
      notation.setLength(notation.length() - 1);
    }

    return notation.toString();
  }

  /**
   * method to convert the polymers ids of the connection
   *
   * @param notation connection description in HELM
   * @param source polymer id of source
   * @param target polymer id of target
   * @param convertIds Map of old polymer ids with the new polymer ids
   * @return connection description in HELM with the changed polymer ids
   *         according to the map
   * @throws HELM1ConverterException
   */
  private static String convertConnection(String notation, String source, String target, Map<String, String> convertIds) throws HELM1ConverterException {
    try {
      String test = notation.replace(source, "one");
      test = test.replace(target, "two");
      test = test.replace("one", convertIds.get(source));
      test = test.replace("two", convertIds.get(target));
      return test;
    } catch (NullPointerException ex) {
      LOG.error("Connection can't be downgraded to HELM1-Format");
      throw new HELM1ConverterException("Connection can't be downgraded to HELM1-Format");
    }
  }

  /**
   * method to find all adhocMonomers in one Polymer
   *
   * @param elements HELM representation of polymer elements
   * @return HELM representation of polymer elements with the smiles
   *         representation for adhoc monomers
   * @throws HELM1FormatException if the monomers can not be converted to the ad
   *           hoc monomer representation
   * @throws ValidationException if the smiles representation of a monomer is
   *           not valid
   */
  /*
   * method has to be changed !!! including smiles -> to generate canonical
   * representation; this method has to be tested in further detail
   */
  private static Map<String, String> findAdHocMonomers(String elements, String type) throws HELM1FormatException, ValidationException {
    /* find adHocMonomers */
    try {
      Map<String, String> listMatches = new HashMap<String, String>();
      String[] listelements = elements.split("\\.");
      if (type == "RNA") {
        for (String element : listelements) {
          List<String> monomerIds;
          monomerIds = SimpleNotationParser.getMonomerIDList(element, type, MonomerFactory.getInstance().getMonomerStore());

          for (String id : monomerIds) {

            Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer(type, id);
            if (monomer.isAdHocMonomer()) {
              listMatches.put(element, "[" + monomer.getCanSMILES() + "]");

            }
          }
        }

      } else {
        for (String element : listelements) {
          Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer(type, element.replace("[", "").replace("]", ""));
          try {
            if (monomer.isAdHocMonomer()) {
              listMatches.put(element, "[" + monomer.getCanSMILES() + "]");
            }
          } catch (NullPointerException e) {
            if (!(Chemistry.getInstance().getManipulator().validateSMILES(element.substring(1, element.length() - 1)))) {
              throw new ValidationException("SMILES as Monomer is not valid");
            }
          }
        }
      }

      return listMatches;
    } catch (MonomerLoadingException | NotationException e) {
      throw new HELM1FormatException(e.getMessage());
    }
  }

  /**
   * method to translate/convert the adhocMonomers into valid SMILES
   *
   * @param monomersList Map of adhocMonomers with the type and the alternate
   *          monomer id SMILES
   * @return Map of adhocMonomers with the monomer alternate id and the
   *         appropriate SMILES
   * @throws HELM1FormatException if the SMILES for the Monomer can not be found
   */
  private static Map<String, String> convertAdHocMonomersIntoSMILES(Map<String, String> monomersList) throws HELM1FormatException {
    Map<String, String> convert = new HashMap<String, String>();
    try {
      for (Map.Entry<String, String> element : monomersList.entrySet()) {

        Monomer m;
        m = MonomerFactory.getInstance().getMonomerStore().getMonomer(element.getValue().toString(), element.getKey().toString());

        String smiles = m.getCanSMILES();
        AbstractChemistryManipulator manipulator = Chemistry.getInstance().getManipulator();
        String canSmiles = manipulator.canonicalize(smiles);
        convert.put(element.getKey().toString(), canSmiles);
      }
      return convert;
    } catch (MonomerLoadingException | CTKException e) {
      throw new HELM1FormatException("SMILES for Monomer can not be found");
    }
  }

}
