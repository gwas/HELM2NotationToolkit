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
import java.util.List;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerStore;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationGroup;
import org.helm.notation2.parser.notation.polymer.MonomerNotationList;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MethodsForContainerHELM2
 * 
 * @author hecht
 */
public final class MethodsForContainerHELM2 {

  private static final Logger LOG =
      LoggerFactory.getLogger(MethodsForContainerHELM2.class);
  
  /**
   * method to get all HELM1 valid MonomerNotations Only on these monomers
   * required HELM1 functions are performed
   * 
   * @param monomerNotations List of MonomerNotation
   * @return List of Monomer
   * @throws HELM2HandledException if the HELM2 features were there
   */
  public static List<Monomer> getListOfHandledMonomers(List<MonomerNotation> monomerNotations)
      throws HELM2HandledException {
    List<Monomer> items = new ArrayList<Monomer>();
    for (MonomerNotation monomerNotation : monomerNotations) {
      /* group element */
      if (monomerNotation instanceof MonomerNotationGroup || monomerNotation instanceof MonomerNotationList) {
        throw new HELM2HandledException("Functions can't be called for HELM2 objects");
      }

      else {
        try {
          int count = Integer.parseInt(monomerNotation.getCount());
          if (count == 0 || count > 1) {
            throw new HELM2HandledException("Functions can't be called for HELM2 objects");
          }

          // for (int j = 0; j < count; j++) {
            items.addAll(Validation.getAllMonomers(monomerNotation));
          // }
        } catch (NumberFormatException | JDOMException | MonomerException | IOException e) {
          throw new HELM2HandledException("Functions can't be called for HELM2 objects");
        }

      }
    }
    return items;
  }

  /**
   * method to get all HELM1 valid MonomerNotations Only on these monomers
   * required HELM1 functions are performed
   * 
   * @param monomerNotations List of MonomerNotation
   * @return List of Monomer
   * @throws HELM2HandledException if HELM2 features are there
   */
  public static List<Monomer> getListOfHandledMonomersOnlyBase(List<MonomerNotation> monomerNotations)
      throws HELM2HandledException {
    LOG.debug("Get all bases of the rna");
    List<Monomer> items = new ArrayList<Monomer>();

    for (MonomerNotation monomerNotation : monomerNotations) {
      /* group element */
      if (monomerNotation instanceof MonomerNotationGroup) {
        throw new HELM2HandledException("Functions can't be called for HELM2 objects");
      }

      else {
        try {
          int count = Integer.parseInt(monomerNotation.getCount());
          if (count == 0) {
            throw new HELM2HandledException("Functions can't be called for HELM2 objects");
          }

          for (int j = 0; j < count; j++) {
            System.out.println(monomerNotation);
            items.addAll(Validation.getAllMonomersOnlyBase(monomerNotation));
          }
        } catch (NumberFormatException | JDOMException | MonomerException | IOException e) {
          System.out.println(e.getMessage() + monomerNotation);
          throw new HELM2HandledException("Functions can't be called for HELM2 objects");
        }

      }
    }
    return items;
  }

  /**
   * method to get all MonomerNotations for all given polymers
   * 
   * @param polymers List of PolymerNotation
   * @return List of MonomerNotation
   */
  protected static List<MonomerNotation> getListOfMonomerNotation(List<PolymerNotation> polymers) {
    List<MonomerNotation> items = new ArrayList<MonomerNotation>();
    for (PolymerNotation polymer : polymers) {
      items.addAll(polymer.getListMonomers());
    }

    return items;

  }

  /**
   * method to get all monomers for all MonomerNotations
   * 
   * @param monomerNotations List of MonomerNotation
   * @return List of Monomer
   * @throws MonomerException if the Monomer is not valid
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException if HELM2 features are there
   * @throws CTKException
   */
  protected static List<Monomer> getListOfMonomer(List<MonomerNotation> monomerNotations) throws MonomerException,
      IOException, JDOMException, HELM2HandledException, CTKException {
    List<Monomer> items = new ArrayList<Monomer>();
    for (MonomerNotation monomerNotation : monomerNotations) {
      items.addAll(Validation.getAllMonomers(monomerNotation));
    }
    return items;

  }

  /**
   * method to get all polymers for one specific polymer type
   * 
   * @param str specific polymer type
   * @param polymers List of PolymerNotation
   * @return List of PolymerNotation with the specific type
   */
  protected static List<PolymerNotation> getListOfPolymersSpecificType(String str, List<PolymerNotation> polymers) {
    List<PolymerNotation> list = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : polymers) {
      if (polymer.getPolymerID().getType().equals(str)) {
        list.add(polymer);
      }
    }
    return list;
  }

  /**
   * method to get the monomer from the database!
   * 
   * @param type Type of the Monomer
   * @param id Id of the Monomer
   * @return Monomer
   * @throws MonomerException if the desired monomer is not in the database
   */
  protected static Monomer getMonomer(String type, String id) throws MonomerException {
    try {
      MonomerFactory monomerFactory = MonomerFactory.getInstance();
      MonomerStore monomerStore = monomerFactory.getMonomerStore();
      Monomer monomer;
      /* Monomer was saved to the database */
      monomer = monomerStore.getMonomer(type, id);
      if (monomer == null) {
        /*
         * smiles check! Maybe the smiles is already included in the data base
         */
        if (monomerFactory.getSmilesMonomerDB().get(id) != null) {
          monomer = monomerFactory.getSmilesMonomerDB().get(id);
          return monomer;
        }
 else {

          monomer = monomerFactory.getSmilesMonomerDB().get(id);
          if (monomer == null) {
            /* Rgroups information are not given -> only smiles information */
            AbstractChemistryManipulator manipulator = Chemistry.getInstance().getManipulator();
            manipulator.validateSMILES(id);
            monomer = new Monomer(type, "Undefined", "", "");
            monomer.setAdHocMonomer(true);
            monomer.setCanSMILES(manipulator.canonicalize(id));
          }
        }
      }
      return monomer;
    } catch (CTKException | IOException e) {
      /*
       * monomer is not in the database and also not a valid SMILES -> throw exception
       */
      throw new MonomerException("Defined Monomer is not in the database and also not a valid SMILES");
    }
  }

  /**
   * method to get all edge connections
   * 
   * @param connections
   * @return List of all edge ConnectionNotation
   */
  protected static List<ConnectionNotation> getAllEdgeConnections(List<ConnectionNotation> connections) {
    List<ConnectionNotation> listEdgeConnection = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : connections) {
      if (!(connection.getrGroupSource().equals("pair"))) {
        listEdgeConnection.add(connection);
      }
    }
    return listEdgeConnection;
  }

  /**
   * * method to get all base pair connections
   * 
   * @param connections List of ConnectionNotation
   * @return List of all base pair ConnectionNotation
   */
  protected static List<ConnectionNotation> getAllBasePairConnections(List<ConnectionNotation> connections) {
    List<ConnectionNotation> listEdgeConnection = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : connections) {
      if ((connection.getrGroupSource().equals("pair"))) {
        listEdgeConnection.add(connection);
      }
    }
    return listEdgeConnection;
  }
}
