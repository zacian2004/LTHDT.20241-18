package tree;

import interfaces.balanceable.Balanceable;

public class BalancedBinaryTree extends BinaryTree implements Balanceable {
    private int  maxDepthDifference;

    public BalancedBinaryTree(int value) {
        super(value);
    }

    // Implement methods to maintain balance of the tree
    @Override
    public boolean isBalanced () {
        // Phương thức kiểm tra cân b��ng cây
        return true;
    }

    @Override
    public void balance() {
        // Phương thức đảo đật và cân b��ng cây
        // (Nếu cây đang bị lệch)
    }
}
