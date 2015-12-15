
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
import org.helm.notation2.parser.exceptionparser.HELM1ConverterException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HELM1
 * 
 * @author hecht
 */
public final class HELM1Utils {


  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(HELM1Utils.class);

  private static HELM2Notation helm2notation;

  private static String firstSection = ""; // polymer section

  private static String secondSection = ""; // connection section

  private static String thirdSection = ""; // pair section

  private static String fourthSection = ""; // annotation section
  
  /**
   * method to reproduce a standard HELM1
   * 
   * @param helm2notation
   * @return
   * @throws HELM1FormatException
   * @throws NotationException
   */
  public static String getStandard(HELM2Notation helm2notation) throws HELM1FormatException, NotationException {
    HELM1Utils.helm2notation = helm2notation;
    try {
      setStandardHELMFirstSection();
      setStandardHELMSecondSectionAndThirdSection();
      setStandardHELMFourthSection();
    } catch (HELM1ConverterException | MonomerException | IOException | JDOMException | CTKException e) {
      throw new HELM1FormatException(e.getMessage());
    } // -> this methods also sets the
      // thirdSection

    return firstSection + "$" + secondSection + "$" + thirdSection + "$" + fourthSection + "$";
  }
  
  /**
   * method to generate from a helm2notation a valid canonical HELM1
   * 
   * @param helm2notation
   * @return
   * @throws HELM1FormatException
   * @throws NotationException
   */
  public static String getCanonical(HELM2Notation helm2notation) throws HELM1FormatException, NotationException {
    HELM1Utils.helm2notation = helm2notation;
    Map<String, String> convertsortedIdstoIds;
    try {
      convertsortedIdstoIds = setCanonicalHELMFirstSection();
      setCanonicalHELMSecondSection(convertsortedIdstoIds);
    }

    catch (ClassNotFoundException | MonomerException | IOException | JDOMException | CTKException | HELM1ConverterException e) {
      LOG.error("Canonical HELM 1 can not be generated due to HELM2 features");
      throw new HELM1FormatException("Canonical HELM 1 can not be generated due to HELM2 features");
    }
    return firstSection + "$" + secondSection + "$" + thirdSection + "$" + fourthSection + "$";
  }

  /* polymer section */
  private static void setStandardHELMFirstSection() throws HELM1ConverterException, CTKSmilesException, MonomerException, IOException, JDOMException, CTKException, NotationException {
    StringBuilder notation = new StringBuilder();

    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      String id = polymer.getPolymerID().getID();
      String elements_toHELM = polymer.getPolymerElements().toHELM();
      Map<String, String> AdHocList = findAdHocMonomers(elements_toHELM, polymer.getPolymerID().getType());
      Map<String, String> convert = convertAdHocMonomersIntoSMILES(AdHocList);
      for (Map.Entry<String, String> e : convert.entrySet()) {
        elements_toHELM = elements_toHELM.replaceAll(e.getKey(), e.getValue());
      }
      notation.append(id + "{" + elements_toHELM + "}" + "|");
    }

