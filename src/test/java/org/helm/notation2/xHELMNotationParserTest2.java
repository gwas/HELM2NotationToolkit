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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.MonomerStore;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.xHelmNotationParser;
import org.helm.notation2.exception.ConnectionNotationException;
import org.helm.notation2.exception.GroupingNotationException;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.exception.PolymerIDsException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import chemaxon.marvin.plugin.PluginException;

public class xHELMNotationParserTest2 {

  private ContainerHELM2 containerhelm2 = null;

  private Element getXHELMRootElement(String resource) throws JDOMException,
      IOException {

    InputStream in = new FileInputStream(resource);

    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(in);

    return doc.getRootElement();
  }

  private void readNotation(String notation) throws ParserException, JDOMException {
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
    containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(), new InterConnections());
  }

  /**
   * method to validate the input HELM-String
   *
   * @param helm
   * @throws ParserException
   * @throws ValidationException
   * @throws org.jdom.JDOMException
   */
  public void validateHELM(String helm) throws ParserException,
      ValidationException, JDOMException {
    /* Read */
    readNotation(helm);

    /* Validate */
    try {
      Validation.validateNotationObjects(containerhelm2);
    } catch (MonomerException | GroupingNotationException | ConnectionNotationException | PolymerIDsException e) {
      throw new ValidationException(e.getMessage());
    }
  }

  @Test
  public void testParseXHelmNotation() throws JDOMException, IOException,
      MonomerException, NotationException, StructureException,
      ClassNotFoundException, PluginException, ParserException,
      ValidationException, HELM1FormatException, JDOMException {

    String workingDir = System.getProperty("user.dir");
    Element xHELMRootElement =
        getXHELMRootElement("src/test/resources/org/helm/notation/tools/resources/PeptideLinkerNucleotide.xhelm");
    String helmString = xHelmNotationParser.getHELMNotationString((xHELMRootElement));

    MonomerFactory monomerFactory = MonomerFactory.getInstance();
    MonomerStore monomerStore = monomerFactory.getMonomerStore(); // read //
                                                                  // monomers to
                                                                  // store

    MonomerStore store = xHelmNotationParser.getMonomerStore(xHELMRootElement);
    updateMonomerStore(store);

    AssertJUnit.assertEquals("RNA1{[am6]P.R(C)P.R(U)P.R(U)P.R(G)P.R(A)P.R(G)P.R(G)}|PEPTIDE1{[aaa].C.G.K.E.D.K.R}|CHEM1{SMCC}$PEPTIDE1,CHEM1,2:R3-1:R2|RNA1,CHEM1,1:R1-1:R1$$$", helmString);

    /* Read + Validate */
    validateHELM(helmString);
    String canonicalNotation = new WebService().convertStandardHELMToCanonicalHELM(helmString);

    AssertJUnit.assertEquals("CHEM1{SMCC}|PEPTIDE1{[aaa].C.G.K.E.D.K.R}|RNA1{[am6]P.R(C)P.R(U)P.R(U)P.R(G)P.R(A)P.R(G)P.R(G)}$CHEM1,PEPTIDE1,1:R2-2:R3|CHEM1,RNA1,1:R1-1:R1$$$", canonicalNotation);



    xHELMRootElement = getXHELMRootElement("src/test/resources/org/helm/notation/tools/resources/simple.xhelm");
    helmString = xHelmNotationParser.getHELMNotationString(xHELMRootElement);

    monomerStore = xHelmNotationParser.getMonomerStore(xHELMRootElement);
    updateMonomerStore(monomerStore);

    AssertJUnit.assertEquals("PEPTIDE1{G.K.A.[A_copy]}$$$$", helmString);
    validateHELM(helmString);

    xHELMRootElement = getXHELMRootElement("src/test/resources/org/helm/notation/tools/resources/InlineSmiles.xhelm");
    helmString = xHelmNotationParser.getComplexNotationString(xHELMRootElement);

    monomerStore = xHelmNotationParser.getMonomerStore(xHELMRootElement);
    updateMonomerStore(monomerStore);
    AssertJUnit.assertEquals("PEPTIDE1{A.A.G.[O[C@@H]([C@H](N[*])C([*])=O)c1ccc2ccccc2c1 |$;;;;_R1;;_R2;;;;;;;;;;;$|].C.T.T}$$$$", helmString);
    validateHELM(helmString);
    xHELMRootElement = getXHELMRootElement("src/test/resources/org/helm/notation/tools/resources/RNAWithInline.xhelm");
    helmString = xHelmNotationParser.getComplexNotationString(xHELMRootElement);
    monomerStore = xHelmNotationParser.getMonomerStore(xHELMRootElement);
    updateMonomerStore(monomerStore);
    AssertJUnit.assertEquals("RNA1{[C[C@@]1([*])O[C@H](CO[*])[C@@H](O[*])[C@H]1O |$;;_R3;;;;;_R1;;;_R2;;$|](A)P.RP.[C[C@@]1([*])O[C@H](CO[*])[C@@H](O[*])[C@H]1O |$;;_R3;;;;;_R1;;;_R2;;$|](T)P.R([Cc1nc2c(nc(N)[nH]c2=O)n1[*] |$;;;;;;;;;;;;_R1$|])P.R([Cc1cc(N)nc(=O)n1[*] |$;;;;;;;;;_R1$|])}$$$$", helmString);
    validateHELM(helmString);
  }

  @Test
  public void testQRPeptide() throws JDOMException, IOException,
      MonomerException, NotationException, StructureException,
      ClassNotFoundException, ParserException, ValidationException, JDOMException {
    Element xHELMRootElement = getXHELMRootElement("src/test/resources/org/helm/notation/tools/resources/qr_peptide.xhelm");
    String helmString = xHelmNotationParser.getComplexNotationString(xHELMRootElement);
    MonomerStore store = xHelmNotationParser.getMonomerStore(xHELMRootElement);
    updateMonomerStore(store);
    AssertJUnit.assertEquals("PEPTIDE1{[QR]}$$$$", helmString);
    validateHELM(helmString);
  }

  private void updateMonomerStore(MonomerStore monomerStore) throws MonomerLoadingException, IOException, MonomerException {
    for (Monomer monomer : monomerStore.getAllMonomersList()) {
      MonomerFactory.getInstance().getMonomerStore().addNewMonomer(monomer);
      // save monomer db to local file after successful update //
      MonomerFactory.getInstance().saveMonomerCache();
    }
  }

}
