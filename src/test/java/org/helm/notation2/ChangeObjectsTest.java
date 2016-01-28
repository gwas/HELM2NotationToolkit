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

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.StructureException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.RNAUtilsException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.exceptionparser.HELM1ConverterException;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;

import org.testng.annotations.Test;

/**
 * ChangeObjectsTest
 *
 * @author hecht
 * @version $Id$
 */
public class ChangeObjectsTest {

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

  @Test
  public void testSimpleMethods() throws ParserException, JDOMException, IOException, NotationException {
    String notation = "PEPTIDE1{A.G}|PEPTIDE2{L.G}$$$$V2.0";
    ContainerHELM2 containerhelm2 = readNotation(notation);

    ChangeObjects.deleteMonomerNotation(1, containerhelm2.getAllPolymers().get(0));
    // ChangeObjects.changePolymerNotation(0,
    // ChangeObjects.deleteMonomerNotation(containerhelm2.getAllPolymers().get(0),
    // 1), containerhelm2);

    MonomerNotation monomerNotation = new MonomerNotationUnit("D", "PEPTIDE");
    monomerNotation.setCount("3");
    ChangeObjects.addMonomerNotation(0, containerhelm2.getAllPolymers().get(0), monomerNotation);
    ChangeObjects.changeMonomerNotation(1, containerhelm2.getHELM2Notation().getListOfPolymers().get(0), monomerNotation);
    ChangeObjects.deleteMonomerNotation(1, containerhelm2.getHELM2Notation().getListOfPolymers().get(0));
    try {
      ChangeObjects.deleteMonomerNotation(0, containerhelm2.getHELM2Notation().getListOfPolymers().get(0));
    } catch (NotationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    PolymerNotation polymer = containerhelm2.getHELM2Notation().getListOfPolymers().get(1);
    ChangeObjects.deletePolymerNotation(1, containerhelm2);

    System.out.println(containerhelm2.getHELM2Notation().toHELM2());

    ChangeObjects.deletePolymerNotation(0, containerhelm2);
    ChangeObjects.addPolymerNotation(0, polymer, containerhelm2);
    ChangeObjects.addPolymerNotation(0, polymer, containerhelm2);

    System.out.println(containerhelm2.getHELM2Notation().toHELM2());
    // containerhelm2 = ChangeObjects.deletePolymerNotation(1, containerhelm2);

    // ChangeObjects.changePolymerNotation(0,
    // ChangeObjects.deleteMonomerNotation(containerhelm2.getAllPolymers().get(0),
    // 0), containerhelm2);
  }

  @Test
  public void testReplaceMonomer() throws NotationException, MonomerLoadingException, MonomerException, JDOMException, IOException, ParserException,
      org.helm.notation2.parser.exceptionparser.NotationException, HELM1ConverterException, StructureException, RNAUtilsException, HELM2HandledException, org.helm.notation.NotationException {

    String notation =
        "RNA1{R(A)P.R(G)P.R(C)P.R(U)P.R(A)P.R(A)P.R(A)P.R(G)P.R(G)}|RNA2{R(C)P.R(C)P.R(U)P.R(U)P.R(U)P.R(A)P.R(G)P.R(C)P.R(U)}$$$$";
    String result = ComplexNotationParser.replaceMonomer(notation, Monomer.NUCLIEC_ACID_POLYMER_TYPE, "P", "sP");
    System.out.println("Result: " + result);

    ContainerHELM2 containerhelm2 = readNotation(notation);
    ChangeObjects.replaceMonomer(containerhelm2, "RNA", "P", "sP");

    System.out.println(containerhelm2.getHELM2Notation().toHELM2());

    /**/
    notation = "PEPTIDE1{A'23'\"jkj\".C.D'12'.E'24'}|PEPTIDE2{G'22'.C.S'8'.P.P.P.P.P.P.P.P.P.K'6'}$$$$V2.0";

    containerhelm2 = readNotation(notation);
    ChangeObjects.replaceMonomer(containerhelm2, "RNA", "N", "G");

    System.out.println(containerhelm2.getHELM2Notation().toHELM2());

    /**/
    notation =
        "PEPTIDE1{(A'3'\"Mutation\".G)'3'.X.G.C.(_,N).(A:10,G:30,R:30).T.C.F.D.W\"mutation\".(A:?+G:1.5).C}|RNA1{R(A)P.((R(N)P)'3'.R(G)P)'4'.(R(G)P)'3-7'\"mutation\"}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G1:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$V2.0";

    containerhelm2 = readNotation(notation);

    System.out.println(containerhelm2.getHELM2Notation().toHELM2());
    ChangeObjects.replaceMonomer(containerhelm2, "PEPTIDE", "N", "G");

    System.out.println(containerhelm2.getHELM2Notation().toHELM2());

  }

}
