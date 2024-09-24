package assignmnet01;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NonLeafNode extends Node implements Serializable {
    private List<Integer> keys;
    private List<Node> children;

    public NonLeafNode(int size) {
        super(size, false);
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }
    
    public List<Integer> getKeys() { return this.keys; }

    public List<Node> getChildren() { return this.children; }

    public Node insert(int key, int value) {
        int p = 0;
        while (p < keys.size() && keys.get(p) < key) {
            p++;
        }

        Node child = children.get(p);
        // 재귀
        Node newChild = child.insert(key, value);

        // 자식 노드가 분할되어 새로운 부모 노드가 반환된 경우 처리
        if (newChild instanceof NonLeafNode && newChild != child) {
            NonLeafNode newNonLeafNode = (NonLeafNode) newChild;
            keys.add(p, newNonLeafNode.keys.get(0)); // 분할된 첫번째 키 추가
            children.add(p + 1, newNonLeafNode.children.get(1)); // 새 자식노드를 추가

            if (keys.size() > size) {
                return split();
            }
        }

        return this;
    }

    private Node split() {
        int mid = keys.size() / 2;

        NonLeafNode newNonLeaf = new NonLeafNode(this.size);
        newNonLeaf.keys.addAll(keys.subList(mid + 1, keys.size()));
        newNonLeaf.children.addAll(children.subList(mid + 1, children.size()));

        NonLeafNode parent = new NonLeafNode(size);
        parent.getKeys().add(keys.get(mid));

        keys.subList(mid, keys.size()).clear();
        children.subList(mid + 1, children.size()).clear();

        parent.getChildren().add(this);
        parent.getChildren().add(newNonLeaf);
        return parent;
    }

    public Node delete(int key) {
        int p = 0;
        while (p < keys.size() && keys.get(p) < key) {
            p++;
        }

        Node child = children.get(p);
        Node newChild = child.delete(key);

        if (newChild == null) {
            children.remove(p);
            keys.remove(p);
        }

        if (children.size() < (size / 2)) {
            if (p > 0 && children.get(p -1).getKeys().size() > (size / 2)) {
                borrowFromPrev(p);
            } else if (p < children.size() - 1 && children.get(p -1).getKeys().size() > (size / 2)) {
                borrowFromNext(p);
            } else if (p > 0) {
                return mergeWithPrev(p);
            } else if (p < children.size() - 1) {
                return mergeWithNext(p);
            }
        }

        return this;
    }

    private void borrowFromPrev(int p) {
        Node prevNode = children.get(p -1);
        Node curNode = children.get(p);

        int bKey = prevNode.getKeys().remove(prevNode.getKeys().size() - 1);
        int parentKey = keys.get(p -1);
        keys.set(p-1, bKey);

        curNode.getKeys().add(0, parentKey);
        curNode.getChildren().add(0, prevNode.getChildren().remove(prevNode.getChildren().size() - 1));
    }

    private void borrowFromNext(int p) {
        Node nextNode = children.get(p + 1);
        Node curNode = children.get(p);

        int bKey = nextNode.getKeys().remove(0);
        int parentKey = keys.get(p);
        keys.set(p, bKey);

        curNode.getKeys().add(parentKey);
        curNode.getChildren().add(nextNode.getChildren().remove(0));
    }

    private Node mergeWithPrev(int p) {
        Node prevNode = children.get(p -1);
        Node curNode = children.get(p);

        prevNode.getKeys().add(keys.remove(p-1));
        prevNode.getKeys().addAll(curNode.getKeys());
        prevNode.getChildren().addAll(curNode.getChildren());

        children.remove(p);

        return this;
    }

    private Node mergeWithNext(int p) {
        Node nextNode = children.get(p+1);
        Node curNode = children.get(p);

        nextNode.getKeys().add(keys.remove(p));
        nextNode.getKeys().addAll(nextNode.getKeys());
        nextNode.getChildren().addAll(nextNode.getChildren());

        children.remove(p + 1);
        return this;
    }

    public Integer search(int key) {
        List<Integer> searchList = new ArrayList<>();
        for (int i =0; i<keys.size(); i++) {
            searchList.add(keys.get(i));
        }
        for (int i =0; i<searchList.size(); i++) {
            System.out.print(searchList.get(i));
            if (i < searchList.size() -1)
                System.out.print(',');
        }
        System.out.println();

        int p = 0;
        while (p < keys.size() && keys.get(p) <= key) {
            p++;
        }

        return children.get(p).search(key);
    }

    public List<Integer> rangeSearch(int startKey, int endKey) {
        int p = 0;
        while (p < keys.size() && keys.get(p) <= startKey) {
            p++;
        }
        return children.get(p).rangeSearch(startKey, endKey);
    }
}
