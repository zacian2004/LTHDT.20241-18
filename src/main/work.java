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
    private Stack<GenericTree> undoStackGT;
    private Stack<GenericTree> redoStackGT;

    private Stack<BinaryTree> undoStackBiT;
    private Stack<BinaryTree> redoStackBiT;
    
    private Stack<BalancedTree> undoStackBaT;
    private Stack<BalancedTree> redoStackBaT;

    private Stack<BalancedBinaryTree> undoStackBBT;
    private Stack<BalancedBinaryTree> redoStackBBT;
 
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
        undoStackGT = new Stack<>();
        redoStackGT = new Stack<>();
        undoStackBiT = new Stack<>();
        redoStackBiT = new Stack<>();
        undoStackBaT = new Stack<>();
        redoStackBaT = new Stack<>();
        undoStackBBT = new Stack<>();
        redoStackBBT = new Stack<>();

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

            if(typeTree == 0 && !GenericT.isFound(childVal)){
                saveStateForUndo();
                treePane.getChildren().clear();
                GenericT.insert(parentVal, childVal);
                visualizeTree(GenericT.getRoot(), 400, 50, 200, 100);
            } else if(typeTree == 1 && !BinaryT.isFound(childVal)){
                saveStateForUndo();
                treePane.getChildren().clear();
                BinaryT.insert(parentVal, childVal);
                visualizeTree(BinaryT.getRoot(), 400, 50, 200, 100);
            } else if(typeTree == 2 && !BalanceT.isFound(childVal)){
                saveStateForUndo();
                treePane.getChildren().clear();
                BalanceT.insert(parentVal, childVal);
                visualizeTree(BalanceT.getRoot(), 400, 50, 200, 100);
            } else if(typeTree == 3 && !BBT.isFound(childVal)){
                saveStateForUndo();
                treePane.getChildren().clear();
                BBT.insert(parentVal, childVal);
                visualizeTree(BBT.getRoot(), 400, 50, 200, 100);
            } else {
                System.out.println("Nut " + childVal + " da ton tai!\n");
            }

        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số nguyên hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Node");
        dialog.setHeaderText("Nhập giá trị nút cần xóa:");
        dialog.setContentText("Value:");
        dialog.showAndWait().ifPresent(deleteStr -> {
            try {
                int value = Integer.parseInt(deleteStr);

                if(typeTree == 0){
                    saveStateForUndo();
                    GenericT.delete(value);
                    checkAndRepaint();
                } else if(typeTree == 1){
                    saveStateForUndo();
                    BinaryT.delete(value);
                    checkAndRepaint();
                } else if(typeTree == 2){
                    saveStateForUndo();
                    BalanceT.delete(value);
                    checkAndRepaint();
                } else if(typeTree == 3){
                    saveStateForUndo();
                    BBT.delete(value);
                    checkAndRepaint();
                }
            } catch (NumberFormatException ex) {
                System.out.println("Lỗi: Giá trị nhập vào không hợp lệ.\n");
            }
        });
    }

    @FXML
    private void handleUpdate() {
        TextInputDialog currentDialog = new TextInputDialog();
        currentDialog.setTitle("Update Node");
        currentDialog.setHeaderText("Nhập giá trị nút hiện tại:");
        currentDialog.setContentText("Current Value:");
        currentDialog.showAndWait().ifPresent(currentStr -> {
            TextInputDialog newDialog = new TextInputDialog();
            newDialog.setTitle("Update Node");
            newDialog.setHeaderText("Nhập giá trị mới:");
            newDialog.setContentText("New Value:");
            newDialog.showAndWait().ifPresent(newStr -> {
                try {
                    int current = Integer.parseInt(currentStr);
                    int newValue = Integer.parseInt(newStr);

                    if(typeTree == 0 && !GenericT.isFound(newValue)){
                        saveStateForUndo();
                        GenericT.update(current, newValue);
                        System.out.println("Đã cập nhật nút " + current + " thành " + newValue + "\n");
                        checkAndRepaint();
                    } else if(typeTree == 1 && !BinaryT.isFound(newValue)){
                        saveStateForUndo();
                        BinaryT.update(current, newValue);
                        System.out.println("Đã cập nhật nút " + current + " thành " + newValue + "\n");
                        checkAndRepaint();
                    } else if(typeTree == 2 && !BalanceT.isFound(newValue)){
                        saveStateForUndo();
                        BalanceT.update(current, newValue);
                        System.out.println("Đã cập nhật nút " + current + " thành " + newValue + "\n");
                        checkAndRepaint();
                    } else if(typeTree == 3 && !BBT.isFound(newValue)){
                        saveStateForUndo();
                        BBT.update(current, newValue);
                        System.out.println("Đã cập nhật nút " + current + " thành " + newValue + "\n");
                        checkAndRepaint();
                    } else {
                        System.out.println("Nut " + newValue + " da ton tai!\n");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Lỗi: Giá trị nhập vào không hợp lệ.\n");
                }
            });
        });
    }

    private void saveStateForUndo() {
        if(typeTree == 0){
            undoStackGT.push(GenericT.copyTree());
            redoStackGT.clear();
        } else if(typeTree == 1) {
            undoStackBiT.push(BinaryT.copyTree());
            redoStackBiT.clear();
        } else if(typeTree == 2) {
            undoStackBaT.push(BalanceT.copyTree());
            redoStackBaT.clear();
        } else if(typeTree == 3) {
            undoStackBBT.push(BBT.copyTree());
            redoStackBBT.clear();
        }
    }

    @FXML
    public void handleUndo() {
        if(typeTree == 0){
            if (!undoStackGT.isEmpty()) {
                redoStackGT.push(GenericT.copyTree());
                GenericT = undoStackGT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể undo thêm nữa.");
            }
        } else if(typeTree == 1) {
            if (!undoStackBiT.isEmpty()) {
                redoStackBiT.push(BinaryT.copyTree());
                BinaryT = undoStackBiT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể undo thêm nữa.");
            }
        } else if(typeTree == 2) {
            if (!undoStackBaT.isEmpty()) {
                redoStackBaT.push(BalanceT.copyTree());
                BalanceT = undoStackBaT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể undo thêm nữa.");
            }
        } else if(typeTree == 3) {
            if (!undoStackBBT.isEmpty()) {
                redoStackBBT.push(BBT.copyTree());
                BBT = undoStackBBT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể undo thêm nữa.");
            }
        }
    }

    @FXML
    public void handleRedo() {
        if(typeTree == 0){
            if (!redoStackGT.isEmpty()) {
                undoStackGT.push(GenericT.copyTree());
                GenericT = redoStackGT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể redo thêm nữa.");
            }
        } else if(typeTree == 1) {
            if (!redoStackBiT.isEmpty()) {
                undoStackBiT.push(BinaryT.copyTree());
                BinaryT = redoStackBiT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể redo thêm nữa.");
            }
        } else if(typeTree == 2) {
            if (!redoStackBaT.isEmpty()) {
                undoStackBaT.push(BalanceT.copyTree());
                BalanceT = redoStackBaT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể redo thêm nữa.");
            }
        } else if(typeTree == 3) {
            if (!redoStackBBT.isEmpty()) {
                undoStackBBT.push(BBT.copyTree());
                BBT = redoStackBBT.pop();
                checkAndRepaint();
            } else {
                System.out.println( "Không thể redo thêm nữa.");
            }
        }
    }

    private void checkAndRepaint() {
        treePane.getChildren().clear();
        if(typeTree == 0){
            if (GenericT != null && GenericT.getRoot() != null) {
                visualizeTree(GenericT.getRoot(), treePane.getWidth() / 2, 50, treePane.getWidth() / 4, 75);
            }
        } else if(typeTree == 1) {
            if (BinaryT != null && BinaryT.getRoot() != null) {
                visualizeTree(BinaryT.getRoot(), treePane.getWidth() / 2, 50, treePane.getWidth() / 4, 75);
            }
        } else if(typeTree == 2) {
            if (BalanceT != null && BalanceT.getRoot() != null) {
                visualizeTree(BalanceT.getRoot(), treePane.getWidth() / 2, 50, treePane.getWidth() / 4, 75);
            }
        } else if(typeTree == 3) {
            if (BBT != null && BBT.getRoot() != null) {
                visualizeTree(BBT.getRoot(), treePane.getWidth() / 2, 50, treePane.getWidth() / 4, 75);
            }
        }
    }
}
