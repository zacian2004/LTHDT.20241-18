package main;

import java.util.Optional;
import java.util.Stack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import tree.*;
import javafx.scene.control.Button;

public class work {
    //Data của cây
    public static int typeTree = 0; // generic = 0, binary = 1, balance = 2, balancebinary = 3;
    public static BalancedBinaryTree BBT;
    public static BalancedTree BalanceT;
    public static GenericTree GenericT;
    public static BinaryTree BinaryT;
    public static int maxDepthDiff;
    private Stack<GenericTree> undoStack;
    private Stack<GenericTree> redoStack;
 
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Slider zoomSlider;

    private Scale scale = new Scale();
    @FXML
    private MenuItem New;

    @FXML
    private MenuItem iniroot;

    @FXML 
    private MenuItem menuButton;

    @FXML
    private Button redoButton;

    @FXML
    private Button undButton;

    @FXML
    private Pane treePane;

    @FXML
    public void initialize() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        // Áp dụng scale cho treePane
        treePane.getTransforms().add(scale);

        // Thiết lập sự kiện khi slider thay đổi
        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Nhân với một hệ số nhỏ hơn để làm chậm quá trình thu phóng
            double zoomFactor = newValue.doubleValue() * 0.01; // 0.1 là hệ số làm chậm (có thể điều chỉnh theo nhu cầu)

            // Đảm bảo zoom không bị quá nhỏ (tránh giá trị âm hoặc quá nhỏ)
            zoomFactor = Math.max(0.01, zoomFactor); // Đảm bảo zoomFactor luôn >= 0.1

            scale.setX(zoomFactor); // Điều chỉnh tỷ lệ theo chiều ngang
            scale.setY(zoomFactor); // Điều chỉnh tỷ lệ theo chiều dọc
        });
    }
    
    @FXML
    public void NewClick(ActionEvent event) throws Exception {
        System.out.println("Work...");
        stage = (Stage) redoButton.getScene().getWindow();

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("init.fxml"));
        Parent root = loader.load();
        stage = new Stage();
        stage.setTitle("Init Node");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void menuClick(ActionEvent event) throws Exception {
        stage = (Stage) redoButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();
        stage.setTitle("Visualize Tree");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handlePlay(ActionEvent event) throws Exception {
        if(typeTree == 0){
            visualizeTree(GenericT.getRoot(), 400, 50, 200, 100);
        } else if(typeTree == 1) {
            visualizeTree(BinaryT.getRoot(), 400, 50, 200, 100);
        } else if(typeTree == 2) {
            visualizeTree(BalanceT.getRoot(), 400, 50, 200, 100);
        } else if(typeTree == 3) {
            visualizeTree(BBT.getRoot(), 400, 50, 200, 100);
        }
    }

    private void visualizeTree(tree.Node node, double x, double y, double xOffset, double yOffset) {
        
        if (node == null) return;
    
        // Vẽ nút hiện tại (hình tròn + giá trị)
        Circle circle = new Circle(x, y, 15, Color.BLACK);
        Text text = new Text(String.valueOf(node.getData()));
        text.setFill(Color.WHITE);
        text.setX(x - 5);
        text.setY(y + 5);
    
        // Thêm các thành phần vào giao diện
        treePane.getChildren().addAll(circle, text);
    
        // Tính toán vị trí các nút con và vẽ chúng
        int totalChildren = node.getChildren().size();
        if (totalChildren == 0) return; // Không có con thì dừng lại
    
        double startX = x - (xOffset * (totalChildren - 1) / 2); // Vị trí bắt đầu cho các nút con
    
        for (int i = 0; i < totalChildren; i++) {
            tree.Node child = node.getChildren().get(i);
            double childX = startX + (i * xOffset); // Tính toán tọa độ x cho từng nút con
            double childY = y + yOffset; // Tọa độ y của nút con
    
            // Vẽ đường nối giữa nút cha và nút con
            Line line = new Line(x, y, childX, childY);
            treePane.getChildren().add(line);
    
            // Đệ quy vẽ các nút con
            visualizeTree(child, childX, childY, xOffset / 1.5, yOffset);
        }
    }

    @FXML
    public void handleInsert(ActionEvent event) {
        try {
            // Tạo cửa sổ nhập liệu
            TextInputDialog parentDialog = new TextInputDialog();
            parentDialog.setTitle("Insert Operation");
            parentDialog.setHeaderText("Insert a new node");
            parentDialog.setContentText("Enter Parent Value:");

            Optional<String> parentResult = parentDialog.showAndWait();
            if (!parentResult.isPresent()) return; // Thoát nếu không nhập

            TextInputDialog childDialog = new TextInputDialog();
            childDialog.setTitle("Insert Operation");
            childDialog.setHeaderText("Insert a new node");
            childDialog.setContentText("Enter Child Value:");

            Optional<String> childResult = childDialog.showAndWait();
            if (!childResult.isPresent()) return;

            // Chuyển đổi giá trị từ chuỗi sang số
            int parentVal = Integer.parseInt(parentResult.get());
            int childVal = Integer.parseInt(childResult.get());

            if(typeTree == 0){
                saveStateForUndo();
                treePane.getChildren().clear();
                GenericT.insert(parentVal, childVal);
                visualizeTree(GenericT.getRoot(), 400, 50, 200, 100);
            } else if(typeTree == 1){
                saveStateForUndo();
                treePane.getChildren().clear();
                BinaryT.insert(parentVal, childVal);
                visualizeTree(BinaryT.getRoot(), 400, 50, 200, 100);
            } else if(typeTree == 2){
                saveStateForUndo();
                treePane.getChildren().clear();
                BalanceT.insert(parentVal, childVal);
                visualizeTree(BalanceT.getRoot(), 400, 50, 200, 100);
            } else if(typeTree == 3){
                saveStateForUndo();
                treePane.getChildren().clear();
                BBT.insert(parentVal, childVal);
                visualizeTree(BBT.getRoot(), 400, 50, 200, 100);
            }

        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số nguyên hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveStateForUndo() {
        undoStack.push(GenericT.copyTree());
        redoStack.clear();
    }

    @FXML
    public void handleUndo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(GenericT.copyTree());
            GenericT = undoStack.pop();
            checkAndRepaint();
        } else {
            System.out.println( "Không thể undo thêm nữa.");
        }
    }

    @FXML
    public void handleRedo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(GenericT.copyTree());
            GenericT = redoStack.pop();
            checkAndRepaint();
        } else {
            System.out.println( "Không thể redo thêm nữa.");
        }
    }

    private void checkAndRepaint() {
        treePane.getChildren().clear();
        if (GenericT != null && GenericT.getRoot() != null) {
            visualizeTree(GenericT.getRoot(), treePane.getWidth() / 2, 50, treePane.getWidth() / 4, 75);
        }
    }
}
