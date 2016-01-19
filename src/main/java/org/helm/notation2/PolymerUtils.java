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
import java.util.List;

import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;


/**
 * PolymerUtils, class to provide methods for polymer
 * 
 * @author hecht
 */
public class PolymerUtils {


  /**
   * decompose the HELM2 into smaller HELM2 objects
   * 
   * @param polymers list of PolymerNotations
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
  


  /**
   * method to get the total monomer count of one PolymerNotation
   * 
   * @param polymer PolymerNotation
   * @return monomer count
   */
  protected static int getTotalMonomerCount(PolymerNotation polymer) {
    return polymer.getPolymerElements().getListOfElements().size();
  }

  protected void getNotationByReplacingSMILES() {

  }

  /**
   * method to get all self-cycle Connections
   * 
   * @param connections list of ConnectionNotation
   * @return list of all self-cycle Connections
   */
  private static  List<ConnectionNotation> getAllSelfCycleConnections(List<ConnectionNotation> connections) {
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
