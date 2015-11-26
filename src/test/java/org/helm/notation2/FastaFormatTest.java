package org.helm.notation2;

import java.io.IOException;
import org.helm.notation2.exception.FastaFormatException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.jdom.JDOMException;
import org.testng.annotations.Test;

public class FastaFormatTest {
  @Test
  public void testReadFastaPEPTIDE() throws FastaFormatException {
    FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(">gi|5524211|gb|AAD44166.1| cytochrome b [Elephas maximus maximus]\nLCLYTHIGRNIYYGSYLYSETWNTGIMLLLITMATAFMGYVLPWGQMSFWGATVITNLFSAIPYIGTNLV\nEWIWGGFSVDKATLNRFFAFHFILPFTMVALAGVHLTFLHETGSNNPLGLTSDSDKIPFHPYYTIKDFLG");
  }

  @Test(expectedExceptions = FastaFormatException.class)
  public void testReadFastaPEPTIDEWithException() throws FastaFormatException {
    FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(">gi|5524211|gb|AAD44166.1| cytochrome b [Elephas maximus maximus]\nLCLYTHIGRN_YYGSYLYSETWNTGIMLLLITMATAFMGYVLPWGQMSFWGATVITNLFSAIPYIGTNLV\nEWIWGGFSVDKATLNRFFAFHFILPFTMVALAGVHLTFLHETGSNNPLGLTSDSDKIPFHPYYTIKDFLG");
  }

  @Test
  public void testReadFastaRNA() throws FastaFormatException {
    FastaFormat.generateRNAPolymersFromFastaFormatHELM1(">Seq1 [organism=Carpodacus mexicanus] C.mexicanus clone 6b actin (act) mRNA, partial cds\nCCTTTATCTAATCTTTGGAGCATGAGCTGGCATAGTTGGAACCGCCCTCAGCCTCCTCATCCGTGCAGAA\nCTTGGACAACCTGGAACTCTTCTAGGAGACGACCAAATTTACAATGTAATCGTCACTGCCCACGCCTTCG\nTAATAATTTTCTTTATAGTAATACCAATCATGATCGGTGGTTTCGGAAACTGACTAGTCCCACTCATAAT\nCGGCGCCCCCGACATAGCATTCCCCCGTATAAACAACATAAGCTTCTGACTACTTCCCCCATCATTTCTT\nTTACTTCTAGCATCCTCCACAGTAGAAGCTGGAGCAGGAACAGGGTGAACAGTATATCCCCCTCTCGCTG\nGTAACCTAGCCCATGCCGGTGCTTCAGTAGACCTAGCCATCTTCTCCCTCCACTTAGCAGGTGTTTCCTC\nTATCCTAGGTGCTATTAACTTTATTACAACCGCCATCAACATAAAACCCCCAACCCTCTCCCAATACCAA\nACCCCCCTATTCGTATGATCAGTCCTTATTACCGCCGTCCTTCTCCTACTCTCTCTCCCAGTCCTCGCTG");
  }

  @Test(expectedExceptions = FastaFormatException.class)
  public void testReadFastaRNAWithExcepiton() throws FastaFormatException {
    FastaFormat.generateRNAPolymersFromFastaFormatHELM1(">Seq1 [organism=Carpodacus mexicanus] C.mexicanus clone 6b actin (act) mRNA, partial cds\nCCTTTATCTAATCTTTGGAGCATGAGCTGGCATAGTTGGAACCGCCCTCAGCCTCCTCATCCGTGCAG_A\nCTTGGACAACCTGGAACTCTTCTAGGAGACGACCAAATTTACAATGTAATCGTCACTGCCCACGCCTTCG\nTAATAATTTTCTTTATAGTAATACCAATCATGATCGGTGGTTTCGGAAACTGACTAGTCCCACTCATAAT\nCGGCGCCCCCGACATAGCATTCCCCCGTATAAACAACATAAGCTTCTGACTACTTCCCCCATCATTTCTT\nTTACTTCTAGCATCCTCCACAGTAGAAGCTGGAGCAGGAACAGGGTGAACAGTATATCCCCCTCTCGCTG\nGTAACCTAGCCCATGCCGGTGCTTCAGTAGACCTAGCCATCTTCTCCCTCCACTTAGCAGGTGTTTCCTC\nTATCCTAGGTGCTATTAACTTTATTACAACCGCCATCAACATAAAACCCCCAACCCTCTCCCAATACCAA\nACCCCCCTATTCGTATGATCAGTCCTTATTACCGCCGTCCTTCTCCTACTCTCTCTCCCAGTCCTCGCTG");
  }

