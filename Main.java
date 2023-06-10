package tx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Game of Life");
		var pane = new BorderPane();
		
		//sub_pane which is a child of the pane here
		var instance = new GameOfLife(50); //50*50
		var sub_pane = instance.pane_from_current();
		pane.setBottom(sub_pane);
		
		//button
		Button b = new Button("Next");
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				instance.next_generation();
				pane.setBottom(instance.pane_from_current());
			}
			
		});
		pane.setLeft(b);
		
		
		var scene = new Scene(pane, 550, 580);
		
		primaryStage.setScene(scene);
