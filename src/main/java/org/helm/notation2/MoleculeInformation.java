
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
   * @return
   * @throws BuilderMoleculeException
   */
  protected static double getMolecularWeight(HELM2Notation helm2notation) throws BuilderMoleculeException {
    MoleculeInformation.helm2notation = helm2notation;
    /* First build one big molecule; List of molecules? */
    AbstractMolecule molecule = buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return 0;

  }

  protected static double getExaxtMass(HELM2Notation helm2notation) throws BuilderMoleculeException {
    /* First build one big moleucle; List of molecules */
    MoleculeInformation.helm2notation = helm2notation;
    AbstractMolecule molecule = buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return 0;
  }

  protected static String getMolecularFormular(HELM2Notation helm2notation) throws BuilderMoleculeException {
    /* First build one big molecule */
    MoleculeInformation.helm2notation = helm2notation;
    AbstractMolecule molecule = buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return null;
  }

  protected static AbstractMolecule getMolecule(HELM2Notation helm2notation) throws BuilderMoleculeException {
    MoleculeInformation.helm2notation = helm2notation;
    return buildMolecule();
  }

}
