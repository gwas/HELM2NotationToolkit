package org.helm.notation2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.calculation.ExtinctionCoefficient;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MoleculeInformationTest {

  @Test
  public void testgetMolecularFormularExamples() throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException {
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    String result = testMolecularFormular(notation);
    Assert.assertEquals(result, "C16H20N4O4");
  }

  @Test
  public void testgetExactMass() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException {
    Double resultEditor = (double) 332.15;
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    Assert.assertEquals(BigDecimal.valueOf(testExactMass(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
  }

  @Test
  public void testgetMolecularWeight() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException {
    Double resultEditor = (double) 332.35;
    String notation = "CHEM1{[MCC]}|CHEM2{[Az]}$CHEM2,CHEM1,1:R1-1:R1$$$";
    Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
  }

  @Test
  public void testgetMolecularWeightWithSMILES() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException {
    Double resultEditor = (double) 106.12;
    String notation = "CHEM1{[[*]OCCOCCO[*] |$_R1;;;;;;;;_R2$|]}$$$$";
    Assert.assertEquals(BigDecimal.valueOf(testMolecularWeight(notation)).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), resultEditor.toString());
  }

  private String testMolecularFormular(String notation) throws ExceptionState, IOException, JDOMException,
      BuilderMoleculeException, CTKException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    return MoleculeInformation.getMolecularFormular(containerhelm2.getHELM2Notation());
  }

  private Double testExactMass(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    return MoleculeInformation.getExactMass(containerhelm2.getHELM2Notation());

  }

  private Double testMolecularWeight(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    return MoleculeInformation.getMolecularWeight(containerhelm2.getHELM2Notation());

  }
}
