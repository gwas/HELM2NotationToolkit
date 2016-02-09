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
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Image class to generate image generation of monomers and of the helm molecule
 *
 * @author hecht
 */
public final class Images {

  /**
   *
   */
  private static final int PICTURE_HEIGHT = 1000;

  /**
   *
   */
  private static final int PICTURE_WIDTH = 1000;

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(Images.class);

  /**
   * Default constructor.
   */
  private Images() {

  }

  /**
   * generates an image of the atom/bond representation of monomer
   *
   * @param monomer Input Monomer
   * @param rgroupsInformation information if the rgroups should be should or
   *          not
   * @return an image of the monomer in byte[]
   * @throws CTKException
   * @throws BuilderMoleculeException if the molecule can't be built
   * @throws ChemistryException if the Chemistry Engine can not be initialized
   */
  public static byte[] generateImageofMonomer(Monomer monomer, boolean rgroupsInformation) throws BuilderMoleculeException, CTKException, ChemistryException {
    LOG.info("Image generation process of monomer starts");
    /* First build one molecule */
    AbstractMolecule molecule;
    if (rgroupsInformation) {
      molecule = BuilderMolecule.getMoleculeForMonomer(monomer);
      LOG.info("Molecule was built");
    } else {
      molecule = BuilderMolecule.mergeRgroups(BuilderMolecule.getMoleculeForMonomer(monomer));
      LOG.info("Molecule was built and unused rgroups were merged into it");
    }
    String molFile;
    molFile = Chemistry.getInstance().getManipulator().convertMolecule(molecule, AbstractChemistryManipulator.StType.MOLFILE);
    LOG.info("Generate molfile for the built molecule");
    return Chemistry.getInstance().getManipulator().renderMol(molFile, OutputType.PNG, PICTURE_WIDTH, PICTURE_HEIGHT, (int) Long.parseLong("D3D3D3", 16));
  }

  /**
   * method to generate an image of the HELM molecule
   *
   * @param helm2notation input HELMNotation
   * @return the generated image in byte[]
   * @throws BuilderMoleculeException if the HELM molecule can't be built
   * @throws CTKException
   * @throws IOException
   * @throws ChemistryException if the Chemistry Engine can not initialized
   */
  public static byte[] generateImageHELMMolecule(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException, IOException, ChemistryException {
    LOG.info("Image generation process of HELM molecule starts");
    /* get SMILES representation for the whole molecule */
    String smiles = SMILES.getSMILESForAll(helm2notation);
    LOG.info("Get for the whole HELMNotation the smiles representation");
    AbstractMolecule molecule = Chemistry.getInstance().getManipulator().getMolecule(smiles, null);
    LOG.info("Molecule was created using the smiles generation");
    String molFile = Chemistry.getInstance().getManipulator().convertMolecule(molecule, AbstractChemistryManipulator.StType.MOLFILE);
    LOG.info("Generate molfile for the built molecule(s)");
    return Chemistry.getInstance().getManipulator().renderMol(molFile, OutputType.PNG, PICTURE_WIDTH, PICTURE_HEIGHT, (int) Long.parseLong("D3D3D3", 16));
  }
}
