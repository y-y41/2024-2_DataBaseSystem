package assignmnet01;

import java.util.List;
import java.util.Scanner;

public class BPlusTreeCLI {
    private static BPlusTree bPlusTree; // BPlusTree 객체를 static으로 관리

    public static void main(String[] args) {
        String command = args[0]; // 첫 번째 인자는 함수 실행 명령어
        String indexFile = args[1]; // 두 번째 인자는 인덱스 파일의 경로
        Scanner scanner = new Scanner(System.in);
        try { // FileIO 등 예외발생 처리
            switch (command) {
                case "-c": // Creation
                    int size = Integer.parseInt(args[2]); // 세 번째 인자는 B+Tree의 최대 크기

                    FileIO.createIndexFile(indexFile);
                    bPlusTree = new BPlusTree(size);

                    FileIO.saveTree(indexFile, bPlusTree);
                    System.out.println("Success to create");
                    break;

                case "-i": // Insertion
                    String iDataFile = args[2]; // 세 번쨰 인자는 입력 데이터 파일의 경로

                    bPlusTree = FileIO.loadTree(indexFile);
                    if (bPlusTree == null) {
                        System.out.println("인덱스 파일을 먼저 생성해주세요.");
                        break;
                    }

                    List<int[]> dataList = FileIO.readDataFile(iDataFile);
                    for (int[] pair : dataList) {
                        bPlusTree.insert(pair[0], pair[1]);
                    }

                    FileIO.saveTree(indexFile, bPlusTree);
                    System.out.println("Success to insert");
                    break;

                case "-d": // Deletion
                    String dDataFile = args[2]; // 세 번째 인자는 삭제 키 파일의 경로

                    bPlusTree = FileIO.loadTree(indexFile);
                    if (bPlusTree == null) {
                        System.out.println("인덱스 파일을 먼저 생성해주세요.");
                        break;
                    }

                    List<Integer> keyList = FileIO.readDeleteFile(dDataFile);
                    for (int key : keyList) {
                        bPlusTree.delete(key);
                    }

                    FileIO.saveTree(indexFile, bPlusTree);
                    System.out.println("Success to delete");
                    break;

                case "-s": // Single Key Search
                    int searchKey = Integer.parseInt(args[2]); // 세 번째 인자는 검색할 키의 값

                    bPlusTree = FileIO.loadTree(indexFile);
                    if (bPlusTree == null) {
                        System.out.println("인덱스 파일을 먼저 생성해주세요.");
                        break;
                    }

                    Integer result = bPlusTree.search(searchKey);
                    if (result != null)
                        System.out.println("Success to search");
                    break;

                case "-r": // Ranged Search
                    int startKey = Integer.parseInt(args[2]); // 세 번째 인자는 범위 검색을 시작할 키의 값
                    int endKey = Integer.parseInt(args[3]); // 네 번째 인자는 범위 검색을 끝낼 키의 값

                    bPlusTree = FileIO.loadTree(indexFile);
                    if (bPlusTree == null) {
                        System.out.println("인덱스 파일을 먼저 생성해주세요.");
                        break;
                    }

                    List<Integer> results = bPlusTree.rangeSearch(startKey, endKey);
                    if (results.isEmpty())
                        // 범위 검색의 결과가 비어있으면 "NOT FOUND" 출력
                        System.out.println("NOT FOUND");
                    else
                        System.out.println("Success to range search");
                    break;

                default:
                    System.out.println("알 수 없는 명령어입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.close();
        System.out.println("프로그램을 종료합니다.");
    }
}
