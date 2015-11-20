/*--
 *
 * @(#) ContainerHELM2.java
 *
 *
 */
package org.helm.notation2;

import java.io.IOException;
import java.util.ArrayList;

import org.helm.notation.MonomerException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.Exception.HELM2HandledException;
import org.helm.notation2.parser.Notation.HELM2Notation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationList;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnit;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotationUnitRNA;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code ContainerHELM2}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class ContainerHELM2 {

  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(ContainerHELM2.class);

  HELM2Notation helm2notation;

  InterConnections interconnection;
  
  ArrayList<PolymerUnit> polymerunits = new ArrayList<PolymerUnit>();

  public static final String PEPTIDE = "PEPTIDE";

  public static final String RNA = "RNA";

  public static final String BLOB = "BLOB";

  public static final String CHEM = "CHEM";

  public ContainerHELM2(HELM2Notation helm2notation,
      InterConnections interconnection) {
    this.helm2notation = helm2notation;
    this.interconnection = interconnection;
  }

  public HELM2Notation getHELM2Notation() {
    return helm2notation;
  }

  public InterConnections getInterconnection() {
    return interconnection;
  }

  public void buildMolecule() {

  }
  
  public double getMolecularWeight() {
    /* First build one big molecule */
    System.out.println("BuildOneBigMolecule");
    buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return 0;

  }

  public static double getExaxtMass() {
    return 0;
  }

  public static String getMolecularFormular() {
    return null;
  }

}
