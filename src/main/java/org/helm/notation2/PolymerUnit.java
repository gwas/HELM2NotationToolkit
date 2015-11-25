/*--
 *
 * @(#) PolymerUnit.java
 *
 *
 */
package org.helm.notation2;

import java.util.List;

import org.helm.notation2.parser.notation.polymer.PolymerNotation;


/**
 * {@code PolymerUnit}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class PolymerUnit {

  List<SingleUnit> elements;

  PolymerNotation polymerNotation;

  public PolymerUnit(PolymerNotation polymerNotation, List<SingleUnit> elements) {
    this.polymerNotation = polymerNotation;
    this.elements = elements;
  }

  public void addSingleUnit(SingleUnit single) {
    elements.add(single);
  }

}
