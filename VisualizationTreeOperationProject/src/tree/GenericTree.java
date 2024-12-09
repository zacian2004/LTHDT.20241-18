
package tree;

public class GenericTree extends Tree{
    private Node root;

    public GenericTree(int rootData) {
        this.root = new Node(rootData);
    }

    //getter setter 
    public Node getRoot() {
        return root;
    }

    public Node findParent(Node root, int value) {
        if (root == null) return null; // Nếu cây rỗng, trả về null
    
        // Kiểm tra xem root có phải là cha của nút cần tìm không
        for (Node child : root.getChildren()) {
            if (child.getData() == value) {
                return root; // Nếu tìm thấy, trả về nút cha
            }
        }
    
        // Nếu không tìm thấy trong danh sách con, thực hiện đệ quy
        for (Node child : root.getChildren()) {
            Node found = findParent(child, value);
            if (found != null) {
                return found; // Nếu tìm thấy nút cha trong cây con, trả về
            }
        }
    
        return null; // Không tìm thấy nút cha
    }
    
    @Override
    public void insert(int parent, int child) {
        Node parentNode = search(root, parent);
        if(parentNode == null) {
            System.out.println("Không tìm thấy nút cha cần chèn");
            return;
        }
        parentNode.getChildren().add(new Node(child));
    }

    @Override
    public Node search(Node root, int value) {
        if(root == null) return null; //Nếu nốt null thì trả về null

        if(root.getData() == value) return root; // đã tìm thấy Node chính là root

        for(Node child : root.getChildren()) { //Thực hiện vòng lặp tìm từng child
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
        Node parentNode = findParent(root, child);
        if (parentNode != null) {
            // Xóa nút con khỏi cha của nó
            parentNode.getChildren().removeIf(node -> node.getData() == child);
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

}
