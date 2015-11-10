
package org.helm2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.helm.notation.NotationException;
import org.helm.notation.model.PolymerNode;
import org.helm.notation2.parser.GroupingSection.BetweenGroupingParser;
import org.helm.notation2.parser.Notation.HELM2Notation;
import org.helm.notation2.parser.Notation.Connection.ConnectionNotation;
import org.helm.notation2.parser.Notation.Grouping.GroupingNotation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationList;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Notationsobjects
 * 
 * @author hecht
 */
public class Notationsobjects {

  private static final Logger logger =
      LoggerFactory.getLogger(BetweenGroupingParser.class);

  public Notationsobjects() {

  }

  
  /**
   * method to check if the connections and the grouping are correct
   * 
   * @throws NotationException
   */
  public void validateNotationobjects(HELM2Notation helm2notation)
      throws NotationException {
    /*all polymer ids have to be unique*/
    validateUniquePolymerIDs(helm2notation);
    
    
    /* validate the grouping section */
    if (!validateGrouping(helm2notation)) {
      /* the grouping section is not valid: throw new Exception */
      System.out.println("Wrong");

    }
    /* validate the connection */
    if (!validateConnections(helm2notation)) {

    }
  }


  /**
   * method to validate the attachment
   */
  public static boolean validateConnections(HELM2Notation helm2notation) {
    /* is polymer ID there */
    ArrayList<ConnectionNotation> listConnection =
        helm2notation.getListOfConnections();
    ArrayList<String> listPolymerIDs = helm2notation.getPolymerAndGroupingIDs();

    for (int i = 0; i < listConnection.size(); i++) {
      /* check for each connection */
      /* the entities have to be there */
      /* also in the case of a group */
      if (!(listPolymerIDs.contains(listConnection.get(i).getSourceId().getID()))) {
        return false;
      }
      if (!(listPolymerIDs.contains(listConnection.get(i).getTargetId().getID()))) {
        return false;
      }

      /* check if the required connection is possible -> look at attachment? */
      /* 1:R1, 2:R3 */
      /* get the source monomer + get the target monomer */
      PolymerNotation source =
          helm2notation.getPolymerNotation(listConnection.get(i).getSourceId().getID());
      String sourceUnit = listConnection.get(i).getSourceUnit();
      MonomerNotation first = source.getMonomer(Integer.parseInt(sourceUnit));

      /* Get the target */
      PolymerNotation target =
          helm2notation.getPolymerNotation(listConnection.get(i).getTargetId().getID());
      String targetUnit = listConnection.get(i).getTargetUnit();
      MonomerNotation second = target.getMonomer(Integer.parseInt(targetUnit));

      /* can the two form a connection */


    }

    return true;
  }

  /**
   * method to validate the grouping
   */
  public static boolean validateGrouping(HELM2Notation helm2notation) {
    ArrayList<GroupingNotation> listGroupings =
        helm2notation.getListOfGroupings();
    ArrayList<String> listPolymerIDs = helm2notation.getPolymerAndGroupingIDs();
    
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
   * @throws NotationException
   * 
   */
  public static boolean validateUniquePolymerIDs(HELM2Notation helm2notation)
      throws NotationException {
    ArrayList<String> listPolymerIDs = helm2notation.getPolymerAndGroupingIDs();
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


}
