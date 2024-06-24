package New.Classes;

import New.Errors.DuplicateIDError;
import New.Errors.NoSuchProject;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ProjectManager {
    private HashMap<Integer, Project> projects;

    public HashMap<Integer, Project> getProjects() {
        return projects;
    }

    public ProjectManager(List<Project> projects) {
        for (Project project : projects) {
            this.projects.put(project.getID(), project);
        }
    }

    public void addProject(Project project) throws DuplicateIDError {
        // add to hashmap
        if (this.projects.containsKey(project.getID())) {
            throw new DuplicateIDError();
        } else {
            this.projects.put(project.getID(), project);
        }
    }

    public void updateProject(int ID, String name, Category category, String[] members, String description, Country country, String logo) throws NoSuchProject {
        if (!this.projects.containsKey(ID)) {
            throw new NoSuchProject();
        } else {
            Project project = (Project)this.projects.get(ID);
            if (name != null) {
                project.setName(name);
            }

            if (category != null) {
                project.setCategory(category);
            }

            if (members != null) {
                project.setMembers(members);
            }

            if (description != null) {
                project.setDescription(description);
            }

            if (country != null) {
                project.setCountry(country);
            }

            if (logo != null) {
                project.setLogo(new Image(new File(logo).toURI().toString()));
            }

            if (name != null) {
                project.setName(name);
            }

            if (name != null) {
                project.setName(name);
            }

        }
    }

    public void deleteProject(int ID) throws NoSuchProject {
        Project project = (Project)this.projects.remove(ID);
        if (project == null) {
            throw new NoSuchProject();
        }
    }

    public List<Integer> sortedKeysList(){
        Set<Integer> integerSet = this.projects.keySet();
        return mergeSort(new ArrayList<>(integerSet));
    }

    public static boolean onlyDigits(String str, int n) {
        // check whether if a string only contains digits
        for (int i = 0; i < n; i++) {

            if (str.charAt(i) < '0'
                    || str.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> mergeSort(List<Integer> list) {
        if (list.size() <= 1) {
            return list;
        }

        int middle = list.size() / 2;
        List<Integer> left = new ArrayList<>(list.subList(0, middle));
        List<Integer> right = new ArrayList<>(list.subList(middle, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private static List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> mergedList = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex) <= right.get(rightIndex)) {
                mergedList.add(left.get(leftIndex));
                leftIndex++;
            } else {
                mergedList.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        while (leftIndex < left.size()) {
            mergedList.add(left.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex < right.size()) {
            mergedList.add(right.get(rightIndex));
            rightIndex++;
        }

        return mergedList;
    }
}