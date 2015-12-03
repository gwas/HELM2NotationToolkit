
package org.helm.notation2;

import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PeptideUtils
 * 
 * @author hecht
 */
public final class PeptideUtils {

  protected static String getNaturalAnalogSequence(PolymerNotation polymer) throws HELM2HandledException {
    return FastaFormat.generateFastaFromPeptide(MethodsForContainerHELM2.getListOfHandledMonomers(polymer.getListMonomers()));
  }

}