    notation.setLength(notation.length() - 1);
    firstSection = notation.toString();
  }

  /* connection section */
  private static void setStandardHELMSecondSectionAndThirdSection() throws HELM1ConverterException {
    StringBuilder notationSecond = new StringBuilder();
    StringBuilder notationThird = new StringBuilder();
    for (ConnectionNotation connectionNotation : helm2notation.getListOfConnections()) {
      /* pairs will be not shown */
      if (!(connectionNotation.toHELM().equals(""))) {
        notationSecond.append(connectionNotation.toHELM() + "|");
      }

      else {
        notationThird.append(connectionNotation.toHELM2() + "|");
      }
    }
    if (notationSecond.length() > 1) {
      notationSecond.setLength(notationSecond.length() - 1);
    }
    if (notationThird.length() > 1) {
      notationThird.setLength(notationThird.length() - 1);
    }

    secondSection = notationSecond.toString();
    thirdSection = notationThird.toString();
  }


  /* annotation section : will be the same of the HELM2 section */
  private static void setStandardHELMFourthSection() {
    StringBuilder sb = new StringBuilder();

    for (AnnotationNotation annotation : helm2notation.getListOfAnnotations()) {
      sb.append(annotation.toHELM2() + "|");
    }
    if (sb.length() > 1) {
      sb.setLength(sb.length() - 1);
    }
    fourthSection = sb.toString();
  }

  private static Map<String, String> setCanonicalHELMFirstSection() throws CTKSmilesException, MonomerException, IOException, JDOMException, CTKException,
      HELM1ConverterException, ClassNotFoundException, NotationException {
    Map<String, String> idLabelMap = new HashMap<String, String>();
    Map<String, List<String>> labelIdMap = new TreeMap<String, List<String>>();

    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      String id = polymer.getPolymerID().getID();
      String elements_toHELM = polymer.getPolymerElements().toHELM();
      Map<String, String> AdHocList = findAdHocMonomers(elements_toHELM, polymer.getPolymerID().getType());
      Map<String, String> convert = convertAdHocMonomersIntoSMILES(AdHocList);
      for (Map.Entry<String, String> e : convert.entrySet()) {
        elements_toHELM = elements_toHELM.replaceAll(e.getKey(), e.getValue());
      }

      idLabelMap.put(id, elements_toHELM);
      if (labelIdMap.containsKey(elements_toHELM)) {
        List<String> l = labelIdMap.get(elements_toHELM);
        l.add(id);
      } else {
        List<String> l = new ArrayList<String>();
        l.add(id);
        labelIdMap.put(elements_toHELM, l);
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
      int count_CHEM = 0;
      int count_PEPTIDE = 0;
      int count_RNA = 0;
      for (String id : sortedIdList) {

        int count = 0;
        if (helm2notation.getPolymerNotation(id).getPolymerID().getType().equals("PEPTIDE")) {
          count_PEPTIDE += 1;
          count = count_PEPTIDE;
        }
        if (helm2notation.getPolymerNotation(id).getPolymerID().getType().equals("CHEM")) {
          count_CHEM += 1;
          count = count_CHEM;
        }
        if (helm2notation.getPolymerNotation(id).getPolymerID().getType().equals("RNA")) {
          count_RNA += 1;
          count = count_RNA;
        }

        notation = notation + helm2notation.getPolymerNotation(id).getPolymerID().getType() + count + "{" + helm2notation.getPolymerNotation(id).toHELM() + "}|";
        convertsortedIdstoIds.put(helm2notation.getPolymerNotation(id).getPolymerID().getID(), helm2notation.getPolymerNotation(id).getPolymerID().getType() + count);

      }

      notation = notation.substring(0, notation.length() - 1);
      notationList.add(notation);
    }

    Collections.sort(notationList);
    firstSection = notationList.get(0);
    return convertsortedIdstoIds;
  }

  /**
   * method to generate a canonical HELM 1 connection section
   * 
   * @param convertsortedIdstoIds
   * @return
   * @throws HELM1ConverterException
   */
  private static String setCanonicalHELMSecondSection(Map<String, String> convertsortedIdstoIds) throws HELM1ConverterException {
    StringBuilder notation = new StringBuilder();
    for (ConnectionNotation connectionNotation : helm2notation.getListOfConnections()) {
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

    return secondSection = notation.toString();
  }

  /**
   * method to convert the polymers ids of the connection
   * 
   * @param notation
   * @param source
   * @param target
   * @param convertIds
   * @return
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
   * @param elements
   * @return
   * @throws MonomerLoadingException
   * @throws NotationException
   * @throws CTKException
   */
  private static Map<String, String> findAdHocMonomers(String elements, String type) throws MonomerLoadingException, NotationException, CTKException {
    /*find adHocMonomers*/
    Map<String, String> listMatches = new HashMap<String, String>();
    String[] listelements = elements.split("\\.");
    if(type == "RNA"){
      for (String element : listelements) {
        List<String> monomerIds = SimpleNotationParser.getMonomerIDList(element, type, MonomerFactory.getInstance().getMonomerStore());
        for(String id : monomerIds){
     
      
          Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer(type, id);
        if (monomer.isAdHocMonomer()) {
          listMatches.put(element, "[" + monomer.getCanSMILES() + "]");

        }
      }
      }
      
    }
    
    else{
      for(String element : listelements){
        Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer(type, element.replace("[", "").replace("]", ""));
        try {
          if (monomer.isAdHocMonomer()) {
            listMatches.put(element, "[" + monomer.getCanSMILES() + "]");
          }
        } catch (NullPointerException e) {
          if (!(Chemistry.getInstance().getManipulator().validateSMILES(element))) {
            System.out.println("False");
          }
        }
      }
    }
   
    return listMatches;
  }

  /**
   * method to translate/convert the adhocMonomers into valid SMILES
   * 
   * @param monomersList
   * @return
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   * @throws CTKSmilesException
   * @throws CTKException
   */
  private static Map<String, String> convertAdHocMonomersIntoSMILES(Map<String, String> monomersList) throws MonomerException, IOException, JDOMException, CTKSmilesException, CTKException {
    Map<String, String> convert = new HashMap<String, String>();

    for (Map.Entry<String, String> element : monomersList.entrySet()) {

      Monomer m = MonomerFactory.getInstance().getMonomerStore().getMonomer(element.getValue().toString(), element.getKey().toString());
      String smiles = m.getCanSMILES();
      AbstractChemistryManipulator manipulator = Chemistry.getInstance().getManipulator();
      String canSmiles = manipulator.canonicalize(smiles);
      // to Do
      convert.put(element.getKey().toString(), canSmiles);
      /* Rgroups??? */
    }
    return convert;
  }


  
}
