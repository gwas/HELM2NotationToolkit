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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.ChemicalToolKit;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.MolAtom;
import org.helm.chemtoolkit.Molecule;
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

}

// public static RgroupStructure buildMoleculefromSinglePolymer(PolymerNotation
// polymernotation) throws BuilderMoleculeException, MonomerException,
// IOException, JDOMException, HELM2HandledException,
// CTKException {
//
// /* Contents of the RgroupStructure */
// RgroupStructure structure = new RgroupStructure();
// Molecule molecule;
// Map<String, MolAtom> rmap;
//
// /* Case 1: BLOB -> throw exception */
// if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_BLOB)) {
// LOG.error("Molecule can't be build for BLOB");
// throw new BuilderMoleculeException("Molecule can't be build for BLOB");
// }
//
// /* Case 2: CHEM */
// else if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_CHEM))
// {
// List<Monomer> validMonomers =
// MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());
//
// }
//
// /* Case 3: RNA or PEPTIDE */
// else if (polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_RNA) ||
// polymernotation.getPolymerID().getType().equals(POLYMER_TYPE_PEPTIDE)) {
// List<Monomer> validMonomers =
// MethodsForContainerHELM2.getListOfHandledMonomers(polymernotation.getPolymerElements().getListOfElements());
// structure = buildMoleculefromPeptideOrRNA(validMonomers);
// }
//
// else{
// LOG.error("Molecule can't be build for unknown polymer type");
// throw new BuilderMoleculeException("Molecule can't be build for unknown
// polymer type");
// }
//
// return structure;
// }
//
//
// public static void buildMoleculefromPolymers(List<PolymerNotation> notlist,
// List<ConnectionNotation> connectionlist) throws BuilderMoleculeException,
// MonomerException,
// IOException,
// JDOMException,
// HELM2HandledException, CTKException {
// Map<String, PolymerNotation> map = new HashMap<String, PolymerNotation>();
// Map<String, RgroupStructure> structure = new HashMap<String,
// RgroupStructure>();
//
// /*Build for every single polymer a molecule*/
// for(PolymerNotation node: notlist){
// map.put(node.getPolymerID().getID(),node);
// buildMoleculefromSinglePolymer(node);
// }
//
//
//
// for (ConnectionNotation connection : connectionlist) {
//
// /*Group Id -> throw exception*/
// if (connection.getSourceId() instanceof GroupEntity ||
// connection.getTargetId() instanceof GroupEntity) {
// LOG.error("Molecule can't be build for group connection");
// throw new BuilderMoleculeException("Molecule can't be build for group
// connection");
// }
//
// /*Get the source molecule + target molecule*/
// System.out.println("Get Source Molecule + Target Molecule");
//
//
// /*
// * connection details: have to be an integer value + specific
// * MonomerNotationUnit
// */
// int source;
// int target;
// try {
// source = Integer.parseInt(connection.getSourceUnit());
// target = Integer.parseInt(connection.getTargetUnit());
// } catch (NumberFormatException e) {
// throw new BuilderMoleculeException("Connection has to be unambiguous");
// }
//
// /*if the */
// if((MethodsForContainerHELM2.isMonomerSpecific(map.get(connection.getSourceId().getID()),
// source) &&
// MethodsForContainerHELM2.isMonomerSpecific(map.get(connection.getTargetId().getID()),
// target))){
// throw new BuilderMoleculeException("Connection has to be unambiguous");
// }
//
//
// /* R group of connection is unknown */
// if (connection.getrGroupSource().equals("?") ||
// connection.getrGroupTarget().equals("?")) {
// throw new BuilderMoleculeException("Connection's R groups have to be known");
// }
//
// System.out.println("Build Molecule");
// System.out.println("Update R groups");
//
// }
//
// }
//
// private static RgroupStructure buildMoleculefromCHEM(List<Monomer>
// validMonomers) throws BuilderMoleculeException {
// RgroupStructure structure = new RgroupStructure();
// /* MonomerNotationList or Count should be handled */
// /* a chemical molecule should only contain one monomer */
// if (validMonomers.size() == 1) {
// try {
// if (validMonomers.get(0).getCanSMILES() != null) {
// /* Build monomer + Rgroup information! */
//
// Molecule molecule = new Molecule(validMonomers.get(0).getCanSMILES());
// LOG.info("");
// Map<String, MolAtom> rgroupMap = molecule.getRgroups();
// Map<String, MolAtom> rmap = new HashMap<String, MolAtom>();
// Set keyset = rgroupMap.keySet();
// for (Iterator it = keyset.iterator(); it.hasNext();) {
// String key = (String) it.next();
// rmap.put("1:" + key, (MolAtom) rgroupMap.get(key));
// }
//
// /* Build RgroupStructure */
// structure.setMolecule(molecule);
// structure.setRgroupMap(rmap);
// return structure;
//
// } else {
// LOG.error("Chemical molecule should have canonical smiles");
// throw new BuilderMoleculeException("Chemical molecule should have canoncial
// smiles");
// }
// }
//
// catch (NullPointerException e) {
// throw new BuilderMoleculeException("Monomer is not stored in the monomer
// database");
// }
// } else {
// LOG.error("Chemical molecule should contain exactly one monomer");
// throw new BuilderMoleculeException("Chemical molecule should contain exactly
// one monomer");
// }
// }
////
//// private static RgroupStructure buildMoleculefromPeptideOrRNA(List<Monomer>
//// validMonomers) throws BuilderMoleculeException {
//// RgroupStructure structure = new RgroupStructure();
//// IMoleculeBase currentMolecule;
//// Map<String, IAtomBase> currentRmap;
//// IMoleculeBase molecule;
//// Map<String, IAtomBase> rmap = new HashMap<String, IAtomBase>();
//// IMoleculeBase prevMolecule = null;
////
////
//// if(validMonomers.size() == 0|| validMonomers == null){
//// throw new BuilderMoleculeException("Polymer (Peptide/RNA) has no
// contents");
//// }
//// for (int i = 0; i < validMonomers.size(); i++) {
////
//// if (prevMolecule != null) {
//// currentMolecule = ChemicalToolKit.getTestINSTANCE("").getManipulator();
//// currentMolecule = new IMoleculeBase(validMonomers.get(i).getCanSMILES());
//// currentRmap = currentMolecule.getRgroups();
//// /* BackBone Connection */
//// if
//// (validMonomers.get(i).getMonomerType().equals(Monomer.BACKBONE_MOMONER_TYPE))
//// {
//// LOG.info("Merge the previous with the current Monomer on the right
// attachment
//// and left attachment");
//// prevMolecule =
//// ChemicalToolKit.getINSTANCE().getManipulator().merge(prevMolecule,
//// rmap.get(Attachment.BACKBONE_MONOMER_RIGHT_ATTACHEMENT), currentMolecule,
//// currentRmap.get(Attachment.BACKBONE_MONOMER_RIGHT_ATTACHEMENT));
////
//// LOG.info("Remove the attachments");
//// rmap.remove(Attachment.BACKBONE_MONOMER_RIGHT_ATTACHEMENT);
//// currentRmap.remove(Attachment.BACKBONE_MONOMER_LEFT_ATTACHEMENT);
////
//// LOG.info("Merge unused attachment points");
//// // possible unused R groups on previous backbone
//// /* what to do with the count -> Markus */
//// Set keySet = currentRmap.keySet();
//// for (Iterator it = keySet.iterator(); it.hasNext();) {
//// String key = (String) it.next();
//// int monomercount = i + 1;
//// rmap.put("" + monomercount + ":" + key, (MolAtom) currentRmap.get(key));
//// }
////
//// }
////
//// /* Backbone to Branch Connection */
//// else if
//// (validMonomers.get(i).getMonomerType().equals(Monomer.BRANCH_MOMONER_TYPE))
// {
//// LOG.info("Merge the previous with the current Monomer on the branch
//// attachment point");
//// prevMolecule =
//// ChemicalToolKit.getINSTANCE().getManipulator().merge(prevMolecule,
//// rmap.get(Attachment.BACKBONE_MONOMER_BRANCH_ATTACHEMENT), currentMolecule,
//// currentRmap.get(Attachment.BRANCH_MONOMER_ATTACHEMENT));
////
//// System.out.println("Remove the attachments");
//// LOG.info("Remove the attachments");
//// rmap.remove(Attachment.BACKBONE_MONOMER_BRANCH_ATTACHEMENT);
//// currentRmap.remove(Attachment.BRANCH_MONOMER_ATTACHEMENT);
////
//// System.out.println("Merge unused attachment points");
//// }
////
//// /* Connection is unknown */
//// else {
//// LOG.error("Intra connection is unknown");
//// throw new BuilderMoleculeException("Intra connection is unknown");
//// }
//// }
////
//// /* first monomer */
//// else {
////
//// prevMolecule = new Molecule(validMonomers.get(i).getCanSMILES());
//// rmap.put("1:"
//// + Attachment.BACKBONE_MONOMER_LEFT_ATTACHEMENT, (MolAtom)
//// prevMolecule.getRgroups().get(Attachment.BACKBONE_MONOMER_LEFT_ATTACHEMENT));
////
//// }
////
//// }
////
//// /* last monomer */
//// LOG.info("Set unused R group on the last backbone monomer");
//// // int monomerCount = validMonomers.size();
//// // Map map = structureList.get(prevMonomerIndex).getRgroupMap();
//// // Set keySet = map.keySet();
//// // for (Iterator it = keySet.iterator(); it.hasNext();) {
//// // String key = (String) it.next();
//// // rmap.put(monomerCount + ":" + key, (MolAtom) map.get(key));
////
//// // }
////
//// LOG.info("Return the molecule");
//// structure.setMolecule(prevMolecule);
//// structure.setRgroupMap(rmap);
////
//// return structure;
//// }
//
// }
//
