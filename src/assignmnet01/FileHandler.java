package assignmnet01;

//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static List<int[]> readDataFile(String dataFile) throws IOException {
        List<int[]> dataList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(dataFile));

        String line;
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
        while ((line = reader.readLine()) != null) {
            int key = Integer.parseInt(line.trim());
            keyList.add(key);
        }

        reader.close();
        return keyList;
    }

    public static void writeResultsToFile(String resultFile, List<Integer> results) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile));

        for (int result : results) {
            writer.write(result + "\n");
        }

        writer.close();

    }
    public static void createIndexFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete(); // 파일 이미존재시 삭제
        }
        if (file.createNewFile()) {
            System.out.println("인덱스 파일 생성");
        } else {
            System.out.println("인덱스 파일 생성 실패");
        }
    }
//
//    public static void insertDataToFile(String fileName, int key, int value) throws IOException {
//        FileWriter writer = new FileWriter(fileName, true);
//        writer.write(key + "," + writer + "\n");
//        writer.close();
//        System.out.println("데이터 삽임: key + " + key + ", value = " + value);
//    }
}
