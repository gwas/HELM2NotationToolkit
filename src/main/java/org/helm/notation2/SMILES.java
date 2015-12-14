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

import java.util.List;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SMILES
 * 
 * 
 * @author hecht
 */
public final class SMILES {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(SMILES.class);



  /**
   * method to generate smiles for the whole HELMNotation
   * 
   * @param helm2notation
   * @return
   * @throws BuilderMoleculeException
   * @throws CTKException
   */
  protected static String getSMILESForAll(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException {
    /* Build Molecues */
    List<AbstractMolecule> molecules =
        BuilderMolecule.buildMoleculefromPolymers(helm2notation.getListOfPolymers(), MethodsForContainerHELM2.getAllEdgeConnections(helm2notation.getListOfConnections()));
    /* get for every molecule the smiles */
    StringBuffer sb = new StringBuffer();
    for (AbstractMolecule molecule : molecules) {
      molecule = BuilderMolecule.mergeRgroups(molecule);
      sb.append(Chemistry.getInstance().getManipulator().convertMolecule(molecule, AbstractChemistryManipulator.StType.SMILES) + ".");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();

  }

  /**
   * method to generate canonical smiles for the whole HELMNotation
   * 
   * @param helm2notation
   * @return canonical smiles
   * @throws BuilderMoleculeException
   * @throws CTKSmilesException
   * @throws CTKException
   */
  protected static String getCanonicalSmilesForAll(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKSmilesException, CTKException {
    List<AbstractMolecule> molecules = BuilderMolecule.buildMoleculefromPolymers(helm2notation.getListOfPolymers(), helm2notation.getListOfConnections());

    /* get for every molecule the canonical smiles */
    StringBuffer sb = new StringBuffer();
    for (AbstractMolecule molecule : molecules) {
      molecule = BuilderMolecule.mergeRgroups(molecule);
      sb.append(Chemistry.getInstance().getManipulator().canonicalize(Chemistry.getInstance().getManipulator().convertMolecule(molecule, AbstractChemistryManipulator.StType.SMILES) + "."));
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();
  }

  /* cannot generate SMILEs */
  protected void containsGenericStructure() {
  }

}

