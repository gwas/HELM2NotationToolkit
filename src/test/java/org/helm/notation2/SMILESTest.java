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

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SMILESTest
 *
 * @author hecht
 */
public class SMILESTest {
  ParserHELM2 parser;

  @Test
  public void testSMILES() throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException {
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      parser = new ParserHELM2();

      String test =
          "PEPTIDE1{D}|PEPTIDE2{E}|PEPTIDE3{L.R}|CHEM1{[MCC]}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";
      test += "V2.0";
      parser.parse(test);
      ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(),
          new InterConnections());
      String smile = SMILES.getSMILESForAll(containerhelm2.getHELM2Notation());
      System.out.println(smile);
      String expectedResult = "OC(=O)C1CCC(CN2C(=O)C=CC2=O)CC1.[H]N[C@@H](CCC(=O)C(=O)[C@@H](N[H])CC(=O)C(=O)[C@H](CCCNC(N)=N)NC(=O)[C@@H](N[H])CC(C)C)C(O)=O";
      System.out.println(expectedResult);
      Assert.assertEquals(smile, expectedResult);
    }
  }

  @Test
  public void testSMILESCanonical() throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException {
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      parser = new ParserHELM2();

      String test =
          "PEPTIDE1{D}|PEPTIDE2{E}|PEPTIDE3{L.R}|CHEM1{[MCC]}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";
      test += "V2.0";
      parser.parse(test);
      ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(),
          new InterConnections());
      String canSmile = SMILES.getCanonicalSMILESForAll(containerhelm2.getHELM2Notation());
      Assert.assertEquals(canSmile, "OC(=O)C1CCC(CN2C(=O)C=CC2=O)CC1.CC(C)C[C@H](N)C(=O)N[C@@H](CCCNC(N)=N)C(=O)C(=O)C[C@H](N)C(=O)C(=O)CC[C@H](N)C(O)=O");
    }
  }

  public void testHELM1AgainstHELM2(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException, MonomerException,
      StructureException {
    if (Chemistry.getInstance().getManipulatorType().equals(ManipulatorType.MARVIN)) {
      parser = new ParserHELM2();
      String test = notation;
      test += "V2.0";
      parser.parse(test);
      ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(),
          new InterConnections());
      SMILES.getSMILESForAll(containerhelm2.getHELM2Notation());
      SMILES.getCanonicalSMILESForAll(containerhelm2.getHELM2Notation());

    }
  }

  /* this has to be tested! */
  // @Test
  public void testSelfCycle() throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException, NotationException, MonomerException, StructureException {
    // backbone cyclic peptide
    String notation = "PEPTIDE1{A.A.G.K}$PEPTIDE1,PEPTIDE1,1:R1-4:R2$$$";
    testHELM1AgainstHELM2(notation);

    // branch cyclic RNA
    notation = "RNA1{R(C)P.RP.R(A)P.RP.R(A)P.R(U)P}$RNA1,RNA1,4:R3-9:R3$$$";
    testHELM1AgainstHELM2(notation);

    // backbone cyclic RNA
    notation = "RNA1{R(C)P.RP.R(A)P.RP.R(A)P.R(U)P}$RNA1,RNA1,1:R1-16:R2$$$";
    testHELM1AgainstHELM2(notation);

    // backbone and branch cyclic RNA
    notation = "RNA1{R(C)P.RP.R(A)P.RP.R(A)P.R(U)P}$RNA1,RNA1,4:R3-9:R3|RNA1,RNA1,1:R1-16:R2$$$";
    testHELM1AgainstHELM2(notation);

    // cyclic chem
    notation = "CHEM1{SS3}|CHEM2{SS3}$CHEM1,CHEM2,1:R1-1:R1|CHEM1,CHEM2,1:R2-1:R2$$$";
    testHELM1AgainstHELM2(notation);

    // peptide-chem cycles
    notation = "PEPTIDE1{H.H.E.E.E}|CHEM1{SS3}|CHEM2{EG}$PEPTIDE1,CHEM2,5:R2-1:R2|CHEM2,CHEM1,1:R1-1:R2|PEPTIDE1,CHEM1,1:R1-1:R1$$$";
    testHELM1AgainstHELM2(notation);

    // multiple peptide-chem cycles
    notation =
        "PEPTIDE1{E.E.E.E.E}|PEPTIDE2{E.D.D.I.A.C.D.E}|CHEM1{SS3}|CHEM2{SS3}|CHEM3{SS3}$PEPTIDE2,CHEM2,8:R2-1:R1|PEPTIDE1,CHEM3,5:R2-1:R2|PEPTIDE1,CHEM1,1:R1-1:R1|PEPTIDE2,CHEM3,1:R1-1:R1|CHEM1,CHEM2,1:R2-1:R2$$$";
    testHELM1AgainstHELM2(notation);
  }

}
