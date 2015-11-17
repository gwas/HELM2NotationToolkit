/*--
 *
 * @(#) ContainerHELM2.java
 *
 *
 */
package org.helm2;

import java.io.IOException;
import java.util.ArrayList;

import org.helm.notation.MonomerException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.parser.Notation.HELM2Notation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationGroup;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationList;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnit;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.helm2.exception.HELM2HandledException;
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
  
  ArrayList<PolymerUnit> polymerunits = new ArrayList<PolymerUnit>();

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

  public ArrayList<MonomerNotation> getListOfMonomerNotation(ArrayList<PolymerNotation> not) {
    ArrayList<MonomerNotation> items = new ArrayList<MonomerNotation>();
    for (int i = 0; i < not.size(); i++) {
      items.addAll(not.get(i).getListMonomers());
    }

    return items;

  }

  public ArrayList<Monomer> getListOfMonomer(ArrayList<MonomerNotation> not) throws MonomerException, IOException, JDOMException, HELM2HandledException {
    ArrayList<Monomer> items = new ArrayList<Monomer>();
    for (int i = 0; i < not.size(); i++) {
      items.addAll(Validation.getAllMonomers(not.get(i)));
    }
    return items;

  }



  /**
   * Only on these monomers calculation methods, getsmiles should be performed
   * 
   * @param not
   * @return
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException
   */
  public ArrayList<Monomer> getListOfHandledMonomers(ArrayList<MonomerNotation> not) throws MonomerException, IOException, JDOMException, HELM2HandledException {
    ArrayList<Monomer> items = new ArrayList<Monomer>();
    for (int i = 0; i < not.size(); i++) {
      /* group element */
      if (not.get(i) instanceof MonomerNotationGroup) {
        throw new HELM2HandledException("Functions can't be called for HELM2 objects");
      }

      else {
        try {
          int count = Integer.parseInt(not.get(i).getCount());
          if (count == 0) {
            throw new HELM2HandledException("Functions can't be called for HELM2 objects");
          }

          for (int j = 0; j < count; j++) {
            items.addAll(Validation.getAllMonomers(not.get(i)));
          }
        } catch (NumberFormatException e) {
          throw new HELM2HandledException("Functions can't be called for HELM2 objects");
        }

      }
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

  public String getSMILES() {
    return null;

  }
  
}
