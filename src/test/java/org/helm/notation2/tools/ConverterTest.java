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
package org.helm.notation2.tools;

import java.awt.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.Attachment;
import org.helm.notation2.Monomer;
import org.helm.notation2.MonomerFactory;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.MonomerLoadingException;
import org.helm.notation2.exception.NotationException;
import org.helm.notation2.exception.ParserException;
import org.jdom2.JDOMException;
import org.testng.annotations.Test;

/**
 * ConverterTest
 * @author hecht
 *
 */
public class ConverterTest {

	
	
	//@Test
	public void testMonomerConversion() throws ChemistryException, BuilderMoleculeException, CTKException, IOException, NotationException, ParserException, JDOMException{
		Monomer m = MonomerFactory.getInstance().getMonomerStore().getMonomer("PEPTIDE", "A");
		Monomer mn = MonomerFactory.getInstance().getMonomerStore().getMonomer("PEPTIDE", "C");
		String smilesOLD = m.getCanSMILES();
		java.util.List<Attachment> old = m.getAttachmentList();
		   byte[] result = Images.generateImageofMonomer(m, true);
		    if (!Files.exists(Paths.get("test-output"))) {
		      Files.createDirectories(Paths.get("test-output"));
		    }
		    try (FileOutputStream out = new FileOutputStream("test-output" + File.separator + "MonomerTestPictureOld.png")) {
		      out.write(result);
		    }
		    Monomer convertedM = Converter.convertMonomer(m);
		    Monomer n = Converter.convertMonomer(mn);
		
		

		    
		    result = Images.generateImageofMonomer(m, true);
		    if (!Files.exists(Paths.get("test-output"))) {
		      Files.createDirectories(Paths.get("test-output"));
		    }
		    try (FileOutputStream out = new FileOutputStream("test-output" + File.separator + "MonomerTestPictureConversion.png")) {
		      out.write(result);
		    }
		    
		    

		   String notation = "PEPTIDE1{A.A}$$$$V2.0";
		    String smiles = SMILES.getCanonicalSMILESForAll(HELM2NotationUtils.readNotation(notation));
		   System.out.println(smiles);
		   
	}
	
	//@Test
	public void testConversionCompleteMonomerStore() throws MonomerLoadingException, ChemistryException{
		Converter.convertMonomerStore();
		
		
	}
	
}
