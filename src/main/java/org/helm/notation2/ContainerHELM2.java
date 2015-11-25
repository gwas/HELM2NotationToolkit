/*--
 *
 * @(#) ContainerHELM2.java
 *
 *
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.ChemicalToolKit;
import org.helm.chemtoolkit.ChemistryManipulator;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.PermutationAndExpansion;
import org.helm.notation2.parser.exceptionparser.HELM1ConverterException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code ContainerHELM2}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class ContainerHELM2 {


  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(ContainerHELM2.class);

  HELM2Notation helm2notation;

  InterConnections interconnection;

  List<PolymerUnit> polymerunits = new ArrayList<PolymerUnit>();

  public static final String PEPTIDE = "PEPTIDE";

  public static final String RNA = "RNA";

  public static final String BLOB = "BLOB";

  public static final String CHEM = "CHEM";

  private static final String CHEM_ADHOC = "CM#";

  private static final String PEPTIDE_ADHOC = "PM#";

  private static final String RNA_ADHOC = "NM#";

  public ContainerHELM2(HELM2Notation helm2notation,
      InterConnections interconnection) {
    this.helm2notation = helm2notation;
    this.interconnection = interconnection;
  }

  public HELM2Notation getHELM2Notation() {
    return helm2notation;
  }

  public InterConnections getInterconnection() {
    return interconnection;
  }

  public void buildMolecule() {

  }

  public double getMolecularWeight() {
    /* First build one big molecule */
    System.out.println("BuildOneBigMolecule");
    buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return 0;

  }

  public static double getExaxtMass() {
    return 0;
  }

  public static String getMolecularFormular() {
    return null;
  }

  /**
   * @return
   * @throws ClassNotFoundException
   * @throws IOException
   * @throws HELM1ConverterException
   * @throws JDOMException
   * @throws MonomerException
   * @throws CTKException
   * @throws CTKSmilesException
   */
  public String getCanonicalHELM() throws ClassNotFoundException, IOException, HELM1ConverterException, MonomerException, JDOMException, CTKSmilesException, CTKException {

    String firstSection = "";
    String secondSection = "";
    String thirdSection = "";
    String fourthSection = "";
    List<PolymerNotation> nodeList = helm2notation.getListOfPolymers();
    List<ConnectionNotation> edgeList = helm2notation.getListOfConnections();
    // deal with ad hoc CHEM monomer here, use smiles instead of temp ID
    /* Hashmap adhocMonomer id -> SMILES */

    // deal with backbone cycles self cycles: both node and edge needs to be
    // modified if backbone cycles are involved

    /* Deal with the first section: sort polymer ids */
    Map<String, String> idLabelMap = new HashMap<String, String>();
    Map<String, List<String>> labelIdMap = new TreeMap<String, List<String>>();

    for (PolymerNotation node : nodeList) {
      /*
       * save all adhoc monomers -> find right expression for it -> replace** /
       * 
       */
      String id = node.getPolymerID().getID();
      String elements_toHELM = node.getPolymerElements().toHELM();
      Map<String, String> AdHocList = findAdHocMonomers(elements_toHELM);
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

    /* deal with connections */
    String connectionString = "";
    for (ConnectionNotation connection : edgeList) {
      /* canonicalize connection */
      /* change the id's of the polymers to the sorted ids */
      List<String> connections = new ArrayList<String>();
      String source = connection.getSourceId().getID();
      String target = connection.getTargetId().getID();

      /* pairs will be not shown */
      if (!(connection.toHELM().equals(""))) {
        connections.add(convertConnection(connection.toHELM(), source, target, convertsortedIdstoIds));
        connections.add(convertConnection(connection.toReverseHELM(), source, target, convertsortedIdstoIds));
        Collections.sort(connections);
        connectionString = connectionString + (connections.get(0) + "|");
      }
    }
    if (connectionString.endsWith("|")) {
      connectionString = connectionString.substring(0, connectionString.length() - 1);
    }

    firstSection = notationList.get(0) + "$";
    secondSection = connectionString + "$";
    thirdSection = "$";
    fourthSection = "$";
    return firstSection + secondSection + thirdSection + fourthSection;
  }


  private String convertConnection(String notation, String source, String target, Map<String, String> convertIds) throws HELM1ConverterException {
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

  private Map<String, String> findAdHocMonomers(String elements) {
    Pattern pattern = Pattern.compile(CHEM_ADHOC + "\\d+" + "|" + PEPTIDE_ADHOC + "\\d+" + "|" + RNA_ADHOC + "\\d+");
    Matcher matcher = pattern.matcher(elements);
    Map<String, String> listMatches = new HashMap<String, String>();
    while (matcher.find()) {
      if (matcher.group(0).contains(CHEM_ADHOC)) {
        listMatches.put(matcher.group(0), Monomer.CHEMICAL_POLYMER_TYPE);
      } else if (matcher.group(0).contains(PEPTIDE_ADHOC)) {
        listMatches.put(matcher.group(0), Monomer.PEPTIDE_POLYMER_TYPE);
      } else {
        listMatches.put(matcher.group(0), Monomer.PEPTIDE_POLYMER_TYPE);
      }

    }

    return listMatches;
  }

  private Map<String, String> convertAdHocMonomersIntoSMILES(Map<String, String> monomersList) throws MonomerException, IOException, JDOMException, CTKSmilesException, CTKException {
    Map<String, String> convert = new HashMap<String, String>();

    for (Map.Entry<String, String> element : monomersList.entrySet()) {

      Monomer m = MonomerFactory.getInstance().getMonomerStore().getMonomer(element.getValue().toString(), element.getKey().toString());
      String smiles = m.getCanSMILES();
      ChemistryManipulator manipulator = ChemicalToolKit.getTestINSTANCE("").getManipulator();
      // String canSmiles = manipulator.canonicalize(smiles);
      convert.put(element.getKey().toString(), smiles);
      /* Rgroups??? */
    }
    return convert;
  }

  public String getStandardHELM() throws ClassNotFoundException, IOException, HELM1ConverterException, MonomerException, JDOMException, CTKSmilesException, CTKException {
    return getCanonicalHELM();
  }
}

