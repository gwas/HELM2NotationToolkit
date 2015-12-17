package org.helm.notation2;

import java.io.IOException;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.exception.ParserException;
import org.helm.notation2.parser.ConverterHELM1ToHELM2;
import org.helm.notation2.parser.ParserHELM2;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom2.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestContainerHELM2 {

  @Test
  public void testaddNotation() throws ParserException, JDOMException, org.helm.notation2.parser.exceptionparser.NotationException {
    int number = 0;
    ContainerHELM2 current =
        readNotation("PEPTIDE1{*}\"LC\"|PEPTIDE2{*}\"HC\"|PEPTIDE3{*}\"HC\"|PEPTIDE4{*}\"LC\"|CHEM1{*}$G1,CHEM1,K:R3-1:R1|PEPTIDE2,PEPTIDE3,250:R3-250:R3\"Hinge S-S connection\"|PEPTIDE2,PEPTIDE3,252:R3-252:R3\"Hinge S-Sconnection\"|PEPTIDE1,PEPTIDE2,120:R3-248:R3\"LC Hinge S-Sconnection\"|PEPTIDE4,PEPTIDE3,120:R3-248:R3\"LC Hinge S-Sconnection\"$G1(PEPTIDE1+PEPTIDE2+PEPTIDE3+PEPTIDE4)|G2(G1+CHEM1:4.5)$Hallo$V2.0");
    
    ContainerHELM2 toadd =
        readNotation("PEPTIDE1{A.X.G.C.(_,N).(A:10,G:30,R:30).T.C.F.D.W\"mutation\".(A:?+G:1.5).C}|RNA1{R(A)P.(R(N)P)'4'.(R(G)P)'3-7'\"mutation\"}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G1:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$V2.0");
    number = toadd.getHELM2Notation().getListOfPolymers().size() + current.getHELM2Notation().getListOfPolymers().size();
    current.addHELM2notation(toadd.getHELM2Notation());

    Assert.assertEquals(current.getHELM2Notation().getListOfPolymers().size(), number);
   }

  private ContainerHELM2 readNotation(String notation) throws ParserException, JDOMException {
    /* HELM1-Format -> */
    if (!(notation.contains("V2.0"))) {
      notation = new ConverterHELM1ToHELM2().doConvert(notation);
    }
    /* parses the HELM notation and generates the necessary notation objects */
    ParserHELM2 parser = new ParserHELM2();
    try {
      parser.parse(notation);
    } catch (ExceptionState | IOException e) {
      throw new ParserException(e.getMessage());
    }
    ContainerHELM2 containerhelm2 = new ContainerHELM2(parser.getHELM2Notation(), new InterConnections());
    return containerhelm2;
  }
}
