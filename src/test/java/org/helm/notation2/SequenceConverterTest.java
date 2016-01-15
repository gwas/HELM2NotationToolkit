
package org.helm.notation2;

import java.io.IOException;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.NotationException;
import org.helm.notation.NucleotideLoadingException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.NucleotideConverter;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SequenceConverterTest
 * 
 * 
 * @author hecht
 */
public class SequenceConverterTest {


  @Test
  public void getNucleotideSequenceFromNotation() throws NucleotideLoadingException, MonomerLoadingException, NotationException, MonomerException, IOException, JDOMException, StructureException,
      org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, ParserException, RNAUtilsException {
    String notation = "RNA1{R(T)P.R(G)P.R(U)}$$$$";
    Assert.assertEquals(SequenceConverter.getNucleotideSequenceFromNotation(readNotation(notation).getHELM2Notation()), NucleotideConverter.getInstance().getNucleotideSequencesFromComplexNotation(notation));
    notation = "RNA1{[dR](U)P.R(T)P.R(G)P.R(U)}$$$$";
    Assert.assertEquals(SequenceConverter.getNucleotideSequenceFromNotation(readNotation(notation).getHELM2Notation()), NucleotideConverter.getInstance().getNucleotideSequencesFromComplexNotation(notation));

  }

  @Test(expectedExceptions = HELM2HandledException.class)
  public void getNucleotideSequenceFromNotationWithException() throws NucleotideLoadingException, MonomerLoadingException, NotationException, MonomerException, IOException, JDOMException, StructureException,
      org.helm.notation2.parser.exceptionparser.NotationException, HELM2HandledException, ParserException, RNAUtilsException {
    String notation = "RNA1{R(T)P.R(G)P.(R(U))'3'}$$$$";
    System.out.println(SequenceConverter.getNucleotideSequenceFromNotation(readNotation(notation).getHELM2Notation()));
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
