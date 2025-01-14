package pis.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pis.Database.Database;

public class LoginController {

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private void initialize() {
        // Menambahkan event handler untuk TextField dan PasswordField
        username.setOnKeyPressed(this::handleKeyPressed);
        password.setOnKeyPressed(this::handleKeyPressed);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                login(); // Panggil metode login ketika Enter ditekan
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Database Tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void login() throws SQLException {
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
        connect = Database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            result = prepare.executeQuery();

            Alert alert;

            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Login Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Welcome!");
                    alert.showAndWait();

                    // HIDE LOGIN FORM
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.close();

                    // Load the next scene (dashboard)
                    Parent root = FXMLLoader.load(getClass().getResource("/pis/Design/dashboard.fxml"));
                    Stage newStage = new Stage();
                    Scene scene = new Scene(root);

                    newStage.setTitle("Dashboard Page");

                    Image icon = new Image(getClass().getResourceAsStream("/pis/Assets/bus.jpg"));
                    newStage.getIcons().add(icon);
                    newStage.setScene(scene);
                    newStage.show();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username or password");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
