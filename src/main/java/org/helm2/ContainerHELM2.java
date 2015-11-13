/*--
 *
 * @(#) ContainerHELM2.java
 *
 *
 */
package org.helm2;

import java.util.ArrayList;

import org.helm.notation2.parser.Notation.HELM2Notation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
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

  public static final String PEPTIDE = "PEPTIDE";

  public static final String RNA = "RNA";

  public static final String BLOB = "BLOB";

  public static final String CHEM = "CHEM";

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

  public ArrayList<MonomerNotation> getListOfMonomers(ArrayList<PolymerNotation> not) {
    ArrayList<MonomerNotation> items = new ArrayList<MonomerNotation>();
    for (int i = 0; i < not.size(); i++) {
      items.addAll(not.get(i).getListMonomers());
    }

    return items;

  }

  public ArrayList<PolymerNotation> getListOfPolymersSpecificType(String str, ArrayList<PolymerNotation> not) {
   ArrayList<PolymerNotation> list = new ArrayList<PolymerNotation>();
    for (int i = 0; i < not.size(); ++i) {
      if (not.get(i).getPolymerID().getType().equals(str)) {
        list.add(not.get(i));
      }
    }
    return list;
  }

}
