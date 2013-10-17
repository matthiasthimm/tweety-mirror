package net.sf.tweety.plugin.parameter;

import java.io.File;
import java.util.ArrayList;

/**
 * This parameter holds a file-list of possible arguments
 * 
 * @author Bastian Wolf
 * 
 */

public class FileListCommandParameter extends CommandParameter {

	/**
	 * the value each instantiated needs, has to be in selections
	 */
	private File[] value;

	/**
	 * 
	 * @param id
	 * @param des
	 */
	public FileListCommandParameter(String id, String des) {
		super(id, des);

	}

	/**
	 * returns the given instantiation argument value for this parameter
	 * 
	 * @return the given instantiation argument value for this parameter
	 */
	public File[] getValue() {
		return value;
	}

	/**
	 * sets the instantiated parameter argument value, value has to be one of
	 * the options contained in selections
	 * 
	 * @param value
	 *            the value given as argument value
	 */
	public void setValue(File[] value) {
		this.value = value;
	}

	/**
	 * checks whether a cli input parameter argument is valid for the called
	 * command parameter
	 */
	@Override
	public boolean isValid(String s) {
		// check for valid path
		return false;
	}

	/**
	 * instantiates a new parameter iff the given value ist valid for this
	 * command parameter
	 */
	@Override
	public CommandParameter instantiate(String filename) {
		
		return null;
	}

	
	public CommandParameter instantiate(File[] files){
		FileListCommandParameter fl = (FileListCommandParameter) this.clone();
		fl.setValue(files);
		
		return fl;
		
	}
	
	
	// TODO: implement ArrayList-Instantiation
	public CommandParameter instantiate(ArrayList<String> a){
		File[] out = new File[a.size()];

		for (int i = 0; i < a.size(); i++) {
			// if(this.isValid(a.get(i))){
			out[i] = new File(a.get(i).toString()).getAbsoluteFile();
			// }
		}

		FileListCommandParameter filelist = (FileListCommandParameter) this
				.clone();
		filelist.setValue(out);

		return filelist;
	}

	@Override
	public Object clone(){
		return new FileListCommandParameter(this.getIdentifier(), this.getDescription());
	}
}
