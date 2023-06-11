package tx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameOfLife {
	private int MAX;
	private HashMap<String, Boolean> CURRENT_GENERATION;

	public GameOfLife(int max) {
		this.MAX = max;
		this.CURRENT_GENERATION = this.generate_startset();
	}
	
	public void next_generation() {
		var NEXT_GENERATION = new HashMap<String, Boolean>();
    
		for(int y = 0; y < this.MAX; y++) {
			for(int x = 0; x < this.MAX; x++) {
				var counter = 0;
				
				var MOMENTAN_AUSGEFULLT = cord(x,y);
				
				counter += (this.cord(x-1,y)   == true) ? 1 : 0; //[ML]
				counter += (this.cord(x-1,y-1) == true) ? 1 : 0; //[OL]
				counter += (this.cord(x,y-1)   == true) ? 1 : 0; //[OM]
				counter += (this.cord(x+1,y-1) == true) ? 1 : 0; //[OR]
				counter += (this.cord(x+1,y)   == true) ? 1 : 0; //[MR]
				counter += (this.cord(x+1,y+1) == true) ? 1 : 0; //[UR]
				counter += (this.cord(x,y+1)   == true) ? 1 : 0; //[UM]
				counter += (this.cord(x-1,y+1) == true) ? 1 : 0; //[UL]
				
				if(MOMENTAN_AUSGEFULLT == true && counter == 2) {
					NEXT_GENERATION.put(x+":"+y, true); //überlebt
				}
				else if(counter == 3) {
					NEXT_GENERATION.put(x+":"+y, true); //wird geboren
				}
				else {
					NEXT_GENERATION.put(x+":"+y, false); //stirbt
				}
				counter = 0;
			}
		}
		this.CURRENT_GENERATION = NEXT_GENERATION;
	}
	
	public Boolean cord(int x, int y) {
		if(x == this.MAX)	x = 0;
		if(y == this.MAX) y = 0;
		if(x == -1) x = this.MAX-1;
		if(y == -1) y = this.MAX-1;
		return this.CURRENT_GENERATION.get(x+":"+y);
	}
	
	public Pane pane_from_current() {
		var sub_pane = new Pane();
		for(int y = 0; y < this.MAX; y++) {
			for(int x = 0; x < this.MAX; x++) {
				var r = new Rectangle(10,10);
				r.setX(x*11);
				r.setY(y*11);
				r.setArcHeight(6);
				r.setArcWidth(6);
        
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
    
		List<String> alive_list = Arrays.asList(alive); //in liste umwandeln
		var startset = new HashMap<String, Boolean>();
    
		for (int y = 0; y < this.MAX; y++) {
			for(int x = 0; x < this.MAX; x++) {
				if(alive_list.contains((x+":"+y)))
				{
					startset.put(x+":"+y, true);
				} else {
					startset.put(x+":"+y, false);
				}
			}
		}
		return startset;
	}
}
