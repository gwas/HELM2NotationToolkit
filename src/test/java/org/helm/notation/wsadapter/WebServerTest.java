package org.helm.notation.wsadapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.helm.notation2.Attachment;
import org.helm.notation2.Monomer;
import org.helm.notation2.MonomerFactory;
import org.helm.notation2.Nucleotide;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.EncoderException;
import org.helm.notation2.exception.MonomerException;
import org.helm.notation2.exception.NotationException;
import org.helm.notation2.wsadapter.MonomerWSLoader;
import org.helm.notation2.wsadapter.MonomerWSSaver;
import org.helm.notation2.wsadapter.NucleotideWSLoader;
import org.helm.notation2.wsadapter.NucleotideWSSaver;
import org.jdom2.JDOMException;
import org.testng.annotations.Test;

public class WebServerTest {

  // @Test
  public void loadNucleotides() throws NotationException, MonomerException, IOException, JDOMException, URISyntaxException {

    NucleotideWSLoader n = new NucleotideWSLoader();
    Map<String, String> map = n.loadNucleotideStore();
    System.out.println(map.size());
  }

  // @Test
  public void loadMonomers() throws NotationException, MonomerException, IOException, JDOMException, URISyntaxException, ChemistryException, EncoderException {
    Map<String, org.helm.notation2.Attachment> mon = MonomerFactory.getInstance().getAttachmentDB();
    MonomerWSLoader n = new MonomerWSLoader("PEPTIDE");
    Map<String, Monomer> map = n.loadMonomerStore(mon);
    System.out.println(map.size());
  }

  @Test
  public void testWebserverMonomer() {
    MonomerWSSaver saver = new MonomerWSSaver();
    Monomer monomer = new Monomer("PEPTIDE", "Backbone", "", "Sabi");
    saver.saveMonomerToStore(monomer);
  }

  @Test
  public void testWebserverNucleotide() {
    NucleotideWSSaver saver = new NucleotideWSSaver();
    Nucleotide nucleotide = new Nucleotide("SABI", "R(S)P");
    nucleotide.setNaturalAnalog("T");
    saver.saveNucleotideToStore(nucleotide);
  }

}
