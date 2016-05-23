package org.helm.notation2.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.helm.notation2.Attachment;
import org.helm.notation2.Chemistry;
import org.helm.notation2.Monomer;
import org.helm.notation2.MonomerFactory;
import org.helm.notation2.MonomerStore;
import org.helm.notation2.exception.ChemistryException;
import org.helm.notation2.exception.MonomerLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Converter {

	  /** The Logger for this class */
	  private static final Logger LOG = LoggerFactory.getLogger(Converter.class);
	
	private Converter(){
		
	}
	
	
	
	/**
	 * @param m
	 * @return
	 * @throws ChemistryException
	 */
	public static Monomer convertMonomer(Monomer m) throws ChemistryException{
		
		/*convert SMILES*/
		String smiles =  m.getCanSMILES();
		LOG.debug("originalSMILES: " + smiles);
		String convertedSMILES = Chemistry.getInstance().getManipulator().convertExtendedSmiles(smiles);
		m.setCanSMILES(convertedSMILES);
		
		
		/*convert Attachments*/
		for(Attachment a : m.getAttachmentList()){
			smiles = a.getCapGroupSMILES();
			convertedSMILES =  Chemistry.getInstance().getManipulator().convertExtendedSmiles(smiles);
			a.setCapGroupSMILES(convertedSMILES);
		}
		
		/*merge Attachments into Smiles*/
		m.setCanSMILES(mergeAttachmentsIntoSmiles(m.getCanSMILES(), m.getAttachmentList()));
		
		return m;
	}
	
	private static String mergeAttachmentsIntoSmiles(String smiles, List<Attachment> attachments){
		LOG.debug("OldSMILES: " + smiles);
		Map<Integer, String> map = new HashMap<Integer, String>();
		for(Attachment attachment: attachments){
			map.put(Integer.parseInt(attachment.getLabel().split("R")[1]), attachment.getCapGroupName());
		}
		
		if(smiles !=  null){
			Pattern pattern = Pattern.compile("\\[\\*:(\\d+)\\]");
			Matcher matcher = pattern.matcher(smiles);
			StringBuilder sb = new StringBuilder();
			int start = 0;
			while(matcher.find()){
				sb.append(smiles.substring(start,matcher.start()) + "[" + map.get(Integer.parseInt(matcher.group(1))) +   ":" + matcher.group(1) + "]");
				start = matcher.end();
			}
			
			if(start < smiles.length()){
				sb.append(smiles.substring(start));
			}
			LOG.debug("NewSMILES: " + sb.toString());
			return sb.toString();
		}
		return smiles;
		
		
		
	}
	
	
	
	public static void convertMonomerStore() throws MonomerLoadingException, ChemistryException{
	MonomerStore monomerStore = MonomerFactory.getInstance().getMonomerStore();
	
	/*convert MonomerStore*/
	List<Monomer> monomers = monomerStore.getAllMonomersList();
	
	for(Monomer monomer: monomers){

		monomer = convertMonomer(monomer);
	}
	
	/*convert default Attachments*/
	Map<String, Attachment> attachments = MonomerFactory.getInstance().getAttachmentDB();
	for(Map.Entry<String, Attachment> e: attachments.entrySet()){
		
		String smiles = Chemistry.getInstance().getManipulator().convertExtendedSmiles(e.getValue().getCapGroupSMILES());
		e.getValue().setCapGroupSMILES(smiles);
	}
	
	
	/*update smiles db*/
	Map<String, Monomer > mapSmiles = monomerStore.getSmilesMonomerDB();
	Set<Map.Entry<String, Monomer>> local = mapSmiles.entrySet();
	Map<String, Monomer> changedmapSmiles = new HashMap<String, Monomer>();
	
	for(Map.Entry<String, Monomer> e : local){
		Monomer m = e.getValue();
		changedmapSmiles.put(m.getCanSMILES(),m);
	}
	

	mapSmiles = changedmapSmiles;
	
	}
	

}
