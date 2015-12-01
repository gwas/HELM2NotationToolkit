package org.helm.notation2;

import java.io.IOException;

import org.helm.notation.MonomerException;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom.JDOMException;
import org.testng.annotations.Test;

public class xHELMTest {

  @Test
  public void testxHELMExamples() throws ExceptionState, IOException, JDOMException, FastaFormatException, AnalogSequenceException, MonomerException, HELM1FormatException {
    String notation = "RNA1{R(U)P.R(T)P.R(G)P.R(C)}$$$$";
    testxHELM1(notation);

    notation = "PEPTIDE1{(A.G).L}$$$$";
    testxHELM2(notation);

    notation = "PEPTIDE1{[dF].[dN].[dL]}$$$$";
    testxHELM1(notation);
  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testxHELMWithException() throws ExceptionState, IOException, JDOMException, FastaFormatException, AnalogSequenceException, MonomerException, HELM1FormatException {
    String notation;

    notation = "PEPTIDE1{(A+G).L}$$$$";
    testxHELM1(notation);

  }

  private void testxHELM2(String notation) throws ExceptionState, IOException, JDOMException, MonomerException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    System.out.println(xHELM.getXHELM2(containerhelm2));
  }

  private void testxHELM1(String notation) throws ExceptionState, IOException, JDOMException, MonomerException, HELM1FormatException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    System.out.println(xHELM.getXHELM(containerhelm2));
  }
}
