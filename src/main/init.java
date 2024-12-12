package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class init {

    @FXML
    private ChoiceBox<String> TreeType;

    private String[] choices = {"Generic Tree", "Binary Tree", "Balanced Tree", "Binary Balanced Tree"};

    @FXML
    private TextField maxDepthDiff;

    @FXML
    private TextField rootValue;

    @FXML
    private Button Create;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Thêm các giá trị vào ChoiceBox
        TreeType.getItems().addAll(choices);
    }

    // Xử lý sự kiện khi nhấn nút "Create"
    @FXML
    public void handleCreate() {
        try {
            // Lấy giá trị từ các trường nhập liệu
            String selectedTreeType = TreeType.getValue(); // Lấy kiểu cây được chọn
            int depthDiff = Integer.parseInt(maxDepthDiff.getText()); // Chuyển đổi chuỗi thành số
            int rootVal = Integer.parseInt(rootValue.getText()); // Chuyển đổi chuỗi thành số

            // In ra giá trị để kiểm tra
            System.out.println("Tree Type: " + selectedTreeType);
            System.out.println("Max Depth Difference: " + depthDiff);
            System.out.println("Root Value: " + rootVal);

            // Thêm logic để xử lý tạo cây tại đây
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập giá trị hợp lệ cho các trường số!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
