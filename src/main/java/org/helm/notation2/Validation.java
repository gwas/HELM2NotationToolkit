/**
 * *****************************************************************************
 * /**
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.ChemicalToolKit;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation2.exception.AttachmentException;
import org.helm.notation2.exception.ConnectionNotationException;
import org.helm.notation2.exception.GroupingNotationException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.PolymerIDsException;
import org.helm.notation2.parser.groupingsection.BetweenGroupingParser;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.ValidationMethod;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.grouping.GroupingElement;
import org.helm.notation2.parser.notation.grouping.GroupingNotation;
import org.helm.notation2.parser.notation.polymer.HELMEntity;
import org.helm.notation2.parser.notation.polymer.GroupEntity;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroup;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroupElement;
import org.helm.notation2.parser.notation.polymer.MonomerNotationList;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Validation
 * 
 * @author hecht
 */
public class Validation {

  private static final Logger LOG =
      LoggerFactory.getLogger(BetweenGroupingParser.class);



  /**
   * method to check if the generated notation objects by the parser are correct
   * the polymer ids have to be unique; all monomers have to be valid; all used
   * polymer ids in the grouping section have to be there; all connections have
   * to be valid
   * 
   * @param ContainerHELM2
   * @throws NotationException
   * @throws JDOMException
   * @throws IOException
   * @throws MonomerException
   * @throws GroupingNotationException
   * @throws ConnectionNotationException
   * @throws PolymerIDsException
   * @throws AttachmentException
   * @throws org.helm.notation2.parser.ExceptionParser.NotationException
   * @throws org.jdom.JDOMException
   * @throws HELM2HandledException
   * @throws CTKException
   */
  public static void validateNotationObjects(ContainerHELM2 containerhelm2)
      throws MonomerException, IOException, JDOMException,
      GroupingNotationException, ConnectionNotationException,
      PolymerIDsException, AttachmentException,
      org.helm.notation2.parser.exceptionparser.NotationException,
      org.jdom.JDOMException, HELM2HandledException, CTKException, NotationException {


    /*all polymer ids have to be unique*/
    if (!validateUniquePolymerIDs(containerhelm2)) {
      LOG.info("Polymer ids have to be unique");
      throw new PolymerIDsException("Polymer IDs have to be unique");
    }
    /* Validation of Monomers */
    if (!validateMonomers(MethodsForContainerHELM2.getListOfMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers()))) {
      LOG.info("Monomers have to be valid");
      throw new MonomerException("Monomers have to be valid");
    }
    /* validate the grouping section */
    if (!validateGrouping(containerhelm2)) {
      LOG.info("Group information is not valid");
      throw new GroupingNotationException("Group notation is not valid");
    }
    /* validate the connection */
    if (!validateConnections(containerhelm2)) {
      LOG.info("Connection information is not valid");
      throw new ConnectionNotationException("Connection notation is not valid");
    }
  }

  /**
   * method to validate a list of MonomerNotation objects
   * 
   * @param mon List of MonomerNotation objects
   * @return
   * @throws IOException
   * @throws MonomerException
   * @throws JDOMException
   * @throws NotationException
   * @throws CTKException
   */
  protected static boolean validateMonomers(List<MonomerNotation> mon) throws MonomerException, IOException, JDOMException, CTKException, NotationException {
    List<MonomerNotation> monomerNotations = mon;
    for (MonomerNotation monomerNotation : monomerNotations) {
      if (!(isMonomerValid(monomerNotation.getID(), monomerNotation.getType()))) {
        return false;
      }
    }
    return true;
  }

  /**
   * method to valid all existent connections in the Notation objects
   * 
   * @param containerhelm2
   * @return
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   * @throws AttachmentException
   * @throws PolymerIDsException
   * @throws org.helm.notation2.parser.ExceptionParser.NotationException
   * @throws org.jdom.JDOMException
   * @throws NotationException
   * @throws HELM2HandledException
   * @throws CTKException
   * @throws org.jdom.JDOMException
   * @throws org.helm.notation2.parser.exceptionparser.NotationException
   */
  protected static boolean validateConnections(ContainerHELM2 containerhelm2) throws PolymerIDsException, MonomerException, IOException, JDOMException, HELM2HandledException, CTKException,
      AttachmentException, org.helm.notation2.parser.exceptionparser.NotationException, org.jdom.JDOMException, NotationException {

    List<ConnectionNotation> listConnections = containerhelm2.getHELM2Notation().getListOfConnections();
    List<String> listPolymerIDs = containerhelm2.getHELM2Notation().getPolymerAndGroupingIDs();

    /* Hash-Map to save only specific InterConnections */
    InterConnections interconnection = containerhelm2.getInterconnection();
    boolean specific = true;
    /* check for each single connection */
    for (ConnectionNotation connection : listConnections) {

      /* check polymer ids */
      checkPolymerIDSConnection(connection, listPolymerIDs);

      /*check for unspecific interaction*/
      if (connection.getSourceId() instanceof GroupEntity || connection.getTargetId() instanceof GroupEntity) {
        specific = false;
      }

      /* check Monomers:-> can be number */
      PolymerNotation source = containerhelm2.getHELM2Notation().getPolymerNotation(connection.getSourceId().getID());
      String sourceUnit = connection.getSourceUnit();
      PolymerNotation target = containerhelm2.getHELM2Notation().getPolymerNotation(connection.getTargetId().getID());
      String targetUnit = connection.getTargetUnit();

      /* check for specific interactions */
      if (isConnectionSpecific(connection) && specific) {
        /*
         * interaction seems to be specific: it is given in number -> place of
         * monomer
         */
        /* Get Monomers */
        specific = true;
        int occurenceOne = Integer.parseInt(sourceUnit);
        int occurenceTwo = Integer.parseInt(targetUnit);

        /*
         * if the monomers are a group or a list of monomers -> is it no more
         * specific
         */
        /* can the two form a connection */
        List<Monomer> listMonomersOne = getAllMonomers(source.getMonomerNotation(occurenceOne));
        List<Monomer> listMonomersTwo = getAllMonomers(target.getMonomerNotation(occurenceTwo));


        /* check each single Attachment */
        checkAttachment(listMonomersOne, listMonomersTwo, connection, containerhelm2, interconnection, specific);
        }

      /*Unspecific Interaction*/
        else {
        List<Integer> listMonomerOccurencesOne = getMonomer(sourceUnit, connection.getSourceId(), containerhelm2);
        List<Integer> listMonomerOccurencesTwo = getMonomer(targetUnit, connection.getTargetId(), containerhelm2);
        /* ? - section has to be included */
        if (listMonomerOccurencesOne.isEmpty()) {
          for (Integer occurenceTwo : listMonomerOccurencesTwo) {
            List<Monomer> listMonomersTwo = getAllMonomers(target.getMonomerNotation(occurenceTwo));
            checkSingleAttachment(listMonomersTwo, connection.getrGroupTarget(), containerhelm2, connection, interconnection, connection.getTargetId().getID());
          }
        }
        for (Integer occurenceOne : listMonomerOccurencesOne) {
          /* get Monomers */
          List<Monomer> listMonomersOne = getAllMonomers(source.getMonomerNotation(occurenceOne));
          checkSingleAttachment(listMonomersOne, connection.getrGroupSource(), containerhelm2, connection, interconnection, connection.getSourceId().getID());
          /* check single attachment */
          for (Integer occurenceTwo : listMonomerOccurencesTwo) {
            List<Monomer> listMonomersTwo = getAllMonomers(target.getMonomerNotation(occurenceTwo));
            checkSingleAttachment(listMonomersTwo, connection.getrGroupTarget(), containerhelm2, connection, interconnection, connection.getTargetId().getID());
            checkAttachment(listMonomersOne, listMonomersTwo, connection, containerhelm2, interconnection, false);

          }

        }
      
    }

    
  }
    return true;
  }


  /**
   * method to check if the described connection is specific
   * 
   * @param connectionNotation
   * @return
   */
  private static boolean isConnectionSpecific(ConnectionNotation connectionNotation) {
    String connection = connectionNotation.getSourceUnit() + ":" + connectionNotation.getrGroupSource() + "-" + connectionNotation.getTargetUnit() + ":" + connectionNotation.getrGroupTarget();
    /* check for specific interaction */
    if (connection.matches("\\d+:R\\d-\\d+:R\\d|\\d+:pair-\\d+:pair")) {
      return true;
    }
    return false;
  }

  /**
   * method to get
   * 
   * @param sourceUnit
   * @param id
   * @param containerhelm2
   * @return
   * @throws NotationException
   * @throws JDOMException
   * @throws org.jdom.JDOMException
   * @throws IOException
   * @throws MonomerException
   * @throws org.helm.notation2.parser.ExceptionParser.NotationException
   * @throws AttachmentException
   */
  private static List<Integer> getMonomer(String sourceUnit, HELMEntity e, ContainerHELM2 containerhelm2) throws org.helm.notation2.parser.exceptionparser.NotationException, MonomerException,
      IOException, org.jdom.JDOMException, JDOMException, NotationException, AttachmentException {
    List<Integer> occurences = new ArrayList<Integer>();
    
    /* The monomer's position in the polymer is specified */
    try{
      occurences.add(Integer.parseInt(sourceUnit));
      return occurences;  
    }
    /*
     * The monomer is described through a group/mixture or a monomer unit e.g.
     * specific amino acid, the monomer can also be unknown
     */
    catch (NumberFormatException ex) {
      MonomerNotation mon = ValidationMethod.decideWhichMonomerNotation(sourceUnit, e.getType());
      /* it is only one monomer e.g. C */
      if (mon instanceof MonomerNotationUnit) {
        PolymerNotation polymerNotation = containerhelm2.getHELM2Notation().getPolymerNotation(e.getID());
        /* monomer can also be unknown */
        if (sourceUnit.equals("?")) {
          return occurences;
        }
        for (int i = 0; i < polymerNotation.getPolymerElements().getListOfElements().size(); i++) {
          if (polymerNotation.getPolymerElements().getListOfElements().get(i).getID().equals(sourceUnit)) {
            occurences.add(i + 1);
          }
        }

        /* the specified monomer does not exist in the polymer */
        if (occurences.isEmpty()) {
          throw new AttachmentException("Monomer is not there");
        }
    }

      /* second: group (mixture or or) or list */
    else if (mon instanceof MonomerNotationGroup || mon instanceof MonomerNotationList) {
      PolymerNotation polymerNotation =
          containerhelm2.getHELM2Notation().getPolymerNotation(e.getID());
        Map<String, String> elements = new HashMap<String, String>();
        for (MonomerNotationGroupElement groupElement : ((MonomerNotationGroup) mon).getListOfElements()) {
          elements.put(groupElement.getMonomerNotation().getID(), "");
      }

        for (int i = 0; i < polymerNotation.getPolymerElements().getListOfElements().size(); i++) {
        if (elements.containsKey(polymerNotation.getPolymerElements().getListOfElements().get(i).getID())) {
          elements.put(polymerNotation.getPolymerElements().getListOfElements().get(i).getID(), "1");
          occurences.add(i + 1);
          }
        }
      if (occurences.size() < elements.size() || elements.containsValue("")) {
        throw new AttachmentException("Not all Monomers are there");
      }
    }
    return occurences;
    }
  }


  /**
   * method to validate every GroupNotation from the Notation objects
   * 
   * @param containerhelm2
   * @return
   */
  protected static boolean validateGrouping(ContainerHELM2 containerhelm2) {
    List<GroupingNotation> listGroupings =
        containerhelm2.getHELM2Notation().getListOfGroupings();
    List<String> listPolymerIDs =
        containerhelm2.getHELM2Notation().getPolymerAndGroupingIDs();
    
    /*validate each group*/
    for (GroupingNotation grouping : listGroupings) {
      /* check for each group element if the polymer id is there */
      for (GroupingElement groupingElement : grouping.getAmbiguity().getListOfElements()) {
        if (!(listPolymerIDs.contains(groupingElement.getID().getID()))) {
          LOG.info("Element of Group: "
              + groupingElement.getID().getID()
              + " does not exist");
          return false;
        }
      }
    }
    return true;
  }

  /**
   * method to check if all existent polymer ids are unique
   * 
   * @throws PolymerIDsException
   * 
   */
  protected static boolean validateUniquePolymerIDs(ContainerHELM2 containerhelm2)
      throws PolymerIDsException {
    List<String> listPolymerIDs =
        containerhelm2.getHELM2Notation().getPolymerAndGroupingIDs();
    Map<String, String> uniqueId = new HashMap<String, String>();
    for (String polymerID : listPolymerIDs) {
      uniqueId.put(polymerID, "");
    }
    if (listPolymerIDs.size() > uniqueId.size()) {
      throw new PolymerIDsException("Polymer node IDs are not unique");
    }

    return true;
  }

  /**
   * method to get the number of single monomernotation -> toDo
   * 
   * @param helm2notation
   * @return
   */
  protected static int getMonomerCountAll(HELM2Notation helm2notation) {
    List<PolymerNotation> listPolymers = helm2notation.getListOfPolymers();
    int count = 0;
    for (PolymerNotation polymer : listPolymers) {
      count += getMonomerCount(polymer);
    }
    return count;
  }

  private static int getMonomerCount(PolymerNotation notation) {
    int count = 0;
    for (MonomerNotation element : notation.getPolymerElements().getListOfElements()) {
      count += getMonomerCountFromMonomerNotation(element);
    }
    return count;
  }

  private static int getMonomerCountFromMonomerNotation(MonomerNotation notation) {
    int multiply;
    try {
      multiply = Integer.parseInt(notation.getCount());
      if (multiply < 1) {
        multiply = 1;
      }
    } catch (NumberFormatException e) {
      multiply = 1;
    }

    if (notation instanceof MonomerNotationGroup) {
      return 1 * multiply;
    }
    if (notation instanceof MonomerNotationList) {
      int count = 0;
      for (MonomerNotationUnit unit : ((MonomerNotationList) notation).getListofMonomerUnits()) {
        count += getMonomerCountFromMonomerNotation(unit);
      }
      return count * multiply;
    }

    if (notation instanceof MonomerNotationUnitRNA) {
      int count = 0;
      for (MonomerNotationUnit unit : ((MonomerNotationUnitRNA) notation).getContents()) {
        count += getMonomerCountFromMonomerNotation(unit);
      }
      return count * multiply;
    }
    return 1 * multiply;
  }

  /**
   * method to check if the given polymer id exists in the given list of polymer
   * ids
   * 
   * @param str
   * @param listPolymerIDs
   * @return
   * @throws PolymerIDsException
   */
  protected static boolean checkExistenceOfPolymerID(String str, List<String> listPolymerIDs) throws PolymerIDsException {
    if (listPolymerIDs.contains(str)) {
      return true;
    }
    LOG.info("Polymer Id does not exist");
    throw new PolymerIDsException("Polymer ID does not exist");
  }

  /**
   * method to check for one given monomer the existence in the given type
   * 
   * @param str
   * @param type
   * @return
   * @throws IOException
   * @throws MonomerException
   * @throws JDOMException
   * @throws NotationException
   * @throws CTKException
   */
  protected static boolean isMonomerValid(String str, String type) throws MonomerException, IOException, JDOMException, CTKException, NotationException {
    MonomerFactory monomerFactory = MonomerFactory.getInstance();
    /* Search in Database */
    MonomerStore monomerStore = monomerFactory.getMonomerStore();
    if (monomerStore.hasMonomer(type, str)) {
      LOG.info("Monomer is located in the database: " + str);
      return true;
    }

    else if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']' &&
        monomerStore.hasMonomer(type, str.substring(1, str.length() - 1))) {
      LOG.info("Monomer is located in the database: " + str);
      return true;
    }

    /* polymer type is Blob: accept all */
    else if (type.equals("BLOB")) {
      LOG.info("Blob's Monomer Type: " + str);
      return true;
    }

    /* new unknown monomer for peptide */
    else if (type.equals("PEPTIDE") && str.equals("X")) {
      LOG.info("Unknown monomer type for peptide: " + str);
      return true;
    }

    /* new unknown monomer for peptide */
    else if (type.equals("RNA") && str.equals("N")) {
      LOG.info("Unknown monomer type for rna: " + str);
      return true;
    }

    /* new unknown types */
    else if (str.equals("?") || str.equals("_")) {
      LOG.info("Unknown types: " + str);
      return true;
    }

    /* nucleotide */
    else if (type.equals("RNA")) {
      /* change */
      SimpleNotationParser.getMonomerIDList(str, type, monomerStore);
      LOG.info("Nucleotide type for RNA: " + str);
      return true;
    }

    else {

      /* SMILES Check */
      if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
        str = str.substring(1, str.length() - 1);
      }

      return ChemicalToolKit.getTestINSTANCE("").getManipulator().validateSMILES(str);
    }
  }


  /**
   * method to check for one connection if the two polymer ids exist
   * 
   * @param not
   * @param listPolymerIDs
   * @return
   * @throws PolymerIDsException
   */
  private static boolean checkPolymerIDSConnection(ConnectionNotation not, List<String> listPolymerIDs) throws PolymerIDsException {
    /* the polymer ids have to be there */
    return checkExistenceOfPolymerID(not.getSourceId().getID(), listPolymerIDs) && checkExistenceOfPolymerID(not.getTargetId().getID(), listPolymerIDs);

  }

  /**
   * method to get for one MonomerNotation all valid contained monomers
   * 
   * @param not
   * @return
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException
   * @throws CTKException
   */
  protected static List<Monomer> getAllMonomers(MonomerNotation not) throws MonomerException, IOException, JDOMException, HELM2HandledException, CTKException {
    List<Monomer> monomers = new ArrayList<Monomer>();


    MonomerFactory monomerFactory = MonomerFactory.getInstance();
    MonomerStore monomerStore = monomerFactory.getMonomerStore();
    if (not instanceof MonomerNotationUnitRNA){
      monomers.addAll(getMonomersRNA((MonomerNotationUnitRNA) not, monomerStore));

    }
      else if (not instanceof MonomerNotationUnit){
      String id = not.getID();
      if (id.startsWith("[") && id.endsWith("]")) {
        id = id.substring(1, id.length() - 1);
      }
      monomers.add(MethodsForContainerHELM2.getMonomer(not.getType(), id));
    }
 else if (not instanceof MonomerNotationGroup) {
      for (MonomerNotationGroupElement groupElement : ((MonomerNotationGroup) not).getListOfElements()) {
        String id = groupElement.getMonomerNotation().getID();
        if (id.startsWith("[") && id.endsWith("]")) {
          id = id.substring(1, id.length() - 1);
        }
        monomers.add(MethodsForContainerHELM2.getMonomer(not.getType(), id));
      }
    } else if (not instanceof MonomerNotationList) {
      for (MonomerNotationUnit listElement : ((MonomerNotationList) not).getListofMonomerUnits()) {
        if (listElement instanceof MonomerNotationUnitRNA) {
          monomers.addAll(getMonomersRNA(((MonomerNotationUnitRNA) listElement), monomerStore));
        }
 else {
          String id = listElement.getID();
          if (id.startsWith("[") && id.endsWith("]")) {
            id = id.substring(1, id.length() - 1);
          }
          monomers.add(MethodsForContainerHELM2.getMonomer(not.getType(), id));
        }
      }

    }

    return monomers;
  }

  private static boolean checkAttachmentPoint(Monomer mon, String str) throws AttachmentException {
    if (!(mon.getAttachmentListString().contains(str))) {
      if (!(str.equals("?"))) {
        LOG.info("Attachment point for source is not there");
        throw new AttachmentException(
            "Attachment point for source is not there: "
                + str);
      }
    }
    return true;
  }



  /**
   * method to check if the described attachment is valid
   * 
   * @param firsts
   * @param seconds
   * @param not
   * @param containerhelm2
   * @param interconnection
   * @param spec
   * @return
   * @throws AttachmentException
   */
  private static boolean checkAttachment(List<Monomer> listMonomersOne, List<Monomer> listMonomersTwo, ConnectionNotation not, ContainerHELM2 containerhelm2, InterConnections interconnection,
      boolean spec)
      throws AttachmentException {
    boolean specific = spec;
    if (listMonomersOne.size() > 1 || listMonomersTwo.size() > 1) {
      specific = false;
    }
    for (Monomer monomerOne : listMonomersOne) {
      for (Monomer monomerTwo : listMonomersTwo) {
        /* Rna-Basepair-hydrogen bonds */
        if (monomerOne.getPolymerType().equals("RNA")
            && monomerTwo.getPolymerType().equals("RNA")
            && not.getrGroupSource().equals("pair")
            && not.getrGroupTarget().equals("pair")) {
          LOG.info("RNA strand connection");

          if (!(monomerOne.getMonomerType().equals("Branch")
              | monomerTwo.getMonomerType().equals("Branch"))) {
            LOG.info("RNA strand connection is not valid");
            throw new AttachmentException(
                "RNA strand connection is not valid");
          }

          /* is the attachment point already occupied by another monomer? */
          String detailsource = not.getSourceUnit() + "$"
              + not.getrGroupSource();
          String detailtarget = not.getTargetUnit() + "$" + not.getrGroupTarget();

          /* Is the attachment point already occupied by another monomer */
          /* Intra connections */
          if (containerhelm2.getHELM2Notation().getSimplePolymer(not.getSourceId().getID()).getMapIntraConnection().containsKey(detailsource)) {
            throw new AttachmentException(
                "Attachment point is already occupied");
          }
          if (containerhelm2.getHELM2Notation().getSimplePolymer(not.getTargetId().getID()).getMapIntraConnection().containsKey(detailtarget)) {
            throw new AttachmentException(
                "Attachment point is already occupied");
          }





        }

        String detailsource = not.getSourceUnit() + "$"
            + not.getrGroupSource();
        String detailtarget = not.getTargetUnit() + "$" + not.getrGroupTarget();

        /* Inter connections */
        detailsource = not.getSourceId().getID() + "$"
            + detailsource;

        detailtarget = not.getTargetId().getID() + "$"
            + detailtarget;

        /* check */
        if (interconnection.hasKey(detailsource)) {
          throw new AttachmentException(
              "Attachment point is already occupied");
        }

        if (interconnection.hasKey(detailtarget)) {
          throw new AttachmentException(
              "Attachment point is already occupied");
        }
          /* Inter connections */
        detailsource = not.getSourceId().getID() + "$" + not.getSourceUnit() + "$"
            + not.getrGroupSource();
        detailtarget = not.getTargetId().getID() + "$" + not.getTargetUnit() + "$"
            + not.getrGroupTarget();
        

        if (specific) {
          /* save only specific interactions */
          interconnection.addConnection(detailsource, "");
          interconnection.addConnection(detailtarget, "");
        }
          

    
  }
        
      }
    return true;

}

  /**
   * method to check for one attachment point the validation
   * 
   * @param monomers
   * @param rGroup
   * @param containerhelm2
   * @param not
   * @param interconnection
   * @param id
   * @return
   * @throws AttachmentException
   */
  private static boolean checkSingleAttachment(List<Monomer> monomers, String rGroup, ContainerHELM2 containerhelm2, ConnectionNotation not, InterConnections interconnection, String id)
      throws AttachmentException {

    for (Monomer monomer : monomers) {
      /* Are the attachment points there */
      checkAttachmentPoint(monomer, rGroup);

      /* is the attachment point already occupied by another monomer? */
      String detail = not.getSourceUnit() + "$"
          + not.getrGroupSource();

      /* Is the attachment point already occupied by another monomer */
      /* Intra connections */
      if (containerhelm2.getHELM2Notation().getSimplePolymer(id).getMapIntraConnection().containsKey(detail)) {
        throw new AttachmentException(
            "Attachment point is already occupied");
      }

      /* Inter connections */
      detail = id + "$"
          + detail;

      /* check */
      if (interconnection.hasKey(detail)) {
        throw new AttachmentException(
            "Attachment point is already occupied");
      }

    }
    return true;
  }


  private static List<Monomer> getMonomersRNA(MonomerNotationUnitRNA rna, MonomerStore monomerStore) throws HELM2HandledException, MonomerException, IOException, JDOMException, CTKException {
    List<Monomer> monomers = new ArrayList<Monomer>();
    for (MonomerNotationUnit unit : rna.getContents()) {
      String id = unit.getID().replace("[", "");
        id = id.replace("]", "");
      Monomer mon = MethodsForContainerHELM2.getMonomer(rna.getType(), id);
      try {
        String detail = mon.getMonomerType();
        if (detail.equals("Branch")) {
          monomers.add(MethodsForContainerHELM2.getMonomer(rna.getType(), id));
        }
      } catch (NullPointerException e) {
        throw new HELM2HandledException("Functions can't be called for HELM2 objects");
      }
      }
    return monomers;
    }

}

