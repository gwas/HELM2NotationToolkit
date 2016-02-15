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
package org.helm.notation2.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.helm.notation2.Monomer;
import org.helm.notation2.MonomerStore;
import org.helm.notation2.exception.MonomerException;
import org.helm.notation2.exception.NotationException;
import org.jdom2.JDOMException;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

/**
 * Class to parse the XHELM XML format
 *
 * @author maisel
 *
 */
public class xHelmNotationParser {

  /**
   * Extracts the HELM string from the root node of the XHELM document
   *
   * @param rootElement
   * @return the complex notation string
   */
  public static String getHELMNotationString(Element rootElement) {
    Element helmNotationElement = rootElement.getChild("HelmNotation");
    return helmNotationElement.getText();
  }

  /**
   * Extracts the complex notation string from the root node of the XHELM
   * document
   *
   * @param rootElement
   * @return the complex notation string
   */
  public static String getComplexNotationString(Element rootElement) {
    Element helmNotationElement = rootElement.getChild("HelmNotation");
    return helmNotationElement.getText();
  }

  /**
   * Generates the monomer store from a given XHELM document
   *
   * @param rootElement
   * @return a monomer store
   * @throws MonomerException
   * @throws IOException
   */
  public static MonomerStore getMonomerStore(Element rootElement)
      throws MonomerException, IOException {
    MonomerStore monomerStore = new MonomerStore();
    Element monomerListElement = rootElement.getChild("Monomers");
    if (monomerListElement != null) {
      @SuppressWarnings("unchecked")
      List<Element> elementList = monomerListElement.getChildren("Monomer");

      for (Element monomerElement : elementList) {
        Monomer m = MonomerParser.getMonomer(monomerElement);
        monomerStore.addMonomer(m);
      }
    }
    return monomerStore;
  }

}
