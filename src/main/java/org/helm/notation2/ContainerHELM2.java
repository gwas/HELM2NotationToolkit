/*--
 *
 * @(#) ContainerHELM2.java
 *
 *
 */
package org.helm.notation2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.ChemicalToolKit;
import org.helm.chemtoolkit.ChemistryManipulator;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.PermutationAndExpansion;
import org.helm.notation2.parser.exceptionparser.HELM1ConverterException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
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

  private HELM2Notation helm2notation;

  private InterConnections interconnection;


  private static final String PEPTIDE = "PEPTIDE";

  private static final String RNA = "RNA";

  private static final String BLOB = "BLOB";

  private static final String CHEM = "CHEM";


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

