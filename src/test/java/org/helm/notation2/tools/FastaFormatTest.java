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

import org.helm.chemtoolkit.CTKException;
import org.helm.notation2.exception.AnalogSequenceException;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.exceptionparser.NotationException;
import org.helm.notation2.parser.notation.HELM2Notation;
import org.helm.notation2.tools.FastaFormat;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FastaFormatTest {
  @Test
  public void testReadFastaPEPTIDE() throws FastaFormatException, ChemistryException {
    HELM2Notation helm2notation =
        FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(">gi|5524211|gb|AAD44166.1| cytochrome b [Elephas maximus maximus]\nLCLYTHIGRNIYYGSYLYSETWNTGIMLLLITMATAFMGYVLPWGQMSFWGATVITNLFSAIPYIGTNLV\nEWIWGGFSVDKATLNRFFAFHFILPFTMVALAGVHLTFLHETGSNNPLGLTSDSDKIPFHPYYTIKDFLG");
    Assert.assertEquals(helm2notation.toHELM2(), "PEPTIDE1{L.C.L.Y.T.H.I.G.R.N.I.Y.Y.G.S.Y.L.Y.S.E.T.W.N.T.G.I.M.L.L.L.I.T.M.A.T.A.F.M.G.Y.V.L.P.W.G.Q.M.S.F.W.G.A.T.V.I.T.N.L.F.S.A.I.P.Y.I.G.T.N.L.V.E.W.I.W.G.G.F.S.V.D.K.A.T.L.N.R.F.F.A.F.H.F.I.L.P.F.T.M.V.A.L.A.G.V.H.L.T.F.L.H.E.T.G.S.N.N.P.L.G.L.T.S.D.S.D.K.I.P.F.H.P.Y.Y.T.I.K.D.F.L.G}\"gi|5524211|gb|AAD44166.1| cytochrome b [Elephas maximus maximus]\"$$$$V2.0");
  }

  @Test(expectedExceptions = FastaFormatException.class)
  public void testReadFastaPEPTIDEWithException() throws FastaFormatException, ChemistryException {
    FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(">gi|5524211|gb|AAD44166.1| cytochrome b [Elephas maximus maximus]\nLCLYTHIGRN_YYGSYLYSETWNTGIMLLLITMATAFMGYVLPWGQMSFWGATVITNLFSAIPYIGTNLV\nEWIWGGFSVDKATLNRFFAFHFILPFTMVALAGVHLTFLHETGSNNPLGLTSDSDKIPFHPYYTIKDFLG");
  }

  @Test
  public void testReadFastaRNA() throws FastaFormatException, NotationException, IOException, JDOMException, ChemistryException {
    HELM2Notation helm2notation =
        FastaFormat.generateRNAPolymersFromFastaFormatHELM1(">Seq1 [organism=Carpodacus mexicanus] C.mexicanus clone 6b actin (act) mRNA, partial cds\nCCTTTATCTAATCTTTGGAGCATGAGCTGGCATAGTTGGAACCGCCCTCAGCCTCCTCATCCGTGCAGAA\nCTTGGACAACCTGGAACTCTTCTAGGAGACGACCAAATTTACAATGTAATCGTCACTGCCCACGCCTTCG\nTAATAATTTTCTTTATAGTAATACCAATCATGATCGGTGGTTTCGGAAACTGACTAGTCCCACTCATAAT\nCGGCGCCCCCGACATAGCATTCCCCCGTATAAACAACATAAGCTTCTGACTACTTCCCCCATCATTTCTT\nTTACTTCTAGCATCCTCCACAGTAGAAGCTGGAGCAGGAACAGGGTGAACAGTATATCCCCCTCTCGCTG\nGTAACCTAGCCCATGCCGGTGCTTCAGTAGACCTAGCCATCTTCTCCCTCCACTTAGCAGGTGTTTCCTC\nTATCCTAGGTGCTATTAACTTTATTACAACCGCCATCAACATAAAACCCCCAACCCTCTCCCAATACCAA\nACCCCCCTATTCGTATGATCAGTCCTTATTACCGCCGTCCTTCTCCTACTCTCTCTCCCAGTCCTCGCTG");
    Assert.assertEquals(helm2notation.toHELM2(), "RNA1{R(C)P.R(C)P.R(T)P.R(T)P.R(T)P.R(A)P.R(T)P.R(C)P.R(T)P.R(A)P.R(A)P.R(T)P.R(C)P.R(T)P.R(T)P.R(T)P.R(G)P.R(G)P.R(A)P.R(G)P.R(C)P.R(A)P.R(T)P.R(G)P.R(A)P.R(G)P.R(C)P.R(T)P.R(G)P.R(G)P.R(C)P.R(A)P.R(T)P.R(A)P.R(G)P.R(T)P.R(T)P.R(G)P.R(G)P.R(A)P.R(A)P.R(C)P.R(C)P.R(G)P.R(C)P.R(C)P.R(C)P.R(T)P.R(C)P.R(A)P.R(G)P.R(C)P.R(C)P.R(T)P.R(C)P.R(C)P.R(T)P.R(C)P.R(A)P.R(T)P.R(C)P.R(C)P.R(G)P.R(T)P.R(G)P.R(C)P.R(A)P.R(G)P.R(A)P.R(A)P.R(C)P.R(T)P.R(T)P.R(G)P.R(G)P.R(A)P.R(C)P.R(A)P.R(A)P.R(C)P.R(C)P.R(T)P.R(G)P.R(G)P.R(A)P.R(A)P.R(C)P.R(T)P.R(C)P.R(T)P.R(T)P.R(C)P.R(T)P.R(A)P.R(G)P.R(G)P.R(A)P.R(G)P.R(A)P.R(C)P.R(G)P.R(A)P.R(C)P.R(C)P.R(A)P.R(A)P.R(A)P.R(T)P.R(T)P.R(T)P.R(A)P.R(C)P.R(A)P.R(A)P.R(T)P.R(G)P.R(T)P.R(A)P.R(A)P.R(T)P.R(C)P.R(G)P.R(T)P.R(C)P.R(A)P.R(C)P.R(T)P.R(G)P.R(C)P.R(C)P.R(C)P.R(A)P.R(C)P.R(G)P.R(C)P.R(C)P.R(T)P.R(T)P.R(C)P.R(G)P.R(T)P.R(A)P.R(A)P.R(T)P.R(A)P.R(A)P.R(T)P.R(T)P.R(T)P.R(T)P.R(C)P.R(T)P.R(T)P.R(T)P.R(A)P.R(T)P.R(A)P.R(G)P.R(T)P.R(A)P.R(A)P.R(T)P.R(A)P.R(C)P.R(C)P.R(A)P.R(A)P.R(T)P.R(C)P.R(A)P.R(T)P.R(G)P.R(A)P.R(T)P.R(C)P.R(G)P.R(G)P.R(T)P.R(G)P.R(G)P.R(T)P.R(T)P.R(T)P.R(C)P.R(G)P.R(G)P.R(A)P.R(A)P.R(A)P.R(C)P.R(T)P.R(G)P.R(A)P.R(C)P.R(T)P.R(A)P.R(G)P.R(T)P.R(C)P.R(C)P.R(C)P.R(A)P.R(C)P.R(T)P.R(C)P.R(A)P.R(T)P.R(A)P.R(A)P.R(T)P.R(C)P.R(G)P.R(G)P.R(C)P.R(G)P.R(C)P.R(C)P.R(C)P.R(C)P.R(C)P.R(G)P.R(A)P.R(C)P.R(A)P.R(T)P.R(A)P.R(G)P.R(C)P.R(A)P.R(T)P.R(T)P.R(C)P.R(C)P.R(C)P.R(C)P.R(C)P.R(G)P.R(T)P.R(A)P.R(T)P.R(A)P.R(A)P.R(A)P.R(C)P.R(A)P.R(A)P.R(C)P.R(A)P.R(T)P.R(A)P.R(A)P.R(G)P.R(C)P.R(T)P.R(T)P.R(C)P.R(T)P.R(G)P.R(A)P.R(C)P.R(T)P.R(A)P.R(C)P.R(T)P.R(T)P.R(C)P.R(C)P.R(C)P.R(C)P.R(C)P.R(A)P.R(T)P.R(C)P.R(A)P.R(T)P.R(T)P.R(T)P.R(C)P.R(T)P.R(T)P.R(T)P.R(T)P.R(A)P.R(C)P.R(T)P.R(T)P.R(C)P.R(T)P.R(A)P.R(G)P.R(C)P.R(A)P.R(T)P.R(C)P.R(C)P.R(T)P.R(C)P.R(C)P.R(A)P.R(C)P.R(A)P.R(G)P.R(T)P.R(A)P.R(G)P.R(A)P.R(A)P.R(G)P.R(C)P.R(T)P.R(G)P.R(G)P.R(A)P.R(G)P.R(C)P.R(A)P.R(G)P.R(G)P.R(A)P.R(A)P.R(C)P.R(A)P.R(G)P.R(G)P.R(G)P.R(T)P.R(G)P.R(A)P.R(A)P.R(C)P.R(A)P.R(G)P.R(T)P.R(A)P.R(T)P.R(A)P.R(T)P.R(C)P.R(C)P.R(C)P.R(C)P.R(C)P.R(T)P.R(C)P.R(T)P.R(C)P.R(G)P.R(C)P.R(T)P.R(G)P.R(G)P.R(T)P.R(A)P.R(A)P.R(C)P.R(C)P.R(T)P.R(A)P.R(G)P.R(C)P.R(C)P.R(C)P.R(A)P.R(T)P.R(G)P.R(C)P.R(C)P.R(G)P.R(G)P.R(T)P.R(G)P.R(C)P.R(T)P.R(T)P.R(C)P.R(A)P.R(G)P.R(T)P.R(A)P.R(G)P.R(A)P.R(C)P.R(C)P.R(T)P.R(A)P.R(G)P.R(C)P.R(C)P.R(A)P.R(T)P.R(C)P.R(T)P.R(T)P.R(C)P.R(T)P.R(C)P.R(C)P.R(C)P.R(T)P.R(C)P.R(C)P.R(A)P.R(C)P.R(T)P.R(T)P.R(A)P.R(G)P.R(C)P.R(A)P.R(G)P.R(G)P.R(T)P.R(G)P.R(T)P.R(T)P.R(T)P.R(C)P.R(C)P.R(T)P.R(C)P.R(T)P.R(A)P.R(T)P.R(C)P.R(C)P.R(T)P.R(A)P.R(G)P.R(G)P.R(T)P.R(G)P.R(C)P.R(T)P.R(A)P.R(T)P.R(T)P.R(A)P.R(A)P.R(C)P.R(T)P.R(T)P.R(T)P.R(A)P.R(T)P.R(T)P.R(A)P.R(C)P.R(A)P.R(A)P.R(C)P.R(C)P.R(G)P.R(C)P.R(C)P.R(A)P.R(T)P.R(C)P.R(A)P.R(A)P.R(C)P.R(A)P.R(T)P.R(A)P.R(A)P.R(A)P.R(A)P.R(C)P.R(C)P.R(C)P.R(C)P.R(C)P.R(A)P.R(A)P.R(C)P.R(C)P.R(C)P.R(T)P.R(C)P.R(T)P.R(C)P.R(C)P.R(C)P.R(A)P.R(A)P.R(T)P.R(A)P.R(C)P.R(C)P.R(A)P.R(A)P.R(A)P.R(C)P.R(C)P.R(C)P.R(C)P.R(C)P.R(C)P.R(T)P.R(A)P.R(T)P.R(T)P.R(C)P.R(G)P.R(T)P.R(A)P.R(T)P.R(G)P.R(A)P.R(T)P.R(C)P.R(A)P.R(G)P.R(T)P.R(C)P.R(C)P.R(T)P.R(T)P.R(A)P.R(T)P.R(T)P.R(A)P.R(C)P.R(C)P.R(G)P.R(C)P.R(C)P.R(G)P.R(T)P.R(C)P.R(C)P.R(T)P.R(T)P.R(C)P.R(T)P.R(C)P.R(C)P.R(T)P.R(A)P.R(C)P.R(T)P.R(C)P.R(T)P.R(C)P.R(T)P.R(C)P.R(T)P.R(C)P.R(C)P.R(C)P.R(A)P.R(G)P.R(T)P.R(C)P.R(C)P.R(T)P.R(C)P.R(G)P.R(C)P.R(T)P.R(G)}\"Seq1 [organism=Carpodacus mexicanus] C.mexicanus clone 6b actin (act) mRNA, partial cds\"$$$$V2.0");
  }

  @Test(expectedExceptions = NotationException.class)
  public void testReadFastaRNAWithExcepiton() throws FastaFormatException, NotationException, IOException, JDOMException, ChemistryException {
    FastaFormat.generateRNAPolymersFromFastaFormatHELM1(">Seq1 [organism=Carpodacus mexicanus] C.mexicanus clone 6b actin (act) mRNA, partial cds\nCCTTTATCTAATCTTTGGAGCATGAGCTGGCATAGTTGGAACCGCCCTCAGCCTCCTCATCCGTGCAG_A\nCTTGGACAACCTGGAACTCTTCTAGGAGACGACCAAATTTACAATGTAATCGTCACTGCCCACGCCTTCG\nTAATAATTTTCTTTATAGTAATACCAATCATGATCGGTGGTTTCGGAAACTGACTAGTCCCACTCATAAT\nCGGCGCCCCCGACATAGCATTCCCCCGTATAAACAACATAAGCTTCTGACTACTTCCCCCATCATTTCTT\nTTACTTCTAGCATCCTCCACAGTAGAAGCTGGAGCAGGAACAGGGTGAACAGTATATCCCCCTCTCGCTG\nGTAACCTAGCCCATGCCGGTGCTTCAGTAGACCTAGCCATCTTCTCCCTCCACTTAGCAGGTGTTTCCTC\nTATCCTAGGTGCTATTAACTTTATTACAACCGCCATCAACATAAAACCCCCAACCCTCTCCCAATACCAA\nACCCCCCTATTCGTATGATCAGTCCTTATTACCGCCGTCCTTCTCCTACTCTCTCTCCCAGTCCTCGCTG");
  }

  @Test
  public void testReadFastaPEPTIDE2() throws FastaFormatException, ChemistryException {
    HELM2Notation helm2notation =
        FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(">seq0\nFQTWEEFSRAAEKLYLADPMKVRVVLKYRHVDGNLCIKVTDDLVCLVYRTDQAQDVKKIEKF\n>seq1\nKYRTWEEFTRAAEKLYQADPMKVRVVLKYRHCDGNLCIKVTDDVVCLLYRTDQAQDVKKIEKFHSQLMRLMELKVTDNKECLKFKTDQAQEAKKMEKLNNIFFTLM\n>seq2\nEEYQTWEEFARAAEKLYLTDPMKVRVVLKYRHCDGNLCMKVTDDAVCLQYKTDQAQDVKKVEKLHGK\n>seq3\nMYQVWEEFSRAVEKLYLTDPMKVRVVLKYRHCDGNLCIKVTDNSVCLQYKTDQAQDVK\n>seq4\nEEFSRAVEKLYLTDPMKVRVVLKYRHCDGNLCIKVTDNSVVSYEMRLFGVQKDNFALEHSLL\n>seq5\nSWEEFAKAAEVLYLEDPMKCRMCTKYRHVDHKLVVKLTDNHTVLKYVTDMAQDVKKIEKLTTLLM\n>seq6\nFTNWEEFAKAAERLHSANPEKCRFVTKYNHTKGELVLKLTDDVVCLQYSTNQLQDVKKLEKLSSTLLRSI\n>seq7\nSWEEFVERSVQLFRGDPNATRYVMKYRHCEGKLVLKVTDDRECLKFKTDQAQDAKKMEKLNNIFF\n>seq8\nSWDEFVDRSVQLFRADPESTRYVMKYRHCDGKLVLKVTDNKECLKFKTDQAQEAKKMEKLNNIFFTLM\n>seq9\nKNWEDFEIAAENMYMANPQNCRYTMKYVHSKGHILLKMSDNVKCVQYRAENMPDLKK\n>seq10\nFDSWDEFVSKSVELFRNHPDTTRYVVKYRHCEGKLVLKVTDNHECLKFKTDQAQDAKKMEK");
    Assert.assertEquals(helm2notation.getListOfPolymers().size(), 11);
  }

  @Test(expectedExceptions = FastaFormatException.class)
  public void testHELMToFastaPeptide() throws ExceptionState, IOException, JDOMException, FastaFormatException, org.helm.notation.NotationException, ChemistryException {
    String notation = "PEPTIDE1{[dY]'3'.I.K}|PEPTIDE2{G.H}$$$$";
    testHELMtoFastaPEPTIDE(notation);
  }

  @Test(expectedExceptions = FastaFormatException.class)
  public void testHELMToFastaPeptideWithException() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, org.helm.notation.NotationException, ChemistryException {
    String notation = "PEPTIDE1{[dY]'3-5'.I.K}|PEPTIDE2{G.H}$$$$";
    testHELMtoFastaPEPTIDE(notation);
  }

  @Test
  public void testHELMToFastaRNA() throws ExceptionState, IOException, JDOMException, FastaFormatException, org.helm.notation.NotationException, ChemistryException {
    String notation = "RNA1{R(U)P.R(T)P.R(G)P.R(C)}$$$$";
    Assert.assertEquals(testHELMtoFastaRNA(notation), ">RNA1\nUTGC\n");
  }

  private void testHELMtoFastaPEPTIDE(String notation) throws ExceptionState, IOException, JDOMException,
      FastaFormatException, org.helm.notation.NotationException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    System.out.println(FastaFormat.generateFastaFromPeptidePolymer(parserHELM2.getHELM2Notation().getListOfPolymers()));
  }

  private String testHELMtoFastaRNA(String notation) throws ExceptionState, IOException, JDOMException,
      FastaFormatException, org.helm.notation.NotationException, ChemistryException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    return FastaFormat.generateFastaFromRNAPolymer(parserHELM2.getHELM2Notation().getListOfPolymers());
  }

  @Test
  public void testHELMToAnalogSequenceExamples() throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, ChemistryException, CTKException {
    String notation = "RNA1{[dR](U)P.R(T)P.R(G)P.R(C)}$$$$";
    Assert.assertEquals(testHELMAnalogSequence(notation), "RNA1{R(U)P.R(T)P.R(G)P.R(C)}$$$$V2.0");

    notation = "RNA1{(R(U)P+R(T)P).R(T)P.R(G)P.R(C)}$$$$";
    Assert.assertEquals(testHELMAnalogSequence(notation), "RNA1{(R(U)P+R(T)P).R(T)P.R(G)P.R(C)}$$$$V2.0");

    notation = "PEPTIDE1{(A.G).L}$$$$";
    Assert.assertEquals(testHELMAnalogSequence(notation), "PEPTIDE1{(A.G).L}$$$$V2.0");

    notation = "PEPTIDE1{[dF].[dN].[dL]}$$$$";
    testHELMAnalogSequence(notation);
  }

  private String testHELMAnalogSequence(String notation) throws ExceptionState, IOException, JDOMException,
      FastaFormatException, AnalogSequenceException, ChemistryException, CTKException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    return FastaFormat.convertIntoAnalogSequence(parserHELM2.getHELM2Notation()).toHELM2();

  }

}
