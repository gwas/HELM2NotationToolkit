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

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.AttachmentList;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.ManipulatorFactory;
import org.helm.chemtoolkit.AbstractChemistryManipulator.OutputType;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.helm.notation.MonomerException;
import org.helm.notation.model.Attachment;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.connection.ConnectionNotation;
import org.helm.notation2.parser.notation.polymer.GroupEntity;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Molecule
 * 
 * @author hecht
 */
public final class BuilderMolecule {
  private static final String POLYMER_TYPE_BLOB = "BLOB";

  private static final String POLYMER_TYPE_CHEM = "CHEM";

  private static final String POLYMER_TYPE_RNA = "RNA";

  private static final String POLYMER_TYPE_PEPTIDE = "PEPTIDE";

  public enum E_PolymerType {
    Blob, Chem, RNA, Peptide
  }

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(BuilderMolecule.class);

  private void blub(E_PolymerType pt) {
    switch (pt) {
    case Blob:

      break;
    case Chem:
      break;
    case Peptide:
      break;
    default:
      break;
    }
  }



  protected static AbstractMolecule buildMoleculefromSinglePolymer(PolymerNotation
 polymernotation) throws BuilderMoleculeException, MonomerException,
 IOException, JDOMException, HELM2HandledException,
 CTKException {

 /* Case 1: BLOB -> throw exception */
 if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_BLOB)) {
 LOG.error("Molecule can't be build for BLOB");
 throw new BuilderMoleculeException("Molecule can't be build for BLOB");
 }

 /* Case 2: CHEM */
 else if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_CHEM))
 {
      List<Monomer> validMonomers = MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());
      return buildMoleculefromCHEM(validMonomers);
 }

 /* Case 3: RNA or PEPTIDE */
 else if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_RNA) ||
 polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_PEPTIDE)) {
 List<Monomer> validMonomers =
 MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());
      return buildMoleculefromPeptideOrRNA(validMonomers);

    }

 else{
 LOG.error("Molecule can't be build for unknown polymer type");
      throw new BuilderMoleculeException("Molecule can't be build for unknown polymer type");
 }

 }

  public static List<AbstractMolecule> buildMoleculefromPolymers(List<PolymerNotation> notlist,
      List<ConnectionNotation> connectionlist) throws BuilderMoleculeException, CTKException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
          IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Map<String, PolymerNotation> map = new HashMap<String, PolymerNotation>();

    Map<String, AbstractMolecule> mapMolecules = new HashMap<String, AbstractMolecule>();
    Map<String, String> mapConnections = new HashMap<String, String>();
    Map<String, Map<String, IAtomBase>> mapIAtomBase = new HashMap<String, Map<String, IAtomBase>>();
    
    List<AbstractMolecule> listMolecules = new ArrayList<AbstractMolecule>();
    AbstractMolecule molecule = null;
    /*Build for every single polymer a molecule*/
    for(PolymerNotation node: notlist){
      map.put(node.getPolymerID().getID(),node);
        try {
        AbstractMolecule currentmolecule = buildMoleculefromSinglePolymer(node);
        mapMolecules.put(node.getPolymerID().getID(), currentmolecule);
        mapIAtomBase.put(node.getPolymerID().getID(), currentmolecule.getRgroups());
        } catch (MonomerException | IOException | JDOMException | HELM2HandledException | CTKException e) {
          throw new BuilderMoleculeException(e.getMessage());
        }
    }

    Map<String[], IAtomBase[]> toAddConnection = new LinkedHashMap<String[], IAtomBase[]>();
    for (ConnectionNotation connection : connectionlist) {
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
      ;
      molecule =
          ManipulatorFactory.buildManipulator(ManipulatorType.MARVIN).merge(one, mapIAtomBase.get(connection.getSourceId().getID()).get(RgroupOne), two, mapIAtomBase.get(connection.getTargetId().getID()).get(RgroupTwo));
      mapMolecules.put(idFirst + idSecond, molecule);
      mapConnections.put(connection.getSourceId().getID(), idFirst + idSecond);
      mapConnections.put(connection.getTargetId().getID(), idFirst + idSecond);

    }

    for (AbstractMolecule content : mapMolecules.values()) {
      listMolecules.add(content);
    }


    return listMolecules;
 }

  private static AbstractMolecule buildMoleculefromCHEM(List<Monomer> validMonomers) throws BuilderMoleculeException, IOException, CTKException {
    /* MonomerNotationList or Count should be handled */
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

          // molecule = buildSingleMolecule(molecule);

          LOG.info("");
          Map<String, IAtomBase> rgroupMap = molecule.getRgroups();
          Map<String, IAtomBase> rmap = new HashMap<String, IAtomBase>();
          Set keyset = rgroupMap.keySet();
          for (Iterator it = keyset.iterator(); it.hasNext();) {
            String key = (String) it.next();
            rmap.put("1:" + key, (IAtomBase) rgroupMap.get(key));
          }

          return molecule;

        } else {
          LOG.error("Chemical molecule should have canonical smiles");
          throw new BuilderMoleculeException("Chemical molecule should have canoncial smiles");
        }
      }

      catch (NullPointerException e) {
        throw new BuilderMoleculeException("Monomer is not stored in the monomer database");
      }
    } else {
      LOG.error("Chemical molecule should contain exactly one monomer");
      throw new BuilderMoleculeException("Chemical molecule should contain exactly one monomer");
    }
  }

  private static AbstractMolecule buildMoleculefromPeptideOrRNA(List<Monomer> validMonomers) throws BuilderMoleculeException, IOException, CTKException {
    Map<AbstractMolecule, IAtomBase[]> listMolecules = new LinkedHashMap<AbstractMolecule, IAtomBase[]>();
    AbstractMolecule currentMolecule = null;
    AbstractMolecule molecule;
    AbstractMolecule prevMolecule =
        Chemistry.getInstance().getManipulator().getMolecule(validMonomers.get(0).getCanSMILES(), generateAttachmentList(validMonomers.get(0).getAttachmentList()));
    AbstractMolecule firstMolecule = null;
    String smiles = "";

    Monomer prevMonomer = null;

    if (validMonomers.size() == 0 || validMonomers == null) {
      throw new BuilderMoleculeException("Polymer (Peptide/RNA) has no contents");
    }
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

    prevMolecule = firstMolecule;
    String molFile;

    molFile = Chemistry.getInstance().getManipulator().convertMolecule(firstMolecule, AbstractChemistryManipulator.StType.MOLFILE);

    byte[] output = Chemistry.getInstance().getManipulator().renderMol(molFile, OutputType.PNG, 1000, 1000, (int) Long.parseLong("D3D3D3", 16));
    try (FileOutputStream out = new FileOutputStream("test-output\\picture_1.png")) {
      out.write(output);
    }
    int i = 2;
    for (Entry<AbstractMolecule, IAtomBase[]> entry : listMolecules.entrySet()) {
      prevMolecule = Chemistry.getInstance().getManipulator().merge(prevMolecule, entry.getValue()[0], entry.getKey(), entry.getValue()[1]);
      firstMolecule = prevMolecule;

      molFile = Chemistry.getInstance().getManipulator().convertMolecule(firstMolecule, AbstractChemistryManipulator.StType.MOLFILE);

      output = Chemistry.getInstance().getManipulator().renderMol(molFile, OutputType.PNG, 1000, 1000, (int) Long.parseLong("D3D3D3", 16));
      try (FileOutputStream out = new FileOutputStream("test-output\\picture_" + i + ".png")) {
        out.write(output);
      }
      i++;
    }

    return prevMolecule;
  }

  private static AttachmentList generateAttachmentList(List<Attachment> listAttachments) {
    AttachmentList list = new AttachmentList();

    for (Attachment attachment : listAttachments) {
      list.add(new org.helm.chemtoolkit.Attachment(attachment.getAlternateId(), attachment.getLabel(), attachment.getCapGroupName(), attachment.getCapGroupSMILES()));
    }
    return list;
  }

  protected static AbstractMolecule mergeRgroups(AbstractMolecule molecule) throws CTKException, IOException {
    Map<AbstractMolecule, IAtomBase[]> listMolecules = new LinkedHashMap<AbstractMolecule, IAtomBase[]>();
    for (org.helm.chemtoolkit.Attachment attachment : molecule.getAttachments()) {
      int groupId = AbstractMolecule.getIdFromLabel(attachment.getLabel());
      String smiles = attachment.getSmiles();
      LOG.debug(smiles);
      AbstractMolecule rMol = Chemistry.getInstance().getManipulator().getMolecule(smiles, null);
      IAtomBase[] bases = {molecule.getRGroupAtom(groupId, true), rMol.getRGroupAtom(groupId, true)};
      listMolecules.put(rMol, bases);
    }

    for (Map.Entry<AbstractMolecule, IAtomBase[]> addedMolecule : listMolecules.entrySet()) {

      molecule = Chemistry.getInstance().getManipulator().merge(molecule, addedMolecule.getValue()[0], addedMolecule.getKey(), addedMolecule.getValue()[1]);
    }
    return molecule;
  }

  protected static AbstractMolecule getMoleculeForMonomer(Monomer monomer) throws IOException, CTKException {
    String smiles = monomer.getCanSMILES();

    List<Attachment> listAttachments = monomer.getAttachmentList();
    AttachmentList list = new AttachmentList();

    for (Attachment attachment : listAttachments) {
      list.add(new org.helm.chemtoolkit.Attachment(attachment.getAlternateId(), attachment.getLabel(), attachment.getCapGroupName(), attachment.getCapGroupSMILES()));
    }
    return Chemistry.getInstance().getManipulator().getMolecule(smiles, list);

  }
}
