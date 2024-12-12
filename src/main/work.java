package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class work {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private MenuItem New;

    @FXML
    private MenuItem iniroot;
    
    @FXML
    public void NewClick(ActionEvent event) throws Exception {
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

    @FXML
    public void inirootClick(ActionEvent event) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insert.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Insert Node");
            stage.setScene(new Scene(root));
            stage.show();
    }

}
