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


	public GameOfLife(int max) {
		this.MAX = max;
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
		 * -> Was ist mit dem nachbarfeld rechts (X+1, Y), wenn X bereits auf Feld 50 ist? Welchen wert hat dieses Feld?
		 * => Lösung, wir nehemen das erste feld also X=0 als nachbarn, so geht das auch auf der Y achse.
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
		 * 
		 * [3] Ein Feld ist in seiner nächsten Generation NICHT AUSGEFÜLLT, wenn es in seiner jetzigen generation nur 1 oder mehr als 3 
		 * also 4+ ausgefüllte nachbarn hat. <TOT/Gar nicht erst geboren>
		 * 
		 * 
		 * Also auf an die Implementation:
		 * */
		
		var NEXT_GENERATION = new HashMap<String, Boolean>();
		
		for(int y = 0; y < this.MAX; y++) {
			for(int x = 0; x < this.MAX; x++) {
				//Nahcbarfeld counter
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
				//[1] Regel
				if(MOMENTAN_AUSGEFULLT == true && (counter == 2 || counter == 3)) {
					NEXT_GENERATION.put(x+":"+y, true); //überlebt
				}
				//[2] Regel
				else if(MOMENTAN_AUSGEFULLT == false && counter == 3) {
					NEXT_GENERATION.put(x+":"+y, true); //wird geboren
				}
				//[3] Regel
				else {
					NEXT_GENERATION.put(x+":"+y, false); //stirbt
				}
				counter = 0;
			}
		}
//nächste Generation wird als diese Generation gesetzt.
		this.CURRENT_GENERATION = NEXT_GENERATION;
	}
	
	public Boolean cord(int x, int y) {
		String lx = Integer.toString(x);
		String ly = Integer.toString(y);
		//[Logik] wenn x oder y über seine breite hinausgeht oder kleiner als index 0, wird das jeweils erste oder letzte element zugewiesen:
//Achtung! Das hier zum String gecastet wird ist unnötig,weil es unten eh passiert gensz die lx und ly ebenfalls unnötig sind Mann kann direkt x y nehmen bzw verändern.
		if(x == this.MAX) {
			lx = "0";
		}
		if(y == this.MAX) {
			ly = "0";
		}
		if(x == -1) {
			lx = Integer.toString(49);
		}
		if(y == -1) {
			ly = Integer.toString(49);
		}
		var m = this.CURRENT_GENERATION.get(lx+":"+ly);
		//System.out.println(lx+":"+ly+"("+x+":"+y+")="+m);
		return m;
	}
	
	public Pane pane_from_current() {
		var sub_pane = new Pane();
		for(int y = 0; y < this.MAX; y++) {
			//System.out.println(y+":");
			for(int x = 0; x < this.MAX; x++) {
				//neues Quadrat
				var r = new Rectangle(10,10);
				r.setX(x*11);
				r.setY(y*11);
				if(this.CURRENT_GENERATION.get(x+":"+y)) {
					r.setFill(Color.GREY);
				}else{
					r.setFill(Color.WHITE);
				}
				//System.out.println(this.CURRENT_GENERATION.get(x+":"+y));
				sub_pane.getChildren().add(r);
			}
		}
		return sub_pane;
	}
	
	
	public HashMap<String, Boolean> generate_startset() {
		//startset definieren-> alle Felder in diesem Array sind die Felder die am Anfang alive sind
		String[] alive = {
				// Loaf
			    "10:10", "11:10", "9:11", "12:11", "10:12", "12:12", "11:13",
			    // Glider
			    "20:20", "21:20", "22:20", "22:21", "21:22",
			    // F-Pentomino
			    "30:30", "31:30", "29:31", "30:31", "30:32",
			    // Tumbler
			    "40:40", "41:40", "42:40", "43:40", "44:40", "45:40", "41:41", "42:41", "43:41", "44:41", "45:41", "41:42", "43:42", "44:42", "45:42", "41:43", "42:43", "43:43", "44:43", "45:43",
			    // R-Pentomino
			    "5:5", "6:5", "6:6", "5:6", "5:7",
			    // Lightweight Spaceship
			    "15:15", "16:15", "17:15", "18:15", "14:16", "18:16", "18:17", "14:18", "17:18",
			    // Weitere Formen hier
		};
		//das eben angelegte Array in eine Liste umwandeln um es einfacher zu handlen
		List<String> alive_list = Arrays.asList(alive);
		
		//hashmap inistialisieren die als Key "x:y" und als value einen boolean nimmt. true = ALIVE, false = DEAD
		var startset = new HashMap<String, Boolean>();
		
		//erstmal alle coordinaten erzeugen
		for (int y = 0; y < this.MAX; y++) {
			//und noch die y coordinaten
			for(int x = 0; x < this.MAX; x++) {
				//element
				if(alive_list.contains((x+":"+y)))
				{
					//die coordinate befindet sich im array also ist sie ALIVE by default
					startset.put(x+":"+y, true);
					//System.out.println(startset.get(x+":"+y));
				} else {
					//die coordinate ist nicht im array also ist sie DEAD by default
					startset.put(x+":"+y, false);
				}
			}
		}
		return startset;
	}

}
