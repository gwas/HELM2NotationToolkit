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
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.tools.ComplexNotationParser;
import org.helm.notation2.ContainerHELM2;
import org.helm.notation2.InterConnections;
import org.helm.notation2.MethodsForContainerHELM2;
import org.helm.notation2.SMILES;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom.JDOMException;
import org.testng.annotations.Test;

/**
 * SMILESTest
 * 
 * @author hecht
 */
public class SMILESTest {
  ParserHELM2 parser;

  @Test
  public void testSMILES() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException, CTKException {
    parser = new ParserHELM2();

    String test = "PEPTIDE1{D}|PEPTIDE2{E}|PEPTIDE3{L.R}|CHEM1{[MCC]}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";
    test += "V2.0";
    parser.parse(test);
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(),
        new InterConnections());
    SMILES smiles = new SMILES();
    String smile = smiles.getSMILES(MethodsForContainerHELM2.getListOfHandledMonomers(MethodsForContainerHELM2.getListOfMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers())));
  }

  @Test
  public void testSMILESOLD() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException {

    String test = "PEPTIDE1{D}|PEPTIDE2{E}|PEPTIDE3{L.R}|CHEM1{[MCC]}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";

    String smiles = ComplexNotationParser.getComplexPolymerSMILES(test);
  }

  @Test
  public void testcanonical() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException, ClassNotFoundException {

    String test = "PEPTIDE1{D}|PEPTIDE3{L.R}|PEPTIDE2{E}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";
    MonomerStore store = new MonomerStore();
    String canonicalNotation = ComplexNotationParser.getCanonicalNotation(test, false, store);

  }


  public void testcanonicalCHEM() throws ExceptionState, IOException, JDOMException, NotationException, MonomerException, org.jdom2.JDOMException, StructureException, CalculationException,
      HELM2HandledException, ClassNotFoundException {

    String test = "PEPTIDE1{D}|PEPTIDE2{E}|PEPTIDE3{L.R}|CHEM1{[MCC]}$PEPTIDE1,PEPTIDE2,1:R2-1:R3|PEPTIDE3,PEPTIDE1,2:R2-1:R3$$$";
    MonomerStore store = new MonomerStore();
    String canonicalNotation = ComplexNotationParser.getCanonicalNotation(test, true, store);

  }

}
