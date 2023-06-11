package tx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Game of Life");
		var border_pane = new BorderPane();
		
		//neue GameOfLifeinstanz anlegen
		var instance = new GameOfLife(50); //50*50
		//die sub_pane generieren
		var sub_pane = instance.pane_from_current();
		//die sub_pane zur borderpane hinzufügen
		border_pane.setBottom(sub_pane);
		
		//button - mit jedem klick des buttons wird die nächste generation generiert und mit border_pane.setBottom() geupdated.
		Button b = new Button("Next");
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//nächste generation generieren
				instance.next_generation();
				//update der anzeige
				border_pane.setBottom(instance.pane_from_current());
			}
		});
		border_pane.setLeft(b); //button oben hinzufügen		
				
		var scene = new Scene(border_pane, 550, 580);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
