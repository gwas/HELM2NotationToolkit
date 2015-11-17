/*--
 *
 * @(#) PolymerUnit.java
 *
 *
 */
package org.helm2;

import java.util.ArrayList;

import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.helm.notation2.parser.Notation.Polymer.PolymerNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code PolymerUnit}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class PolymerUnit {

  ArrayList<SingleUnit> elements;

  PolymerNotation polymerNotation;

  public PolymerUnit(PolymerNotation polymerNotation, ArrayList<SingleUnit> elements) {
    this.polymerNotation = polymerNotation;
    this.elements = elements;
  }

  public void addSingleUnit(SingleUnit single) {
    elements.add(single);
  }

}
