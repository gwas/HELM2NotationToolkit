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
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.StateMachineParser;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.exceptionparser.HELM1ConverterException;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCanonicalHELM {

  StateMachineParser parser;

  @Test
  public void testCanonicalSHELM() throws HELM1FormatException, ExceptionState, IOException, JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test = "PEPTIDE1{C}|PEPTIDE2{Y.V.N.L.I}$PEPTIDE2,PEPTIDE1,5:R2-2:R3$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);
  }

  @Test
  public void testCanonicalHELMExtended() throws ExceptionState, IOException, JDOMException, HELM1FormatException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|RNA3{R(A)P.R(C)}$RNA2,RNA3,5:pair-2:pair|RNA2,RNA3,2:pair-5:pair$$$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);
  }

  @Test
  public void testCanonicalHELMExtended2() throws ClassNotFoundException, HELM1FormatException, NotationException,
      MonomerException, IOException, StructureException, org.jdom2.JDOMException,
      ExceptionState, JDOMException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{R.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    Assert.assertEquals(HELM1Utils.getCanonical(parser.notationContainer), ComplexNotationParser.getCanonicalNotation(test, false) + "V2.0");
  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMExtendedWithCounts() throws HELM1FormatException, ExceptionState, IOException,
      JDOMException, NotationException, ChemistryException {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{R'3'.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMExtendedWithCountsWithException() throws HELM1FormatException, ExceptionState,
      IOException, JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{R'3-5'.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMExtendedWithMonomerNotationList() throws HELM1FormatException, ExceptionState,
      IOException, JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{(R.T.L)'3'.I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMExtendedWithMonomerNotationGroup() throws HELM1FormatException, ExceptionState,
      IOException, JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{(R,T).I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testCanonicalHELMExtendedWithBLOB() throws HELM1FormatException, ExceptionState, IOException,
      JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|BLOB1{Hallo}|PEPTIDE1{I.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMExtendedWithUnknownMonomer() throws HELM1FormatException, ExceptionState, IOException,
      JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(N)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{_.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);
  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMExtendedWithUnknownMonomerTypeforPeptide() throws HELM1FormatException, ExceptionState,
      IOException, JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE1{X.P}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);
  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMExtendedWithUnknownMonomerTypeforRNA() throws HELM1FormatException, ExceptionState,
      IOException, JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(N)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{C}$PEPTIDE3,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);
  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testCanonicalHELMConnectionWithGroup() throws HELM1FormatException, ExceptionState, IOException,
      JDOMException, NotationException, ChemistryException

  {
    parser = new StateMachineParser();

    String test = "RNA1{R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{C}$G1,PEPTIDE2,1:R2-2:R3$$$";
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);
  }

  @Test
  public void testCanonicalHELMConnection() throws HELM1FormatException, ExceptionState, IOException, JDOMException,
      ClassNotFoundException, NotationException, MonomerException, StructureException,
      org.jdom2.JDOMException, ChemistryException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{C}|RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{R.I.P}|RNA3{R(A)P.R(C)}$PEPTIDE1,PEPTIDE2,1:R2-2:R3|RNA2,RNA3,2:pair-5:pair|RNA2,RNA3,5:pair-2:pair$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    Assert.assertEquals(HELM1Utils.getCanonical(parser.notationContainer), ComplexNotationParser.getCanonicalNotation("PEPTIDE1{C}|RNA1{R(C)P.R(T)P.R(G)}|RNA2{R(G)P.R(T)}|PEPTIDE2{R.E}|PEPTIDE3{R.I.P}|RNA3{R(A)P.R(C)}$PEPTIDE1,PEPTIDE2,1:R2-2:R3$RNA2,RNA3,2:pair-5:pair|RNA2,RNA3,5:pair-2:pair$$", false)
        + "V2.0");
  }

  @Test
  public void testCanonicalHELMCHEM() throws HELM1FormatException, ExceptionState, IOException, JDOMException, NotationException, ChemistryException {
    parser = new StateMachineParser();

    String test = "CHEM1{MCC}$$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test
  public void testCanonicalSMILES() throws HELM1FormatException, ExceptionState, IOException, JDOMException, NotationException, ChemistryException {

    parser = new StateMachineParser();

    String test = "CHEM1{[[*]OCCOCCOCCO[*] |$_R1;;;;;;;;;;;_R3$|]}$$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test
  public void testCanonicalSMILES2() throws ExceptionState, IOException,
      JDOMException, ClassNotFoundException, HELM1ConverterException,
      MonomerException, org.jdom2.JDOMException, CTKSmilesException, CTKException, NotationException,
      StructureException, HELM1FormatException, ChemistryException {

    /* add monomer to the database -> */
    parser = new StateMachineParser();

    String test = "CHEM1{[CC([*])C(=O)NCCC(=O)NCCOCCOCCC([*])=O |$;;_R1;;;;;;;;;;;;;;;;;;_R2;$|]}$$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getCanonical(parser.notationContainer);

  }

  @Test
  public void testStandardHELM() throws ExceptionState, IOException, JDOMException, HELM1FormatException,
      CTKSmilesException, HELM1ConverterException, MonomerException, org.jdom2.JDOMException,
      CTKException, NotationException, ValidationException, ChemistryException {

    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{D.F.L.I}|PEPTIDE2{G}|CHEM1{[MCC]}|RNA1{R(G)P.R(T)P.R(T)}|RNA2{R(A)P.R(A)P.R(C)}$PEPTIDE2,PEPTIDE1,1:R2-1:R3|RNA1,RNA2,8:pair-2:pair|RNA1,RNA2,2:pair-8:pair|RNA1,RNA2,5:pair-5:pair$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getStandard(parser.notationContainer);

  }

  @Test(expectedExceptions = HELM1FormatException.class)
  public void testStandardHELMWithException() throws ExceptionState, IOException, JDOMException, HELM1FormatException,
      CTKSmilesException, HELM1ConverterException, MonomerException,
      org.jdom2.JDOMException,
      CTKException, NotationException, ValidationException, ChemistryException {

    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{N.F.L.I}|PEPTIDE2{G}|CHEM1{[MCC]}|RNA1{R(G)P.R(T)P.R(T)}|RNA2{R(A)P.R(A)P.R(C)}$PEPTIDE2,PEPTIDE1,1:R2-1:R3|RNA1,RNA2,8:pair-2:pair|RNA1,RNA2,2:pair-8:pair|RNA1,RNA2,5:pair-?:pair$$$";
    for (int i = 0; i < test.length(); i++) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    HELM1Utils.getStandard(parser.notationContainer);

  }

  @Test
  public void testOldExamples() throws CTKSmilesException, ExceptionState, IOException, JDOMException,
      HELM1ConverterException, org.jdom2.JDOMException, CTKException,
      HELM1FormatException, NotationException, MonomerException, ValidationException, ChemistryException {
    String notation =
        "PEPTIDE1{H.H.E.E.E}|CHEM1{SS3}|CHEM2{EG}$PEPTIDE1,CHEM2,5:R2-1:R2|CHEM2,CHEM1,1:R1-1:R2|PEPTIDE1,CHEM1,1:R1-1:R1$$$";
    testGetCanonicalNotation(notation);

    // change node order
    notation =
        "CHEM1{SS3}|PEPTIDE1{H.H.E.E.E}|CHEM2{EG}$PEPTIDE1,CHEM2,5:R2-1:R2|CHEM2,CHEM1,1:R1-1:R2|PEPTIDE1,CHEM1,1:R1-1:R1$$$";
    testGetCanonicalNotation(notation);

    // change edge order
    notation =
        "CHEM1{SS3}|PEPTIDE1{H.H.E.E.E}|CHEM2{EG}$CHEM2,CHEM1,1:R1-1:R2|PEPTIDE1,CHEM2,5:R2-1:R2|PEPTIDE1,CHEM1,1:R1-1:R1$$$";
    testGetCanonicalNotation(notation);

    // change edge direction
    notation =
        "CHEM1{SS3}|PEPTIDE1{H.H.E.E.E}|CHEM2{EG}$CHEM2,CHEM1,1:R1-1:R2|CHEM2,PEPTIDE1,1:R2-5:R2|PEPTIDE1,CHEM1,1:R1-1:R1$$$";
    testGetCanonicalNotation(notation);

    // change node id
    notation =
        "CHEM4{SS3}|PEPTIDE2{H.H.E.E.E}|CHEM2{EG}$CHEM2,CHEM4,1:R1-1:R2|CHEM2,PEPTIDE2,1:R2-5:R2|PEPTIDE2,CHEM4,1:R1-1:R1$$$";
    testGetCanonicalNotation(notation);

    // backbone cyclic peptide
    notation = "PEPTIDE1{K.A.A.G.K}$PEPTIDE1,PEPTIDE1,1:R1-5:R2$$$";
    testGetCanonicalNotation(notation);

    // annotated peptides
    notation =
        "PEPTIDE1{K.A.A.G.K}|PEPTIDE2{K.A.A.G.K}|RNA1{R(A)P.R(G)}|CHEM1{Alexa}$PEPTIDE1,PEPTIDE1,1:R1-5:R2$$PEPTIDE1{hc}|PEPTIDE2{lc}$";
    testGetCanonicalNotation(notation);

    // chemical connection
    notation =
        "RNA1{R(A)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(C)P.R(C)P.R(C)P.R(C)}$RNA1,CHEM1,1:R1-1:R1$$$";
    testGetCanonicalNotation(notation);

    notation =
        "RNA1{R(A)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(G)P.R(C)P.R(C)P.R(C)P.R(C)}|PEPTIDE1{A.G.G.G.K.K.K.K}$PEPTIDE1,CHEM2,8:R2-1:R1|RNA1,CHEM1,1:R1-1:R1$$$";
    testGetCanonicalNotation(notation);

  }

  /**
   * @param notation
   * @throws JDOMException
   * @throws IOException
   * @throws ExceptionState
   * @throws org.jdom2.JDOMException
   * @throws CTKException
   * @throws MonomerException
   * @throws HELM1ConverterException
   * @throws CTKSmilesException
   * @throws HELM1FormatException
   * @throws NotationException
   * @throws ValidationException
   * @throws ChemistryException
   */
  private void testGetCanonicalNotation(String notation) throws ExceptionState, IOException, JDOMException,
      CTKSmilesException, HELM1ConverterException, MonomerException, org.jdom2.JDOMException,
      CTKException, HELM1FormatException, NotationException, ValidationException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    HELM1Utils.getStandard(parser.notationContainer);
  }

}
