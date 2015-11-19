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
import java.util.ArrayList;
import java.util.HashMap;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.model.Monomer;
import org.helm.notation2.Exception.BuilderMoleculeException;
import org.helm.notation2.Exception.HELM2HandledException;
import org.helm.notation2.parser.Notation.Connection.ConnectionNotation;
import org.helm.notation2.parser.Notation.Polymer.Entity;
import org.helm.notation2.parser.Notation.Polymer.GroupEntity;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Molecule
 * 
 * @author hecht
 */
public final class BuilderMolecule {
  static String POLYMER_TYPE_BLOB = "BLOB";

  static String POLYMER_TYPE_CHEM = "CHEM";

  static String POLYMER_TYPE_RNA = "RNA";

  static String POLYMER_TYPE_PEPTIDE = "PEPTIDE";

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(BuilderMolecule.class);


  public static void buildMoleculefromSinglePolymer(PolymerNotation polymernotation) throws BuilderMoleculeException, MonomerException, IOException, JDOMException, HELM2HandledException,
      CTKException {
    /* Case 1: BLOB */
    if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_BLOB)) {
      LOG.error("Molecule can't be build for BLOB");
      throw new BuilderMoleculeException("Molecule can't be build for BLOB");
    }

    /* Case 2: CHEM -> throw new Exception */
    else if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_CHEM)) {
      ArrayList<Monomer> validMonomers = MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());

      /* a chemical molecule should only contain one monomer */
      if (validMonomers.size() == 1) {
        /* What to do with SMILES */
        try{
          if (validMonomers.get(0).getCanSMILES() != null) {
            /* Build monomer + Rgroup information! */
            System.out.println("Build chemical monomer + Rgroup information");
          } else {
            LOG.error("Chemical molecule should have canonical smiles");
            throw new BuilderMoleculeException("Chemical molecule should have canoncial smiles");
          }
        }
          
          catch(NullPointerException e){
          throw new BuilderMoleculeException("Monomer is not stored in the monomer database");
          }
        }
         else {
          LOG.error("Chemical molecule should contain exactly one monomer");
          throw new BuilderMoleculeException("Chemical molecule should contain exactly one monomer");
        }
      }
        


    /* Case 3: RNA or PEPTIDE */
    else if(polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_RNA) || polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_CHEM)) {
      ArrayList<Monomer> validMonomers = MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());
      buildMoleculefromPeptideOrRNA(validMonomers);
    }
    
    else{
      LOG.error("Molecule can't be build for unknown polymer type");
      throw new BuilderMoleculeException("Molecule can't be build for unknown polymer type");
    }
  }


  public static void buildMoleculefromPolymers(ArrayList<PolymerNotation> notlist, ArrayList<ConnectionNotation> connectionlist) throws BuilderMoleculeException, MonomerException, IOException,
      JDOMException,
      HELM2HandledException, CTKException {
    HashMap <String, PolymerNotation> map = new HashMap<String, PolymerNotation>();
    
    
    /*Build for every single polymer a molecule*/
    for(PolymerNotation node: notlist){
      map.put(node.getPolymerID().getID(),node);
      buildMoleculefromSinglePolymer(node);
    }
    
    
    
    for (ConnectionNotation connection : connectionlist) {

      /*Group Id -> throw exception*/
      if (connection.getSourceId() instanceof GroupEntity || connection.getTargetId() instanceof GroupEntity) {
        LOG.error("Molecule can't be build for group connection");
        throw new BuilderMoleculeException("Molecule can't be build for group connection");
      }
      
      /*Get the source molecule + target molecule*/
      System.out.println("Get Source Molecule + Target Molecule");
      
      
      /*
       * connection details: have to be an integer value + specific
       * MonomerNotationUnit
       */
      int source;
      int target;
      try {
        source = Integer.parseInt(connection.getSourceUnit());
        target = Integer.parseInt(connection.getTargetUnit());
      } catch (NumberFormatException e) {
        throw new BuilderMoleculeException("Connection has to be unambiguous");
      }

      /*if the */
      if((MethodsForContainerHELM2.isMonomerSpecific(map.get(connection.getSourceId().getID()), source) && MethodsForContainerHELM2.isMonomerSpecific(map.get(connection.getTargetId().getID()), target))){
        throw new BuilderMoleculeException("Connection has to be unambiguous");
      }

      
      /* R group of connection is unknown */
      if (connection.getrGroupSource().equals("?") || connection.getrGroupTarget().equals("?")) {
        throw new BuilderMoleculeException("Connection's R groups have to be known");
      }

      System.out.println("Build Molecule");
      System.out.println("Update R groups");

    }

  }

  private static void buildMoleculefromPeptideOrRNA(ArrayList<Monomer> validMonomers) throws BuilderMoleculeException {

    Monomer prevMonomer = null;
    for (int i = 0; i < validMonomers.size(); i++) {

      if (prevMonomer != null) {

        /* BackBone Connection */
        if (validMonomers.get(i).getMonomerType().equals(Monomer.BACKBONE_MOMONER_TYPE)) {
          System.out.println("Merge the previous with the current Monomer on the right attachment and left attachment");
          System.out.println("Remove the attachments");
          System.out.println("Merge unused attachment points");
          prevMonomer = validMonomers.get(i);
        }

        /* Backbone to Branch Connection */
        else if (validMonomers.get(i).getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
          System.out.println("Merge the previous with the current Monomer on the branch attachment point");
          System.out.println("Remove the attachments");
          System.out.println("Merge unused attachment points");
        }

        /* Connection is unknown */
        else {
          LOG.error("Intra connection is unknown");
          throw new BuilderMoleculeException("Intra connection is unknown");
        }
      }

      /* first monomer */
      else {

        prevMonomer = validMonomers.get(i);
        System.out.println("Set attachment points");
      }

    }

    /* last monomer */
    System.out.println("Set unused R group on the last backbone monomer");

    System.out.println("Return the molecule");
  }
}
