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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MoleculeInformation
 * 
 * @author hecht
 */
public final class MoleculeInformation {
  private static HELM2Notation helm2notation;
  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(MoleculeInformation.class);

  /**
   * method to build from one notation one molecule
   * 
   * @throws BuilderMoleculeException
   */
  private static List<AbstractMolecule> buildMolecule() throws BuilderMoleculeException {

    return BuilderMolecule.buildMoleculefromPolymers(helm2notation.getListOfPolymers(), MethodsForContainerHELM2.getAllEdgeConnections(helm2notation.getListOfConnections()));
  }

  /**
   * method to get the molecular weight for the whole HELM
   * 
   * @return MolecularWeight
   * @throws BuilderMoleculeException if the whole molecule can not be built
   * @throws CTKException
   */
  protected static double getMolecularWeight(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException {
    MoleculeInformation.helm2notation = helm2notation;
    /* First build one big molecule; List of molecules? */
    List<AbstractMolecule> molecules = buildMolecule();
    Double result = 0.0;
    for (AbstractMolecule molecule : molecules) {
      molecule = BuilderMolecule.mergeRgroups(molecule);
      result += Chemistry.getInstance().getManipulator().getMoleculeInfo(molecule).getMolecularWeight();
    }
    return result;

  }

  /**
   * method to get the ExactMass for the whole HELM
   * 
   * @param helm2notation
   * @return ExactMass
   * @throws BuilderMoleculeException if the whole molecule can not be built
   * @throws CTKException
   */
  protected static double getExactMass(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException {
    /* First build one big moleucle; List of molecules */
    MoleculeInformation.helm2notation = helm2notation;
    List<AbstractMolecule> molecules = buildMolecule();
    Double result = 0.0;
    for (AbstractMolecule molecule : molecules) {
      result += Chemistry.getInstance().getManipulator().getMoleculeInfo(molecule).getExactMass();
    }
    return result;
  }

  /**
   * method to get the MolecularFormular for the whole HELM
   * 
   * @param helm2notation
   * @return MolecularFormular
   * @throws BuilderMoleculeException if the whole molecule can not be built
   * @throws CTKException
   */
  protected static String getMolecularFormular(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException {
    /* First build one big molecule */
    MoleculeInformation.helm2notation = helm2notation;
    List<AbstractMolecule> molecules = buildMolecule();
    Map<String, Integer> atomNumberMap = new TreeMap<String, Integer>();
    for(AbstractMolecule molecule : molecules){
      atomNumberMap = generateAtomNumberMap(molecule, atomNumberMap);
    }
    StringBuilder sb = new StringBuilder();
    Set<String> atoms = atomNumberMap.keySet();
    for (Iterator<String> i = atoms.iterator(); i.hasNext();) {
      String atom = i.next();
      String num = atomNumberMap.get(atom).toString();
      if (num.equals("1")) {
        num = "";
      }
      sb.append(atom);
      sb.append(num.toString());
    }
    return sb.toString();

  }

  /**
   * method to get a Molecule for the whole HELM
   * 
   * @param helm2notation
   * @return Molecule
   * @throws BuilderMoleculeException if the whole molecule can not be built
   */
  protected static List<AbstractMolecule> getMolecule(HELM2Notation helm2notation) throws BuilderMoleculeException {
    MoleculeInformation.helm2notation = helm2notation;
    return buildMolecule();
  }



  private static Map<String, Integer> generateAtomNumberMap(AbstractMolecule molecule, Map<String, Integer> mapAtoms) throws BuilderMoleculeException, CTKException {
    molecule = BuilderMolecule.mergeRgroups(molecule);
    String formula = Chemistry.getInstance().getManipulator().getMoleculeInfo(molecule).getMolecularFormula();
    String atom = "";
    String number = "";

    for (int i = 0; i < formula.length(); i++) {
      String oneChar = String.valueOf(formula.charAt(i));
      if (oneChar.matches("[A-Z]")) {
        if (atom.length() == 0) {
          atom = oneChar;
        } else {
          if (number == "") {
            number = "1";
          }
          if (mapAtoms.get(atom) != null) {
            mapAtoms.put(atom, mapAtoms.get(atom) + Integer.valueOf(number));
          }
 else {

            mapAtoms.put(atom, Integer.valueOf(number));
          }
          atom = oneChar;
          number = "";
        }
      } else if (oneChar.matches("[a-z]")) {
        if (atom.length() > 0) {
          atom = atom + oneChar;
        }
      } else {
        if (number.length() == 0) {
          number = oneChar;
        } else {
          number = number + oneChar;
        }
      }
    }

    if (number == "") {
      number = "1";
    }

    if (mapAtoms.get(atom) != null) {
      mapAtoms.put(atom, mapAtoms.get(atom) + Integer.valueOf(number));
    } else {

      mapAtoms.put(atom, Integer.valueOf(number));
    }
    return mapAtoms;
  }

}
