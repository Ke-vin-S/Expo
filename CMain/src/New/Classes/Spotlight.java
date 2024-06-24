package New.Classes;

import New.Classes.Category;
import New.Classes.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Spotlight {
    private final HashMap<Category, List<Project>> map = new HashMap<>();

    {
        for (Category category: Category.values()){
            map.put(category, new ArrayList<>());
        }
    }
    public Spotlight(List<Project> projects){
        for (Project project : projects){
            map.get(project.getCategory()).addLast(project);
        }
    }

    public List<Project> getProjects(){
        Random random = new Random();
        List<Project> list = new ArrayList<Project>();
        for (Category category: Category.values()){
            List<Project> projects = map.get(category);
            if (projects.isEmpty()) continue;
            int index = random.nextInt(projects.size());
            list.add(projects.get(index));
        }
        return list;
    }

}
