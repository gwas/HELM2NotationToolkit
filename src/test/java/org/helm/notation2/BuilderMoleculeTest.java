package org.helm.notation2;

import java.io.IOException;

import org.helm.chemtoolkit.CTKException;
import org.helm.notation.CalculationException;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.helm.notation.StructureException;
import org.helm.notation2.exception.BuilderMoleculeException;
import org.helm.notation2.exception.HELM2HandledException;
import org.helm.notation2.parser.exceptionparser.ExceptionState;
import org.helm.notation2.parser.notation.polymer.MonomerNotationUnit;
import org.helm.notation2.parser.notation.polymer.PolymerNotation;
import org.jdom.JDOMException;
import org.testng.annotations.Test;

public class BuilderMoleculeTest {

// @Test(expectedExceptions = BuilderMoleculeException.class)
// public void testBuildMoleculeFromSinglePolymerBLOBWithException() throws
// ExceptionState, MonomerException,
// IOException, NotationException, JDOMException, org.jdom2.JDOMException,
// StructureException, CalculationException, HELM2HandledException,
// BuilderMoleculeException, CTKException
//
// {
//
// PolymerNotation node = new PolymerNotation("BLOB1");
// BuilderMolecule.buildMoleculefromSinglePolymer(node);
//
// }
//
// // @Test(expectedExceptions = BuilderMoleculeException.class)
// public void testBuildMoleculeFromSinglePolymerCHEMEmptyWithException() throws
// ExceptionState, MonomerException,
// IOException, NotationException, JDOMException, org.jdom2.JDOMException,
// StructureException, CalculationException, HELM2HandledException,
// BuilderMoleculeException, CTKException
//
// {
//
// PolymerNotation node = new PolymerNotation("CHEM1");
// BuilderMolecule.buildMoleculefromSinglePolymer(node);
// }
//
// @Test
// public void testBuildMoleculeFromSinglePolymerCHEM() throws ExceptionState,
// MonomerException,
// IOException, NotationException, JDOMException, org.jdom2.JDOMException,
// StructureException, CalculationException, HELM2HandledException,
// BuilderMoleculeException, CTKException
//
// {
//
// PolymerNotation node = new PolymerNotation("CHEM1");
// MonomerNotationUnit mon = new MonomerNotationUnit("[MCC]",
// node.getPolymerID().getType());
// node.getPolymerElements().getListOfElements().add(mon);
// BuilderMolecule.buildMoleculefromSinglePolymer(new
// PolymerNotation(node.getPolymerID(), node.getPolymerElements(), ""));
// }
//
// @Test(expectedExceptions = HELM2HandledException.class)
// public void testBuildMoleculeFromSinglePolymerCHEMUnknownWithException()
// throws ExceptionState, MonomerException,
// IOException, NotationException, JDOMException, org.jdom2.JDOMException,
// StructureException, CalculationException, HELM2HandledException,
// BuilderMoleculeException, CTKException
//
// {
//
// PolymerNotation node = new PolymerNotation("CHEM1");
// MonomerNotationUnit mon = new MonomerNotationUnit("[CZ]",
// node.getPolymerID().getType());
// node.getPolymerElements().getListOfElements().add(mon);
// BuilderMolecule.buildMoleculefromSinglePolymer(new
// PolymerNotation(node.getPolymerID(), node.getPolymerElements(), ""));
// }
//
// @Test
// public void testBuildMoleculeFromSinglePolymerCHEMSMILES() throws
// ExceptionState, MonomerException,
// IOException, NotationException, JDOMException, org.jdom2.JDOMException,
// StructureException, CalculationException, HELM2HandledException,
// BuilderMoleculeException, CTKException
//
// {
//
// PolymerNotation node = new PolymerNotation("CHEM1");
// MonomerNotationUnit mon = new
// MonomerNotationUnit("[OC(=O)C1CCC(CN2C(=O)C=CC2=O)CC1]",
// node.getPolymerID().getType());
// node.getPolymerElements().getListOfElements().add(mon);
// BuilderMolecule.buildMoleculefromSinglePolymer(new
// PolymerNotation(node.getPolymerID(), node.getPolymerElements(), ""));
// }
//


}
