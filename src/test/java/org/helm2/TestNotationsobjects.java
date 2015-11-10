package org.helm2;

import java.io.IOException;

import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation2.parser.StateMachineParser;
import org.helm.notation2.parser.ExceptionParser.ExceptionState;
import org.jdom.JDOMException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestNotationsobjects {
  StateMachineParser parser;

  @Test
  public void testValidationGrouping() throws ExceptionState, MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{A.X.G.C.(_,N).(A:10,G:30,R:30).T.C.F.D.W\"mutation\".(A:?+G:1.5).C}|RNA1{R(A)P.(R(N)P)'4'.(R(G)P)'3-7'\"mutation\"}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G1:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";


    Assert.assertTrue(Notationsobjects.validateGrouping(parser.notationContainer));
  }

  @Test
  public void testValidationGroupingWithException() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{A.X.G.C.(_,N).(A:10,G:30,R:30).T.C.F.D.W\"mutation\".(A:?+G:1.5).C}|RNA1{R(A)P.(R(N)P)'4'.(R(G)P)'3-7'\"mutation\"}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G1:45,CHEM3:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    // System.out.println(parser.toJSON());
    Assert.assertFalse(Notationsobjects.validateGrouping(parser.notationContainer));
  }

  @Test
  public void testValidationGroupingFalseGroup() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{A.X.G.C.(_,N).(A:10,G:30,R:30).T.C.F.D.W\"mutation\".(A:?+G:1.5).C}|RNA1{R(A)P.(R(N)P)'4'.(R(G)P)'3-7'\"mutation\"}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G3:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    Assert.assertFalse(Notationsobjects.validateGrouping(parser.notationContainer));
  }

  @Test
  public void testvalidateUniquePolymerIDs() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{A.X.G.C.(_,N).(A:10,G:30,R:30).T.C.F.D.W\"mutation\".(A:?+G:1.5).C}|RNA1{R(A)P.(R(N)P)'4'.(R(G)P)'3-7'\"mutation\"}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G3:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    Assert.assertTrue(Notationsobjects.validateUniquePolymerIDs(parser.notationContainer));
  }

  @Test(expectedExceptions = NotationException.class)
  public void testvalidateUniquePolymerIDsWithException() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{A.X.G.C.(_,N).(A:10,G:30,R:30).T.C.F.D.W\"mutation\".(A:?+G:1.5).C}|RNA1{R(A)P.(R(N)P)'4'.(R(G)P)'3-7'\"mutation\"}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G1(G3:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    Assert.assertTrue(Notationsobjects.validateUniquePolymerIDs(parser.notationContainer));
  }

  @Test
  public void testGetMonomerCountsSimple() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{A.X.G.C.C}|RNA1{R(A)P.(R(N)P).(R(G)P)}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G3:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    Assert.assertEquals(Notationsobjects.getMonomerCount(parser.notationContainer), 16);
  }

  @Test
  public void testGetMonomerCountsExtended() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "PEPTIDE1{A.X.G.C.C.(A.X.C)'4'}|RNA1{R(A)P.(R(N)P).(RP)}|CHEM1{?}|BLOB1{BEAD}\"Animated Polystyrene\"$PEPTIDE1,BLOB1,X:R3-?:?\"Specific Conjugation\"|PEPTIDE1,CHEM1,(A+T):R3-?:?|PEPTIDE1,PEPTIDE1,(4,8):pair-12:pair$G1(PEPTIDE1:1+RNA1:2.5-2.7+BLOB1)|G2(G3:45,CHEM1:55)${\"Name\":\"lipid nanoparticle with RNA payload and peptide ligand\"}$";
    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    Assert.assertEquals(Notationsobjects.getMonomerCount(parser.notationContainer), 27);
  }

  @Test
  public void testConnection() throws ExceptionState,
      MonomerException,
      IOException, NotationException, JDOMException, org.jdom2.JDOMException

  {
    parser = new StateMachineParser();

    String test =
        "RNA1{[sP].R(C)[sP].R(U)P.R(G)P.R([dabA])P.R(G)P.R(A)P.R(G)P.R(G)P.[dR](G)P.R(U)}|RNA2{R(A)P.R(C)P.R(C)P.R(C)P.R(U)P.R(C)P.R(U)P.R(C)P.R(A)P.R(G)}$RNA1,RNA2,9:pair-23:pair|RNA1,RNA2,6:pair-26:pair|RNA1,RNA2,21:pair-11:pair|RNA1,RNA2,15:pair-17:pair|RNA1,RNA2,12:pair-20:pair|RNA1,RNA2,24:pair-8:pair|RNA1,RNA2,30:pair-2:pair|RNA1,RNA2,18:pair-14:pair|RNA1,RNA2,27:pair-5:pair|RNA1,RNA2,3:pair-29:pair$$$";

    ;
    for (int i = 0; i < test.length(); ++i) {
      parser.doAction(test.charAt(i));
    }
    test += "V2.0";

    System.out.println(Notationsobjects.validateConnections(parser.notationContainer));

  }


}
