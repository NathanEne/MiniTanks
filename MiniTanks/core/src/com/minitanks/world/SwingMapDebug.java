package com.minitanks.world;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;


public class SwingMapDebug extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        int width = 30, height = 30;
        ArrayList<float[]> wallP = MapGenerator.generateWallPoints(width, height);
        Group root = new Group();
        ObservableList list = root.getChildren();
        for (float[] p : wallP){
            Circle c = new Circle(p[0]*20, p[1]*20, p[2]*20);
            c.setFill(Color.GRAY);
            list.add(c);
        }

        Scene scene = new Scene(root, width*20, height*20);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String args[]){
        launch(args);
    }
}
