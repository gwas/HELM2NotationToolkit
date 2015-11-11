
package org.helm2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.model.Monomer;
import org.helm.notation.model.PolymerNode;
import org.helm.notation2.parser.GroupingSection.BetweenGroupingParser;
import org.helm.notation2.parser.Notation.HELM2Notation;
import org.helm.notation2.parser.Notation.ValidationMethod;
import org.helm.notation2.parser.Notation.Connection.ConnectionNotation;
import org.helm.notation2.parser.Notation.Grouping.GroupingNotation;
import org.helm.notation2.parser.Notation.Polymer.Entity;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationList;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnit;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.helm2.exception.AttachmentException;
import org.helm2.exception.ConnectionNotationException;
import org.helm2.exception.GroupingNotationException;
import org.helm2.exception.PolymerIDsException;
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
   * method to check if the connections and the grouping are correct
   * 
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
   */
  public void validateNotationObjects(ContainerHELM2 containerhelm2)
      throws NotationException, MonomerException, IOException, JDOMException,
      GroupingNotationException, ConnectionNotationException,
      PolymerIDsException, AttachmentException,
      org.helm.notation2.parser.ExceptionParser.NotationException,
      org.jdom.JDOMException {
    /*all polymer ids have to be unique*/
    if (!validateUniquePolymerIDs(containerhelm2)) {
      logger.info("Polymer ids have to be unique");
      throw new PolymerIDsException("Polymer IDs have to be unique");
    }
    /* validate the grouping section */
    if (!validateGrouping(containerhelm2)) {
      logger.info("Group section is not valid");
      throw new GroupingNotationException("Group notation is not valid");
    }
    /* validate the connection */
    if (!validateConnections(containerhelm2)) {
      throw new ConnectionNotationException("Connection notation is not valid");
    }
  }


  /**
   * method to validate all connections in the connection section
   * 
   * @throws JDOMException
   * @throws IOException
   * @throws MonomerException
   * @throws AttachmentException
   * @throws PolymerIDsException
   * @throws org.jdom.JDOMException
   * @throws NotationException
   * @throws org.helm.notation2.parser.ExceptionParser.NotationException
   */
  public static boolean validateConnections(ContainerHELM2 containerhelm2)
      throws MonomerException, IOException, JDOMException, AttachmentException,
      PolymerIDsException,
      org.helm.notation2.parser.ExceptionParser.NotationException,
      org.jdom.JDOMException, NotationException {

    ArrayList<ConnectionNotation> listConnection =
        containerhelm2.getHELM2Notation().getListOfConnections();
    ArrayList<String> listPolymerIDs =
        containerhelm2.getHELM2Notation().getPolymerAndGroupingIDs();
    /* Hash-Map to save all InterConnections */
    InterConnections interconnection = containerhelm2.getInterconnection();

    /* check for each single connection */
    for (int i = 0; i < listConnection.size(); i++) {
      /* the polymer ids have to be there */
      checkExistenceOfPolymerID(listConnection.get(i).getSourceId().getID(), listPolymerIDs);
      checkExistenceOfPolymerID(listConnection.get(i).getTargetId().getID(), listPolymerIDs);


      /* check if the required connection is possible -> look at attachment */
      /* get the source monomer */
      PolymerNotation source =
          containerhelm2.getHELM2Notation().getPolymerNotation(listConnection.get(i).getSourceId().getID());
      String sourceUnit = listConnection.get(i).getSourceUnit();
      ArrayList<Integer> numberOne = new ArrayList<Integer>();
      try{
        numberOne.add(Integer.parseInt(sourceUnit));
      }
      catch(NumberFormatException e){
        numberOne =
            getNumberForMonomer(sourceUnit, listConnection.get(i).getSourceId(), containerhelm2);
      }
      for (int x = 0; x < numberOne.size(); x++) {
        MonomerNotation first = source.getMonomer(numberOne.get(x));

        /* Get the target monomer */
        PolymerNotation target =
            containerhelm2.getHELM2Notation().getPolymerNotation(listConnection.get(i).getTargetId().getID());
        String targetUnit = listConnection.get(i).getTargetUnit();
        ArrayList<Integer> numberTwo = new ArrayList<Integer>();
        try {
          numberTwo.add(Integer.parseInt(targetUnit));
        } catch (NumberFormatException e) {
          numberTwo =
              getNumberForMonomer(targetUnit, listConnection.get(i).getTargetId(), containerhelm2);
        }
        for(int y = 0; y < numberTwo.size(); y++){
          MonomerNotation second =
              target.getMonomer(numberTwo.get(y));


          /* can the two form a connection */
          MonomerFactory monomerFactory = MonomerFactory.getInstance();
          /* Search in Database : both have to be the type Branch */
          MonomerStore monomerStore = monomerFactory.getMonomerStore();
          String id = first.getID().replace("[", "");
          id = id.replace("]", "");
          Monomer one =
              monomerStore.getMonomer(source.getPolymerID().getType(), id);
          id = second.getID().replace("[", "");
          id = id.replace("]", "");
          Monomer two =
              monomerStore.getMonomer(target.getPolymerID().getType(), id);

          /* Rna-Basepair-hydrogen bonds */
          if (source.getPolymerID().getType().equals("RNA")
              && target.getPolymerID().getType().equals("RNA")
              && listConnection.get(i).getrGroupSource().equals("pair")
              && listConnection.get(i).getrGroupTarget().equals("pair")) {
            logger.info("RNA strand connection");

            if (!(one.getMonomerType().equals("Branch")
                | two.getMonomerType().equals("Branch"))) {
              logger.info("RNA strand connection is not valid");
              throw new AttachmentException(
                  "RNA strand connection is not valid");
            }

          }

          else {

            /* Is the attachment point there */
            if (!(one.getAttachmentListString().contains(listConnection.get(i).getrGroupSource()))) {
              System.out.println(listConnection.get(i).getrGroupSource());
              logger.info("Attachment point for source is not there");
              throw new AttachmentException(
                  "Attachment point for source is not there");
            }
            if (!(two.getAttachmentListString().contains(listConnection.get(i).getrGroupTarget()))) {
              System.out.println("Attachment point for target is not there");
              logger.info("Attachment point for target is not there");
              throw new AttachmentException(
                  "Attachment point for target is not there");
            }

            /*
             * are the attachments points valid OH-H or H-OH, H-H and OH-OH are
             * not allowed
             */
            if (!(one.getAttachment(listConnection.get(i).getrGroupSource()).getCapGroupName().equals("H")
                && two.getAttachment(listConnection.get(i).getrGroupTarget()).getCapGroupName().equals("OH")
                ||
                one.getAttachment(listConnection.get(i).getrGroupSource()).getCapGroupName().equals("OH")
                    && two.getAttachment(listConnection.get(i).getrGroupTarget()).getCapGroupName().equals("H")
                ||
                one.getAttachment(listConnection.get(i).getrGroupSource()).getCapGroupName().equals("H")
                    && two.getAttachment(listConnection.get(i).getrGroupTarget()).getCapGroupName().equals("H")
                    && one.getAlternateId().equals("C")
                    && two.getAlternateId().equals("C"))) {
              logger.info("Attachment points can not form a connection");
              throw new AttachmentException(
                  "Attachment points can not form a connectíon");
            }

          }

          /* is the attachment point already occupied by another monomer? */
          String detailsource = listConnection.get(i).getSourceUnit() + "$"
              + listConnection.get(i).getrGroupSource();
          String detailtarget = listConnection.get(i).getTargetUnit() + "$"
              + listConnection.get(i).getrGroupSource();

          /* Intra connections */
          if (containerhelm2.getHELM2Notation().getSimplePolymer(listConnection.get(i).getSourceId().getID()).getMapIntraConnection().containsKey(detailsource)) {
            throw new AttachmentException(
                "Attachment point is already occupied");
          }

          if (containerhelm2.getHELM2Notation().getSimplePolymer(listConnection.get(i).getTargetId().getID()).getMapIntraConnection().containsKey(detailtarget)) {
            throw new AttachmentException(
                "Attachment point is already occupied");
          }

          /* Inter connections */
          detailsource = listConnection.get(i).getSourceId().getID() + "$"
              + detailsource;
          detailtarget = listConnection.get(i).getTargetId().getID() + "$"
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

          interconnection.addConnection(detailsource, "");
          interconnection.addConnection(detailtarget, "");

        }

      }
    }

    return true;
  }

  /**
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
   */
  private static ArrayList<Integer> getNumberForMonomer(String sourceUnit,
      Entity e,
      ContainerHELM2 containerhelm2)
          throws org.helm.notation2.parser.ExceptionParser.NotationException,
          MonomerException, IOException, org.jdom.JDOMException, JDOMException,
          NotationException {
    /* different possibilites are possible */
    /* first: monomer type */
    ArrayList<Integer> occurences = new ArrayList<Integer>();
    MonomerNotation mon =
        new ValidationMethod().decideWhichMonomerNotation(sourceUnit, e.getType());
    if (mon instanceof MonomerNotationUnit) {
      PolymerNotation polymerNotation =
          containerhelm2.getHELM2Notation().getPolymerNotation(e.getID());
    for(int i = 0; i < polymerNotation.getPolymerElements().getListOfElements().size(); i++){
        if (polymerNotation.getPolymerElements().getListOfElements().get(i).getID().equals(sourceUnit)) {
          occurences.add(i);
      }
    }

    }
      
    System.out.println(occurences);
    

    /* second: group or */
    /* third: group and */
    return occurences;
  }

  /**
   * method to validate the grouping
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
   * method to get the number of all occuring monomers
   * 
   * @param helm2notation
   * @return
   */
  public static int getMonomerCount(HELM2Notation helm2notation) {
    ArrayList<PolymerNotation> listPolymers = helm2notation.getListOfPolymers();
    int count = 0;
    for (int i = 0; i < listPolymers.size(); i++) {
      ArrayList<MonomerNotation> listMonomers =
          listPolymers.get(i).getPolymerElements().getListOfElements();
      for (int j = 0; j < listMonomers.size(); j++) {
        int multiply = 1;
        try {
        multiply = Integer.parseInt(listMonomers.get(j).getCount());
        } catch (NumberFormatException e) {
          multiply = 1;
        }

        //int multiply = Integer.parseInt(listMonomers.get(i).getCount());
        if (listMonomers.get(j) instanceof MonomerNotationList) {

          count +=
              ((MonomerNotationList) listMonomers.get(j)).getListofMonomerUnits().size()
                  * multiply;
        } else if (listMonomers.get(j) instanceof MonomerNotationUnitRNA) {
          count +=
              ((MonomerNotationUnitRNA) listMonomers.get(j)).getContents().size()
                  * multiply;
        } else {
          count += multiply;
        }
      }

    }

    return count;

  }

  public static boolean checkExistenceOfPolymerID(String str,
      ArrayList<String> listPolymerIDs) throws PolymerIDsException {
    if (listPolymerIDs.contains(str)) {
      return true;
    }
    System.out.println("Polymer Id is not there");
    logger.info("Polymer Id is not there");
    throw new PolymerIDsException("Polymer ID does not exist");
  }




}
