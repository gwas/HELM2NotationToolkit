/*--
 *
 * @(#) PolymerUtils.java
 *
 *
 */
package org.helm.notation2;

import java.util.ArrayList;
import java.util.List;

import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PolymerUtils
 * 
 * @author hecht
 */
public class PolymerUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(PolymerUtils.class);


  /**
   * decompose the HELM2 into smaller HELM2 notations
   * 
   * @param polymers
   * @param connection: list contains only selfcycle connections
   * @return list of ContainerHELM2 objects
   */
  protected List<ContainerHELM2> decompose(List<PolymerNotation> polymers, List<ConnectionNotation> connections) {
    List<ContainerHELM2> list = new ArrayList<ContainerHELM2>();
    List<ConnectionNotation> AllselfConnections = getAllSelfCycleConnections(connections);
    for (PolymerNotation polymer : polymers) {
      HELM2Notation helm2notation = new HELM2Notation();
      helm2notation.addPolymer(polymer);
      List<ConnectionNotation> selfConnections = getSelfCycleConnections(polymer.getPolymerID().getID(), AllselfConnections);
      for (ConnectionNotation selfConnection : selfConnections) {
        helm2notation.addConnection(selfConnection);
      }
      list.add(new ContainerHELM2(helm2notation, new InterConnections()));

    }

    return list;
  }
  


  protected static int getTotalMonomerCount(PolymerNotation polymer) throws HELM2HandledException {
    return polymer.getPolymerElements().getListOfElements().size();
  }

  protected void getNotationByReplacingSMILES() {

  }

  private static  List<ConnectionNotation> getAllSelfCycleConnections(List<ConnectionNotation> connections) {
    List<ConnectionNotation> listSelfCycle = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : connections) {
      if ((connection.getTargetId().getID().equals(connection.getSourceId().getID()))) {
        listSelfCycle.add(connection);
      }
    }
    return listSelfCycle;
  }

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
