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
import java.util.List;

import org.helm.notation.model.Monomer;
import org.helm.notation.tools.StructureParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

  protected String getSMILES(List<Monomer> monomerlist) throws IOException {
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

  protected void getCanonicalSmilesForAll() {

  }


}

