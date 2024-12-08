package tree;

import interfaces.balanceable.Balanceable;

public class BalancedTree extends GenericTree implements Balanceable{
    private int maxDepthDifference;
    
    public BalancedTree(int rootData, int maxDepthDifference) {
        super(rootData);
        this.maxDepthDifference = maxDepthDifference;
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
