package org.helm.notation2;

import java.io.IOException;

import org.helm.notation.MonomerFactory;
import org.helm.notation.MonomerLoadingException;
import org.helm.notation.model.Monomer;
import org.testng.annotations.Test;

public class MonomerManagementActions {

  @Test
  public void testgetMolecularFormularExamples() throws MonomerLoadingException {
    MonomerFactory.getInstance();
    boolean flag = false;
    while (!flag) {
      try {
        Monomer monomer = MonomerFactory.getInstance().getMonomerStore().getMonomer("CHEM", "Az");
        if (monomer != null) {
          flag = true;
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        System.out.println(e.getMessage());
      }
    }
    // Assert.assertEquals(result, "C16H20N4O4");
  }


}
