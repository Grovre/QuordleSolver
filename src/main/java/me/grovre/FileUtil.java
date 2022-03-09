package me.grovre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {

    private final File f;

    public FileUtil(File f) {
        this.f = f;
    }

    public ArrayList<String> readFileLines() {
        assert this.f != null;
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fr = new FileReader(this.f);
            BufferedReader br = new BufferedReader(fr);
            while(br.ready()) {
                String line = br.readLine();
                if(line.length() == 5) lines.add(line);
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
