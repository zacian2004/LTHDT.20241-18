package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class menu {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button startButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button quitButton;

    @FXML
    public void startClick(ActionEvent event) throws IOException {
        System.out.println("Work...");
        // Lấy Stage từ nút startButton khi người dùng nhấn nút
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Tải file FXML cho giao diện Data Structure Selection
        root = FXMLLoader.load(getClass().getResource("work.fxml"));
        // Thiết lập giao diện mới
        scene = new Scene(root);
        // Đặt Scene mới vào Stage
        stage.setScene(scene);
        stage.setTitle("Visualize Tree");
        stage.show();
    }

    // Phương thức xử lý nút Help
    @FXML
    public void helpClick(ActionEvent event) throws IOException {
        System.out.println("Loading help...");
        // Lấy Stage từ nút helpButton khi người dùng nhấn nút
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        // Tải file FXML cho giao diện help
        root = FXMLLoader.load(getClass().getResource("help.fxml"));
        // Thiết lập giao diện Help
        scene = new Scene(root);
        // Đặt Scene mới vào Stage
        stage.setScene(scene);
        stage.setTitle("Help");
        stage.show();
    }

    // Phương thức xử lý nút Quit
    @FXML
    public void quitClick(ActionEvent event) {
        System.out.println("Quitting...");
        System.exit(0); // Thoát ứng dụng
    }
}