package assignmnet01;

import java.io.Serializable;
import java.util.List;

public abstract class Node implements Serializable {
    protected int size;
    protected boolean isLeaf;

    public Node(int size, boolean isLeaf) {
        this.size = size;
        this.isLeaf = isLeaf;
    }

    public abstract List<Integer> getKeys();
    public abstract List<Node> getChildren();
    public abstract Node insert(int key, int value);
    public abstract Node delete(int key);
    public abstract Integer search(int key);
    public abstract List<Integer> rangeSearch(int startKey, int endKey);
}
