package assignmnet01;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class BPlusTreeCLI {
    private static BPlusTree bPlusTree;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Input command ('quit' to exit)");
            String[] commandLine = scanner.nextLine().split(" ");
            if (commandLine[0].equals("quit"))
                break;
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
                        int size = Integer.parseInt(commandLine[4]);
                        FileIO.createIndexFile(indexFile);
                        bPlusTree = new BPlusTree(size);
                        FileIO.saveTree(indexFile, bPlusTree);
                        System.out.println("Success to create");
                        break;

                    case "-i":
                        String iDataFile = commandLine[4];

                        if (bPlusTree == null) {
                            System.out.println("인덱스 파일을 먼저 생성해주세요.");
                            break;
                        }
                        bPlusTree = FileIO.loadTree(indexFile);

                        List<int[]> dataList = FileIO.readDataFile(iDataFile);
                        for (int[] pair : dataList) {
                            bPlusTree.insert(pair[0], pair[1]);
                        }
                        FileIO.saveTree(indexFile, bPlusTree);
                        System.out.println("Success to insert");
                        break;

                    case "-d":
                        String dDataFile = commandLine[4];

                        if (bPlusTree == null) {
                            System.out.println("인덱스 파일을 먼저 생성해주세요.");
                            break;
                        }
                        bPlusTree = FileIO.loadTree(indexFile);

                        List<Integer> keyList = FileIO.readDeleteFile(dDataFile);
                        for (int key : keyList) {
                            bPlusTree.delete(key);
                        }
                        FileIO.saveTree(indexFile, bPlusTree);
                        System.out.println("Success to delete");
                        break;

                    case "-s":
                        int searchKey = Integer.parseInt(commandLine[4]);
                        if (bPlusTree == null) {
                            System.out.println("인덱스 파일을 먼저 생성해주세요.");
                            break;
                        }

                        bPlusTree = FileIO.loadTree(indexFile);
                        Integer result = bPlusTree.search(searchKey);
                        break;

                    case "-r":
                        int startKey = Integer.parseInt(commandLine[4]);
                        int endKey = Integer.parseInt(commandLine[5]);
                        if (bPlusTree == null) {
                            System.out.println("인덱스 파일을 먼저 생성해주세요.");
                            break;
                        }
                        bPlusTree = FileIO.loadTree(indexFile);

                        List<Integer> results = bPlusTree.rangeSearch(startKey, endKey);
                        if (results == null)
                            System.out.println("NOT FOUND");
                        break;
                    default:
                        System.out.println("알 수 없는 명령어입니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        scanner.close();
        System.out.println("프로그램을 종료합니다.");
    }
}
