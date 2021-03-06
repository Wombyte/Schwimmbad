import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JOptionPane;

public class Schwimmbad {
	
	String text = "";
	
	public static void main(String args[]) {
		new Schwimmbad();
	}
	
	int kinder = 0;
	int jugendliche = 0;
	int erwachsene = 32;
	
	int gutscheine = 2;
	boolean wochenende = true;
	boolean ferien = false;
	
	public Schwimmbad() {
		//Daten einlesen
		kinder = Integer.parseInt( JOptionPane.showInputDialog("Wie viele Kinder (u4) sind dabei?"));
		jugendliche = Integer.parseInt( JOptionPane.showInputDialog("Wie viele Jugendliche (u16) sind dabei?"));
		erwachsene = Integer.parseInt( JOptionPane.showInputDialog("Wie viele Erwachsene sind dabei?"));
		gutscheine = Integer.parseInt( JOptionPane.showInputDialog("Wie viele Gutscheine sind dabei?"));
		wochenende = Boolean.parseBoolean( JOptionPane.showInputDialog("Ist gerade Wochenende?"));
		ferien = Boolean.parseBoolean( JOptionPane.showInputDialog("Sind gerade Ferien?"));
		
		//kontrolliert ob die Gruppe komplett eintreten darf
		if(kinder > 0 && erwachsene == 0) {
			JOptionPane.showMessageDialog(null, "Die kleinen Kinder können nicht ins Schwimmbad, da keine Person über 16 Jahren anwesend ist");
		}
		else{
			double kleinsterPreis = kleinsterPreis();
			
			JOptionPane.showMessageDialog(null, text + "Die Gruppe muss " + kleinsterPreis + "€ bezahlen");
		}
		
	}
	
	/*
	 * erstellt und vergleicht alle Preisvarianten mit Gutscheinen
	 * liefert die billigste zurück
	 */
	public double kleinsterPreis() {
		if(gutscheine == 0) return preisBerechnen(jugendliche, erwachsene);
		if(ferien) return preisBerechnen(jugendliche, erwachsene);
		
		double min_preis = preisBerechnen(jugendliche, erwachsene);
		double preis;
		String rabatt = "";
		String kostenlos = "";
			
		//Gutschein ohne -10%
		for(int i = 0; i <= gutscheine; i++) {
			//vermeidet negative anzahl an Personen
			if(jugendliche-i >= 0 && erwachsene-(gutscheine-i) >= 0) {
				preis = preisBerechnen(jugendliche-i, erwachsene-(gutscheine-i));
				text += "kostenlos: " + i + " Jugendliche und " + (gutscheine-i) + " erwachsene" + '\n';
				text += " = " + preis + '\n';
				if(preis < min_preis) {
					min_preis = preis;
				}
			}
		}
			
		//Gutscheine mit einmal -10%
		for(int i = 0; i <= gutscheine-1; i++) {
			//vermeidet negative anzahl an Personen
			if(jugendliche-i >= 0 && erwachsene-(gutscheine-1-i) >= 0) {
				preis = 0.9*preisBerechnen(jugendliche-i, erwachsene-(gutscheine-1-i));
				text += "kostenlos: " + i + " Jugendliche und " + (gutscheine-1-i) + " erwachsene" + '\n';
				text += "-10% (Gutschein)" + '\n';
				text += " = " + preis + '\n';
				if(preis < min_preis) {
					min_preis = preis;
				}
			}
		}
		return min_preis;
	}
	
	/*
	 * Methode die den minimalen Preis für die Gruppe berechnet
	 */
	public double preisBerechnen(int jugendliche, int erwachsene) {
		double preis = 0;
		
		while(jugendliche + erwachsene > 0) {
			//unterteilung der Menge in Untermengen, die immer <= 6 sind
			int e, j;
			//wenn wochenende ist gelten die Gruppentickets nicht
			//daher werden möglichst viele erwachsene mit min 2 Jugendlichen
			//in eine Gruppe getan, damit Familientickets zusammenkommen
			if(wochenende) {
				e = Math.min(4, erwachsene);
				j = Math.min(6-e, jugendliche); 
			}
			//wenn kein Wochenende ist, sind die Gruppentickets die billigsten
			//daher werden zunächste die Erwachsenen in eine Gruppe getan
			//um die größten ersparsame zu erlangen
			else {
				e = Math.min(6, erwachsene);
				j = Math.min(6-e, jugendliche); 
			}
			
			//die verschiedenen Preise werden ausgerechnet und dann wird das
			//billigste als "min" gespeichert
			double normal = preisNormal(j, e);
			double familienTicket = preisFamilienTicket(j, e);
			double gruppenTicket = preisGruppenTicket(j, e);
			double min = normal;
			
			if(familienTicket < min) {
				min = familienTicket;
			}
			if(gruppenTicket < min) {
				min = gruppenTicket;
			}
			
			//aktualisiert Preis und Anzahl der Personen 
			if(min == normal) {
				text += "Normaler Preis für " + j + " Jugendliche und " + e + " Erwachsene: " + min + "€" + '\n';
			}
			else if(min == gruppenTicket) {
				text += "Gruppenticket für " + j + " Jugendliche und " + e + " Erwachsene: " + min + "€" + '\n';
			}
			else {
				min = 8;
				if(e >= 2 && j >= 2) {
					e = 2;
					j = 2;
					text += "Familienticket für 2 Jugendliche und 2 Erwachsene: 8€" + '\n';
				}
				else if(e >= 1 && j >= 3) {
					e = 1;
					j = 3;
					text += "Familienticket für 3 Jugendliche und 1 Erwachsene: 8€" + '\n';
				}
			}
			preis += min;
			jugendliche -= j;
			erwachsene -= e;
		}
		
		return preis;
		
	}
	
	/*
	 * Methode, die den Preis der Gruppe mit einem Familienticket ausrechnet
	 * wenn keine Familie(3+1 oder 2+2) in der Gruppe ist, wird der normale Preis zurückgeliefert
	 * für die Personen, die nicht zur Familie gehören, wird der normale Preis berechnet
	 */
	public double preisFamilienTicket(int jugendliche, int erwachsene) {
		double preis = 0;
		if(erwachsene >= 2 && jugendliche >= 2) {
			preis += 8;
			erwachsene -= 2;
			jugendliche -= 2;
		}
		if(erwachsene >= 1 && jugendliche >= 3) {
			preis += 8;
			erwachsene -= 1;
			jugendliche -= 3;
		}
		preis += preisNormal(jugendliche, erwachsene);
		return preis;
	}
	
	/*
	 * Methode die den Preis für ein Gruppenticket ausrechnet
	 * wenn wochenende ist, wird der normale Preis zurückgeliefert
	 */
	public double preisGruppenTicket(int jugendliche, int erwachsene) {
		if(!wochenende) {
			return 11;
		}
		else {
			return preisNormal(jugendliche, erwachsene);
		}
	}
	
	/*
	 * Methode, die den normalen Preis ausgibt
	 * also 2.5€ für Jugendliche und 3.5€ für Erwachsenene
	 */
	public double preisNormal(int jugendliche, int erwachsene) {
		double preis = jugendliche*2.5 + erwachsene*3.5;;
		if(!wochenende) {
			preis *= 0.8;
		}
		return preis;
	}
}
