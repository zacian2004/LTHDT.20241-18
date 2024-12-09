package test;

import tree.GenericTree;
import tree.Node;

public class generictreetest {
    public static void main(String[] args) {
        // Create a generic tree
        GenericTree tree = new GenericTree(0);
        tree.insert(0, 1);
        tree.insert(0, 2);
        tree.insert(0, 4);
        tree.update(4, 5);
        tree.update(0,10);
        tree.insert(2,11);
        tree.insert(5,13);
        tree.insert(5,14);
        tree.insert(14,17);
        tree.update(14, 3);
        tree.delete(0);

        tree.printTree(tree.getRoot(), "", true);
    }
}
