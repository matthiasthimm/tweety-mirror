package net.sf.tweety.preferences.io;

import net.sf.tweety.preferences.PreferenceOrder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

import net.sf.tweety.util.Pair;

public class POWriter<T> {
	
	public void writeToFile(String filename, PreferenceOrder<T> po){
		
		PrintWriter pw = null;
		try {
		Writer fw = new FileWriter(filename);
		Writer bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw);
		
		String s = "{";
		int count = 1;
		for (T e : po.getDomainElements()){
			
			if (count < po.getDomainElements().size())
				s += e.toString() + ", ";
			else
				s += e.toString();
		count++;
		}
		
		s += "}";
		
		pw.println(s);
		
		Iterator<Pair<T,T>> it = po.iterator();
		while (it.hasNext()){
			Pair<T, T> temp = it.next();
			pw.println(temp.getFirst() + " < " + temp.getSecond());
		}
		} catch (IOException e){
			System.out.println("File could not be generated");
		} finally {
			if (pw != null)
				pw.close();
		}
	}
}
