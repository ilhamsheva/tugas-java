/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pis.Controller.*;

/**
 *
 * @author sheva
 */
public class PIS extends Application {
       
    @Override
    public void start(Stage stage) throws Exception {
<<<<<<< HEAD
        Parent root = FXMLLoader.load(getClass().getResource("Design/Login.fxml"));
=======
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
>>>>>>> 4c38661002152d6c0286e3932e1ed33b0d248e99
        
        Scene scene = new Scene(root);
           
        stage.setScene(scene);
        stage.setTitle("Login Page");
        
        Image icon = new Image(getClass().getResourceAsStream("Assets/bus.jpg"));
        stage.getIcons().add(icon);
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
