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

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.ChemicalToolKit;
import org.helm.chemtoolkit.ChemistryManipulator;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerStore;
import org.helm.notation.StructureException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.Exception.HELM2HandledException;
import org.helm.notation2.parser.Notation.HELM2Notation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationGroup;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnit;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MethodsForContainerHELM2
 * 
 * @author hecht
 */
public final class MethodsForContainerHELM2 {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(MethodsForContainerHELM2.class);

  /**
   * Only on these monomers calculation methods, getsmiles should be performed
   * 
   * @param not
   * @return
   * @throws MonomerException
   * @throws IOException
   * @throws JDOMException
   * @throws HELM2HandledException
   * @throws CTKException
   */
  public static ArrayList<Monomer> getListOfHandledMonomers(ArrayList<MonomerNotation> not) throws MonomerException, IOException, JDOMException, HELM2HandledException, CTKException {
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

  public static ArrayList<MonomerNotation> getListOfMonomerNotation(ArrayList<PolymerNotation> not) {
    ArrayList<MonomerNotation> items = new ArrayList<MonomerNotation>();
    for (int i = 0; i < not.size(); i++) {
      items.addAll(not.get(i).getListMonomers());
    }

    return items;

  }

  public static ArrayList<Monomer> getListOfMonomer(ArrayList<MonomerNotation> not) throws MonomerException, IOException, JDOMException, HELM2HandledException, CTKException {
    ArrayList<Monomer> items = new ArrayList<Monomer>();
    for (int i = 0; i < not.size(); i++) {
      items.addAll(Validation.getAllMonomers(not.get(i)));
    }
    return items;

  }

  public static ArrayList<PolymerNotation> getListOfPolymersSpecificType(String str, ArrayList<PolymerNotation> not) {
    ArrayList<PolymerNotation> list = new ArrayList<PolymerNotation>();
    for (int i = 0; i < not.size(); ++i) {
      if (not.get(i).getPolymerID().getType().equals(str)) {
        list.add(not.get(i));
      }
    }
    return list;
  }

  public static String getSMILES() {
    return null;

  }

  public static String getCanonicalHELM(HELM2Notation not) throws MonomerException, IOException, JDOMException, StructureException, HELM2HandledException, ClassNotFoundException {

    
    return null;
  }

  public static String convertToHELM1(HELM2Notation not) throws HELM2HandledException {
    /* Grouping section is additional! */
    if (not.getListOfGroupings() != null) {
      throw new HELM2HandledException("Functions can't be called for HELM2 objects");
    }

    /* Connection ambiguity check */

    /* valid Monomers -> */

    /* Annotation checks */

    String notation = "";

    return notation;
  }

  public static boolean isMonomerSpecific(PolymerNotation not, int position) {

    if (not.getPolymerElements().getListOfElements().get(position) instanceof MonomerNotationUnit) {
      return true;
    }
 else {
      return false;
    }
  }

  public static Monomer getMonomer(String type, String id) throws MonomerException, IOException, JDOMException, CTKException {
    MonomerFactory monomerFactory = MonomerFactory.getInstance();
    MonomerStore monomerStore = monomerFactory.getMonomerStore();
    Monomer monomer;
    monomer = monomerStore.getMonomer(type, id);
    if (monomer == null) {
      ChemistryManipulator manipulator = ChemicalToolKit.getTestINSTANCE("").getManipulator();
      try {
        System.out.println(id);
        manipulator.validateSMILES(id);
        monomer = new Monomer(type, "Undefined", "", "");
        monomer.setAdHocMonomer(true);
        monomer.setCanSMILES(manipulator.canonicalize(id));
      } catch (CTKException e) {
        System.out.println(manipulator.validateSMILES(id));

        /*monomer is not in the database and also not a valid SMILES -> throw exception*/
        throw new MonomerException("Defined Monomer is not in the database and also not a valid SMILES");
      }
    }
   
    return monomer;
  }



}
