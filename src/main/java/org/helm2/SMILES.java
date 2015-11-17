
package org.helm2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.model.Attachment;
import org.helm.notation.model.Monomer;
import org.helm.notation.model.PolymerEdge;
import org.helm.notation.model.PolymerNode;
import org.helm.notation.model.RgroupStructure;
import org.helm.notation.tools.MonomerParser;
import org.helm.notation.tools.SimpleNotationParser;
import org.helm.notation.tools.StructureParser;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.struc.MolAtom;
import chemaxon.struc.Molecule;

/**
 * SMILES
 * 
 * 
 * @author hecht
 */
public class SMILES {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(SMILES.class);

  public String getSMILESForAll(ArrayList<Monomer> monomerlist) throws IOException {
    StringBuffer sb = new StringBuffer();
    for (Monomer element : monomerlist) {
      String smi = element.getCanSMILES();
      if(sb.length()>0){
        sb.append(".");
      }
      sb.append(smi);
    }
    String mixtureSmiles = sb.toString();
    
    Molecule mol = StructureParser.getMolecule(mixtureSmiles);
    return mol.toFormat("smiles:u");
    
    
  }

  public void getCanonicalSmilesForAll() {

  }


}
