package org.helm.notation2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.helm.chemtoolkit.CTKException;
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
  public void TestGenerationImageOfMonomer() throws CTKException, IOException {
    Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer("RNA", "P");
    byte[] result = Images.generateImageofMonomer(monomer);
    try (FileOutputStream out = new FileOutputStream("test-output\\MonomerTestPicture.png")) {
      out.write(result);
    }
  }

  // @Test
  public void TestGenerationImageOfHELMNotation() throws CTKException, IOException, ParserException, JDOMException, BuilderMoleculeException, ClassNotFoundException, NoSuchMethodException,
      SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "RNA1{R(U)P}|RNA2{R(U)P.R(G)}|RNA3{R(C)P.R(A)}|CHEM1{[MCC]}$RNA1,CHEM1,3:R2-1:R1$RNA2,RNA3,5:pair-2:pair|RNA2,RNA3,2:pair-5:pair$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);
    byte[] result = Images.generateImageHELMMolecule(containerhelm2.getHELM2Notation());
    System.out.println(result);

    try (FileOutputStream out = new FileOutputStream("test-output\\TestGenerationImageOfHELMNotationComplex.png")) {
      out.write(result);
    }
  }

  @Test
  public void TestGenerationImageOfHELMNotationProblemCase() throws CTKException, IOException, ParserException, JDOMException, BuilderMoleculeException, ClassNotFoundException, NoSuchMethodException,
      SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String notation = "RNA1{R(A)P.R(G)}$$$$";
    ContainerHELM2 containerhelm2 = readNotation(notation);
    byte[] result = Images.generateImageHELMMolecule(containerhelm2.getHELM2Notation());
    try (FileOutputStream out = new FileOutputStream("test-output\\TestGenerationImageOfHELMNotationSimple.png")) {
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
