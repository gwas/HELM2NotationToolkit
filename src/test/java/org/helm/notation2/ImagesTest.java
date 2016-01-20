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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.helm.notation.MonomerFactory;
import org.helm.notation.model.Monomer;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.JDOMException;
import org.testng.annotations.Test;

public class ImagesTest {

  @Test
  public void TestGenerationImageOfMonomer() throws BuilderMoleculeException, CTKException, FileNotFoundException, IOException {
    Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer("RNA", "P");
    byte[] result = Images.generateImageofMonomer(monomer);
    try (FileOutputStream out = new FileOutputStream("test-output\\MonomerTestPicture.png")) {
      out.write(result);
    }
  }

  @Test
  public void TestGenerationImageOfHELMNotation() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException {
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      String notation = "RNA1{R(U)P}|RNA2{R(U)P.R(G)}|RNA3{R(C)P.R(A)}|CHEM1{[MCC]}$RNA1,CHEM1,3:R2-1:R1|RNA2,RNA3,5:pair-2:pair|RNA2,RNA3,2:pair-5:pair$$$";
      ContainerHELM2 containerhelm2 = readNotation(notation);
      byte[] result = Images.generateImageHELMMolecule(containerhelm2.getHELM2Notation());

      try (FileOutputStream out = new FileOutputStream("test-output\\TestGenerationImageOfHELMNotationComplex.png")) {
        out.write(result);
      }
    }
  }

  @Test
  public void TestGenerationImageOfHELMNotationPEPTIDEComplex() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException {
    String notation = "PEPTIDE1{D.F.D}|PEPTIDE2{C}|PEPTIDE3{E.D}$PEPTIDE3,PEPTIDE1,2:R3-1:R3|PEPTIDE2,PEPTIDE1,1:R3-3:R3$$$";
    // String notation =
    // "PEPTIDE1{D.F.D}|PEPTIDE2{C}$PEPTIDE2,PEPTIDE1,1:R3-3:R3$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);
    byte[] result = Images.generateImageHELMMolecule(containerhelm2.getHELM2Notation());

    try (FileOutputStream out = new FileOutputStream("test-output\\TestGenerationImageOfHELMNotationPEPTIDeComplex.png")) {
      out.write(result);
    }
  }



  @Test
  public void TestGenerationImageOfHELMNotationProblemCase() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException {
    String notation = "RNA1{R(A)P.R(G)}$$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);
    byte[] result = Images.generateImageHELMMolecule(containerhelm2.getHELM2Notation());
    try (FileOutputStream out = new FileOutputStream("test-output\\TestGenerationImageOfHELMNotationSimple.png")) {
      out.write(result);
    }
  }


  @Test
  public void TestGenerationImageOfHELMNotationSimpleCase() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException {
    String notation = "PEPTIDE1{G.G.G}$$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);
    byte[] result = Images.generateImageHELMMolecule(containerhelm2.getHELM2Notation());
    try (FileOutputStream out = new FileOutputStream("test-output\\TestGenerationImageOfHELMNotationSimpleCase.png")) {
      out.write(result);
    }
  }

  private ContainerHELM2 readNotation(String notation) throws ParserException, JDOMException {
    /* HELM1-Format -> */
    if (!(notation.contains("V2.0"))) {
      notation = new ConverterHELM1ToHELM2().doConvert(notation);
    }
    /* parses the HELM notation and generates the necessary notation objects */
    ParserHELM2 parser = new ParserHELM2();
    try {
      parser.parse(notation);
    } catch (ExceptionState | IOException e) {
      throw new ParserException(e.getMessage());
    }
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(), new InterConnections());
    return containerhelm2;
  }
}
