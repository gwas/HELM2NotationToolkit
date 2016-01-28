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

import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.grouping.GroupingNotation;
import org.helm.notation2.parser.notation.polymer.BlobEntity;
import org.helm.notation2.parser.notation.polymer.ChemEntity;
import org.helm.notation2.parser.notation.polymer.GroupEntity;
import org.helm.notation2.parser.notation.polymer.HELMEntity;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ContainerHELM2
 *
 * @author hecht
 */
public class ContainerHELM2 {

  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(ContainerHELM2.class);

  private HELM2Notation helm2notation;

  private InterConnections interconnection;

  public ContainerHELM2(HELM2Notation helm2notation,
      InterConnections interconnection) {
    this.helm2notation = helm2notation;
    this.interconnection = interconnection;
  }

  public final HELM2Notation getHELM2Notation() {
    return helm2notation;
  }

  public final InterConnections getInterconnection() {
    return interconnection;
  }

  /**
   * method to generate a JSON-Object from the NotationContainer
   *
   * @return NotationContainer in JSON-Format
   */
  protected final String toJSON() {
    ObjectMapper mapper = new ObjectMapper();

    try {
      String jsonINString = mapper.writeValueAsString(helm2notation);
      jsonINString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(helm2notation);

      return jsonINString;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * method to get all polymers of this object
   *
   * @return list of polymer notation
   */
  public final List<PolymerNotation> getAllPolymers() {
    return helm2notation.getListOfPolymers();
  }

  /**
   * method to get all connections of this object
   *
   * @return list of connection notation
   */
  public final List<ConnectionNotation> getAllConnections() {
    return helm2notation.getListOfConnections();
  }

  /**
   * method to get all edge connections of this object
   *
   * @return all edge connections of this object
   */
  public final List<ConnectionNotation> getAllEdgeConnections() {
    return MethodsForContainerHELM2.getAllEdgeConnections(getAllConnections());
  }

  /**
   * method to get all base pair connections of this object
   *
   * @return all base pair connections of this object
   */
  public final List<ConnectionNotation> getAllBasePairConnections() {
    List<ConnectionNotation> listEdgeConnection = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : helm2notation.getListOfConnections()) {
      if ((connection.getrGroupSource().equals("pair"))) {
        listEdgeConnection.add(connection);
      }
    }
    return listEdgeConnection;
  }

  /**
   * method to get all annotations of this object
   *
   * @return all annotation of this object
   */
  public final List<AnnotationNotation> getAllAnnotations() {
    return helm2notation.getListOfAnnotations();
  }

  /**
   * method to get all rna polymers of this object
   *
   * @return list of rna polymers
   */
  public final List<PolymerNotation> getRNAPolymers() {
    List<PolymerNotation> rnaPolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof RNAEntity) {
        rnaPolymers.add(polymer);
      }
    }
    return rnaPolymers;
  }

  /**
   * method to get all peptide polymers of this object
   *
   * @return list of peptide polymers
   */
  public final List<PolymerNotation> getPeptidePolymers() {
    List<PolymerNotation> peptidePolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof PeptideEntity) {
        peptidePolymers.add(polymer);
      }
    }
    return peptidePolymers;
  }

  /**
   * method to get all chem polymers of this object
   *
   * @return list of chem polymers
   */
  public final List<PolymerNotation> getCHEMPolymers() {
    List<PolymerNotation> chemPolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof ChemEntity) {
        chemPolymers.add(polymer);
      }
    }
    return chemPolymers;
  }

  /**
   * method to get all blob polymers of this object
   *
   * @return list of blob polymers
   */
  public final List<PolymerNotation> getBLOBPolymers() {
    List<PolymerNotation> blobPolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof BlobEntity) {
        blobPolymers.add(polymer);
      }
    }
    return blobPolymers;
  }

  /**
   * method to add a new HELMNotation to the existing one
   *
   * @param newHELM2Notation new HELMNotation
   * @throws NotationException if the HELMNotation is not valid
   */
  public final void addHELM2notation(HELM2Notation newHELM2Notation) throws NotationException {
    Map<String, String> mapIds = generateMapChangeIds(newHELM2Notation.getPolymerAndGroupingIDs());
    /* method to merge the new HELM2Notation into the existing one */

    /* section 1 */
    /* id's have to changed */
    section1(newHELM2Notation.getListOfPolymers(), mapIds);
    /* section 2 */
    section2(newHELM2Notation.getListOfConnections(), mapIds);
    /* section 3 */
    section3(newHELM2Notation.getListOfGroupings(), mapIds);
    /* section 4 */
    section4(newHELM2Notation.getListOfAnnotations(), mapIds);
  }

  /**
   * method to generate a Map of old ids with the new ids
   *
   * @param newIDs
   * @return
   */
  private Map<String, String> generateMapChangeIds(List<String> newIDs) {
    Map<String, String> mapIds = new HashMap<String, String>();
    List<String> oldIds = helm2notation.getPolymerAndGroupingIDs();

    Map<String, String> mapOldIds = new HashMap<String, String>();
    for (String oldID : oldIds) {
      mapOldIds.put(oldID, "");
    }
    for (String newId : newIDs) {
      if (mapOldIds.containsKey(newId)) {
        int i = 1;
        String type = newId.split("\\d")[0];

        while (mapOldIds.containsKey(type + i)) {
          i++;
        }
        mapIds.put(newId, type + i);
      }
    }

    return mapIds;

  }

  /**
   * method to add PolymerNotations to the existent
   *
   * @param polymers PolymerNotation
   * @param mapIds Map of old and new Ids
   * @throws NotationException
   */
  private void section1(List<PolymerNotation> polymers, Map<String, String> mapIds) throws NotationException {
    for (PolymerNotation polymer : polymers) {
      if (mapIds.containsKey(polymer.getPolymerID().getID())) {
        /* change id */
        PolymerNotation newpolymer = new PolymerNotation(mapIds.get(polymer.getPolymerID().getID()));
        newpolymer = new PolymerNotation(newpolymer.getPolymerID(), polymer.getPolymerElements());
        helm2notation.addPolymer(newpolymer);
      } else {
        helm2notation.addPolymer(polymer);
      }

    }

  }

  /**
   * method to add ConnectionNotation to the existent
   *
   * @param connections ConnectionNotatoin
   * @param mapIds Map of old and new Ids
   * @throws NotationException
   */
  private void section2(List<ConnectionNotation> connections, Map<String, String> mapIds) throws NotationException {

    for (ConnectionNotation connection : connections) {
      HELMEntity first = connection.getSourceId();
      String idFirst = first.getID();

      HELMEntity second = connection.getTargetId();
      String idSecond = second.getID();

      if (mapIds.containsKey(idFirst)) {
        first = new ConnectionNotation(mapIds.get(idFirst)).getSourceId();
      }

      if (mapIds.containsKey(idSecond)) {
        second = new ConnectionNotation(mapIds.get(idSecond)).getSourceId();
      }

      ConnectionNotation newConnection = new ConnectionNotation(first, second, connection.getSourceUnit(), connection.getrGroupSource(), connection.getTargetUnit(), connection.getrGroupTarget(),
          connection.getAnnotation());

      helm2notation.addConnection(newConnection);
    }

  }

  /**
   * method to add groupings to the existent grouping section
   *
   * @param groupings new GroupingNotations
   * @param mapIds map of old and new Ids
   * @throws NotationException
   */
  private void section3(List<GroupingNotation> groupings, Map<String, String> mapIds) throws NotationException {

    for (GroupingNotation grouping : groupings) {
      GroupEntity groupID = grouping.getGroupID();
      if (mapIds.containsKey(groupID.getID())) {
        groupID = new GroupingNotation(mapIds.get(groupID.getID())).getGroupID();
      }

      String details = grouping.toHELM2().split("\\(")[1].split("\\)")[0];
      details = changeIDs(details, mapIds);
      helm2notation.addGrouping(new GroupingNotation(groupID, details));

    }
  }

  /**
   * method to add annotations to the existent annotation section
   *
   * @param annotations new AnnotationNotations
   * @param mapIds Map of old and new Ids
   */
  private void section4(List<AnnotationNotation> annotations, Map<String, String> mapIds) {
    for (AnnotationNotation annotation : annotations) {
      String notation = annotation.getAnnotation();
      notation = changeIDs(notation, mapIds);
      helm2notation.addAnnotation(new AnnotationNotation(notation));
    }
  }

  /**
   * method to change the ids in a text according to map
   *
   * @param text input text
   * @param mapIds Map of old Id's value and new Id's
   * @return text with changed ids
   */
  private String changeIDs(String text, Map<String, String> mapIds) {
    String result = text;
    for (String key : mapIds.keySet()) {
      result = result.replace(key, mapIds.get(key));
    }
    return result;
  }

  /**
   * method to get the total number of MonomerNotationUnits
   *
   * @return number of MonomerNotationUnits
   */
  public int getTotalMonomerCount() {
    int result = 0;
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      result += PolymerUtils.getTotalMonomerCount(polymer);
    }
    return result;
  }

  /**
   * This function replaces smiles in complex notation with temporary ids To Do
   */
  public final void replaceSMILESWithTemporaryIds() {
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      PolymerUtils.replaceSMILES(polymer);
    }

  }

  /**
   * method to check if any of the rna polymers have a modified nucleotide
   *
   * @return true if at least one rna polymer has a modified nucleotide
   * @throws NotationException
   */
  public boolean hasNucleotideModification() throws NotationException {
    for (PolymerNotation polymer : getRNAPolymers()) {
      if (RNAUtils.hasNucleotideModification(polymer)) {
        return true;
      }
    }
    return false;
  }

  /**
   * this method will automatically add base pair info into notation only if it
   * contains two RNA polymer notations and there is no base pairing info
   *
   * @throws NotationException
   * @throws RNAUtilsException
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException
   * @throws org.helm.notation.NotationException
   */
  public void hybridize() throws NotationException, RNAUtilsException, IOException, JDOMException, HELM2HandledException, org.helm.notation.NotationException {
    if (getAllBasePairConnections().isEmpty() && getRNAPolymers().size() == 2) {
      List<ConnectionNotation> connections = RNAUtils.hybridize(getRNAPolymers().get(0), getRNAPolymers().get(1));
      for (ConnectionNotation connection : connections) {
        ChangeObjects.addConnection(connection, getAllConnections().size(), this);
      }
    }
  }

  /**
   * generate formated siRNA sequence with default padding char " " and
   * base-pair char "|"
   *
   * @return string array of formated nucloeotide sequence
   * @throws NotationException
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   */
  public String[] getFormatedSirnaSequences() throws NotationException, RNAUtilsException, HELM2HandledException {
    return getFormatedSirnaSequences(ComplexNotationParser.DEFAULT_PADDING_CHAR, ComplexNotationParser.DEFAULT_BASE_PAIR_CHAR);
  }

  /**
   * @param paddingChar
   * @param basePairChar
   * @return string array of formated nucleotide sequence
   * @throws NotationException
   * @throws RNAUtilsException
   * @throws HELM2HandledException
   */
  public String[] getFormatedSirnaSequences(String paddingChar, String basePairChar) throws NotationException, RNAUtilsException, HELM2HandledException {
    if (null == paddingChar || paddingChar.length() != 1) {
      throw new NotationException(
          "Padding string must be single character");
    }

    if (null == basePairChar || basePairChar.length() != 1) {
      throw new NotationException(
          "Base pair string must be single character");
    }

    List<PolymerNotation> rnaList = getRNAPolymers();
    int count = rnaList.size();
    if (count == 0) {
      return new String[0];
    } else if (count == 1) {
      return new String[] {RNAUtils.getSequence(rnaList.get(0))};
    } else if (count == 2) {
      String rna1Seq = null;
      String rna2Seq = null;
      String rna1Annotation = null;
      String rna2Annotation = null;
      PolymerNotation one = null;
      PolymerNotation two = null;

      for (PolymerNotation node : rnaList) {
        if (node.getPolymerID().getID().equals("RNA1")) {
          rna1Seq = RNAUtils.getSequence(node);
          rna1Annotation = node.getAnnotation();
          one = node;
        } else if (node.getPolymerID().getID().equals("RNA2")) {
          rna2Seq = RNAUtils.getSequence(node);
          two = node;
          rna2Annotation = node.getAnnotation();
        }
      }
      String reverseRna2Seq = RNAUtils.getReverseSequence(two);

      List<ConnectionNotation> connections = getAllBasePairConnections();
      if (null == connections || connections.size() == 0) {
        return new String[] {rna1Seq, rna2Seq};
      } else {
        Map<Integer, Integer> monomerPositionMap = getSirnaMonomerPositionMap(connections);
        Map<Integer, Integer> seqPositionMap = new HashMap<Integer, Integer>();
        Set<Integer> monomerSet = monomerPositionMap.keySet();
        for (Integer key : monomerSet) {
          Integer value = monomerPositionMap.get(key);
          Integer seqKey = new Integer(key.intValue() / 3 + 1);
          Integer seqValue = new Integer(value.intValue() / 3 + 1);
          seqPositionMap.put(seqKey, seqValue);
        }

        Set<Integer> seqSet = seqPositionMap.keySet();
        List<Integer> seqList = new ArrayList<Integer>();
        for (Integer key : seqSet) {
          seqList.add(key);
        }
        Collections.sort(seqList);

        int rna1First = seqList.get(0).intValue();
        int rna2Last = seqPositionMap.get(seqList.get(0)).intValue();
        int rna1Last = seqList.get(seqList.size() - 1).intValue();
        int rna2First = seqPositionMap.get(seqList.get(seqList.size() - 1)).intValue();

        if ((rna1Last - rna1First) != (rna2Last - rna2First)) {
          throw new NotationException(
              "siRNA matching lengths are different");
        }

        int rna1LeftOverhang = rna1First - 1;
        int rna1RightOverhang = rna1Seq.length() - rna1Last;
        int rna2LeftOverhang = rna2Seq.length() - rna2Last;
        int rna2RightOverhang = rna2First - 1;
        StringBuffer[] sbs = new StringBuffer[3];
        for (int i = 0; i < sbs.length; i++) {
          sbs[i] = new StringBuffer();
        }

        if (rna1LeftOverhang >= rna2LeftOverhang) {
          sbs[0].append(rna1Seq);

          for (int i = 0; i < rna1LeftOverhang; i++) {
            sbs[1].append(paddingChar);
          }
          for (int i = rna1First; i < (rna1Last + 1); i++) {
            Integer in = new Integer(i);
            if (seqPositionMap.containsKey(in)) {
              sbs[1].append(basePairChar);
            } else {
              sbs[1].append(paddingChar);
            }
          }

          for (int i = 0; i < rna1LeftOverhang - rna2LeftOverhang; i++) {
            sbs[2].append(paddingChar);
          }
          sbs[2].append(reverseRna2Seq);
        } else {
          for (int i = 0; i < rna2LeftOverhang - rna1LeftOverhang; i++) {
            sbs[0].append(paddingChar);
          }
          sbs[0].append(rna1Seq);

          for (int i = 0; i < rna2LeftOverhang; i++) {
            sbs[1].append(paddingChar);
          }
          for (int i = rna1First; i < (rna1Last + 1); i++) {
            Integer in = new Integer(i);
            if (seqPositionMap.containsKey(in)) {
              sbs[1].append(basePairChar);
            } else {
              sbs[1].append(paddingChar);
            }
          }

          sbs[2].append(reverseRna2Seq);
        }

        if (rna1RightOverhang >= rna2RightOverhang) {
          for (int i = 0; i < rna1RightOverhang; i++) {
            sbs[1].append(paddingChar);
          }

          for (int i = 0; i < rna1RightOverhang - rna2RightOverhang; i++) {
            sbs[2].append(paddingChar);
          }
        } else {
          for (int i = 0; i < rna2RightOverhang - rna1RightOverhang; i++) {
            sbs[0].append(paddingChar);
          }

          for (int i = 0; i < rna2RightOverhang - rna1RightOverhang; i++) {
            sbs[1].append(paddingChar);
          }
        }

        if ((rna1Annotation != null && rna1Annotation.equalsIgnoreCase("AS"))
            || (rna2Annotation != null && rna2Annotation.equalsIgnoreCase("SS"))) {
          return new String[] {reverseString(sbs[2].toString()),
              reverseString(sbs[1].toString()),
              reverseString(sbs[0].toString())};
        } else {
          return new String[] {sbs[0].toString(), sbs[1].toString(),
              sbs[2].toString()};
        }
      }

    } else {
      throw new NotationException(
          "Structure contains more than two RNA sequences");
    }

  }

  /**
   * @param connections
   * @return
   * @throws NotationException
   */
  private Map<Integer, Integer> getSirnaMonomerPositionMap(List<ConnectionNotation> connections) throws NotationException {
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    for (ConnectionNotation connection : connections) {

      String source = connection.getSourceId().getID();
      String target = connection.getTargetId().getID();
      Integer int1 = new Integer(connection.getSourceUnit());
      Integer int2 = new Integer(connection.getTargetUnit());

      if (source.equals("RNA1") && target.equals("RNA2")) {
        map.put(int1, int2);
      } else if (source.equals("RNA2") && target.equals("RNA1")) {
        map.put(int2, int1);
      } else {
        throw new NotationException(
            "Structure contains more than two RNA sequences");
      }
    }
    return map;
  }

  /**
   * method to reverse a String
   * 
   * @param source
   * @return reversed String
   */
  private static String reverseString(String source) {
    int i;
    int len = source.length();
    StringBuffer dest = new StringBuffer();

    for (i = (len - 1); i >= 0; i--) {
      dest.append(source.charAt(i));
    }
    return dest.toString();
  }

  /**
   * decompose the HELM2 into smaller HELM2 objects
   *
   * @param polymers list of PolymerNotations
   * @param connection: list contains only selfcycle connections
   * @return list of ContainerHELM2 objects
   */
  protected List<ContainerHELM2> decompose() {
    List<ContainerHELM2> list = new ArrayList<ContainerHELM2>();
    List<ConnectionNotation> allselfConnections = getAllSelfCycleConnections(getAllConnections());
    for (PolymerNotation polymer : getAllPolymers()) {
      HELM2Notation helm2notation = new HELM2Notation();
      helm2notation.addPolymer(polymer);
      List<ConnectionNotation> selfConnections = getSelfCycleConnections(polymer.getPolymerID().getID(), allselfConnections);
      for (ConnectionNotation selfConnection : selfConnections) {
        helm2notation.addConnection(selfConnection);
      }
      list.add(new ContainerHELM2(helm2notation, new InterConnections()));

    }

    return list;
  }

  /**
   * method to get all self-cycle Connections
   *
   * @param connections list of ConnectionNotation
   * @return list of all self-cycle Connections
   */
  private static List<ConnectionNotation> getAllSelfCycleConnections(List<ConnectionNotation> connections) {
    List<ConnectionNotation> listSelfCycle = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : connections) {
      if ((connection.getTargetId().getID().equals(connection.getSourceId().getID()))) {
        listSelfCycle.add(connection);
      }
    }
    return listSelfCycle;
  }

  /**
   * method to get for one polymer all self-cycle ConnectionNotations
   *
   * @param id polymer id
   * @param connections list of ConnectionNotation
   * @return list of all self-cycle connections
   */
  private static List<ConnectionNotation> getSelfCycleConnections(String id, List<ConnectionNotation> connections) {
    List<ConnectionNotation> list = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : connections) {
      if (connection.getSourceId().getID().equals(id)) {
        list.add(connection);
      }
    }
    return list;
  }
}
