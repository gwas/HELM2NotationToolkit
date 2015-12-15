package org.helm.notation2;

import java.io.IOException;
import java.util.List;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
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
  public void testBuildMoleculeFromSinglePolymerBLOBWithException() throws org.helm.notation2.parser.exceptionparser.NotationException, BuilderMoleculeException, HELM2HandledException {
    PolymerNotation node = new PolymerNotation("BLOB1");
    BuilderMolecule.buildMoleculefromSinglePolymer(node);
  }

  @Test(expectedExceptions = BuilderMoleculeException.class)
  public void testBuildMoleculeFromSinglePolymerCHEMEmptyWithException() throws org.helm.notation2.parser.exceptionparser.NotationException, BuilderMoleculeException, HELM2HandledException {
    PolymerNotation node = new PolymerNotation("CHEM1");
    BuilderMolecule.buildMoleculefromSinglePolymer(node);
  }

  @Test
  public void testBuildMoleculeFromSinglePolymerCHEM() throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, BuilderMoleculeException, HELM2HandledException, CTKException {
    PolymerNotation node = new PolymerNotation("CHEM1");
    MonomerNotationUnit mon = new MonomerNotationUnit("[MCC]",
        node.getPolymerID().getType());
    node.getPolymerElements().getListOfElements().add(mon);
    RgroupStructure molecule =
        BuilderMolecule.buildMoleculefromSinglePolymer(new PolymerNotation(node.getPolymerID(),
            node.getPolymerElements(), ""));
    System.out.println(molecule.getMolecule().getAttachments().size());
    System.out.println(Chemistry.getInstance().getManipulator().getMoleculeInfo(molecule.getMolecule()).getMolecularFormula());
  }

  @Test
  public void testBuildMoleculeTwoChems() throws ParserException, JDOMException, BuilderMoleculeException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    List<AbstractMolecule> molecule = BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
  }

  @Test
  public void testBuildMoleculeThreeChemsWithoutConnection() throws ParserException, JDOMException, BuilderMoleculeException, CTKException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}|CHEM3{[hxy]}$CHEM1,CHEM2,1:R1-1:R1$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    List<AbstractMolecule> molecule = BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
    System.out.println(Chemistry.getInstance().getManipulator().getMoleculeInfo(molecule.get(1)).getMolecularFormula());
    System.out.println(molecule.size());
  }

  @Test
  public void testBuildMoleculeFourChems() throws ParserException, JDOMException, BuilderMoleculeException, CTKException {
    String notation = "CHEM1{[MCC]}|CHEM2{[PEG2]}|CHEM3{[EG]}|CHEM4{[MCC]}$CHEM3,CHEM4,1:R1-1:R1|CHEM2,CHEM1,1:R1-1:R1|CHEM2,CHEM3,1:R2-1:R2$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    List<AbstractMolecule> molecules = BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C30H40N2O10");
    System.out.println(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()));
  }

  @Test
  public void testBuildMoleculePeptide() throws ParserException, JDOMException, BuilderMoleculeException, CTKException {
    String notation = "PEPTIDE1{L.P.G}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C13H23N3O4");
  }

  @Test
  public void testBuildMoleculeRNA() throws ParserException, JDOMException, BuilderMoleculeException, CTKException {
    String notation = "RNA1{RP}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C5H11O8P");
  }

  @Test
  public void testBuildMoleculeRNAExtended() throws ParserException, JDOMException, BuilderMoleculeException, CTKException {
    String notation = "RNA1{R(A)P.R(G)}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C20H25N10O11P");
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testBuildMoleculeFromSinglePolymerCHEMUnknownWithException() throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, BuilderMoleculeException,
      HELM2HandledException {

    PolymerNotation node = new PolymerNotation("CHEM1");
    MonomerNotationUnit mon = new MonomerNotationUnit("[CZ]",
        node.getPolymerID().getType());
    node.getPolymerElements().getListOfElements().add(mon);
    BuilderMolecule.buildMoleculefromSinglePolymer(new PolymerNotation(node.getPolymerID(), node.getPolymerElements(),
        ""));
  }

  public void testBuildMoleculeComplexPeptide() throws ParserException, JDOMException, BuilderMoleculeException {
    String notation = "PEPTIDE1{D.F.D}|PEPTIDE2{C}|PEPTIDE3{E.D}$PEPTIDE3,PEPTIDE1,2:R3-1:R3|PEPTIDE2,PEPTIDE1,1:R3-3:R3$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);
    BuilderMolecule.buildMoleculefromPolymers(containerhelm2.getHELM2Notation().getListOfPolymers(), MethodsForContainerHELM2.getAllEdgeConnections(containerhelm2.getHELM2Notation().getListOfConnections()));
    
  }

  @Test
  public void testBuildMoleculeFromSinglePolymerCHEMSMILES() throws org.helm.notation2.parser.exceptionparser.NotationException, IOException, BuilderMoleculeException, HELM2HandledException {

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
