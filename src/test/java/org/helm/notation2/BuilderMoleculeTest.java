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

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
import org.helm.notation.NotationException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BuilderMoleculeTest {

  @Test(expectedExceptions = BuilderMoleculeException.class)
  public void testBuildMoleculeFromSinglePolymerBLOBWithException() throws org.helm.notation2.parser.exceptionparser.NotationException, BuilderMoleculeException, HELM2HandledException,
      NotationException {
    PolymerNotation node = new PolymerNotation("BLOB1");
    BuilderMolecule.buildMoleculefromSinglePolymer(node);
  }

  @Test(expectedExceptions = BuilderMoleculeException.class)
  public void testBuildMoleculeFromSinglePolymerCHEMEmptyWithException() throws org.helm.notation2.parser.exceptionparser.NotationException, BuilderMoleculeException, HELM2HandledException,
      NotationException {
    PolymerNotation node = new PolymerNotation("CHEM1");
    BuilderMolecule.buildMoleculefromSinglePolymer(node);
  }

  @Test
  public void testBuildMoleculeFromSinglePolymerCHEM() throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, BuilderMoleculeException, HELM2HandledException, CTKException,
      NotationException {
    PolymerNotation node = new PolymerNotation("CHEM1");
    MonomerNotationUnit mon = new MonomerNotationUnit("[MCC]",
        node.getPolymerID().getType());
    node.getPolymerElements().getListOfElements().add(mon);
    RgroupStructure molecule =
        BuilderMolecule.buildMoleculefromSinglePolymer(new PolymerNotation(node.getPolymerID(),
            node.getPolymerElements(), ""));
    Assert.assertEquals(molecule.getMolecule().getAttachments().size(), 1);
    AbstractMolecule mol = BuilderMolecule.mergeRgroups(molecule.getMolecule());
    Assert.assertEquals(Chemistry.getInstance().getManipulator().getMoleculeInfo(mol).getMolecularFormula(), "C12H15NO4");
  }

  @Test
  public void testBuildMoleculeTwoChems() throws ParserException, JDOMException, BuilderMoleculeException, NotationException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
  }

  @Test
  public void testBuildMoleculeThreeChemsWithoutConnection() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}|CHEM3{[hxy]}$CHEM1,CHEM2,1:R1-1:R1$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    List<AbstractMolecule> molecule = BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
    Assert.assertEquals(molecule.size(), 2);
  }

  @Test
  public void testBuildMoleculeFourChems() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    String notation = "CHEM1{[MCC]}|CHEM2{[PEG2]}|CHEM3{[EG]}|CHEM4{[MCC]}$CHEM3,CHEM4,1:R1-1:R1|CHEM2,CHEM1,1:R1-1:R1|CHEM2,CHEM3,1:R2-1:R2$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C30H40N2O10");
  }

  @Test
  public void testBuildMoleculePeptide() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    String notation = "PEPTIDE1{L.P}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C11H20N2O3");
  }

  @Test
  public void testBuildMoleculeRNA() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    String notation = "RNA1{R(A)P}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C10H14N5O7P");
  }

  @Test
  public void testBuildMoleculeRNAExtended() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, NotationException {
    String notation = "RNA1{R(A)P.R(G)}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C20H25N10O11P");
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testBuildMoleculeFromSinglePolymerCHEMUnknownWithException() throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, BuilderMoleculeException,
      HELM2HandledException, NotationException {

    PolymerNotation node = new PolymerNotation("CHEM1");
    MonomerNotationUnit mon = new MonomerNotationUnit("[CZ]",
        node.getPolymerID().getType());
    node.getPolymerElements().getListOfElements().add(mon);
    BuilderMolecule.buildMoleculefromSinglePolymer(new PolymerNotation(node.getPolymerID(), node.getPolymerElements(),
        ""));
  }

  // @Test
  public void testBuildMoleculeComplexPeptide() throws ParserException, JDOMException, BuilderMoleculeException, NotationException {
    // String notation =
    // "PEPTIDE1{D.F.D}|PEPTIDE2{C}|PEPTIDE3{E.D}$PEPTIDE3,PEPTIDE1,2:R3-1:R3|PEPTIDE2,PEPTIDE1,1:R3-3:R3$$$";
    String notation = "RNA1{R(C)P.RP.R(A)P.RP.R(A)P.R(U)P}$RNA1,RNA1,4:R3-9:R3|RNA1,RNA1,1:R1-16:R2$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);
    BuilderMolecule.buildMoleculefromPolymers(containerhelm2.getHELM2Notation().getListOfPolymers(), MethodsForContainerHELM2.getAllEdgeConnections(containerhelm2.getHELM2Notation().getListOfConnections()));

  }

  @Test
  public void testBuildMoleculeFromSinglePolymerCHEMSMILES() throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, BuilderMoleculeException, HELM2HandledException,
      NotationException {

    PolymerNotation node = new PolymerNotation("CHEM1");
    MonomerNotationUnit mon = new MonomerNotationUnit("[OC(=O)C1CCC(CN2C(=O)C=CC2=O)CC1]",
        node.getPolymerID().getType());
    node.getPolymerElements().getListOfElements().add(mon);
    BuilderMolecule.buildMoleculefromSinglePolymer(new PolymerNotation(node.getPolymerID(), node.getPolymerElements(),
        ""));
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
