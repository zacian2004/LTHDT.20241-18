package tree;

public class BalancedBinaryTree extends BalancedTree {
    public BalancedBinaryTree(int rootData, int maxDepthDifference) {
        super(rootData,maxDepthDifference);
    }

    // Override phương thức insert để đảm bảo mỗi nút chỉ có tối đa 2 con
    @Override
    public void insert(int parent, int child) {
        Node parentNode = search(getRoot(), parent);
        if (parentNode == null) {
            System.out.println("Không tìm thấy nút cha cần chèn");
            return;
        }

        // Kiểm tra nếu nút cha đã có 2 con
        if (parentNode.getChildren().size() >= 2) {
            System.out.println("Nút cha đã có đủ 2 con, không thể thêm nữa");
            return;
        }

        Node childNode = new Node(child);
        childNode.setParent(parentNode);
        parentNode.getChildren().add(childNode);
    }
}

