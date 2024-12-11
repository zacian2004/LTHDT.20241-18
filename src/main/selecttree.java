package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class selecttree {

    @FXML
    private Label labelTreeType; // Nhãn cho loại cây
    @FXML
    private Label labelMaxDifference; // Nhãn cho độ lệch lớn nhất
    @FXML
    private Label labelRootValue; // Nhãn cho giá trị khởi tạo của gốc
    @FXML
    private TextField textFieldMaxDifference; // Trường văn bản cho độ lệch lớn nhất
    @FXML
    private TextField textFieldRootValue; // Trường văn bản cho giá trị khởi tạo của gốc
    @FXML
    private Button buttonNext; // Nút "Tiếp"

    // Phương thức khởi tạo
    @FXML
    public void initialize() {
        // Thiết lập các giá trị mặc định nếu cần
        textFieldMaxDifference.setPromptText("Nhập độ lệch lớn nhất");
        textFieldRootValue.setPromptText("Nhập giá trị khởi tạo");
        
        // Xử lý sự kiện cho nút "Tiếp"
        buttonNext.setOnAction(event -> handleNext());
    }

    // Phương thức xử lý khi nút "Tiếp" được nhấn
    private void handleNext() {
        // Lấy giá trị từ các trường văn bản
        String maxDifference = textFieldMaxDifference.getText();
        String rootValue = textFieldRootValue.getText();

        // Kiểm tra và xử lý giá trị
        if (maxDifference.isEmpty() || rootValue.isEmpty()) {
            System.out.println("Vui lòng nhập đầy đủ thông tin.");
        } else {
            // Xử lý logic tiếp theo, ví dụ: chuyển sang màn hình khác hoặc xử lý dữ liệu
            System.out.println("Độ lệch lớn nhất: " + maxDifference);
            System.out.println("Giá trị khởi tạo của gốc: " + rootValue);
            // Có thể thêm logic chuyển tiếp ở đây
        }
    }
}
