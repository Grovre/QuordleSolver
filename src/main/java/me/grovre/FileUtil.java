package me.grovre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class FileUtil {

    private final File f;

    public FileUtil(File f) {
        this.f = f;
    }

    public LinkedList<String> readFileLines() {
        assert this.f != null;
        LinkedList<String> lines = new LinkedList<>();
        try {
            FileReader fr = new FileReader(this.f);
            BufferedReader br = new BufferedReader(fr);
            while(br.ready()) {
                String line = br.readLine();
                if(line.length() == 5) lines.addLast(line);
            }
            if(!br.ready()) {
                br.close();
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
