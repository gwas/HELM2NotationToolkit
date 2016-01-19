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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.helm.notation.MonomerException;
import org.helm.notation2.exception.ConnectionNotationException;
import org.helm.notation2.exception.GroupingNotationException;
import org.helm.notation2.exception.PolymerIDsException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.grouping.GroupingNotation;
import org.helm.notation2.parser.notation.polymer.BlobEntity;
import org.helm.notation2.parser.notation.polymer.ChemEntity;
import org.helm.notation2.parser.notation.polymer.GroupEntity;
import org.helm.notation2.parser.notation.polymer.HELMEntity;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
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

  public HELM2Notation getHELM2Notation() {
    return helm2notation;
  }

  public InterConnections getInterconnection() {
    return interconnection;
  }

  /**
   * method to generate a JSON-Object from the NotationContainer
   * 
   * @return NotationContainer in JSON-Format
   */
  protected String toJSON() {
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
   * method to validate all notations objects
   * 
   * @throws ConnectionNotationException
   * @throws GroupingNotationException
   * @throws MonomerException
   * @throws PolymerIDsException
   */
  public void validate() throws PolymerIDsException, MonomerException, GroupingNotationException, ConnectionNotationException {
    Validation.validateNotationObjects(this);
  }

  /**
   * method to get all polymers of this object
   * 
   * @return list of polymer notation
   */
  public List<PolymerNotation> getAllPolymers() {
    return helm2notation.getListOfPolymers();
  }

  /**
   * method to get all connections of this object
   * 
   * @return list of connection notation
   */
  public List<ConnectionNotation> getAllConnections() {
    return helm2notation.getListOfConnections();
  }

  /**
   * method to get all edge connections of this object
   * 
   * @return all edge connections of this object
   */
  public List<ConnectionNotation> getAllEdgeConnections() {
    List<ConnectionNotation> listEdgeConnection = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : helm2notation.getListOfConnections()) {
      if (!(connection.getrGroupSource().equals("pair"))) {
        listEdgeConnection.add(connection);
      }
    }
    return listEdgeConnection;
  }

  /**
   * method to get all base pair connections of this object
   * 
   * @return all base pair connections of this object
   */
  public List<ConnectionNotation> getAllBasePairConnections() {
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
  public List<AnnotationNotation> getAllAnnotations() {
    return helm2notation.getListOfAnnotations();
  }

  /**
   * method to get all rna polymers of this object
   * 
   * @return list of rna polymers
   */
  public List<PolymerNotation> getRNAPolymers() {
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
  public List<PolymerNotation> getPeptidePolymers() {
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
  public List<PolymerNotation> getCHEMPolymers() {
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
  public List<PolymerNotation> getBLOBPolymers() {
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
  public void addHELM2notation(HELM2Notation newHELM2Notation) throws NotationException {
    Map<String, String> mapIds = generateMapChangeIds(newHELM2Notation.getPolymerAndGroupingIDs());
    /* method to merge the new HELM2Notation into the existing one */

    /* section 1 */
    /* id's müssen angepasst werden */
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
    for(String oldID : oldIds){
      mapOldIds.put(oldID, "");
    }
    for (String newId : newIDs) {
      if(mapOldIds.containsKey(newId)){
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

  public void getNotationByReplacingSMILES() {
    /* Go for every MonomerNotation */
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      for (MonomerNotation monomerNotation : polymer.getPolymerElements().getListOfElements()) {
        System.out.println(monomerNotation);
      }
    }

  }
}