  @Test
  public void testReadFastaPEPTIDE2() throws FastaFormatException {
    FastaFormat.generatePeptidePolymersFromFASTAFormatHELM1(">seq0\nFQTWEEFSRAAEKLYLADPMKVRVVLKYRHVDGNLCIKVTDDLVCLVYRTDQAQDVKKIEKF\n>seq1\nKYRTWEEFTRAAEKLYQADPMKVRVVLKYRHCDGNLCIKVTDDVVCLLYRTDQAQDVKKIEKFHSQLMRLMELKVTDNKECLKFKTDQAQEAKKMEKLNNIFFTLM\n>seq2\nEEYQTWEEFARAAEKLYLTDPMKVRVVLKYRHCDGNLCMKVTDDAVCLQYKTDQAQDVKKVEKLHGK\n>seq3\nMYQVWEEFSRAVEKLYLTDPMKVRVVLKYRHCDGNLCIKVTDNSVCLQYKTDQAQDVK\n>seq4\nEEFSRAVEKLYLTDPMKVRVVLKYRHCDGNLCIKVTDNSVVSYEMRLFGVQKDNFALEHSLL\n>seq5\nSWEEFAKAAEVLYLEDPMKCRMCTKYRHVDHKLVVKLTDNHTVLKYVTDMAQDVKKIEKLTTLLM\n>seq6\nFTNWEEFAKAAERLHSANPEKCRFVTKYNHTKGELVLKLTDDVVCLQYSTNQLQDVKKLEKLSSTLLRSI\n>seq7\nSWEEFVERSVQLFRGDPNATRYVMKYRHCEGKLVLKVTDDRECLKFKTDQAQDAKKMEKLNNIFF\n>seq8\nSWDEFVDRSVQLFRADPESTRYVMKYRHCDGKLVLKVTDNKECLKFKTDQAQEAKKMEKLNNIFFTLM\n>seq9\nKNWEDFEIAAENMYMANPQNCRYTMKYVHSKGHILLKMSDNVKCVQYRAENMPDLKK\n>seq10\nFDSWDEFVSKSVELFRNHPDTTRYVVKYRHCEGKLVLKVTDNHECLKFKTDQAQDAKKMEK");
  }


  @Test
  public void testHELMToFastaPeptide() throws ExceptionState, IOException, JDOMException, FastaFormatException {
    String notation = "PEPTIDE1{[dY]'3'.I.K}|PEPTIDE2{G.H}$$$$";
    testHELMtoFastaPEPTIDE(notation);
  }

  @Test(expectedExceptions = FastaFormatException.class)
  public void testHELMToFastaPeptideWithException() throws ExceptionState, IOException, JDOMException, FastaFormatException {
    String notation = "PEPTIDE1{[dY]'3-5'.I.K}|PEPTIDE2{G.H}$$$$";
    testHELMtoFastaPEPTIDE(notation);
  }

  @Test
  public void testHELMToFastaRNA() throws ExceptionState, IOException, JDOMException, FastaFormatException {
    String notation = "RNA1{R(U)P.R(T)P.R(G)P.R(C)}$$$$";
    testHELMtoFastaRNA(notation);
  }

  private void testHELMtoFastaPEPTIDE(String notation) throws ExceptionState, IOException, JDOMException, FastaFormatException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    FastaFormat.generateFastaFromPeptidePolymer(containerhelm2.getHELM2Notation().getListOfPolymers());
  }

  private void testHELMtoFastaRNA(String notation) throws ExceptionState, IOException, JDOMException, FastaFormatException {
    ConverterHELM1ToHELM2 converter = new ConverterHELM1ToHELM2();
    String helm2 = converter.doConvert(notation);
    ParserHELM2 parserHELM2 = new ParserHELM2();
    parserHELM2.parse(helm2);

    ContainerHELM2 containerhelm2 = new ContainerHELM2(parserHELM2.getHELM2Notation(), new InterConnections());
    FastaFormat.generateFastaFromRNAPolymer(containerhelm2.getHELM2Notation().getListOfPolymers());
  }
}
