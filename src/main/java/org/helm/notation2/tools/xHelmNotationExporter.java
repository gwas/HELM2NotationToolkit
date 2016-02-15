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
import java.util.Set;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.Monomer;
import org.helm.notation2.MonomerFactory;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.exception.MonomerException;
import org.helm.notation2.exception.NotationException;
import org.helm.notation2.exception.ValidationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * xHELMNotationExporter
 *
 * @author hecht
 */
public final class xHelmNotationExporter {
  public static final String XHELM_ELEMENT = "Xhelm";

  public static final String MONOMER_LIST_ELEMENT = "Monomers";

  public static final String MONOMER_ELEMENT = "Monomer";

  public static final String HELM_NOTATION_ELEMENT = "HelmNotation";

  private static Set<Monomer> set = null;

  /**
   * Default constructor.
   */
  private xHelmNotationExporter() {

  }

  /**
   * method to get xhelm for the helm2 notation with the new functionality
   *
   * @param helm2notation, HELM2Notation object
   * @return xhelm
   * @throws MonomerException
   * @throws JDOMException
   * @throws IOException
   * @throws ChemistryException
   */
  public static String getXHELM2(HELM2Notation helm2notation) throws MonomerException, IOException, JDOMException, ChemistryException {
    set = new HashSet<Monomer>();
    Element root = new Element(xHelmNotationExporter.XHELM_ELEMENT);

    Document doc = new Document(root);

    Element helmElement = new Element(xHelmNotationExporter.HELM_NOTATION_ELEMENT);
    helmElement.setText(helm2notation.toHELM2());

    root.addContent(helmElement);

    Element monomerListElement = new Element(xHelmNotationExporter.MONOMER_LIST_ELEMENT);

    /* save all adhocMonomers */
    for (MonomerNotation monomernotation : MethodsMonomerUtils.getListOfMonomerNotation(helm2notation.getListOfPolymers())) {
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
   * @param helm2notation, HELM2Notation object
   * @return xhelm
   * @throws MonomerException
   * @throws HELM1FormatException
   * @throws JDOMException
   * @throws IOException
   * @throws NotationException
   * @throws CTKException
   * @throws ValidationException
   * @throws ChemistryException if the Chemistry Engine can not be initialized
   */
  public static String getXHELM(HELM2Notation helm2notation) throws MonomerException, HELM1FormatException,
      IOException, JDOMException, NotationException, CTKException, ValidationException, ChemistryException {
    set = new HashSet<Monomer>();
    Element root = new Element(xHelmNotationExporter.XHELM_ELEMENT);

    Document doc = new Document(root);

    Element helmElement = new Element(xHelmNotationExporter.HELM_NOTATION_ELEMENT);
    helmElement.setText(HELM1Utils.getStandard(helm2notation));

    root.addContent(helmElement);

    Element monomerListElement = new Element(xHelmNotationExporter.MONOMER_LIST_ELEMENT);

    /* save all adhocMonomers in the set */
    for (MonomerNotation monomernotation : MethodsMonomerUtils.getListOfMonomerNotation(helm2notation.getListOfPolymers())) {
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
   * @throws ChemistryException
   * @throws CTKException
   */
  private static void addAdHocMonomer(MonomerNotation monomerNotation) throws IOException, JDOMException, ChemistryException {
    Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer(monomerNotation.getType(), monomerNotation.getID().replace("[", "").replace("]", ""));
    if (monomer.isAdHocMonomer()) {
      set.add(monomer);
    }

  }

}
