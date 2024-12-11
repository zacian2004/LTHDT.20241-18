package tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Visualize extends JFrame {
    private GenericTree tree;
    private Stack<GenericTree> undoStack;
    private Stack<GenericTree> redoStack;
    private JTextArea outputArea;
    
    private JPanel treePanel;
    private JTextArea pseudoCodeArea;
    private JButton btnUndo, btnRedo, btnPause, btnResume, btnBack, btnInsert, btnDelete, btnUpdate, btnSearch;

    public Visualize() {
        tree = new GenericTree(1); // Gốc là nút 1
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        setTitle("Tree Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel để hiển thị cây
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (tree != null && tree.getRoot() != null) {
                    int width = getWidth();
                    visualizeTree(g, tree.getRoot(), width / 2, 50, width / 4, 75);
                }
            }
        };
        treePanel.setBackground(Color.WHITE);
        treePanel.setPreferredSize(new Dimension(600, 400));
        add(treePanel, BorderLayout.CENTER);

        /*  Khu vực hiển thị mã giả
        pseudoCodeArea = new JTextArea(10, 30);
        pseudoCodeArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(pseudoCodeArea);
        add(scrollPane, BorderLayout.EAST);*/

        // Khu vực hiển thị kết quả
        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        add(outputScrollPane, BorderLayout.SOUTH);

        // Panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel();
        btnInsert = new JButton("Insert");
        btnDelete = new JButton("Delete");
        btnUpdate = new JButton("Update");
        btnSearch = new JButton("Search");
        btnUndo = new JButton("Undo");
        btnRedo = new JButton("Redo");
        btnPause = new JButton("Pause");
        btnResume = new JButton("Resume");
        btnBack = new JButton("Back to Main Menu");

        buttonPanel.add(btnInsert);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUndo);
        buttonPanel.add(btnRedo);
        buttonPanel.add(btnPause);
        buttonPanel.add(btnResume);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

        // Gắn sự kiện cho các nút
        addActionListeners();
    }

    private void addActionListeners() {
        btnInsert.addActionListener(e -> handleInsert());
        btnDelete.addActionListener(e -> handleDelete());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnSearch.addActionListener(e -> handleSearch());
        btnUndo.addActionListener(e -> undo());
        btnRedo.addActionListener(e -> redo());
        btnPause.addActionListener(e -> pause());
        btnResume.addActionListener(e -> resume());
        btnBack.addActionListener(e -> backToMainMenu());
    }

    private void handleInsert() {
        String parentStr = JOptionPane.showInputDialog(this, "Nhập giá trị nút cha:");
        String childStr = JOptionPane.showInputDialog(this, "Nhập giá trị nút con:");
        try {
            int parent = Integer.parseInt(parentStr);
            int child = Integer.parseInt(childStr);
            saveStateForUndo();
            tree.insert(parent, child);
            outputArea.append("Đã thêm nút " + child + " vào nút cha " + parent + "\n");
            checkAndRepaint();
        } catch (NumberFormatException e) {
            outputArea.append("Lỗi: Giá trị nhập vào không hợp lệ.\n");
        }
    }

    private void handleDelete() {
        String deleteStr = JOptionPane.showInputDialog(this, "Nhập giá trị nút cần xóa:");
        try {
            int value = Integer.parseInt(deleteStr);
            saveStateForUndo();
            tree.delete(value);
            outputArea.append("Đã xóa nút " + value + "\n");
            checkAndRepaint();
        } catch (NumberFormatException e) {
            outputArea.append("Lỗi: Giá trị nhập vào không hợp lệ.\n");
        }
    }

    private void handleUpdate() {
        String currentStr = JOptionPane.showInputDialog(this, "Nhập giá trị nút hiện tại:");
        String newStr = JOptionPane.showInputDialog(this, "Nhập giá trị mới:");
        try {
            int current = Integer.parseInt(currentStr);
            int newValue = Integer.parseInt(newStr);
            saveStateForUndo();
            tree.update(current, newValue);
            outputArea.append("Đã cập nhật nút " + current + " thành " + newValue + "\n");
            checkAndRepaint();
        } catch (NumberFormatException e) {
            outputArea.append("Lỗi: Giá trị nhập vào không hợp lệ.\n");
        }
    }

    public void handleSearch(){
        String searchStr = JOptionPane.showInputDialog(this, "Nhập giá trị nút cần tìm:");
        try {
            int value = Integer.parseInt(searchStr);
            boolean found = tree.isFound(value);
            if (!found) {
                JOptionPane.showMessageDialog(this, "Nút " + value + " không tồn tại trong cây.\n");
            } else {
                JOptionPane.showMessageDialog(this, "Nút " + value + " được tìm thấy trong cây.\n");
            }
        } catch (NumberFormatException e) {
            outputArea.append("Lỗi: Giá trị nhập vào không hợp lệ.\n");
        }
    }

    // Các phương thức chức năng
    public void visualizeTree(Graphics g, Node node, int x, int y, int xOffset, int yOffset) {
        if (node == null) return;
        // Vẽ nút
        g.setColor(Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(node.getData()), x - 5, y + 5);
        int childX = x - xOffset;
        for (Node child : node.getChildren()) {
            // Vẽ đường kết nối
            g.setColor(Color.BLACK);
            g.drawLine(x, y, childX, y + yOffset);

            // Gọi đệ quy để vẽ các nút con
            visualizeTree(g, child, childX, y + yOffset, xOffset / 2, yOffset);

            // Dịch chuyển sang phải cho nút con tiếp theo
            childX += xOffset * 2;
        }
    }

    public void updatePseudoCode(String pseudoCode) {
        pseudoCodeArea.setText(pseudoCode);
    }

    public void performOperation() {
        // Thực hiện các thao tác trên cây
    }

    //Lưu trạng thái cây hiện tại vào undoStack
    private void saveStateForUndo() {
        undoStack.push(tree.copyTree());
        redoStack.clear(); // Xóa redoStack sau khi thực hiện thao tác mới
    }
    
    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(tree.copyTree()); // Lưu trạng thái hiện tại vào redoStack
            tree = undoStack.pop(); // Lấy trạng thái trước đó từ undoStack
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể undo thêm nữa.");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(tree.copyTree()); // Lưu trạng thái hiện tại vào undoStack
            tree = redoStack.pop(); // Lấy trạng thái trước đó từ redoStack
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể redo thêm nữa.");
        }
    }

    public void pause() {
        JOptionPane.showMessageDialog(this, "Paused.");
    }

    public void resume() {
        JOptionPane.showMessageDialog(this, "Resumed.");
    }

    public void backToMainMenu() {
        JOptionPane.showMessageDialog(this, "Returning to Main Menu.");
        dispose(); // Đóng cửa sổ hiện tại
    }

    private void checkAndRepaint() {
        int nodeCount = tree.countNodes();
        int optimalWidth = Math.max(800, nodeCount * 50); // Đảm bảo chiều rộng tối thiểu
        int optimalHeight = Math.max(600, nodeCount * 30); // Đảm bảo chiều cao tối thiểu

        // Nếu kích thước hiện tại không đủ, mở rộng kích thước
        if (optimalWidth > treePanel.getPreferredSize().width || optimalHeight > treePanel.getPreferredSize().height) {
            treePanel.setPreferredSize(new Dimension(optimalWidth, optimalHeight));
            treePanel.revalidate();
            outputArea.append("Cập nhật kích thước cây: " + optimalWidth + "x" + optimalHeight + "\n");
        }

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Visualize visualize = new Visualize();
            visualize.setVisible(true);
        });
    }
}
