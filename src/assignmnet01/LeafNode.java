package assignmnet01;

import java.util.ArrayList;
import java.util.List;

public class LeafNode extends Node {
    private List<Integer> keys;
    private List<Integer> values;
    private LeafNode next;
    private LeafNode prev;

    public LeafNode(int size) {
        super(size, true);
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
        this.next = null;
        this.prev = null;
    }

    public List<Integer> getKeys() { return this.keys; }

//    @Override
    public List<Node> getChildren() {
        return null;
    }

//    @Override
    public Node insert(int key, int value) {
        // 1. 중복키 확인 -> 없어도 됨
        int p = 0;
        while (p < keys.size() && keys.get(p) < key) {
            p++;
        }

        if (p < keys.size() && keys.get(p) == key) {
            System.out.println("중복키는 삽입할 수 없습니다");
            return this;
        }

        // 2. 키와 값을 리스트에 삽입 (정렬된 상태 유지)
        keys.add(p, key);
        values.add(p, value);

        // 3. 노드가 가득 찼다면 분할
        if (keys.size() > size) {
            return split();
        }

        System.out.println("리프노드에 삽입: key = " + key + ", value = " + value);
        return this; // 분할이 필요하지 않으면 현재 노드를 반환
    }

    private Node split() {
        int mid = keys.size() / 2;

        // 새로운 리프 노드 생성 후 절반의 키와 값 이동
        LeafNode newLeaf = new LeafNode(this.size);
        newLeaf.keys.addAll(keys.subList(mid, keys.size()));
        newLeaf.values.addAll(values.subList(mid, values.size()));

        // 기존 노드에서 이동된 키와 값 제거
        keys.subList(mid, keys.size()).clear();
        values.subList(mid, values.size()).clear();

        // 새로운 리프 노드를 현재 노드의 next로 설정
        newLeaf.next = this.next;
        if (this.next != null) {
            this.next.prev = newLeaf;
        }
        this.next = newLeaf;
        newLeaf.prev = this;

        // 세로운 부모 노드를 반환 (부모 노드에서 첫 번째 키를 받음)
        NonLeafNode parent = new NonLeafNode(size);
        parent.addKeys(newLeaf.keys.get(0));
        parent.addChildren(this);
        parent.addChildren(newLeaf);

        System.out.println("리프노드 분할했습니다. 새로운 리프노드를 생성했습니다.");
        return parent; // 부모노드반환
    }

    @Override
    public Node delete(int key) {
        int p = keys.indexOf(key);

        // 1. 키가 있는지 확인
        if (p == -1) {
            System.out.println("키를 찾을 수 없습니다.");
            return this;
        }

        // 2. 키와 값을 리스트에서 제거
        keys.remove(p);
        values.remove(p);

        // 3. 언더플로우
        if (keys.size() < (size / 2)) {
            if (this.next != null && this.next.keys.size() > (size / 2)) {
                // 오른쪽 형제 노드로부터 빌려옴
                borrowFromNext();
            } else if (this.next != null) {
                // 오른쪽 형제노드의 사이즈가 최소보다 작다면 병합
                return mergeWithNext();
            } else if (this.prev != null && this.prev.keys.size() > (size / 2)) {
                // 오른쪽 형제노드가 없다면 왼쪽 형제노드로부터 빌려옴
                borrowFromPrev();
            } else if (this.prev != null) {
                // 왼쪽 형제노드의 사이즈가 최소보다 작다면 병합
                return mergeWithPrev();
            }
        }

        System.out.println("리프노드에서 삭제: key = " + key);
        return this;
    }

    private void borrowFromNext() {
        int borrowdKey = next.keys.remove(0);
        int borrowedValue = next.values.remove(0);

        this.keys.add(borrowdKey);
        this.values.add(borrowedValue);
    }

    private Node mergeWithNext() {
        this.keys.addAll(next.keys);
        this.values.addAll(next.values);
        this.next = next.next;
        if (this.next != null) {
            this.next.prev = this;
        }

        return this;
    }

    private void borrowFromPrev() {
        int bKey = prev.keys.remove(prev.keys.size() - 1);
        int bValue = prev.values.remove(prev.values.size() - 1);

        this.keys.add(0, bKey);
        this.values.add(0, bValue);
    }

    private Node mergeWithPrev() {
        prev.keys.addAll(this.keys);
        prev.values.addAll(this.values);
        prev.next = this.next;
        if (this.next != null) {
            this.next.prev = prev;
        }

        return this;
    }

//    @Override
    public Integer search(int key) {
        int p = keys.indexOf(key);
        if (p != -1) {
            return values.get(p);
        }
        System.out.println("리프노드에서 검색: key = " + key);
        return null;
    }

//    @Override
    public List<Integer> rangeSearch(int startKey, int endKey) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) >= startKey && keys.get(i) <= endKey) {
                result.add(values.get(i));
            }
        }
        System.out.println("리프노드에서 범위 검색: " + startKey + " ~ " + endKey);
        return result;
    }
}
