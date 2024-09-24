package assignmnet01;

import java.util.List;

public class BPlusTree {
    private Node root;
    private int size; //  각 노드의 최대 크기

    public BPlusTree(int size) {
        this.root = new LeafNode(size); // 초기에는 루트가 리프노드
        this.size = size;
    }

    public void insert(int key, int value) {
        Node newRoot = root.insert(key, value);
        if (newRoot != root) { // 루트노드가 분할된 경우, 새로운 루트 설정
            this.root = newRoot;
        }
    }

    public void delete(int key) {
        root = root.delete(key);
    }

    public Integer search(int key) {
        return root.search(key);
    }

    public List<Integer> rangeSearch(int startkey, int endKey) {
        return root.rangeSearch(startkey, endKey);
    }

    public int getSize() {
        return size;
    }
}
