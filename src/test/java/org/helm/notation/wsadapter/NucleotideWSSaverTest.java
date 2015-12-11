package org.helm.notation.wsadapter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.helm.notation.model.Nucleotide;
import org.testng.annotations.Test;

public class NucleotideWSSaverTest {
  // @Test
  public void saveNucleotide() throws UnknownHostException, IOException {
    NucleotideWSSaver nucleotideWsSaver = new NucleotideWSSaver();
    Nucleotide n = new Nucleotide("s", "[fR](T)");
    nucleotideWsSaver.saveNucleotideToStore(n);

  }
}
