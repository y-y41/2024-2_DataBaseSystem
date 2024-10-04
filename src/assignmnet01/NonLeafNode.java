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

        // 1. 재귀를 통해 자식노드에 삽입
        Node child = children.get(p);
        Node newChild = child.insert(key, value);

        // 2. 자식 노드가 분할되어 새로운 부모 노드가 반환된 경우 처리
        if (newChild instanceof NonLeafNode && newChild != child) {
            NonLeafNode newNonLeafNode = (NonLeafNode) newChild;
            keys.add(p, newNonLeafNode.keys.get(0)); // 분할된 첫 번째 키를 부모 노드에 추가
            children.add(p + 1, newNonLeafNode.children.get(1)); // 새 자식노드를 추가

            // 3. 노드가 가득 찼다면 분할
            if (keys.size() > size) {
                return split();
            }
        }

        return this;
    }

    private Node split() {
        // 1. 중간 값 계산
        int mid = keys.size() / 2;

        // 2. 새로운 논리프 노드 생성 후 절반의 키와 값 이동
        NonLeafNode newNonLeaf = new NonLeafNode(this.size);
        newNonLeaf.keys.addAll(keys.subList(mid + 1, keys.size()));
        newNonLeaf.children.addAll(children.subList(mid + 1, children.size()));

        // 3. 새로운 부모 노드 생성
        NonLeafNode parent = new NonLeafNode(size);
        parent.getKeys().add(keys.get(mid));

        // 4. 기존 노드에서 이동된 키와 값 제거
        keys.subList(mid, keys.size()).clear();
        children.subList(mid + 1, children.size()).clear();

        // 5. 부모 노드에 자식으로 추가
        parent.getChildren().add(this);
        parent.getChildren().add(newNonLeaf);
        return parent;
    }

    public Node delete(int key) {
        int p = 0;
        while (p < keys.size() && keys.get(p) <= key) {
            p++;
        }

        // 1. 재귀를 통해 해당 키값이 있는 자식노드 위치 찾기
        Node child = children.get(p);
        Node newChild = child.delete(key);

        // 2. 자식노드가 null이라면 자식 노드와 키 제거
        if (newChild == null) {
            children.remove(p);
            keys.remove(p);
        }

        // 3. 언더플로우
        if (children.size() < (size / 2)) {
            if (p > 0 && children.get(p -1).getKeys().size() > (size / 2)) {
                // 왼쪽 형제에서 차용
                borrowFromPrev(p);
            } else if (p < children.size() - 1 && children.get(p -1).getKeys().size() > (size / 2)) {
                // 오른쪽 형제에서 차용
                borrowFromNext(p);
            } else if (p > 0) {
                // 왼쪽 형제와 병합
                return mergeWithPrev(p);
            } else if (p < children.size() - 1) {
                // 오른쪽 형제와 병합
                return mergeWithNext(p);
            }
        }

        return this;
    }

    private void borrowFromPrev(int p) {
        Node prevNode = children.get(p -1);
        Node curNode = children.get(p);

        // 왼쪽 노드의 마지막 키 차용
        int bKey = prevNode.getKeys().remove(prevNode.getKeys().size() - 1);
        // 부모 키를 가져오고 차용한 키로 대체
        int parentKey = keys.get(p -1);
        keys.set(p-1, bKey);

        // 현재 노드에 부모 키 추가 후 자식 노드 이동
        curNode.getKeys().add(0, parentKey);
        curNode.getChildren().add(0, prevNode.getChildren().remove(prevNode.getChildren().size() - 1));
    }

    private void borrowFromNext(int p) {
        Node nextNode = children.get(p + 1);
        Node curNode = children.get(p);

        // 오른쪽 노드의 첫 번째 키 차용
        int bKey = nextNode.getKeys().remove(0);
        int parentKey = keys.get(p);
        keys.set(p, bKey);

        curNode.getKeys().add(parentKey);
        curNode.getChildren().add(nextNode.getChildren().remove(0));
    }

    private Node mergeWithPrev(int p) {
        Node prevNode = children.get(p -1);
        Node curNode = children.get(p);

        // 왼쪽 노드에 부모 키와 현재 노드 키, 자식 노드 추가
        prevNode.getKeys().add(keys.remove(p-1));
        prevNode.getKeys().addAll(curNode.getKeys());
        prevNode.getChildren().addAll(curNode.getChildren());

        // 현재 노드를 자식에서 제거
        children.remove(p);

        return this;
    }

    private Node mergeWithNext(int p) {
        Node nextNode = children.get(p+1);
        Node curNode = children.get(p);

        // 오른쪽 노드에 부모 키와 현재 노드 키, 자식 노드 추가
        nextNode.getKeys().add(keys.remove(p));
        nextNode.getKeys().addAll(nextNode.getKeys());
        nextNode.getChildren().addAll(nextNode.getChildren());

        children.remove(p + 1);
        return this;
    }

    public Integer search(int key) {
        List<Integer> searchList = new ArrayList<>();

        // 1. 현재 노드의 모든 키를 리스트에 추가
        for (int i =0; i<keys.size(); i++) {
            searchList.add(keys.get(i));
        }

        // 2. 현재 노드의 키를 ","로 연결해 출력
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

        // 3. 재귀를 통해 해당 키가 있는 자식노드 검색
        return children.get(p).search(key);
    }

    public List<Integer> rangeSearch(int startKey, int endKey) {
        int p = 0;
        while (p < keys.size() && keys.get(p) <= startKey) {
            p++;
        }

        // 재귀를 통해 시작할 자식 노드 찾기
        return children.get(p).rangeSearch(startKey, endKey);
    }
}
