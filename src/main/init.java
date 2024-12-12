package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class init implements Initializable{

    @FXML
    private ChoiceBox<String> TreeType;

    private String[] choices = {"Generic Tree", "Binary Tree", "Balanced Tree", "Binary Balanced Tree"};

    @FXML
    private TextField maxDepthDiff;

    @FXML
    private TextField rootValue;

    @FXML
    private Button Create;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
            TreeType.getItems().addAll(choices);
            TreeType.setValue("Gereric Tree");
    }

    // Xử lý sự kiện khi nhấn nút "Create"
    @FXML
    public void handleCreate(ActionEvent event) {
        try {
            // Lấy giá trị từ các trường nhập liệu
            String selectedTreeType = TreeType.getValue(); // Lấy kiểu cây được chọn
            int rootVal = Integer.parseInt(rootValue.getText()); // Chuyển đổi chuỗi thành số

             // Kiểm tra điều kiện đầu vào
            if (selectedTreeType == null || rootValue.getText().isEmpty()) {
                System.out.println("Vui lòng nhập đầy đủ thông tin cơ bản (Tree Type và Root Value)!");
                return;
            }

            int depthDiff = Integer.parseInt(maxDepthDiff.getText()); // Chuyển đổi chuỗi thành số
            // Nếu loại cây là Balanced Tree hoặc Binary Balanced Tree thì kiểm tra thêm depthDiff
            if ((selectedTreeType.equals("Balanced Tree") || selectedTreeType.equals("Binary Balanced Tree")) 
                    && (maxDepthDiff.getText().isEmpty())) {
                System.out.println("Vui lòng nhập giá trị Max Depth Difference cho loại cây Balanced!");
                return;
            }
            // In ra giá trị để kiểm tra
            System.out.println("Tree Type: " + selectedTreeType);
            System.out.println("Max Depth Difference: " + depthDiff);
            System.out.println("Root Value: " + rootVal);

            Stage stage = (Stage) Create.getScene().getWindow();
            stage.close();

            // Thêm logic để xử lý tạo cây tại đây
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập giá trị hợp lệ cho các trường số!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
