/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.tweety.preferences.io;

import net.sf.tweety.commons.util.Triple;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.Relation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

/**
 * 
 * 
 * @author Bastian Wolf
 * @param <T>
 */
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
		
		Iterator<Triple<T, T, Relation>> it = po.iterator();
		while (it.hasNext()){
			Triple<T, T, Relation> temp = it.next();
			if(temp.getThird() == Relation.LESS){
				pw.println(temp.getFirst() + " < " + temp.getSecond());
			}
			if(temp.getThird() == Relation.LESS_EQUAL){
				pw.println(temp.getFirst() + " <= " + temp.getSecond());
			}
		}
		} catch (IOException e){
			System.out.println("File could not be generated");
		} finally {
			if (pw != null)
				pw.close();
		}
	}
}
