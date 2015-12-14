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
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.StructureParser;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import chemaxon.struc.Molecule;

/**
 * SMILES
 * 
 * 
 * @author hecht
 */
public final class SMILES {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(SMILES.class);

  protected static String getSMILES(List<Monomer> monomerlist) throws IOException, CTKException {
    StringBuffer sb = new StringBuffer();
    for (Monomer element : monomerlist) {
      String smi = element.getCanSMILES();
      if(sb.length()>0){
        sb.append(".");
      }
      sb.append(smi);
    }
    String mixtureSmiles = sb.toString();
    
    AbstractMolecule mol = Chemistry.getInstance().getManipulator().getMolecule(mixtureSmiles, null);
    return Chemistry.getInstance().getManipulator().convertMolecule(mol, AbstractChemistryManipulator.StType.SMILES);
  }

  protected static String getSMILESForAll(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException, ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
    /* Build Molecues */
    List<AbstractMolecule> molecules =
        BuilderMolecule.buildMoleculefromPolymers(helm2notation.getListOfPolymers(), MethodsForContainerHELM2.getAllEdgeConnections(helm2notation.getListOfConnections()));

    /* get for every molecule the smiles */
    StringBuffer sb = new StringBuffer();
    for (AbstractMolecule molecule : molecules) {
      // molecule = BuilderMolecule.mergeRgroups(molecule);
      sb.append(Chemistry.getInstance().getManipulator().convertMolecule(molecule, AbstractChemistryManipulator.StType.SMILES) + ".");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();

  }

  protected static String getCanonicalSmilesForAll(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException, ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    /* Build Molecues */
    List<AbstractMolecule> molecules = BuilderMolecule.buildMoleculefromPolymers(helm2notation.getListOfPolymers(), helm2notation.getListOfConnections());

    /* get for every molecule the canonical smiles */
    StringBuffer sb = new StringBuffer();
    for (AbstractMolecule molecule : molecules) {
      sb.append(".");
      System.out.println("ToDo");
    }
    sb.setLength(sb.length() - 1);
    return sb.toString();

  }

  /* cannot generate SMILEs */
  public void containsGenericStructure() {
  }


}

