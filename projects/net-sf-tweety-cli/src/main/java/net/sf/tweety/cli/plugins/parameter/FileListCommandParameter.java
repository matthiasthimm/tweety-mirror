package net.sf.tweety.cli.plugins.parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This parameter holds a file-list of possible arguments
 * 
 * @author Bastian Wolf
 *
 */

public class FileListCommandParameter extends CommandParameter {
	
	/**
	 * all possible values for this parameter's argument
	 */
	private List<File> files;
	
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
	 * 
	 * @param id
	 * @param des
	 * @param files
	 */
	public FileListCommandParameter(String id, String des, List<File> files){
		super(id, des);
		setFiles(files);
	}
	
	/**
	 * returns each possible selection argument
	 * @return each possible selection argument
	 */
	public List<File> getFiles() {
		return files;
	}

	/**
	 * sets new files selection parameter
	 * @param files
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}

	/**
	 * returns the given instantiation argument value for this parameter
	 * @return the given instantiation argument value for this parameter
	 */
	public File[] getValue() {
		return value;
	}

	/**
	 * sets the instantiated parameter argument value,
	 * value has to be one of the options contained in selections
	 * @param value the value given as argument value
	 */
	public void setValue(File[] value) {
		this.value = value;
	}

	/**
	 * checks whether a cli input parameter argument is valid for the called command parameter
	 */
	@Override
	public boolean isValid(String s) {
		
		return false;
	}
	
	/**
	 * instantiates a new parameter iff the given value ist valid for this command parameter
	 */
	@Override
	public CommandParameter instantiate(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<CommandParameter> instantiate(ArrayList<String> a){
		return null;
	}
}
