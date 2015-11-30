
package org.helm.notation2;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.helm.notation.MonomerException;
import org.helm.notation.MonomerStore;
import org.helm.notation.model.Monomer;
import org.helm.notation.tools.MonomerParser;
import org.helm.notation.tools.xHelmNotationExporter;
import org.helm.notation2.exception.HELM1FormatException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.MonomerNotation;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnitRNA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * xHELM
 * 
 * @author hecht
 */
public final class xHELM {

  private static Set<Monomer> set = null;



  protected static String writeXHELM2(ContainerHELM2 containerhelm2) throws MonomerException {
    set = new HashSet<Monomer>();
    Element root = new Element(xHelmNotationExporter.XHELM_ELEMENT);

    Document doc = new Document(root);

    Element helmElement = new Element(xHelmNotationExporter.HELM_NOTATION_ELEMENT);
    helmElement.setText(containerhelm2.getHELM2Notation().toHELM2());

    root.addContent(helmElement);

    Element monomerListElement = new Element(xHelmNotationExporter.MONOMER_LIST_ELEMENT);

      
      /* get all adhocMonomers */
      for (MonomerNotation monomernotation : MethodsForContainerHELM2.getListOfMonomerNotation(containerhelm2.getHELM2Notation().getListOfPolymers())) {
      /* get all elements of an rna */
        if (monomernotation instanceof MonomerNotationUnitRNA) {
        for (MonomerNotationUnit unit : ((MonomerNotationUnitRNA) monomernotation).getContents()) {
          addAdHocMonomer(unit);
        }
        }
 else {
        addAdHocMonomer(monomernotation);

      }
   
      }
      // Distinct monomers
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

  protected static String writeXHELM(ContainerHELM2 containerhelm2) throws MonomerException, HELM1FormatException {
    set = new HashSet<Monomer>();
    Element root = new Element(xHelmNotationExporter.XHELM_ELEMENT);

    Document doc = new Document(root);

    Element helmElement = new Element(xHelmNotationExporter.HELM_NOTATION_ELEMENT);
    helmElement.setText(HELM1.getStandard(containerhelm2.getHELM2Notation()));

    root.addContent(helmElement);

    Element monomerListElement = new Element(xHelmNotationExporter.MONOMER_LIST_ELEMENT);

    /* get all adhocMonomers */
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
    // Distinct monomers
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


  private static void addAdHocMonomer(MonomerNotation monomerNotation) {
    try {
      Monomer monomer = MethodsForContainerHELM2.getMonomer(monomerNotation.getType(), monomerNotation.getID());
      if (monomer.isAdHocMonomer()) {
        System.out.println(monomer.getName());
        set.add(monomer);
      }
    } catch (MonomerException monomer) {

    }
    
  }

}
