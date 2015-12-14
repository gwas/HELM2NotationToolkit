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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.AttachmentList;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.notation.model.Attachment;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.BlobEntity;
import org.helm.notation2.parser.notation.polymer.ChemEntity;
import org.helm.notation2.parser.notation.polymer.GroupEntity;
import org.helm.notation2.parser.notation.polymer.PeptideEntity;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.helm.notation2.parser.notation.polymer.RNAEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * builds for the HELMNotation Molecules
 * 
 * @author hecht
 */
public final class BuilderMolecule {


  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(BuilderMolecule.class);



  protected static AbstractMolecule buildMoleculefromSinglePolymer(PolymerNotation
 polymernotation) throws BuilderMoleculeException, HELM2HandledException {

 /* Case 1: BLOB -> throw exception */
    if (polymernotation.getPolymerID() instanceof BlobEntity) {
      LOG.error("Molecule can't be build for BLOB");
      throw new BuilderMoleculeException("Molecule can't be build for BLOB");
    }

    /* Case 2: CHEM */
    else if (polymernotation.getPolymerID() instanceof ChemEntity) {
      List<Monomer> validMonomers = MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());
      return buildMoleculefromCHEM(validMonomers);
    }

 /* Case 3: RNA or PEPTIDE */
    else if (polymernotation.getPolymerID() instanceof RNAEntity ||
        polymernotation.getPolymerID() instanceof PeptideEntity) {
      List<Monomer> validMonomers =
          MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());
      return buildMoleculefromPeptideOrRNA(validMonomers);
    }

    else {
      LOG.error("Molecule can't be build for unknown polymer type");
      throw new BuilderMoleculeException("Molecule can't be build for unknown polymer type");
    }
 }

  /**
   * method to build molecules for the HELMNotation
   * 
   * @param polymers
   * @param connections
   * @return list of built molecules
   * @throws BuilderMoleculeException
   */
  public static List<AbstractMolecule> buildMoleculefromPolymers(List<PolymerNotation> polymers,
      List<ConnectionNotation> connections) throws BuilderMoleculeException {
    Map<String, PolymerNotation> map = new HashMap<String, PolymerNotation>();

    Map<String, AbstractMolecule> mapMolecules = new HashMap<String, AbstractMolecule>();
    Map<String, String> mapConnections = new HashMap<String, String>();
    Map<String, Map<String, IAtomBase>> mapIAtomBase = new HashMap<String, Map<String, IAtomBase>>();
    

    List<AbstractMolecule> listMolecules = new ArrayList<AbstractMolecule>();
    AbstractMolecule molecule = null;
    /* Build for every single polymer a single molecule */
    for (PolymerNotation polymer : polymers) {
      map.put(polymer.getPolymerID().getID(),polymer);
        try {
        AbstractMolecule currentmolecule = buildMoleculefromSinglePolymer(polymer);
        mapMolecules.put(polymer.getPolymerID().getID(), currentmolecule);
        mapIAtomBase.put(polymer.getPolymerID().getID(), currentmolecule.getRgroups());
      } catch (HELM2HandledException | CTKException e) {
          throw new BuilderMoleculeException(e.getMessage());
        }
    }

    /* Build interconnections between single molecules */
    for (ConnectionNotation connection : connections) {
      /*Group Id -> throw exception*/
      if (connection.getSourceId() instanceof GroupEntity || connection.getTargetId() instanceof GroupEntity) {
        LOG.error("Molecule can't be build for group connection");
        throw new BuilderMoleculeException("Molecule can't be build for group connection");
      }

      /* Get the source molecule + target molecule */
      String idFirst = connection.getSourceId().getID();
      String idSecond = connection.getTargetId().getID();

      if (mapMolecules.get(idFirst) == null) {
        idFirst = mapConnections.get(idFirst);
      }
      if (mapMolecules.get(idSecond) == null) {
        idSecond = mapConnections.get(idSecond);
      }
      
      AbstractMolecule one = mapMolecules.get(idFirst);
      AbstractMolecule two = mapMolecules.get(idSecond);
      mapMolecules.remove(idFirst);
      mapMolecules.remove(idSecond);
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

      /* if the */
      if (!((MethodsForContainerHELM2.isMonomerSpecific(map.get(connection.getSourceId().getID()), source)
          && MethodsForContainerHELM2.isMonomerSpecific(map.get(connection.getTargetId().getID()), target)))) {
        throw new BuilderMoleculeException("Connection has to be unambiguous");
      }


      /* R group of connection is unknown */
      if (connection.getrGroupSource().equals("?") || connection.getrGroupTarget().equals("?")) {
        throw new BuilderMoleculeException("Connection's R groups have to be known");
      }


      String RgroupOne = connection.getrGroupSource();
      String RgroupTwo = connection.getrGroupTarget();
      System.out.println(connection.toHELM2());
      try {
        molecule =
            Chemistry.getInstance().getManipulator().merge(one, mapIAtomBase.get(connection.getSourceId().getID()).get(RgroupOne), two, mapIAtomBase.get(connection.getTargetId().getID()).get(RgroupTwo));
      } catch (CTKException e) {
        throw new BuilderMoleculeException(e.getMessage());
      }
      mapMolecules.put(idFirst + idSecond, molecule);
      mapConnections.put(connection.getSourceId().getID(), idFirst + idSecond);
      mapConnections.put(connection.getTargetId().getID(), idFirst + idSecond);

    }


    for (AbstractMolecule content : mapMolecules.values()) {
      listMolecules.add(content);
    }


    return listMolecules;
 }

  /**
   * method to build a molecule from a chemical component
   * 
   * @param validMonomers all valid Monomers of the chemical component
   * @return Built Molecule
   * @throws BuilderMoleculeException
   */
  private static AbstractMolecule buildMoleculefromCHEM(List<Monomer> validMonomers) throws BuilderMoleculeException {
    /* a chemical molecule should only contain one monomer */
    if (validMonomers.size() == 1) {
      try {
        if (validMonomers.get(0).getCanSMILES() != null) {
          /* Build monomer + Rgroup information! */
          Monomer monomer = validMonomers.get(0);
          String smiles = monomer.getCanSMILES();

          List<Attachment> listAttachments = monomer.getAttachmentList();
          AttachmentList list = new AttachmentList();

          for (Attachment attachment : listAttachments) {
            list.add(new org.helm.chemtoolkit.Attachment(attachment.getAlternateId(), attachment.getLabel(), attachment.getCapGroupName(), attachment.getCapGroupSMILES()));
          }
          
          AbstractMolecule molecule = Chemistry.getInstance().getManipulator().getMolecule(smiles, list);
          return molecule;

        } else {
          LOG.error("Chemical molecule should have canonical smiles");
          throw new BuilderMoleculeException("Chemical molecule should have canoncial smiles");
        }
      }

      catch (NullPointerException ex) {
        throw new BuilderMoleculeException("Monomer is not stored in the monomer database");
      }
      
      catch (IOException | CTKException e) {
        LOG.error("Molecule can't be built");
        throw new BuilderMoleculeException("Molecule can't be built");
      }
    } else {
      LOG.error("Chemical molecule should contain exactly one monomer");
      throw new BuilderMoleculeException("Chemical molecule should contain exactly one monomer");
    }
  }

  /**
   * method to build a molecule from a Peptide or RNA component
   * 
   * @param validMonomers
   * @return Molecule
   * @throws BuilderMoleculeException
   */
  private static AbstractMolecule buildMoleculefromPeptideOrRNA(List<Monomer> validMonomers) throws BuilderMoleculeException {
    try{
    Map<AbstractMolecule, IAtomBase[]> listMolecules = new LinkedHashMap<AbstractMolecule, IAtomBase[]>();
    AbstractMolecule currentMolecule = null;
    AbstractMolecule prevMolecule =
        Chemistry.getInstance().getManipulator().getMolecule(validMonomers.get(0).getCanSMILES(), generateAttachmentList(validMonomers.get(0).getAttachmentList()));
    AbstractMolecule firstMolecule = null;
    Monomer prevMonomer = null;

    if (validMonomers.size() == 0 || validMonomers == null) {
      throw new BuilderMoleculeException("Polymer (Peptide/RNA) has no contents");
    }
      /* First catch all IAtomBases */
    for (Monomer currentMonomer : validMonomers) {
      if (prevMonomer != null) {
        currentMolecule = Chemistry.getInstance().getManipulator().getMolecule(currentMonomer.getCanSMILES(), generateAttachmentList(currentMonomer.getAttachmentList()));
        /* Backbone Connection */
        IAtomBase[] rgroups = new IAtomBase[2];
        if (currentMonomer.getMonomerType().equals(Monomer.BACKBONE_MOMONER_TYPE)) {
          rgroups[0] = prevMolecule.getRGroupAtom(2, true);
          rgroups[1] = currentMolecule.getRGroupAtom(1, true);
          prevMolecule = currentMolecule;
        }
        /* Backbone to Branch Connection */
        else if (currentMonomer.getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE)) {
          rgroups[0] = prevMolecule.getRGroupAtom(3, true);
          rgroups[1] = currentMolecule.getRGroupAtom(1, true);
        }
        /* Unknown connection */
        else {
          LOG.error("Intra connection is unknown");
          throw new BuilderMoleculeException("Intra connection is unknown");
        }
        listMolecules.put(currentMolecule, rgroups);
      }

      else {
        prevMonomer = currentMonomer;
        prevMolecule =
            Chemistry.getInstance().getManipulator().getMolecule(prevMonomer.getCanSMILES(), generateAttachmentList(prevMonomer.getAttachmentList()));
        firstMolecule = prevMolecule;
      }
    }

      /* merge the molecules together */
    prevMolecule = firstMolecule;

      for (Entry<AbstractMolecule, IAtomBase[]> entry : listMolecules.entrySet()) {
        prevMolecule = Chemistry.getInstance().getManipulator().merge(prevMolecule, entry.getValue()[0], entry.getKey(), entry.getValue()[1]);
      }

    return prevMolecule;
    }
 catch (IOException | CTKException e) {
      throw new BuilderMoleculeException("Polymer(Peptide/RNA) molecule can't be built " + e.getMessage());
    }
  }

  /**
   * method to generate the AttachmentList given a list of attachments
   * 
   * @param listAttachments
   * @return AttachmentList
   */
  private static AttachmentList generateAttachmentList(List<Attachment> listAttachments) {
    AttachmentList list = new AttachmentList();

    for (Attachment attachment : listAttachments) {
      list.add(new org.helm.chemtoolkit.Attachment(attachment.getAlternateId(), attachment.getLabel(), attachment.getCapGroupName(), attachment.getCapGroupSMILES()));
    }
    return list;
  }

  /**
   * method to merge all unused rgroups into the molecule
   * 
   * @param molecule
   * @return Molecule
   * @throws BuilderMoleculeException
   */
  protected static AbstractMolecule mergeRgroups(AbstractMolecule molecule) throws BuilderMoleculeException {
    try{
    Map<AbstractMolecule, IAtomBase[]> listMolecules = new LinkedHashMap<AbstractMolecule, IAtomBase[]>();
      /* get the merge informations */
    for (org.helm.chemtoolkit.Attachment attachment : molecule.getAttachments()) {
      int groupId = AbstractMolecule.getIdFromLabel(attachment.getLabel());
      String smiles = attachment.getSmiles();
      LOG.debug(smiles);
      AbstractMolecule rMol = Chemistry.getInstance().getManipulator().getMolecule(smiles, null);
      IAtomBase[] bases = {molecule.getRGroupAtom(groupId, true), rMol.getRGroupAtom(groupId, true)};
      listMolecules.put(rMol, bases);
    }

      /* merge into molecule */
    for (Map.Entry<AbstractMolecule, IAtomBase[]> addedMolecule : listMolecules.entrySet()) {
      molecule = Chemistry.getInstance().getManipulator().merge(molecule, addedMolecule.getValue()[0], addedMolecule.getKey(), addedMolecule.getValue()[1]);
    }
      return molecule;
    } catch (NullPointerException | IOException | CTKException e) {
      throw new BuilderMoleculeException("Unused rgroups can't be merged into the molecule");
    }
  }

  /**
   * method to build a molecule for a given monomer
   * 
   * @param monomer
   * @return Molecule
   * @throws BuilderMoleculeException
   */
  protected static AbstractMolecule getMoleculeForMonomer(Monomer monomer) throws BuilderMoleculeException {
    String smiles = monomer.getCanSMILES();

    List<Attachment> listAttachments = monomer.getAttachmentList();
    AttachmentList list = new AttachmentList();

    for (Attachment attachment : listAttachments) {
      list.add(new org.helm.chemtoolkit.Attachment(attachment.getAlternateId(), attachment.getLabel(), attachment.getCapGroupName(), attachment.getCapGroupSMILES()));
    }
    try {
      return Chemistry.getInstance().getManipulator().getMolecule(smiles, list);
    } catch (IOException | CTKException e) {
      throw new BuilderMoleculeException("Molecule can't be built for the given monomer");
    }

  }
}
