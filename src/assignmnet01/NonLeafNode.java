package assignmnet01;

import java.util.ArrayList;
import java.util.List;

public class NonLeafNode extends Node {
    private List<Integer> keys;
    private List<Node> children;

    public NonLeafNode(int size) {
        super(size, false);
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void addKeys(Integer key) {
        this.keys.add(key);
    }

    public void addChildren(Node child) {
        this.children.add(child);
    }
    
    public List<Integer> getKeys() { return this.keys; }

    public List<Node> getChildren() { return this.children; }

    @Override
    public Node insert(int key, int value) {
        int p = 0;
        while (p < keys.size() && keys.get(p) < key) {
            p++;
        }

        Node child = children.get(p);
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

        System.out.println("논리프노드에 삽입: key = " + key + ", value = " + value);
        return this;
    }

    private Node split() {
        int mid = keys.size() / 2;

        NonLeafNode newNonLeaf = new NonLeafNode(this.size);
        newNonLeaf.keys.addAll(keys.subList(mid + 1, keys.size()));
        newNonLeaf.children.addAll(children.subList(mid + 1, children.size()));

        keys.subList(mid, keys.size()).clear();
        children.subList(mid + 1, children.size()).clear();

        NonLeafNode parent = new NonLeafNode(size);
        parent.addKeys(keys.get(mid));
        parent.addChildren(this);
        parent.addChildren(newNonLeaf);

        System.out.println("논리프 노드를 분할 헀습니다.");
        return parent;
    }

    @Override
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

        System.out.println("논리프노드에서 삭제: key = " + key);
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

    @Override
    public Integer search(int key) {
        int p = 0;
        while (p < keys.size() && keys.get(p) < key) {
            p++;
        }
        System.out.println("논리프노드에서 검색: key = " + key);
        return children.get(p).search(key);

    }

    @Override
    public List<Integer> rangeSearch(int startKey, int endKey) {
        System.out.println("논리프노드에서 범위 검색: " + startKey + " ~ " + endKey);
        return new ArrayList<>();
    }
}
