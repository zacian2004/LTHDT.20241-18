package tree;
public class BinaryTree extends GenericTree{
    //constructor
    public BinaryTree(int value){
        super(value);
    }
    
    public void insert(int parent, int value){
        Node parentNode = search(getRoot(), parent);
        if (parentNode == null) {
            System.out.println("Không tìm thấy nút cha.");
            return;
        }
        
        if(parentNode.getChildren().size() < 2) {
            parentNode.getChildren().add(new Node(value));
        } else {
            System.out.println("Node cha đã đầy, không thể chèn thêm.");
        }
    }
}
