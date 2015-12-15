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

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractChemistryManipulator.OutputType;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Images class to generate image generation of monomers and of the helm
 * molecule
 * 
 * @author hecht
 */
public final class Images {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(Images.class);


  /**
   * @param monomer
   * @return
   * @throws CTKException
   * @throws IOException
   * @throws BuilderMoleculeException
   * @throws NumberFormatException
   */
  protected static byte[] generateImageofMonomer(Monomer monomer) throws BuilderMoleculeException, CTKException {

      /* First build one molecule + merge unused rgoups into it */
    AbstractMolecule molecule = BuilderMolecule.mergeRgroups(BuilderMolecule.getMoleculeForMonomer(monomer));
    LOG.info("Molecule was built and unused rgroups were merged into it");
    String molFile;
      molFile = Chemistry.getInstance().getManipulator().convertMolecule(molecule, AbstractChemistryManipulator.StType.MOLFILE);
    LOG.info("Generate molfile for the built molecule");
    return Chemistry.getInstance().getManipulator().renderMol(molFile, OutputType.PNG, 1000, 1000, (int) Long.parseLong("D3D3D3", 16));

  }

  protected static byte[] generateImageHELMMolecule(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException, IOException {
    /* get SMILES representation for the whole molecule */
   String smiles = SMILES.getSMILESForAll(helm2notation);
    LOG.info("Get for the whole HELMNotation the smiles representation");
   AbstractMolecule molecule = Chemistry.getInstance().getManipulator().getMolecule(smiles, null);
    LOG.info("Molecule was created using the smiles generation");
   String molFile = Chemistry.getInstance().getManipulator().convertMolecule(molecule, AbstractChemistryManipulator.StType.MOLFILE);
    LOG.info("Generate molfile for the built molecule");
    return Chemistry.getInstance().getManipulator().renderMol(molFile, OutputType.PNG, 1000, 1000, (int) Long.parseLong("D3D3D3", 16));

  }
}
