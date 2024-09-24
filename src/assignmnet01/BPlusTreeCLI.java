package assignmnet01;

import java.util.List;
import java.util.Scanner;

public class BPlusTreeCLI {
    private static BPlusTree bPlusTree;

    public static void main(String[] args) {
//        if (args.length < 2) {
//            System.out.println("명령어 형식이 잘못되었습니다.");
//            return;
//        }
//
//        String command = args[0];
//        String indexFile = args[1];
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Input command");
            String[] commandLine = scanner.nextLine().split(" ");
            if (commandLine.length < 5) {
                System.out.println("모든 명령어를 입력하세요");
                continue;
            }

            String language = commandLine[0];
            if (!language.equals("java")) {
                System.out.println("언어를 java로 입력해주세요");
                continue;
            }
            String program = commandLine[1];
            if (!program.equals("bptree")) {
                System.out.println("이 프로그램은 bptree입니다. 알맞은 프로그램 이름을 입력해주세요");
                continue;
            }

            String command = commandLine[2];
            String indexFile = commandLine[3];

            try {
                switch (command) {
                    case "-c":
//                        if (args.length != 3){
//                            System.out.println("노드 크기를 지정해야 합니다.");
//                            return;
//                        }
//                        int size = Integer.parseInt(args[2]);
                        int size = Integer.parseInt(commandLine[4]);
                        FileHandler.createIndexFile(indexFile);
//                    BPlusTree tree = new BPlusTree(size);
                        bPlusTree = new BPlusTree(size);
                        break;

                    case "-i":
//                        if (args.length != 3) {
//                            System.out.println("삽입할 데이터 파일을 지정해야 합니다.");
//                            return;
//                        }
//                        String dataFileForInsert = args[2];
                        String dataFileForInsert = commandLine[4];
                        if (bPlusTree == null) {
                            System.out.println("인덱스파일을 먼저 생성하십시오.");
                            return;
                        }

                        List<int[]> dataList = FileHandler.readDataFile(dataFileForInsert);
                        for (int[] pair : dataList) {
                            bPlusTree.insert(pair[0], pair[1]);
                        }
                        break;

                    case "-d":
//                        if (args.length != 3) {
//                            System.out.println("삭제할 데이터 파일을 지정해야 합니다.");
//                            return;
//                        }
//                        String dataFileForDelete = args[2];
                        String dataFileForDelete = commandLine[4];
                        if (bPlusTree == null) {
                            System.out.println("인덱스파일을 먼저 생성하십시오.");
                            return;
                        }

                        List<Integer> keyList = FileHandler.readDeleteFile(dataFileForDelete);
                        for (int key : keyList) {
                            bPlusTree.delete(key);
                        }
                        break;

                    case "-s":
//                        if (args.length != 3) {
//                            System.out.println("검색할 키 값을 지정해야 합니다.");
//                            return;
//                        }
//                        int searchKey = Integer.parseInt(args[2]);
                        int searchKey = Integer.parseInt(commandLine[4]);
                        if (bPlusTree == null) {
                            System.out.println("인덱스파일을 먼저 생성하십시오.");
                            return;
                        }

                        Integer result = bPlusTree.search(searchKey);
                        break;

                    case "-r":
//                        if (args.length != 4) {
//                            System.out.println("범위 검섹을 위한 시작 키와 종료 키를 지정해야 합니다.");
//                            return;
//                        }
//                        int startKey = Integer.parseInt(args[2]);
//                        int endKey = Integer.parseInt(args[3]);
                        int startKey = Integer.parseInt(commandLine[4]);
                        int endKey = Integer.parseInt(commandLine[5]);
                        if (bPlusTree == null) {
                            System.out.println("인덱스파일을 먼저 생성하십시오.");
                            return;
                        }

                        List<Integer> results = bPlusTree.rangeSearch(startKey, endKey);
                        results.forEach(System.out::println);
                        break;

                    default:
                        System.out.println("알 수 없는 명령어입니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        try {
//            switch (command) {
//                case "-c":
//                    if (args.length != 3){
//                        System.out.println("노드 크기를 지정해야 합니다.");
//                        return;
//                    }
//                    int size = Integer.parseInt(args[2]);
//                    FileHandler.createIndexFile(indexFile);
////                    BPlusTree tree = new BPlusTree(size);
//                    bPlusTree = new BPlusTree(size);
//                    break;
//
//                case "-i":
//                    if (args.length != 3) {
//                        System.out.println("삽입할 데이터 파일을 지정해야 합니다.");
//                        return;
//                    }
//                    String dataFileForInsert = args[2];
//                    if (bPlusTree == null) {
//                        System.out.println("인덱스파일을 먼저 생성하십시오.");
//                        return;
//                    }
//
//                    List<int[]> dataList = FileHandler.readDataFile(dataFileForInsert);
//                    for (int[] pair : dataList) {
//                        bPlusTree.insert(pair[0], pair[1]);
//                    }
//                    break;
//
//                case "-d":
//                    if (args.length != 3) {
//                        System.out.println("삭제할 데이터 파일을 지정해야 합니다.");
//                        return;
//                    }
//                    String dataFileForDelete = args[2];
//                    if (bPlusTree == null) {
//                        System.out.println("인덱스파일을 먼저 생성하십시오.");
//                        return;
//                    }
//
//                    List<Integer> keyList = FileHandler.readDeleteFile(dataFileForDelete);
//                    for (int key : keyList) {
//                        bPlusTree.delete(key);
//                    }
//                    break;
//
//                case "-s":
//                    if (args.length != 3) {
//                        System.out.println("검색할 키 값을 지정해야 합니다.");
//                        return;
//                    }
//                    int searchKey = Integer.parseInt(args[2]);
//                    if (bPlusTree == null) {
//                        System.out.println("인덱스파일을 먼저 생성하십시오.");
//                        return;
//                    }
//
//                    Integer result = bPlusTree.search(searchKey);
//                    break;
//
//                case "-r":
//                    if (args.length != 4) {
//                        System.out.println("범위 검섹을 위한 시작 키와 종료 키를 지정해야 합니다.");
//                        return;
//                    }
//                    int startKey = Integer.parseInt(args[2]);
//                    int endKey = Integer.parseInt(args[3]);
//                    if (bPlusTree == null) {
//                        System.out.println("인덱스파일을 먼저 생성하십시오.");
//                        return;
//                    }
//
//                    List<Integer> results = bPlusTree.rangeSearch(startKey, endKey);
//                    results.forEach(System.out::println);
//                    break;
//
//                default:
//                    System.out.println("알 수 없는 명령어입니다.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
