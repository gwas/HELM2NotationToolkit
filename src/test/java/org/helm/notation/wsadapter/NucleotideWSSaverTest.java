package org.helm.notation.wsadapter;

import java.io.IOException;

import java.net.UnknownHostException;

import org.helm.notation2.Nucleotide;

public class NucleotideWSSaverTest {
  // @Test
  public void saveNucleotide() throws UnknownHostException, IOException {
    NucleotideWSSaver nucleotideWsSaver = new NucleotideWSSaver();
    Nucleotide n = new Nucleotide("s", "[fR](T)");
    nucleotideWsSaver.saveNucleotideToStore(n);

  }
}
