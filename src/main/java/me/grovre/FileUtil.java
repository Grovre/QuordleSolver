package me.grovre;

import java.io.*;
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

    public void writeMissingElements(ArrayList<String> listBefore, ArrayList<String> listAfter) {
        if(this.f.getAbsolutePath()
                .equalsIgnoreCase
                        ("C:\\Users\\lando\\IdeaProjects\\QuordleSolver\\src\\main\\resources\\words.txt")) {
            return;
        }
        ArrayList<String> missingElements = new ArrayList<>(listBefore);
        missingElements.removeIf(listAfter::contains);

        try {
            BufferedWriter fr = new BufferedWriter(new FileWriter(this.f, false));
            for(String s : missingElements) {
                fr.write(s);
                fr.newLine();
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
