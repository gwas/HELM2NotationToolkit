/*--
 *
 * @(#) SingleUnit.java
 *
 *
 */
package org.helm.notation2;

import org.helm.notation.model.Monomer;
import org.helm.notation2.parser.Notation.Polymer.MonomerNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SingleUnit
 * 
 * @author hecht
 */
public class SingleUnit {


  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(SingleUnit.class);

  MonomerNotation monomernotation;

  Monomer monomer;

  public SingleUnit(MonomerNotation monomernotation, Monomer monomer) {
    this.monomernotation = monomernotation;
    this.monomer = monomer;
  }

  public MonomerNotation getMonomerNotation(){
    return monomernotation;
  }

  public Monomer getMonomer() {
    return monomer;
  }

}
