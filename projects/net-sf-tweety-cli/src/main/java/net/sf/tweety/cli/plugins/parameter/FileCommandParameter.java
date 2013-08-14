package net.sf.tweety.cli.plugins.parameter;

import java.io.File;

/**
 * This class extends the CommandParameter to be used with files
 * 
 * @author Bastian Wolf
 *
 */

public class FileCommandParameter extends CommandParameter {

	/**
	 * all possible values for this parameter's argument
	 */
	private String filename;
	
	/**
	 * the value each instantiated needs, has to be in selections
	 */
	private File file;
	
	public FileCommandParameter(String id, String des) {
		super(id, des);

	}

	public FileCommandParameter(String id, String des, String filename){
		super(id, des);
		setFilename(filename);
	}
	
	/**
	 * returns each possible selection argument
	 * @return each possible selection argument
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * sets new selection parameter
	 * @param selections
	 */
	public void setFilename(String s){
		filename = s;
	}
	
	/**
	 * returns the given instantiation argument value for this parameter
	 * @return the given instantiation argument value for this parameter
	 */
	public File getFile() {
		return file;
	}

	/**
	 * sets the instantiated parameter argument value,
	 * value has to be one of the options contained in selections
	 * @param value the value given as argument value
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * checks whether a cli input parameter argument is valid for the called command parameter
	 */
	@Override
	public boolean isValid(String s) {
		// validation criteria?
		return false;
	}
	
	/**
	 * instantiates a new parameter iff the given value ist valid for this command parameter
	 */
	@Override
	public CommandParameter instantiate(String s) {
		
		return null;
	}


}
