package net.sf.tweety.preferences.update;

/**
 * Konzeption:
 * 
 * Der Update-Stream ist eine Datenstruktur, die benutzt wird, um eine Reihe von Updates für dynamische POs
 * einzuspeisen und zu verwalten. In dem Stream stehen Update-Elemente vom Typ Update, die wie folgt aufgebaut sind:
 * Update(Präferenzordnung, Operation, Anzahl der Operationen, Element)
 * (Update<PreferenceOrder<T>, Operation, Integer, T>)
 * 
 * Pro Aggregator-Instanz wird ein UpdateStream sowie Reader/Writer von dessen Konstruktor erzeugt.
 * 
 * Fragen/Probleme:
 * - Writer exklusiv zugänglich für die Listener eines Aggregators?
 *  
 * @author Bastian Wolf
 *
 * @param <T>
 */

public class UpdateStream<T> {
	
	// Konstruktor
	public UpdateStream(){
		
	}
	
	// Methoden....
}
