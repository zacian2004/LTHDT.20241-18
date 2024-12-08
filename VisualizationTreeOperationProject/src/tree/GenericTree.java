package tree;

public class GenericTree extends Tree{
    private Node root;

    public GenericTree(int rootData) {
        root = new Node(rootData);
    }

    //getter setter 
    public Node getRoot() {
        return root;
    }
    
}
