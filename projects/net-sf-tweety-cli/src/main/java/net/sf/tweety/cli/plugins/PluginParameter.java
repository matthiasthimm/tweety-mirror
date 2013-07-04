package net.sf.tweety.cli.plugins;

public class PluginParameter {
	
	private String name;
	
	private String description;
	
	public PluginParameter(){
		new PluginParameter();
	}
	
	public PluginParameter(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
}
