package pis;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private Button add_btn_tickets;

    @FXML
    private Button add_btn_trips;

    @FXML
    private LineChart<String, Number> bar_TP;

    @FXML
    private BarChart<String, Number> bar_TT;

    @FXML
    private Label card_TP;

    @FXML
    private Label card_TT;

    @FXML
    private Button clear_btn_tickets;

    @FXML
    private Button clear_btn_trips;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button delete_btn_tickets;

    @FXML
    private Button delete_btn_trips;

    @FXML
    private TableColumn<Ticket, String> from_tabel_tickets;

    @FXML
    private TableColumn<Ticket, Integer> id_tickets_tabel_tickets;

    @FXML
    private TableColumn<Trip, String> id_tickets_tabel_trips;

    @FXML
    private TableColumn<Trip, Integer> id_trips_tabel_trips;

    @FXML
    private Button logout;

    @FXML
    private TableColumn<Ticket, String> name_tabel_tickets;

    @FXML
    private TextField search_tickets;

    @FXML
    private TextField search_trips;

    @FXML
    private ComboBox<Trip> textfield_from_tickets;

    @FXML
    private TextField textfield_name_tickets;

    @FXML
    private TextField textfield_place_trips;

    @FXML
    private ComboBox<Trip> textfield_to_trips;

    @FXML
    private Button tickets_btn;

    @FXML
    private AnchorPane tickets_form;

    @FXML
    private TableColumn<Ticket, String> to_tabel_tickets;

    @FXML
    private Button trips_btn;

    @FXML
    private AnchorPane trips_form;

    @FXML
    private Button update_btn_tickets;

    @FXML
    private Button update_btn_trips;

    @FXML
    private TableView<Trip> tabel_trips;

    @FXML
    private TableView<Ticket> tabel_tickets;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private ObservableList<Ticket> ticketList;
    private ObservableList<Trip> tripList;
    private ObservableList<String> placeList;

    Alert alert;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTripsTable();
        loadTripsToComboBox();
        showDataTickets();
        initializeTicketsTable();
        updateDashboard();
    }

    // Add Function
    public void addTrips() {
        String sql = "INSERT INTO Trips (place) VALUES (?)";

        connect = Database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);

            if (textfield_place_trips.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in the blank field.");
                alert.showAndWait();
            } else {

                prepare.setString(1, textfield_place_trips.getText());
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Info Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                // Clear text field after adding
                textfield_place_trips.clear();

                // Refresh table data
                showDataTrips();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Trip> showDataTrips() {
        ObservableList<Trip> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Trips ORDER BY id ASC";

        connect = Database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                listData.add(new Trip(result.getInt("id"), result.getString("place")));
            }

            // Set data to the table view
            tabel_trips.setItems(listData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void updateTrips() {
        String sql = "UPDATE Trips SET place = ? WHERE id = ?";

        connect = Database.connectDb();

        try {
            Trip selectedTrip = tabel_trips.getSelectionModel().getSelectedItem();

            if (selectedTrip == null || textfield_place_trips.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a record and fill in the field.");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, textfield_place_trips.getText());
                prepare.setInt(2, selectedTrip.getId());
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Info Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                // Clear text field and refresh table
                textfield_place_trips.clear();
                showDataTrips();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTrips() {
        String sql = "DELETE FROM Trips WHERE id = ?";

        connect = Database.connectDb();

        try {
            Trip selectedTrip = tabel_trips.getSelectionModel().getSelectedItem();

            if (selectedTrip == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a record to delete.");
                alert.showAndWait();
            } else {
                Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation Message");
                confirmAlert.setHeaderText(null);
                confirmAlert.setContentText("Are you sure you want to delete this record?");
                Optional<ButtonType> option = confirmAlert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, selectedTrip.getId());
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    // Clear text field and refresh table
                    textfield_place_trips.clear();
                    showDataTrips();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTrips() {
        textfield_place_trips.clear();
        tabel_trips.getSelectionModel().clearSelection();
    }

    public void initializeTripsTable() {
        // Mengatur kolom di tabel Trips
        id_trips_tabel_trips.setCellValueFactory(new PropertyValueFactory<>("id"));
        id_tickets_tabel_trips.setCellValueFactory(new PropertyValueFactory<>("place"));

        // Menambahkan listener untuk menangani klik di tabel Trips
        tabel_trips.setOnMouseClicked(event -> {
            Trip selectedTrip = tabel_trips.getSelectionModel().getSelectedItem();
            if (selectedTrip != null) {
                textfield_place_trips.setText(selectedTrip.getPlace());
            }
        });

        // Memuat data ke dalam tabel
        showDataTrips();

        // Listener untuk pencarian
        search_trips.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTrips();
        });

    }

    public void searchTrips() {
        String searchKeyword = search_trips.getText().toLowerCase();

        // Periksa apakah data sudah ada
        if (tripList == null) {
            tripList = showDataTrips();
        }

        ObservableList<Trip> filteredList = FXCollections.observableArrayList();

        // Filter data berdasarkan keyword
        for (Trip trip : tripList) {
            if (trip.getPlace().toLowerCase().contains(searchKeyword)) {
                filteredList.add(trip);
            }
        }

        // Tampilkan data hasil filter ke tabel
        tabel_trips.setItems(filteredList);

        // Jika tidak ada hasil
        if (filteredList.isEmpty()) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Results Found");
            alert.setHeaderText(null);
            alert.setContentText("No records found for the keyword: " + searchKeyword);
            alert.showAndWait();
        }
    }

    public void loadTripsToComboBox() {
        ObservableList<Trip> tripList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Trips";

        connect = Database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                tripList.add(new Trip(result.getInt("id"), result.getString("place")));
            }

            textfield_from_tickets.setItems(tripList);
            textfield_to_trips.setItems(tripList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTicket() {
        String sql = "INSERT INTO Tickets (name, from_id, to_id) VALUES (?, ?, ?)";

        connect = Database.connectDb();

        try {
            if (textfield_name_tickets.getText().isEmpty()
                    || textfield_from_tickets.getValue() == null
                    || textfield_to_trips.getValue() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields.");
                alert.showAndWait();
            } else {
                Trip fromTrip = textfield_from_tickets.getValue();
                Trip toTrip = textfield_to_trips.getValue();

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, textfield_name_tickets.getText());
                prepare.setInt(2, fromTrip.getId());
                prepare.setInt(3, toTrip.getId());

                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Info Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();

                // Bersihkan form
                clearTickets();

                // Refresh tabel dan dashboard
                showDataTickets();
                updateDashboard();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeTicketsTable() {
        id_tickets_tabel_tickets.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_tabel_tickets.setCellValueFactory(new PropertyValueFactory<>("name"));
        from_tabel_tickets.setCellValueFactory(new PropertyValueFactory<>("fromPlace"));
        to_tabel_tickets.setCellValueFactory(new PropertyValueFactory<>("toPlace"));
    }

    public ObservableList<Ticket> showDataTickets() {
        ObservableList<Ticket> listData = FXCollections.observableArrayList();

        String sql = "SELECT t.id, t.name, f.place AS from_place, to_t.place AS to_place "
                + "FROM Tickets t "
                + "JOIN Trips f ON t.from_id = f.id "
                + "JOIN Trips to_t ON t.to_id = to_t.id "
                + "ORDER BY t.id ASC";

        connect = Database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                listData.add(new Ticket(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("from_place"),
                        result.getString("to_place")
                ));
            }

            tabel_tickets.setItems(listData);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void updateTicket() {
        String sql = "UPDATE Tickets SET name = ?, from_id = ?, to_id = ? WHERE id = ?";

        connect = Database.connectDb();

        try {
            Ticket selectedTicket = tabel_tickets.getSelectionModel().getSelectedItem();

            if (selectedTicket == null
                    || textfield_name_tickets.getText().isEmpty()
                    || textfield_from_tickets.getValue() == null
                    || textfield_to_trips.getValue() == null) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a record and fill in all fields.");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);

                Trip fromTrip = textfield_from_tickets.getValue();
                Trip toTrip = textfield_to_trips.getValue();

                prepare.setString(1, textfield_name_tickets.getText());
                prepare.setInt(2, fromTrip.getId());
                prepare.setInt(3, toTrip.getId());
                prepare.setInt(4, selectedTicket.getId());

                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Info Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                // Bersihkan form
                clearTickets();
                showDataTickets();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTicket() {
        String sql = "DELETE FROM Tickets WHERE id = ?";

        connect = Database.connectDb();

        try {
            Ticket selectedTicket = tabel_tickets.getSelectionModel().getSelectedItem();

            if (selectedTicket == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select a record to delete.");
                alert.showAndWait();
            } else {
                Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation Message");
                confirmAlert.setHeaderText(null);
                confirmAlert.setContentText("Are you sure you want to delete this record?");
                Optional<ButtonType> option = confirmAlert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, selectedTicket.getId());
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Info Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    // Bersihkan form
                    clearTickets();
                    showDataTickets();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTickets() {
        textfield_name_tickets.clear();
        textfield_from_tickets.getSelectionModel().clearSelection();
        textfield_to_trips.getSelectionModel().clearSelection();
        tabel_tickets.getSelectionModel().clearSelection();
    }

    public void searchTicket() {
        String searchKeyword = search_tickets.getText().toLowerCase(); // Ambil keyword pencarian dari TextField
        ObservableList<Ticket> filteredList = FXCollections.observableArrayList();

        // Periksa apakah daftar data sudah ada
        if (ticketList == null) {
            ticketList = showDataTickets(); // Muat data jika belum ada
        }

        // Filter tiket berdasarkan nama atau lokasi asal/tujuan
        for (Ticket ticket : ticketList) {
            if (ticket.getName().toLowerCase().contains(searchKeyword)
                    || ticket.getFromPlace().toLowerCase().contains(searchKeyword)
                    || ticket.getToPlace().toLowerCase().contains(searchKeyword)) {
                filteredList.add(ticket);
            }
        }

        // Tampilkan data hasil filter ke tabel
        tabel_tickets.setItems(filteredList);

        // Jika tidak ada hasil
        if (filteredList.isEmpty()) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Results Found");
            alert.setHeaderText(null);
            alert.setContentText("No tickets found for the keyword: " + searchKeyword);
            alert.showAndWait();
        }
    }

    public void updateDashboard() {
        // Hitung Total Passenger
        String sqlPassenger = "SELECT COUNT(*) AS total FROM Tickets";
        String sqlTrips = "SELECT COUNT(*) AS total FROM Trips";

        try {
            connect = Database.connectDb();

            // Update Total Passenger
            prepare = connect.prepareStatement(sqlPassenger);
            result = prepare.executeQuery();
            if (result.next()) {
                card_TP.setText(String.valueOf(result.getInt("total")));
            }

            // Update Total Trips
            prepare = connect.prepareStatement(sqlTrips);
            result = prepare.executeQuery();
            if (result.next()) {
                card_TT.setText(String.valueOf(result.getInt("total")));
            }

            // Update Bar Chart for Trips
            updateTripChart();

            // Update Line Chart for Passengers
            updatePassengerChart();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTripChart() {
        String sql = "SELECT place, COUNT(*) AS total FROM Trips GROUP BY place";

        try {
            XYChart.Series<String, Number> tripSeries = new XYChart.Series<>();
            tripSeries.setName("Total Trips");

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                tripSeries.getData().add(new XYChart.Data<>(result.getString("place"), result.getInt("total")));
            }

            bar_TT.getData().clear();
            bar_TT.getData().add(tripSeries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePassengerChart() {
        String sql = "SELECT name, COUNT(*) AS total FROM Tickets GROUP BY name";

        try {
            XYChart.Series<String, Number> passengerSeries = new XYChart.Series<>();
            passengerSeries.setName("Total Passenger");

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                passengerSeries.getData().add(new XYChart.Data<>(result.getString("name"), result.getInt("total")));
            }

            bar_TP.getData().clear();
            bar_TP.getData().add(passengerSeries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. Logout
    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                logout.getScene().getWindow().hide();

                // Redirect to login form
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. Switch Form
    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            tickets_form.setVisible(false);
            trips_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color: #3796a7; -fx-text-fill: #fff; -fx-border-width: 0px;");
            tickets_btn.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-text-fill: #000;");
            trips_btn.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-text-fill: #000;");

        } else if (event.getSource() == tickets_btn) {
            dashboard_form.setVisible(false);
            tickets_form.setVisible(true);
            trips_form.setVisible(false);

            tickets_btn.setStyle("-fx-background-color: #3796a7; -fx-text-fill: #fff; -fx-border-width: 0px;");
            dashboard_btn.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-text-fill: #000;");
            trips_btn.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-text-fill: #000;");

        } else if (event.getSource() == trips_btn) {
            dashboard_form.setVisible(false);
            tickets_form.setVisible(false);
            trips_form.setVisible(true);

            trips_btn.setStyle("-fx-background-color: #3796a7; -fx-text-fill: #fff; -fx-border-width: 0px;");
            dashboard_btn.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-text-fill: #000;");
            tickets_btn.setStyle("-fx-background-color: transparent; -fx-border-width: 1px; -fx-text-fill: #000;");
        }
    }
}
