
package org.helm.notation2;

import java.io.IOException;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OutputFiles
 * 
 * @author hecht
 */
public final class MDLUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(MDLUtils.class);


  protected static String generateMDL(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException, IOException {
    String smiles = SMILES.getSMILESForAll(helm2notation);
    System.out.println(smiles);
    return Chemistry.getInstance().getManipulator().convert(smiles, AbstractChemistryManipulator.StType.SMILES);
  }

}
