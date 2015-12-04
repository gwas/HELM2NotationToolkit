/*--
 *
 * @(#) Images.java
 *
 *
 */
package org.helm.notation2;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Images
 * 
 * @author hecht
 */
public final class Images {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(Images.class);


  protected static void generateImageofMonomer(Monomer monomer) {
    /* buildMolecule for one Monomer */
    /* rufe ChemistryToolKit auf */
  }

  protected static void generateImageHELMMolecule(HELM2Notation helm2notation) throws BuilderMoleculeException, CTKException {
    AbstractMolecule molecule = MoleculeInformation.getMolecule(helm2notation);
    /* rufe ChemistryToolKit auf */
  }
}
