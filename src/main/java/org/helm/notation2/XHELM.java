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
import java.util.HashSet;
import java.util.Set;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.MonomerException;
import org.helm.notation.MonomerFactory;
import org.helm.notation.NotationException;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.MonomerParser;
import org.helm.notation.tools.xHelmNotationExporter;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * xHELM
 *
 * @author hecht
 */
public final class XHELM {

  private static Set<Monomer> set = null;

  /**
   * Default constructor.
   */
  private XHELM() {

  }

  /**
   * method to get xhelm for the helm2 notation with the new functionality
   *
   * @param containerhelm2, helm's notations objects
   * @return xhelm
   * @throws MonomerException
   * @throws JDOMException
   * @throws IOException
   */
  protected static String getXHELM2(ContainerHELM2 containerhelm2) throws MonomerException, IOException, JDOMException {
    set = new HashSet<Monomer>();
    Element root = new Element(xHelmNotationExporter.XHELM_ELEMENT);

    Document doc = new Document(root);

    Element helmElement = new Element(xHelmNotationExporter.HELM_NOTATION_ELEMENT);
    helmElement.setText(containerhelm2.getHELM2Notation().toHELM2());

    root.addContent(helmElement);

    Element monomerListElement = new Element(xHelmNotationExporter.MONOMER_LIST_ELEMENT);

    /* save all adhocMonomers */
    for (MonomerNotation monomernotation : MethodsForContainerHELM2.getListOfMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers())) {
      /* get all elements of an rna */
      if (monomernotation instanceof MonomerNotationUnitRNA) {
        for (MonomerNotationUnit unit : ((MonomerNotationUnitRNA) monomernotation).getContents()) {
          addAdHocMonomer(unit);
        }
      } else {
        addAdHocMonomer(monomernotation);

      }

    }
    /* give the adhocMonomer's information */
    for (Monomer distinctmonomer : set) {
      Element monomerElement = MonomerParser.getMonomerElement(distinctmonomer);
      monomerListElement.getChildren().add(monomerElement);
    }

    root.addContent(monomerListElement);
    XMLOutputter xmlOutput = new XMLOutputter();
    // display nice
    xmlOutput.setFormat(Format.getPrettyFormat());
    return xmlOutput.outputString(doc);
  }

  /**
   * method to get xhelm for the helm notation, only if it was possible to
   * convert the helm in the old format
   *
   * @param containerhelm2, helm's notations objects
   * @return xhelm
   * @throws MonomerException
   * @throws HELM1FormatException
   * @throws JDOMException
   * @throws IOException
   * @throws NotationException
   * @throws CTKException
   * @throws ValidationException
   */
  protected static String getXHELM(ContainerHELM2 containerhelm2) throws MonomerException, HELM1FormatException,
      IOException, JDOMException, NotationException, CTKException, ValidationException {
    set = new HashSet<Monomer>();
    Element root = new Element(xHelmNotationExporter.XHELM_ELEMENT);

    Document doc = new Document(root);

    Element helmElement = new Element(xHelmNotationExporter.HELM_NOTATION_ELEMENT);
    helmElement.setText(HELM1Utils.getStandard(containerhelm2.getHELM2Notation()));

    root.addContent(helmElement);

    Element monomerListElement = new Element(xHelmNotationExporter.MONOMER_LIST_ELEMENT);

    /* save all adhocMonomers in the set */
    for (MonomerNotation monomernotation : MethodsForContainerHELM2.getListOfMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers())) {
      /* get all elements of an rna */
      if (monomernotation instanceof MonomerNotationUnitRNA) {
        for (MonomerNotationUnit unit : ((MonomerNotationUnitRNA) monomernotation).getContents()) {
          addAdHocMonomer(unit);
        }
      } else {
        addAdHocMonomer(monomernotation);
      }
    }

    /* give adhoc monomer's information */
    for (Monomer distinctmonomer : set) {
      Element monomerElement = MonomerParser.getMonomerElement(distinctmonomer);
      monomerListElement.getChildren().add(monomerElement);
    }

    root.addContent(monomerListElement);

    XMLOutputter xmlOutput = new XMLOutputter();
    // display nice
    xmlOutput.setFormat(Format.getPrettyFormat());

    return xmlOutput.outputString(doc);

  }

  /**
   * method to add the monomer to the database if it is an adhoc monomer
   *
   * @param monomerNotation MonomerNotation
   * @throws JDOMException
   * @throws IOException
   */
  private static void addAdHocMonomer(MonomerNotation monomerNotation) throws IOException, JDOMException {
    Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer(monomerNotation.getType(), monomerNotation.getID().replace("[", "").replace("]", ""));
    if (monomer.isAdHocMonomer()) {
      set.add(monomer);
    }

  }

}
