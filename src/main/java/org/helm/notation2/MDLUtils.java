
package org.helm.notation2;

import java.io.IOException;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MDLUtils, class to generate MDL for a HELM molecule
 * 
 * @author hecht
 */
public final class MDLUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(MDLUtils.class);


  /**
   * method to generate MDL for a HELM molecule
   * 
   * @param helm2notation input HELM2Notation
   * @return MDL
   * @throws BuilderMoleculeException if the helm molecule can not be built
   * @throws CTKException
   * @throws IOException
   */
  protected static String generateMDL(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException, IOException {
    String smiles = SMILES.getSMILESForAll(helm2notation);
    return Chemistry.getInstance().getManipulator().convert(smiles, AbstractChemistryManipulator.StType.SMILES);
  }

}
