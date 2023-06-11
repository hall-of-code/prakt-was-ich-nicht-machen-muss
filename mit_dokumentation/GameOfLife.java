package tx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameOfLife {
	//max value for x/y so when the Dataset should have 50*50 pixels this number should be 50
	private int MAX;
	private HashMap<String, Boolean> CURRENT_GENERATION;


	//constructor
	public GameOfLife(int max) {
		//max ist die höhe und breite. 50 wäre also ein Raster von 50*50 Quadraten (die die coordinaten 0-49 auf beiden achsen haben, weil arrays usw ja bei 0 beginnen)
		this.MAX = max;
		
		//das startset wird generiert und als CURRENT_GENERATION gesetzt.
		this.CURRENT_GENERATION = this.generate_startset();
	}
	
	public void next_generation() {
		//so hier soll jetzt die jeweils nächste Generation "heranwachsen"
		
		/* Jetzt der [AUFBAU] 
		 * Also erstmal wo sich die Felder (Quadrate) befinden:
		 * [MM] X  , Y   = das momentane Feld!
		 * [ML] X-1, Y   = das Feld davor
		 * [OL] X-1, Y-1 = das Feld schräg oben links
		 * [OM] X  , Y-1 = das Feld darüber
		 * [OR] X+1, Y-1 = das Feld schräg oben rechts
		 * [MR] X+1, Y   = das Feld danach
		 * [UR] X+1, Y+1 = das Feld schräg unten rechts
		 * [UM] X  , Y+1 = das Feld darunter
		 * [UL] X-1, Y+1 = das Feld schräg unten links 
		 * 
		 * ([OL] steht z.B. für Oben-Links, [ML] für Mitte Links usw.)
		 * 
		 * [Achtung] Endlos implementation, die Regeln gelten auf einem endlosen feld (thoeretisch), wir haben uns aber
		 * für eine bestimte Größe (50*50) entschieden. Das Problem ist jetzt:
		 * 
		 * -> Was ist mit dem nachbarfeld rechts (X+1, Y), wenn X bereits auf Feld 50 ist? Welchen wert hat dieses Feld?
		 * => Lösung, wir nehemen das erste Feld also X=0 als Nachbarn, so geht das auch auf der Y achse.
		 * 	  Damit stellen wir in gewisser weise eine unendlichkeit sicher weil alles was auf der Rechten seite über den rand hinaus geht,
		 *    kommt auf der linken seite wieder herein, also der Nachbar vom 50. Feld (X Koordinate 49) ist demnach das Feld mit dem 1. Feld (X Koordinate 0). 
		 *    Und weil sich das auch auf die Y achse bezieht hat jedes Feld 8 Nachbarn, weil alles was am Rand steht seine nachbarn auf der entgegengesetzten seite 
		 *    findet.
		 *    
		 * Was ein Gehirnf*ck meine fresse.
		 * 
		 * 
		 * Jetzt die [REGELN]
		 * 
		 * [1] Ein Feld ist in seiner nächsten Generation ausgefüllt, wenn es in seiner jetztigen generation 
		 * AUSGEFÜLLT war und 2-3 ausgefüllte nachbarfelder hat. <ÜBERLEBT>
		 * 
		 * [2] Ein Feld ist in seiner nächsten Generation ausgefült, wenn es in seiner jetztigen generation
		 * NICHT AUSGEFÜLLT war, und genau 3 ausgefüllte nachbarfelder hat. <GEBOREN> 
		 * -> Bedeutet bei der [1] Regel müssen wir blos schauen ob es momentan ausgefüllt ist und ob es 2 Nachbarn hat, 
		 * weil mit 3 nachbarn wird ja automatisch ein neues geboren, somit wäre ein zusätzliche if abfrage doppelt gemoppelt.
		 * 
		 * [3] Ein Feld ist in seiner nächsten Generation NICHT AUSGEFÜLLT, wenn es in seiner jetzigen generation nur 1 oder mehr als 3 
		 * also 4+ ausgefüllte nachbarn hat. <TOT/Gar nicht erst geboren>
		 * 
		 * 
		 * Also auf an die Implementation:
		 * */
		
		//wir definieren erstmal eine neue Next-Generation Map in die wir nach und nach die Nächste generation speichern.
		var NEXT_GENERATION = new HashMap<String, Boolean>();
		
		//Jetzt wieder alle Koordinaten durchgehen und alle deren Nachbarfelder überprüfen ob sie momentan ausgefüllt sind.
		for(int y = 0; y < this.MAX; y++) {
			for(int x = 0; x < this.MAX; x++) {
				//Nachbarfeld counter, wird immer um 1 erhöht wenn ein Nachbarfeld ausgefüllt ist.
				var counter = 0;
				
				//[MM] momentanes Feld um das es gerade geht
				var MOMENTAN_AUSGEFULLT = cord(x,y);
				
				//counter += 1 wenn entsprechnede coordinate true zurückgibt also ausgefüllt ist
				counter += (this.cord(x-1,y)   == true) ? 1 : 0; //[ML]
				counter += (this.cord(x-1,y-1) == true) ? 1 : 0; //[OL]
				counter += (this.cord(x,y-1)   == true) ? 1 : 0; //[OM]
				counter += (this.cord(x+1,y-1) == true) ? 1 : 0; //[OR]
				counter += (this.cord(x+1,y)   == true) ? 1 : 0; //[MR]
				counter += (this.cord(x+1,y+1) == true) ? 1 : 0; //[UR]
				counter += (this.cord(x,y+1)   == true) ? 1 : 0; //[UM]
				counter += (this.cord(x-1,y+1) == true) ? 1 : 0; //[UL]
				
				//jetzt noch die logik für die [REGELN]
				//[1] Regel -> Wenn sie in dieser Generation ausgefüllt war, überlebt sie mit nur 2 nachbarn auch in der nächsten generation
				if(MOMENTAN_AUSGEFULLT == true && counter == 2) {
					NEXT_GENERATION.put(x+":"+y, true); //überlebt
				}
				//[2] Regel -> wenn sie mindestens 3 Nachbarn hat, ist sie in der nächsten generation ausgefüllt egal ob sie in dieser generation ausgefüllt war oder nicht
				else if(counter == 3) {
					NEXT_GENERATION.put(x+":"+y, true); //wird geboren
				}
				//[3] Regel -> in jedem anderen fall stirbt sie oder wird gar nicht erst geboren (ausgefüllt)
				else {
					NEXT_GENERATION.put(x+":"+y, false); //stirbt
				}
				
				//nicht vergessen counter wieder auf 0 zu setzen, weil wir ja in der For schleife jede coordinate einzeln durchlaufen und jedes mal alle ihre 8 nachbarn zählen
				counter = 0;
			}
		}
		//Zum Schluss setzen wir die Momentane Generation zur nächsten generation,
		//also ist die Momentane Generation jetzt die Nächste Generation, und mit pane_from_current()
		//kann man jetzt die neue Pane bekommen und in der Main.java dann mit setBottom() updaten/setzen.
		this.CURRENT_GENERATION = NEXT_GENERATION;
	}
	
	
	//Gibt true zurück wenn die gegebene koordinate ausgefüllt ist, sonst false.
	public Boolean cord(int x, int y) {
		//[Logik] wenn x oder y über seine breite hinausgeht oder kleiner als index 0, wird das jeweils erste oder letzte element zugewiesen:
		// => also Nachbar Rechts vom Feld X 50 (Koordinate X 49) ist das erste element (Koordinate X 0) und so weiter
		if(x == this.MAX) {
			x = 0;
		}
		if(y == this.MAX) {
			y = 0;
		}
		if(x == -1) {
			x = this.MAX-1;
		}
		if(y == -1) {
			y = this.MAX-1;
		}
		//und dann einfach true oder false zurückgeben ob der jeweilige nachbar eben ausgefüllt ist oder nicht.
		return this.CURRENT_GENERATION.get(x+":"+y);
	}
	
	
	//generiert eine Pane inklusive aller kleinen Ausgefüllten und nicht ausgefüllten Quadarte -  aus der this.CURRENT_GENERATION hashmap
	public Pane pane_from_current() {
		var sub_pane = new Pane();
		for(int y = 0; y < this.MAX; y++) {
			for(int x = 0; x < this.MAX; x++) {
				//neues Quadrat erzeugen
				var r = new Rectangle(10,10);
				
				//warum hier *11? -> Weil tatsächlich dadurch zwischen den Quadraten am ende immer 1 px abstand zum nächsten ist.
				r.setX(x*11);
				r.setY(y*11);
				
				//optional zur verschönerung abgerundete Ecken:
				r.setArcHeight(6);
				r.setArcWidth(6);
				
				//wenn das entsprechende Feld True ist, heißt es es ist ausgefüllt
				if(this.CURRENT_GENERATION.get(x+":"+y) == true) {
					r.setFill(Color.BLUEVIOLET);
				}else{
					r.setFill(Color.WHITE);
				}
				sub_pane.getChildren().add(r);
			}
		}
		return sub_pane;
	}
	
	//das STARTSET ist der Ausgangszustand, also quasi die Formation mit der wir starten, also wenn das Programm ausgeführt wird, sind die in dem Array "alive" angegebenen
	//elemente die koordinaten "x:y" die ausgefüllt sind.
	public HashMap<String, Boolean> generate_startset() {
		String[] alive = {
			    "10:10", "11:10", "9:11", "12:11", "10:12",
			    "12:12", "11:13", "20:20", "21:20", "22:20",
			    "22:21", "21:22", "30:30", "31:30", "29:31",
			    "30:31", "30:32", "40:40", "41:40", "42:40",
			    "43:40", "44:40", "45:40", "41:41", "42:41",
			    "43:41", "44:41", "45:41", "41:42", "43:42",
			    "44:42", "45:42", "41:43", "42:43", "43:43",
			    "44:43", "45:43", "5:5", "6:5", "6:6", "5:6",
			    "5:7","5:4", "15:15", "16:15", "17:15", "18:15",
			    "14:16", "18:16", "18:17", "14:18", "17:18","17:19"
		};
		
		//das eben angelegte Array in eine Liste umwandeln um es einfacher zu handlen
		List<String> alive_list = Arrays.asList(alive);
		
		//hashmap inistialisieren die als Key "x:y" und als value einen boolean nimmt. true = ausgefüllt, false = nicht ausgefüllt
		var startset = new HashMap<String, Boolean>();
		
		//erstmal alle coordinaten erzeugen
		for (int y = 0; y < this.MAX; y++) {
			//und noch die x coordinaten
			for(int x = 0; x < this.MAX; x++) {
				//element
				if(alive_list.contains((x+":"+y)))
				{
					//die coordinate befindet sich im array also ist sie ausgefüllt
					startset.put(x+":"+y, true);
				} else {
					//die coordinate ist nicht im array also ist sie nicht ausgefüllt
					startset.put(x+":"+y, false);
				}
			}
		}
		return startset;
	}
}
