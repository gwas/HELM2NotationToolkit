package org.helm.notation2;

import java.io.IOException;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom.JDOMException;
import org.testng.annotations.Test;

public class MoleculeInformationTest {
  // @Test
  public void testgetMolecularFormularExamples() throws ExceptionState, IOException, JDOMException, FastaFormatException, AnalogSequenceException, BuilderMoleculeException, CTKException {
    String notation = "CHEM1{[MCC]}|CHEM2{[hxy]}$CHEM1,CHEM2,1:R1-1:R1$$$";
    testMolecularFormular(notation);

  }

  private void testMolecularFormular(String notation) throws ExceptionState, IOException, JDOMException, BuilderMoleculeException, CTKException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    System.out.println(MoleculeInformation.getMolecularFormular(containerhelm2.getHELM2Notation()));
  }
}
