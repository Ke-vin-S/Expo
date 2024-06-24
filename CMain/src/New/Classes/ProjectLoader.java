package New.Classes;

import New.Classes.Project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProjectLoader {
    String fileName = "Data.txt";

    public ProjectLoader() {
    }

    public List<String> load() throws IOException{
        try {
            return Files.readAllLines(Paths.get(this.fileName));
        } catch (IOException var3) {
            throw new IOException();
        }

    }

    public List<Project> makeProjectList(LinkedList<LinkedList<String>> list){
        List<Project> out = new LinkedList<>();
        for (List<String> list1: list){
            out.addLast(new Project(list1));
        }
        return out;
    }
    public LinkedList<LinkedList<String>> splitObjects(List<String> lines) {
        LinkedList<LinkedList<String>> objects = new LinkedList<>();
        objects.add(new LinkedList<>());
        int index = 0;
        Iterator<String> var4 = lines.iterator();

        while(var4.hasNext()) {
            String s = var4.next();
            if (s.equals("***")) {
                objects.add(new LinkedList<>());
                ++index;
                continue;
            }
            objects.get(index).add(s);
        }

        return objects;
    }

    public List<String> makeList(List<Project> projects) {
        // make String list
        List<String> list = new ArrayList<>();
        Iterator<Project> var3 = projects.iterator();

        while(var3.hasNext()) {
            Project project = var3.next();
            list.addAll(project.StringList());
            list.add("***");
        }

        if (!list.isEmpty()) {
            list.removeLast();
        }

        return list;
    }

    public void save(List<Project> projects) throws IOException {
        List<String> list = this.makeList(projects);

        try {
            FileWriter fileWriter = new FileWriter(this.fileName);

            try {
                Iterator<String> var5 = list.iterator();

                while(true) {
                    if (!var5.hasNext()) {
                        System.out.println("Successfully wrote to the file.");
                        break;
                    }

                    String line = var5.next();
                    fileWriter.write(line + System.lineSeparator());
                }
            } catch (Throwable var8) {
                try {
                    fileWriter.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }

                throw var8;
            }

            fileWriter.close();
        } catch (IOException var9) {
            System.out.println("An error occurred.");
            var9.printStackTrace();
            throw new IOException("Failed to write to the file", var9);
        }
    }
}
