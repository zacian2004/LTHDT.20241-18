
package tree;

import java.util.LinkedList;
import java.util.Queue;

public class GenericTree extends Tree{
    private Node root;

    public GenericTree(int rootData) {
        this.root = new Node(rootData);
    }

    //getter setter 
    public Node getRoot() {
        return root;
    }

    @Override
    public void insert(int parent, int child) {
        Node parentNode = search(root, parent);
        if(parentNode == null) {
            System.out.println("Không tìm thấy nút cha cần chèn");
            return;
        }
        Node chilNode = new Node(child);
        chilNode.setParent(parentNode);
        parentNode.getChildren().add(chilNode);
    }

    @Override
    public Node search(Node root, int value) {
        if(root == null) return null; 

        if(root.getData() == value) return root;

        for(Node child : root.getChildren()) {
            Node found = search(child, value);
            if(found!= null) return found;
        }

        return null;
    }

    @Override
    public void delete(int child) {
        // Nếu root là null, không làm gì cả
        if (root == null) {
            System.out.println("Cây rỗng, không thể xóa");
            return;
        }

        // Nếu root là nút cần xóa
        if (root.getData() == child) {
            root = null;  // Xóa cây (root = null)
            System.out.println("Cây đã bị xóa.");
            return;
        }

        // Nếu root không phải là nút cần xóa, tìm và xóa trong cây con
        Node removeNode = search(root, child);
        if ( removeNode.getParent() != null) {
            removeNode.getParent().getChildren().removeIf(node -> node.getData() == child);
            System.out.println("Nút " + child + " đã bị xóa.");
        } else {
            System.out.println("Không tìm thấy nút cần xóa.");
        }
    }


    @Override
    public void update(int currentV, int newV) {
        Node Node = search(root, currentV);
        if(Node == null) {
            System.out.println("Không tìm thấy nút cần cập nhật");
            return;
        }
        Node.setData(newV);
    }
    @Override
    public void DFS(Node root) {
        if(root == null)
            return;
        System.out.print(root.getData()+ " ");
        for(Node child : root.getChildren()) {
            DFS(child);
        }
    }

    @Override
    public void BFS(Node root) {
        if (root == null) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.getData() + " ");
            queue.addAll(current.getChildren());
        }
    }

}
