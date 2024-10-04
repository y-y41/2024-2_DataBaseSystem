package assignmnet01;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeafNode extends Node implements Serializable {
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

    public List<Integer> getKeys() {
        return this.keys;
    }

    public List<Node> getChildren() {
        return null;
    }

    public Node insert(int key, int value) {
        int p = 0;
        while (p < keys.size() && keys.get(p) < key) {
            p++;
        }

        // 1. 키와 값을 리스트에 삽입 (정렬된 상태 유지)
        keys.add(p, key);
        values.add(p, value);

        // 2. 노드가 가득 찼다면 분할
        if (keys.size() > size) {
            return split();
        }
        return this;
    }

    private Node split() {
        // 1. 중간값 계산
        int mid = keys.size() / 2;

        // 2. 새로운 리프 노드 생성 후 절반의 키와 값 이동
        LeafNode newLeaf = new LeafNode(this.size);
        newLeaf.keys.addAll(keys.subList(mid, keys.size()));
        newLeaf.values.addAll(values.subList(mid, values.size()));

        // 3. 기존 노드에서 이동된 키와 값 제거
        keys.subList(mid, keys.size()).clear();
        values.subList(mid, values.size()).clear();

        // 4. 새로운 리프 노드를 현재 노드의 next로 설정
        newLeaf.next = this.next;
        if (this.next != null) {
            this.next.prev = newLeaf;
        }
        this.next = newLeaf;
        newLeaf.prev = this;

        // 5. 세로운 부모 노드를 반환 (부모 노드에 첫 번째 키를 올림)
        NonLeafNode parent = new NonLeafNode(size);
        parent.getKeys().add(newLeaf.keys.get(0)); // 새로운 리프 노드의 첫 번째 키를 부모에 올림
        parent.getChildren().add(this); // 현재 노드를 부모의 첫 번째 자식으로 추가
        parent.getChildren().add(newLeaf); // 새로운 리프 노드를 부모의 두 번째 자식으로 추가

        return parent; // 부모노드반환
    }

    public Node delete(int key) {
        // 1. 키가 있는지 확인
        int p = keys.indexOf(key);
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
                // 오른쪽 형제에서 차용
                borrowFromNext();
            } else if (this.next != null) {
                // 오른쪽 형제노드의 사이즈가 최소보다 작다면 병합
                return mergeWithNext();
            } else if (this.prev != null && this.prev.keys.size() > (size / 2)) {
                // 오른쪽 형제노드가 없다면 왼쪽 형제에서 차용
                borrowFromPrev();
            } else if (this.prev != null) {
                // 왼쪽 형제노드의 사이즈가 최소보다 작다면 병합
                return mergeWithPrev();
            }
        }

        return this;
    }

    private void borrowFromNext() {
        System.out.println("borrowFromNext 호출됨");
        System.out.println("현재 노드 키: " + keys);
        System.out.println("오른쪽 형제 노드 키: " + next.keys);
        // 오른쪽 노드의 첫 번째 키와 값 차용
        int borrowdKey = next.keys.remove(0);
        int borrowedValue = next.values.remove(0);

        this.keys.add(borrowdKey);
        this.values.add(borrowedValue);
        System.out.println("현재 노드 키: " + keys);
        System.out.println("오른쪽 형제 노드 키: " + next.keys);

    }

    private Node mergeWithNext() {
        this.keys.addAll(next.keys);
        this.values.addAll(next.values);

        // 현재 노드의 next를 오른쪽 노드의 next로, 오른쪽 노드의 prev를 현재노드로 설정
        this.next = next.next;
        if (this.next != null) {
            this.next.prev = this;
        }
        return this;
    }

    private void borrowFromPrev() {
        // 왼쪽 노드의 마지막 키와 값 차용
        int bKey = prev.keys.remove(prev.keys.size() - 1);
        int bValue = prev.values.remove(prev.values.size() - 1);

        this.keys.add(0, bKey);
        this.values.add(0, bValue);
    }

    private Node mergeWithPrev() {
        prev.keys.addAll(this.keys);
        prev.values.addAll(this.values);

        // 왼쪽 노드의 next를 현재 노드의 next로, 오른쪽 노드의 prev를 왼쪽 노드로 설정
        prev.next = this.next;
        if (this.next != null) {
            this.next.prev = prev;
        }
        return this;
    }

    public Integer search(int key) {
        int p = keys.indexOf(key);
        if (p != -1) {
            System.out.println(values.get(p)); // 일치하는 키 찾으면 value 출력
            return values.get(p);
        } else {
            System.out.println("NOT FOUND"); // 못찾으면 "NOT FOUND" 출력
            return null;
        }
    }

    public List<Integer> rangeSearch(int startKey, int endKey) {
        List<Integer> results = new ArrayList<>(); // rangeSearch를 성공했는지 확인하기 위해 선언
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) >= startKey && keys.get(i) <= endKey) {
                results.add(keys.get(i));
                // "key,value" 의 형태로 출력
                System.out.print(keys.get(i) + "," + values.get(i));
                System.out.println();
            }
        }

        // next로 넘어가 rangeSearch 지속
        if (next != null && keys.get(keys.size() - 1) <= endKey) {
            results.addAll(next.rangeSearch(startKey, endKey));
        }

        return results;
    }
}
