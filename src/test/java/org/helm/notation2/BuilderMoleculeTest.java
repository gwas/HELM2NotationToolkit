package org.helm.notation2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.AbstractChemistryManipulator.OutputType;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
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
  public void testBuildMoleculeFromSinglePolymerBLOBWithException() throws ExceptionState, MonomerException,
      IOException, NotationException, org.jdom2.JDOMException,
      StructureException, CalculationException, HELM2HandledException,
      BuilderMoleculeException, CTKException

  {

    PolymerNotation node = new PolymerNotation("BLOB1");
    BuilderMolecule.buildMoleculefromSinglePolymer(node);

  }

  @Test(expectedExceptions = BuilderMoleculeException.class)
  public void testBuildMoleculeFromSinglePolymerCHEMEmptyWithException() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException,
      StructureException, CalculationException, HELM2HandledException,
      BuilderMoleculeException, CTKException

  {

    PolymerNotation node = new PolymerNotation("CHEM1");
    BuilderMolecule.buildMoleculefromSinglePolymer(node);
  }

  @Test
  public void testBuildMoleculeFromSinglePolymerCHEM() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException,
      StructureException, CalculationException, HELM2HandledException,
      BuilderMoleculeException, CTKException

  {

    PolymerNotation node = new PolymerNotation("CHEM1");
    MonomerNotationUnit mon = new MonomerNotationUnit("[MCC]",
        node.getPolymerID().getType());
    node.getPolymerElements().getListOfElements().add(mon);
    AbstractMolecule molecule =
        BuilderMolecule.buildMoleculefromSinglePolymer(new PolymerNotation(node.getPolymerID(),
            node.getPolymerElements(), ""));
    System.out.println(molecule.getAttachments().size());
    System.out.println(Chemistry.getInstance().getManipulator().getMoleculeInfo(molecule).getMolecularFormula());
  }

  @Test
  public void testBuildMoleculeTwoChems() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    List<AbstractMolecule> molecule = BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
  }

  @Test
  public void testBuildMoleculeThreeChemsWithoutConnection() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, ClassNotFoundException, NoSuchMethodException,
      SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}|CHEM3{[hxy]}$CHEM1,CHEM2,1:R1-1:R1$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    List<AbstractMolecule> molecule = BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
    System.out.println(Chemistry.getInstance().getManipulator().getMoleculeInfo(molecule.get(1)).getMolecularFormula());
    System.out.println(molecule.size());
  }

  @Test
  public void testBuildMoleculeFourChems() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "CHEM1{[MCC]}|CHEM2{[PEG2]}|CHEM3{[EG]}|CHEM4{[MCC]}$CHEM3,CHEM4,1:R1-1:R1|CHEM2,CHEM1,1:R1-1:R1|CHEM2,CHEM3,1:R2-1:R2$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    List<AbstractMolecule> molecules = BuilderMolecule.buildMoleculefromPolymers(helm2container.getHELM2Notation().getListOfPolymers(), helm2container.getHELM2Notation().getListOfConnections());
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C30H40N2O10");
    System.out.println(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()));
  }

  @Test
  public void testBuildMoleculePeptide() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "PEPTIDE1{L.P.G}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C13H23N3O4");
  }

  @Test
  public void testBuildMoleculeRNA() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "RNA1{RP}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);
    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C5H11O8P");

  }

  @Test
  public void testBuildMoleculeRNAExtended() throws ParserException, JDOMException, BuilderMoleculeException, CTKException, IOException, ClassNotFoundException, NoSuchMethodException,
      SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "RNA1{R(A)P.R(G)}$$$$";
    ContainerHELM2 helm2container = readNotation(notation);

    Assert.assertEquals(MoleculeInformation.getMolecularFormular(helm2container.getHELM2Notation()), "C20H25N10O11P");
  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void testBuildMoleculeFromSinglePolymerCHEMUnknownWithException()
      throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException,
      StructureException, CalculationException, HELM2HandledException,
      BuilderMoleculeException, CTKException

  {

    PolymerNotation node = new PolymerNotation("CHEM1");
    MonomerNotationUnit mon = new MonomerNotationUnit("[CZ]",
        node.getPolymerID().getType());
    node.getPolymerElements().getListOfElements().add(mon);
    BuilderMolecule.buildMoleculefromSinglePolymer(new PolymerNotation(node.getPolymerID(), node.getPolymerElements(),
        ""));
  }

  @Test
  public void testBuildMoleculeFromSinglePolymerCHEMSMILES() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException,
      StructureException, CalculationException, HELM2HandledException,
      BuilderMoleculeException, CTKException

  {

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
