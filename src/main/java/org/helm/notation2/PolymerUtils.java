/*--
 *
 * @(#) PolymerUtils.java
 *
 *
 */
package org.helm.notation2;

import java.util.ArrayList;
import java.util.List;

import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code PolymerUtils}
 * TODO comment me
 * 
 * @author 
 * @version $Id$
 */
public class PolymerUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(PolymerUtils.class);


  /**
   * decompose the HELM2 into smaller HELM2 notations
   * 
   * @param polymers
   * @return list of ContainerHELM2 objects
   */
  protected List<ContainerHELM2> decompose(List<PolymerNotation> polymers) {
    List<ContainerHELM2> list = new ArrayList<ContainerHELM2>();
    for (PolymerNotation polymer : polymers) {
      HELM2Notation helm2notation = new HELM2Notation();
      helm2notation.addPolymer(polymer);
      list.add(new ContainerHELM2(helm2notation, new InterConnections()));
    }
    return list;
  }

  protected void getTotalMonomerCount() {

  }

  protected void getNotationByReplacingSMILES() {

  }
}
