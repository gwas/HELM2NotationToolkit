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

import org.helm.chemtoolkit.AbstractMolecule;
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
  private static AbstractMolecule buildMolecule() throws BuilderMoleculeException {

    return BuilderMolecule.buildMoleculefromPolymers(helm2notation.getListOfPolymers(), helm2notation.getListOfConnections());
  }

  /**
   * method to get the molecular weight for the whole HELM
   * 
   * @return MolecularWeight
   * @throws BuilderMoleculeException if the whole molecule can not be built
   */
  protected static double getMolecularWeight(HELM2Notation helm2notation) throws BuilderMoleculeException {
    MoleculeInformation.helm2notation = helm2notation;
    /* First build one big molecule; List of molecules? */
    AbstractMolecule molecule = buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return 0;

  }

  /**
   * method to get the ExactMass for the whole HELM
   * 
   * @param helm2notation
   * @return ExactMass
   * @throws BuilderMoleculeException if the whole molecule can not be built
   */
  protected static double getExaxtMass(HELM2Notation helm2notation) throws BuilderMoleculeException {
    /* First build one big moleucle; List of molecules */
    MoleculeInformation.helm2notation = helm2notation;
    AbstractMolecule molecule = buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return 0;
  }

  /**
   * method to get the MolecularFormular for the whole HELM
   * 
   * @param helm2notation
   * @return MolecularFormular
   * @throws BuilderMoleculeException if the whole molecule can not be built
   */
  protected static String getMolecularFormular(HELM2Notation helm2notation) throws BuilderMoleculeException {
    /* First build one big molecule */
    MoleculeInformation.helm2notation = helm2notation;
    AbstractMolecule molecule = buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return null;
  }

  /**
   * method to get a Molecule for the whole HELM
   * 
   * @param helm2notation
   * @return Molecule
   * @throws BuilderMoleculeException if the whole molecule can not be built
   */
  protected static AbstractMolecule getMolecule(HELM2Notation helm2notation) throws BuilderMoleculeException {
    MoleculeInformation.helm2notation = helm2notation;
    return buildMolecule();
  }

}
