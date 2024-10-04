package assignmnet01;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    public static void createIndexFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete(); // 파일 이미존재시 삭제
        }
        if (file.createNewFile()) { // 새 파일 생성
            System.out.println("인덱스 파일 생성");
        } else {
            System.out.println("인덱스 파일 생성 실패");
        }
    }

    public static List<int[]> readDataFile(String dataFile) throws IOException {
        List<int[]> dataList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        String line;
        // 한 줄씩 읽어서 ","로 구분된 key, value를 int 배열로 변환해 리스트 추가
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int key = Integer.parseInt(parts[0]);
            int value = Integer.parseInt(parts[1]);
            dataList.add(new int[]{key, value});
        }

        reader.close();
        return dataList;
    }

    public static List<Integer> readDeleteFile(String dataFile) throws IOException {
        List<Integer> keyList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        String line;
        // 한 줄씩 읽어서 key값을 int로 변환해 리스트에 추가
        while ((line = reader.readLine()) != null) {
            int key = Integer.parseInt(line.trim());
            keyList.add(key);
        }

        reader.close();
        return keyList;
    }

    public static void saveTree(String file, BPlusTree tree) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(tree);
        }
    }

    public static BPlusTree loadTree(String file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (BPlusTree) in.readObject();
        }
    }
}
