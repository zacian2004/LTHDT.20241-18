package tree;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Text;

import tree.GenericTree;
import tree.Node;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class Visualize implements Initializable{
    @FXML
    private Pane treePane;
    @FXML
    private TextArea outputArea;
    @FXML
    private Button btnInsert, btnDelete, btnUpdate, btnUndo, btnRedo, btnPause, btnResume, btnBack;
    
    private GenericTree tree;
    private Stage stage;
    private Stack<GenericTree> undoStack;
    private Stack<GenericTree> redoStack;

    public void setTree(GenericTree tree){
        this.tree = tree;
    }

    public GenericTree getTree(){
        return this.tree;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the GenericTree if it is null
        if (tree == null) {
            tree = new GenericTree(0);
        }
        
        // Clear and set default text in the output area
        outputArea.clear();
        outputArea.appendText("Chương trình đã sẵn sàng.\n");
        
        // Ensure undo and redo stacks are empty
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        
        // Set up the tree pane
        treePane.getChildren().clear();  // Clear any preloaded elements in the Pane
        
        // If there is an existing tree, visualize it
        if (tree.getRoot() != null) {
            checkAndRepaint();
        }
        
        // Add other UI initializations if necessary
        btnInsert.setOnAction(this::handleInsert);
        btnDelete.setOnAction(this::handleDelete);
        btnUpdate.setOnAction(this::handleUpdate);
        btnUndo.setOnAction(this::handleUndo);
        btnRedo.setOnAction(this::handleRedo);
        btnPause.setOnAction(this::handlePause);
        btnResume.setOnAction(this::handleResume);
        btnBack.setOnAction(this::handleBackToMainMenu);
        
        // Log completion of initialization
        outputArea.appendText("Giao diện đã được khởi tạo.\n");
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        TextInputDialog parentDialog = new TextInputDialog();
        parentDialog.setTitle("Insert Node");
        parentDialog.setHeaderText("Nhập giá trị nút cha:");
        parentDialog.setContentText("Parent:");
        parentDialog.showAndWait().ifPresent(parentStr -> {
            TextInputDialog childDialog = new TextInputDialog();
            childDialog.setTitle("Insert Node");
            childDialog.setHeaderText("Nhập giá trị nút con:");
            childDialog.setContentText("Child:");
            childDialog.showAndWait().ifPresent(childStr -> {
                try {
                    int parent = Integer.parseInt(parentStr);
                    int child = Integer.parseInt(childStr);
                    saveStateForUndo();
                    tree.insert(parent, child);
                    outputArea.appendText("Đã thêm nút " + child + " vào nút cha " + parent + "\n");
                    checkAndRepaint();
                } catch (NumberFormatException ex) {
                    outputArea.appendText("Lỗi: Giá trị nhập vào không hợp lệ.\n");
                }
            });
        });
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Node");
        dialog.setHeaderText("Nhập giá trị nút cần xóa:");
        dialog.setContentText("Value:");
        dialog.showAndWait().ifPresent(deleteStr -> {
            try {
                int value = Integer.parseInt(deleteStr);
                saveStateForUndo();
                tree.delete(value);
                outputArea.appendText("Đã xóa nút " + value + "\n");
                checkAndRepaint();
            } catch (NumberFormatException ex) {
                outputArea.appendText("Lỗi: Giá trị nhập vào không hợp lệ.\n");
            }
        });
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
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
                    saveStateForUndo();
                    tree.update(current, newValue);
                    outputArea.appendText("Đã cập nhật nút " + current + " thành " + newValue + "\n");
                    checkAndRepaint();
                } catch (NumberFormatException ex) {
                    outputArea.appendText("Lỗi: Giá trị nhập vào không hợp lệ.\n");
                }
            });
        });
    }

    private void visualizeTree(Node node, double x, double y, double xOffset, double yOffset) {
        if (node == null) return;

        Circle circle = new Circle(x, y, 15, Color.BLACK);
        Text text = new Text(String.valueOf(node.getData()));
        text.setFill(Color.WHITE);
        text.setX(x - 5);
        text.setY(y + 5);

        treePane.getChildren().addAll(circle, text);

        double childX = x - xOffset;
        for (Node child : node.getChildren()) {
            Line line = new Line(x, y, childX, y + yOffset);
            treePane.getChildren().add(line);
            visualizeTree(child, childX, y + yOffset, xOffset / 2, yOffset);
            childX += xOffset * 2;
        }
    }

    private void saveStateForUndo() {
        undoStack.push(tree.copyTree());
        redoStack.clear();
    }

    @FXML
    public void handleUndo(ActionEvent event) {
        if (!undoStack.isEmpty()) {
            redoStack.push(tree.copyTree());
            tree = undoStack.pop();
            checkAndRepaint();
        } else {
            showAlert("Undo", "Không thể undo thêm nữa.");
        }
    }

    @FXML
    public void handleRedo(ActionEvent event) {
        if (!redoStack.isEmpty()) {
            undoStack.push(tree.copyTree());
            tree = redoStack.pop();
            checkAndRepaint();
        } else {
            showAlert("Redo", "Không thể redo thêm nữa.");
        }
    }

    @FXML
    public void handlePause(ActionEvent event) {
        showAlert("Pause", "Paused.");
    }

    @FXML
    public void handleResume(ActionEvent event) {
        showAlert("Resume", "Resumed.");
    }

    @FXML
    public void handleBackToMainMenu(ActionEvent event) {
        showAlert("Main Menu", "Returning to Main Menu.");
        Stage stage = (Stage) treePane.getScene().getWindow();
        stage.close();
    }

    private void checkAndRepaint() {
        treePane.getChildren().clear();
        if (tree != null && tree.getRoot() != null) {
            visualizeTree(tree.getRoot(), treePane.getWidth() / 2, 50, treePane.getWidth() / 4, 75);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
