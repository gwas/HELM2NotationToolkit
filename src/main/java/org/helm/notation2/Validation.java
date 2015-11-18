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
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation.tools.StructureParser;
import org.helm.notation2.Exception.AttachmentException;
import org.helm.notation2.Exception.ConnectionNotationException;
import org.helm.notation2.Exception.GroupingNotationException;
import org.helm.notation2.Exception.HELM2HandledException;
import org.helm.notation2.Exception.PolymerIDsException;
import org.helm.notation2.parser.GroupingSection.BetweenGroupingParser;
import org.helm.notation2.parser.Notation.HELM2Notation;
import org.helm.notation2.parser.Notation.ValidationMethod;
import org.helm.notation2.parser.Notation.Connection.ConnectionNotation;
import org.helm.notation2.parser.Notation.Grouping.GroupingNotation;
import org.helm.notation2.parser.Notation.Polymer.Entity;
import org.helm.notation2.parser.Notation.Polymer.GroupEntity;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationGroup;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationList;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnit;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Validation
 * 
 * @author hecht
 */
public class Validation {

  private static final Logger logger =
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
   */
  public void validateNotationObjects(ContainerHELM2 containerhelm2)
      throws NotationException, MonomerException, IOException, JDOMException,
      GroupingNotationException, ConnectionNotationException,
      PolymerIDsException, AttachmentException,
      org.helm.notation2.parser.ExceptionParser.NotationException,
      org.jdom.JDOMException, HELM2HandledException {


    /*all polymer ids have to be unique*/
    if (!validateUniquePolymerIDs(containerhelm2)) {
      logger.info("Polymer ids have to be unique");
      throw new PolymerIDsException("Polymer IDs have to be unique");
    }
    /* Validation of Monomers */
    if (!validateMonomers(MethodsForContainerHELM2.getListOfMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers()))) {
      logger.info("Monomers have to be valid");
      throw new MonomerException("Monomers have to be valid");
    }
    /* validate the grouping section */
    if (!validateGrouping(containerhelm2)) {
      logger.info("Group information is not valid");
      throw new GroupingNotationException("Group notation is not valid");
    }
    /* validate the connection */
    if (!validateConnections(containerhelm2)) {
      logger.info("Connection information is not valid");
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
   */
  public static boolean validateMonomers(ArrayList<MonomerNotation> mon) throws IOException, MonomerException, JDOMException, NotationException {
    ArrayList<MonomerNotation> monomers = mon;
    for (int i = 0; i < monomers.size(); i++) {
      if (!(isMonomerValid(monomers.get(i).getID(), monomers.get(i).getType()))) {
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
   */
  public static boolean validateConnections(ContainerHELM2 containerhelm2)
      throws MonomerException, IOException, JDOMException, AttachmentException,
      PolymerIDsException,
      org.helm.notation2.parser.ExceptionParser.NotationException,
      org.jdom.JDOMException, NotationException, HELM2HandledException {

    ArrayList<ConnectionNotation> listConnection =
        containerhelm2.getHELM2Notation().getListOfConnections();
    ArrayList<String> listPolymerIDs =
        containerhelm2.getHELM2Notation().getPolymerAndGroupingIDs();

    /* Hash-Map to save only specific InterConnections */
    InterConnections interconnection = containerhelm2.getInterconnection();
    boolean specific = true;
    /* check for each single connection */
    for (int i = 0; i < listConnection.size(); i++) {

      /* check polymer ids */
      checkPolymerIDSConnection(listConnection.get(i), listPolymerIDs);

      /*check for unspecific interaction*/
      if(listConnection.get(i).getSourceId() instanceof GroupEntity || listConnection.get(i).getTargetId() instanceof GroupEntity){
        specific = false;
      }

      /* check Monomers:-> can be number */
      PolymerNotation source = containerhelm2.getHELM2Notation().getPolymerNotation(listConnection.get(i).getSourceId().getID());
      String sourceUnit = listConnection.get(i).getSourceUnit();
      PolymerNotation target = containerhelm2.getHELM2Notation().getPolymerNotation(listConnection.get(i).getTargetId().getID());
      String targetUnit = listConnection.get(i).getTargetUnit();

      /* check for specific interactions */
      if (isConnectionSpecific(listConnection.get(i)) && specific) {
        /*
         * interaction seems to be specific: it is given in number -> place of
         * monomer
         */
        /* Get Monomers */
        specific = true;
        int numberOne = Integer.parseInt(sourceUnit);
        int numberTwo = Integer.parseInt(targetUnit);

        /*
         * if the monomers are a group or a list of monomers -> is it no more
         * specific
         */
        MonomerNotation first = source.getMonomer(numberOne);
        MonomerNotation second = target.getMonomer(numberTwo);

        /* can the two form a connection */
        /**/
        ArrayList<Monomer> firsts = getAllMonomers(first);
        ArrayList<Monomer> seconds = getAllMonomers(second);


        /* check each single Attachment */
        checkAttachment(firsts, seconds, listConnection.get(i), containerhelm2, interconnection, specific);
        }
      /*Unspecific Interaction*/
        else {
        ArrayList<Integer> numberOne = getMonomer(sourceUnit,listConnection.get(i).getSourceId(),containerhelm2);
        ArrayList<Integer> numberTwo = getMonomer(targetUnit, listConnection.get(i).getTargetId(), containerhelm2);
        /* ? - section has to be included */
        if (numberOne.isEmpty()) {
          for (int y = 0; y < numberTwo.size(); y++) {
            MonomerNotation second = target.getMonomer(numberTwo.get(y));
            ArrayList<Monomer> seconds = getAllMonomers(second);
            checkSingleAttachment(seconds, listConnection.get(i).getrGroupTarget(), containerhelm2, listConnection.get(i), interconnection, listConnection.get(i).getTargetId().getID());
          }
        }
        for (int x = 0; x < numberOne.size(); x++) {
          /* get Monomers */
          MonomerNotation first = source.getMonomer(numberOne.get(x));
          ArrayList<Monomer> firsts = getAllMonomers(first);
          checkSingleAttachment(firsts, listConnection.get(i).getrGroupSource(), containerhelm2, listConnection.get(i), interconnection, listConnection.get(i).getSourceId().getID());
          /* check single attachment */
          for (int y = 0; y < numberTwo.size(); y++) {
            MonomerNotation second = target.getMonomer(numberTwo.get(y));
            ArrayList<Monomer> seconds = getAllMonomers(second);
            checkSingleAttachment(seconds, listConnection.get(i).getrGroupTarget(), containerhelm2, listConnection.get(i), interconnection, listConnection.get(i).getTargetId().getID());
            checkAttachment(firsts, seconds, listConnection.get(i), containerhelm2, interconnection, false);

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
  private static ArrayList<Integer> getMonomer(String sourceUnit, Entity e, ContainerHELM2 containerhelm2) throws org.helm.notation2.parser.ExceptionParser.NotationException, MonomerException,
      IOException, org.jdom.JDOMException, JDOMException, NotationException, AttachmentException {
    ArrayList<Integer> occurences = new ArrayList<Integer>();
    
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
      HashMap<String,String> elements = new HashMap<String, String>();
        for (int i = 0; i < ((MonomerNotationGroup) mon).getListOfElements().size(); i++) {
          elements.put(((MonomerNotationGroup) mon).getListOfElements().get(i).getMonomer().getID(), "");
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
  public static boolean validateGrouping(ContainerHELM2 containerhelm2) {
    ArrayList<GroupingNotation> listGroupings =
        containerhelm2.getHELM2Notation().getListOfGroupings();
    ArrayList<String> listPolymerIDs =
        containerhelm2.getHELM2Notation().getPolymerAndGroupingIDs();
    
    /*validate each group*/
    for (int i = 0; i < listGroupings.size(); i++) {
      /* check for each group element if the polymer id is there */
      for (int j = 0; j < listGroupings.get(i).getAmbiguity().getListOfElements().size(); j ++){
        if (!(listPolymerIDs.contains(listGroupings.get(i).getAmbiguity().getListOfElements().get(j).getID().getID()))) {
          logger.info("Element of Group: "
              + listGroupings.get(i).getAmbiguity().getListOfElements().get(j).getID().getID()
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
   * @throws NotationException
   * 
   */
  public static boolean validateUniquePolymerIDs(ContainerHELM2 containerhelm2)
      throws NotationException {
    ArrayList<String> listPolymerIDs =
        containerhelm2.getHELM2Notation().getPolymerAndGroupingIDs();
    Map<String, String> uniqueId = new HashMap<String, String>();
    for (int i = 0; i < listPolymerIDs.size(); i++) {
      String str = listPolymerIDs.get(i);
      uniqueId.put(str, "");
    }
    if (listPolymerIDs.size() > uniqueId.size()) {
      throw new NotationException("Polymer node IDs are not unique");
    }

    return true;
  }

  /**
   * method to get the number of single monomernotation -> size of list
   * 
   * @param helm2notation
   * @return
   */
  public static int getMonomerCount(HELM2Notation helm2notation) {
    ArrayList<PolymerNotation> listPolymers = helm2notation.getListOfPolymers();
    int count = 0;
    for (int i = 0; i < listPolymers.size(); i++) {
      count += listPolymers.get(i).getPolymerElements().getListOfElements().size();
    }
    return count;
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
  public static boolean checkExistenceOfPolymerID(String str, ArrayList<String> listPolymerIDs) throws PolymerIDsException {
    if (listPolymerIDs.contains(str)) {
      return true;
    }
    logger.info("Polymer Id does not exist");
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
   */
  public static boolean isMonomerValid(String str, String type) throws IOException, MonomerException, JDOMException, NotationException {
    MonomerFactory monomerFactory = MonomerFactory.getInstance();
    /* Search in Database */
    MonomerStore monomerStore = monomerFactory.getMonomerStore();
    if (monomerStore.hasMonomer(type, str)) {
      logger.info("Monomer is located in the database: " + str);
      return true;
    }

    else if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']' &&
        monomerStore.hasMonomer(type, str.substring(1, str.length() - 1))) {
      logger.info("Monomer is located in the database: " + str);
      return true;
    }

    /* polymer type is Blob: accept all */
    else if (type.equals("BLOB")) {
      logger.info("Blob's Monomer Type: " + str);
      return true;
    }

    /* new unknown monomer for peptide */
    else if (type.equals("PEPTIDE") && str.equals("X")) {
      logger.info("Unknown monomer type for peptide: " + str);
      return true;
    }

    /* new unknown monomer for peptide */
    else if (type.equals("RNA") && str.equals("N")) {
      logger.info("Unknown monomer type for rna: " + str);
      return true;
    }

    /* new unknown types */
    else if (str.equals("?") || str.equals("_")) {
      logger.info("Unknown types: " + str);
      return true;
    }

    /* nucleotide */
    else if (type.equals("RNA")) {
      SimpleNotationParser.getMonomerIDList(str, type, monomerStore);
      logger.info("Nucleotide type for RNA: " + str);
      return true;
    }

    else {

      /* SMILES Check */
      if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
        str = str.substring(1, str.length() - 1);
      }

      return StructureParser.validateSmiles(str);

    }
  }


  /**
   * method to check for one connection if the two polymer ids exist
   * 
   * @param not
   * @param listIDs
   * @return
   * @throws PolymerIDsException
   */
  private static boolean checkPolymerIDSConnection(ConnectionNotation not, ArrayList<String> listIDs) throws PolymerIDsException {
    /* the polymer ids have to be there */
    return checkExistenceOfPolymerID(not.getSourceId().getID(), listIDs) && checkExistenceOfPolymerID(not.getTargetId().getID(), listIDs);

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
   */
  public static ArrayList<Monomer> getAllMonomers(MonomerNotation not) throws MonomerException, IOException, JDOMException, HELM2HandledException {
    ArrayList<Monomer> monomers = new ArrayList<Monomer>();


    MonomerFactory monomerFactory = MonomerFactory.getInstance();
    MonomerStore monomerStore = monomerFactory.getMonomerStore();
    if (not instanceof MonomerNotationUnitRNA){
      monomers.addAll(getMonomersRNA((MonomerNotationUnitRNA) not, monomerStore));

    }
      else if (not instanceof MonomerNotationUnit){
      String id = not.getID().replace("[", "");
      id = id.replace("]", "");
      monomers.add(monomerStore.getMonomer(not.getType(), id));
    }
 else if (not instanceof MonomerNotationGroup) {
      for (int i = 0; i < ((MonomerNotationGroup) not).getListOfElements().size(); i++) {
        String id = ((MonomerNotationGroup) not).getListOfElements().get(i).getMonomer().getID().replace("[", "");
        id = id.replace("]", "");
        monomers.add(monomerStore.getMonomer(not.getType(), id));
      }
    } else if (not instanceof MonomerNotationList) {
      for (int i = 0; i < ((MonomerNotationList) not).getListofMonomerUnits().size(); i++) {
        if (((MonomerNotationList) not).getListofMonomerUnits().get(i) instanceof MonomerNotationUnitRNA) {
          monomers.addAll(getMonomersRNA(((MonomerNotationUnitRNA) ((MonomerNotationList) not).getListofMonomerUnits().get(i)), monomerStore));
        }
 else {
          String id = ((MonomerNotationList) not).getListofMonomerUnits().get(i).getID().replace("[", "");
          id = id.replace("]", "");
          monomers.add(monomerStore.getMonomer(not.getType(), id));
        }
      }

    }

    return monomers;
  }

  private static boolean checkAttachmentPoint(Monomer mon, String str) throws AttachmentException {
    if (!(mon.getAttachmentListString().contains(str))) {
      if (!(str.equals("?"))) {
        logger.info("Attachment point for source is not there");
        throw new AttachmentException(
            "Attachment point for source is not there: "
                + str);
      }
    }
    return true;
  }

  /**
   * method to check if the two attachment points can form a connection
   * 
   * @param first
   * @param second
   * @param one
   * @param two
   * @return
   * @throws AttachmentException
   */
  private static boolean validAttachmentPoints(Monomer first, Monomer second, String one, String two) throws AttachmentException {
    if (one.equals("?") || two.equals("?")) {
      return true;
    }
    if (!(first.getAttachment(one).getCapGroupName().equals("H")
        && second.getAttachment(two).getCapGroupName().equals("OH")
        ||
        first.getAttachment(one).getCapGroupName().equals("OH")
            && second.getAttachment(two).getCapGroupName().equals("H")
        ||
        first.getAttachment(one).getCapGroupName().equals("H")
            && second.getAttachment(two).getCapGroupName().equals("H")
            && first.getAlternateId().equals("C")
            && second.getAlternateId().equals("C") )) {
      logger.info("Attachment points can not form a connection");
      throw new AttachmentException(
          "Attachment points can not form a connectíon");
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
  private static boolean checkAttachment(ArrayList<Monomer> firsts, ArrayList<Monomer> seconds, ConnectionNotation not, ContainerHELM2 containerhelm2, InterConnections interconnection,
      boolean spec)
      throws AttachmentException {
    boolean specific = spec;
    if (firsts.size() > 1 || seconds.size() > 1) {
      specific = false;
    }
    for(int x  = 0; x < firsts.size(); x ++){
      for(int y = 0; y < seconds.size(); y ++){
        /* Rna-Basepair-hydrogen bonds */
        if (firsts.get(x).getPolymerType().equals("RNA")
            && seconds.get(x).getPolymerType().equals("RNA")
            && not.getrGroupSource().equals("pair")
            && not.getrGroupTarget().equals("pair")) {
          logger.info("RNA strand connection");

          if (!(firsts.get(x).getMonomerType().equals("Branch")
              | seconds.get(x).getMonomerType().equals("Branch"))) {
            logger.info("RNA strand connection is not valid");
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



        }
        
        else{
    
          /*
           * are the attachments points valid OH-H or H-OH, H-H and OH-OH are
           * not allowed
           */
          validAttachmentPoints(firsts.get(x), seconds.get(y),not.getrGroupSource(), not.getrGroupTarget());
          
       
        }

          /* Inter connections */
        String detailsource = not.getSourceId().getID() + "$" + not.getSourceUnit() + "$"
            + not.getrGroupSource();
        String detailtarget = not.getTargetId().getID() + "$" + not.getTargetUnit() + "$"
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
   * @param mon
   * @param rGroup
   * @param containerhelm2
   * @param not
   * @param interconnection
   * @param id
   * @return
   * @throws AttachmentException
   */
  private static boolean checkSingleAttachment(ArrayList<Monomer> mon, String rGroup, ContainerHELM2 containerhelm2, ConnectionNotation not, InterConnections interconnection, String id)
      throws AttachmentException {

    for (int i = 0; i < mon.size(); ++i) {
      /* Are the attachment points there */
      checkAttachmentPoint(mon.get(i), rGroup);

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


  private static ArrayList<Monomer> getMonomersRNA(MonomerNotationUnitRNA rna, MonomerStore monomerStore) throws HELM2HandledException {
    ArrayList<Monomer> monomers = new ArrayList<Monomer>();
      for(int i = 0; i < rna.getContents().size(); i ++){
        String id = rna.getContents().get(i).getID().replace("[", "");
        id = id.replace("]", "");
        Monomer mon = monomerStore.getMonomer(rna.getType(), id);
      try {
        String detail = mon.getMonomerType();
        if (detail.equals("Branch")) {
          monomers.add(monomerStore.getMonomer(rna.getType(), id));
        }
      } catch (NullPointerException e) {
        throw new HELM2HandledException("Functions can't be called for HELM2 objects");
      }
      }
    return monomers;
    }

}

