package org.helm.notation2;

import java.io.IOException;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom.JDOMException;
import org.testng.annotations.Test;

public class FastaFormatTest {
  @Test
  public void testReadFasta() throws FastaFormatException, org.helm.notation2.parser.exceptionparser.NotationException, IOException, JDOMException {

    FastaFormat fastaformat = new FastaFormat();
    fastaformat.readFastaFilePeptide(">gi|5524211|gb|AAD44166.1| cytochrome b [Elephas maximus maximus]\nLCLYTHIGRNIYYGSYLYSETWNTGIMLLLITMATAFMGYVLPWGQMSFWGATVITNLFSAIPYIGTNLV\nEWIWGGFSVDKATLNRFFAFHFILPFTMVALAGVHLTFLHETGSNNPLGLTSDSDKIPFHPYYTIKDFLG");
  }
}
