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

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation.NotationException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MDLUtils, class to generate MDL for a HELM molecule.
 *
 * @author hecht
 */
public final class MDLUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(MDLUtils.class);

  /**
   * Default constructor.
   */
  private MDLUtils() {

  }

  /**
   * method to generate MDL for a HELM molecule
   *
   * @param helm2notation input HELM2Notation
   * @return MDL
   * @throws BuilderMoleculeException if the helm molecule can not be built
   * @throws CTKException
   * @throws NotationException
   * @throws ChemistryException if the Chemistry Engine can not be initialized
   */
  public static String generateMDL(final HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException, NotationException, ChemistryException {
    LOG.debug("Generate smiles representation for the whole HELM molecule");
    String smiles = SMILES.getSMILESForAll(helm2notation);
    LOG.debug("Convert smiles to mol");
    return Chemistry.getInstance().getManipulator().convert(smiles, AbstractChemistryManipulator.StType.SMILES);
  }

}
