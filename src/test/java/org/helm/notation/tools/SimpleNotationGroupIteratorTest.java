package org.helm.notation.tools;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.util.ArrayList;

public class SimpleNotationGroupIteratorTest {

	@Test
	public void testSimplePeptide() {
		SimpleNotationGroupIterator iterator = new SimpleNotationGroupIterator(
				"A.K.A");
		ArrayList<String> list = new ArrayList<String>();
		while (iterator.hasNextGroup()) {
			String group = iterator.nextGroup();
			list.add(group);
			System.out.println(group);

		}
		AssertJUnit.assertEquals("there should be three groups!", 3, list.size());
		AssertJUnit.assertEquals("A", list.get(0));
		AssertJUnit.assertEquals("K", list.get(1));
		AssertJUnit.assertEquals("A", list.get(2));

	}

	@Test
	public void testSimpleRNA() {
		SimpleNotationGroupIterator iterator = new SimpleNotationGroupIterator(
				"P.R(A)[sP].RP.R(G)P.[LR]([5meC])");

		ArrayList<String> list = new ArrayList<String>();
		while (iterator.hasNextGroup()) {
			String group = iterator.nextGroup();
			list.add(group);
			System.out.println(group);

		}
		AssertJUnit.assertEquals("there should be five groups!", 5, list.size());
		AssertJUnit.assertEquals("P", list.get(0));
		AssertJUnit.assertEquals("R(A)[sP]", list.get(1));
		AssertJUnit.assertEquals("RP", list.get(2));
		AssertJUnit.assertEquals("R(G)P", list.get(3));
		AssertJUnit.assertEquals("[LR]([5meC])", list.get(4));

	}

	@Test
	public void testSimpleChem() {
		SimpleNotationGroupIterator iterator = new SimpleNotationGroupIterator(
				"SMPEG2");
		String group = null;
		int i = 0;
		while (iterator.hasNextGroup()) {
			group = iterator.nextGroup();
			i++;

		}
		AssertJUnit.assertEquals("SMPEG2", group);
		AssertJUnit.assertEquals("there should be one group!", 1, i);
	}

	@Test
	public void testInlineSmilesPeptide() {
		ArrayList<String> list = new ArrayList<String>();
		SimpleNotationGroupIterator iterator = new SimpleNotationGroupIterator(
				"G.G.K.A.A.[[SeH]C[C@H](N[*])C([*])=O |$;;;;_R1;;_R2;$|].[meC]");
		while (iterator.hasNextGroup()) {
			String group = iterator.nextGroup();
			list.add(group);
		}
		AssertJUnit.assertEquals("there should be 7 groups!", 7, list.size());
		AssertJUnit.assertEquals("G", list.get(0));
		AssertJUnit.assertEquals("G", list.get(1));
		AssertJUnit.assertEquals("K", list.get(2));
		AssertJUnit.assertEquals("A", list.get(3));
		AssertJUnit.assertEquals("A", list.get(4));
		AssertJUnit.assertEquals("[[SeH]C[C@H](N[*])C([*])=O |$;;;;_R1;;_R2;$|]",
				list.get(5));
		AssertJUnit.assertEquals("[meC]", list.get(6));

	}

	@Test
	public void testInlineSmilesChem() {

		SimpleNotationGroupIterator iterator = new SimpleNotationGroupIterator(
				"C[C@H](N[*])C([*])=O |$;;;;_R1;;_R2;$|");
		String group = null;
		int i = 0;
		while (iterator.hasNextGroup()) {
			group = iterator.nextGroup();
			i++;

		}
		AssertJUnit.assertEquals("C[C@H](N[*])C([*])=O |$;;;;_R1;;_R2;$|", group);
		AssertJUnit.assertEquals("there should be one group!", 1, i);
	}

	@Test
	public void testInlineSmilesRNA() {
		SimpleNotationGroupIterator iterator = new SimpleNotationGroupIterator(
				"P.[[*]OC[C@@H]1CN([*])C[C@H]([*])O1 |$_R1;;;;;;_R2;;;_R3;$|](A)[sP].RP.R(G)P.[LR]([5meC])");

		ArrayList<String> list = new ArrayList<String>();
		while (iterator.hasNextGroup()) {
			String group = iterator.nextGroup();
			list.add(group);
			System.out.println(group);

		}
		AssertJUnit.assertEquals("there should be five groups!", 5, list.size());
		AssertJUnit.assertEquals("P", list.get(0));
		AssertJUnit.assertEquals(
				"[[*]OC[C@@H]1CN([*])C[C@H]([*])O1 |$_R1;;;;;;_R2;;;_R3;$|](A)[sP]",
				list.get(1));
		AssertJUnit.assertEquals("RP", list.get(2));
		AssertJUnit.assertEquals("R(G)P", list.get(3));
		AssertJUnit.assertEquals("[LR]([5meC])", list.get(4));

	}

}
