package me.zero.clarinet.util.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.util.ClientUtils;

public class FileUtil {

    public static List<String> read(String file) {
        List<String> data = new ArrayList<String>();
        if (isAlreadyMade(file)) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    try {
                        data.add(line);
                    } catch (Exception e) {
                    }
                }
                bufferedReader.close();
            } catch (IOException ex) {
                ClientUtils.log("Unable to load " + file);
            }
        } else {
            createFile(file);
        }
        return data;
    }

    public static void write(List<String> data, String file) {
        if (!isAlreadyMade(file)) {
            createFile(file);
        }
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String piece : data) {
                bw.write(piece);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            ClientUtils.log("Unable to write to " + file);
        }
    }

    public static void createFile(String file) {
        File theFile = new File(file);
        Path path = Paths.get(file);
        Path parent = Paths.get(theFile.getParent());
        if (!Files.exists(parent)) {
            try {
                Files.createDirectory(parent);
            } catch (IOException e) {
                ClientUtils.log("Unable to create " + theFile.getParent());
            }
        }
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
            ClientUtils.log("Unable to create " + file);
        }
    }

    private static boolean isAlreadyMade(String file) {
        return Files.exists(Paths.get(file));
    }
}
