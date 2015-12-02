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


import java.util.ArrayList;
import java.util.List;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.notation.MonomerException;
import org.helm.notation2.exception.ConnectionNotationException;
import org.helm.notation2.exception.GroupingNotationException;
import org.helm.notation2.exception.PolymerIDsException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.annotation.AnnotationNotation;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.BlobEntity;
import org.helm.notation2.parser.notation.polymer.ChemEntity;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ContainerHELM2
 * 
 * @author hecht
 */
public class ContainerHELM2 {


  /** The Logger for this class */
  private static final Logger LOG =
      LoggerFactory.getLogger(ContainerHELM2.class);

  private HELM2Notation helm2notation;

  private InterConnections interconnection;




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

  /**
   * method to build from oine notation one molecule
   */
  private void buildMolecule() {

  }

  public double getMolecularWeight() {
    /* First build one big molecule */
    System.out.println("BuildOneBigMolecule");
    buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");

    return 0;

  }

  public double getExaxtMass() {
    System.out.println("BuildOneBigMolecule");
    buildMolecule();
    return 0;
  }

  public String getMolecularFormular() {
    /* First build one big molecule */
    System.out.println("BuildOneBigMolecule");
    buildMolecule();
    System.out.println("Rufe vom Chemistry Plugin die MoleculeInfo auf");
    return null;
  }
  
  /* SMILES class */
  public void getSMILES() {

  }

  /* SMILES class */
  public void getCanonicalSMILES()
  {

  }

  /* cannot generate SMILEs */
  public void containsGenericStructure() {
  }

  /* BuilderMolecule */
  public List<AbstractMolecule> getStructure() {
    return new ArrayList<AbstractMolecule>();
  }

  /**
   * method to validate all notations objects
   * 
   * @throws ConnectionNotationException
   * @throws GroupingNotationException
   * @throws MonomerException
   * @throws PolymerIDsException
   */
  public void validate() throws PolymerIDsException, MonomerException, GroupingNotationException, ConnectionNotationException {
    Validation.validateNotationObjects(this);
  }

  /**
   * method to get all polymers of this object
   * 
   * @return list of polymer notation
   */
  public List<PolymerNotation> getAllPolymers() {
    return helm2notation.getListOfPolymers();
  }

  /**
   * method to get all connections of this object
   * 
   * @return list of connection notation
   */
  public List<ConnectionNotation> getAllConnections() {
    return helm2notation.getListOfConnections();
  }

  /**
   * method to get all edge connections of this object
   * 
   * @return
   */
  public List<ConnectionNotation> getAllEdgeConnections() {
    List<ConnectionNotation> listEdgeConnection = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : helm2notation.getListOfConnections()) {
      if (!(connection.getrGroupSource().equals("pair"))) {
        listEdgeConnection.add(connection);
      }
    }
    return listEdgeConnection;
  }

  /**
   * method to get all base pair connections of this object
   * 
   * @return
   */
  public List<ConnectionNotation> getAllBasePairConnections() {
    List<ConnectionNotation> listEdgeConnection = new ArrayList<ConnectionNotation>();
    for (ConnectionNotation connection : helm2notation.getListOfConnections()) {
      if ((connection.getrGroupSource().equals("pair"))) {
        listEdgeConnection.add(connection);
      }
    }
    return listEdgeConnection;
  }

  /**
   * method to get all annotations of this object
   * 
   * @return
   */
  public AnnotationNotation getAllAnnotation() {
    return helm2notation.getAnnotation();
  }

  /**
   * method to get all rna polymers of this object
   * 
   * @return list of rna polymers
   */
  public List<PolymerNotation> getRNAPolymers() {
    List<PolymerNotation> rnaPolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof RNAEntity) {
        rnaPolymers.add(polymer);
      }
    }
    return rnaPolymers;
  }

  /**
   * method to get all peptide polymers of this object
   * 
   * @return list of peptide polymers
   */
  public List<PolymerNotation> getPeptidePolymers() {
    List<PolymerNotation> peptidePolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof PeptideEntity) {
        peptidePolymers.add(polymer);
      }
    }
    return peptidePolymers;
  }

  /**
   * method to get all chem polymers of this object
   * 
   * @return list of chem polymers
   */
  public List<PolymerNotation> getCHEMPolymers() {
    List<PolymerNotation> chemPolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof ChemEntity) {
        chemPolymers.add(polymer);
      }
    }
    return chemPolymers;
  }

  /**
   * method to get all blob polymers of this object
   * 
   * @return list of blob polymers
   */
  public List<PolymerNotation> getBLOBPolymers() {
    List<PolymerNotation> blobPolymers = new ArrayList<PolymerNotation>();
    for (PolymerNotation polymer : helm2notation.getListOfPolymers()) {
      if (polymer.getPolymerID() instanceof BlobEntity) {
        blobPolymers.add(polymer);
      }
    }
    return blobPolymers;
  }

  /* Do we really need this function */
  public void getFormatedSirnaSequences() {

  }

  public void getCanonicalNotation() {
    
  }

  public void addHELM2notation(HELM2Notation newHELM2Notation) {

  }

  public void hybridize() {

  }

  public void decompose() {

  }

  public void replaceMonomer() {
    
  }

  public void standardize() {
  }

  public boolean hasNucleotideModification() {
    return true;
  }

  public int getTotalMonomerCount() {
    return 1;
  }

  public static void getMoleculeInfo() {
    
  }

  public static void getNotaitonByREplacingSMILES() {

  }
}


