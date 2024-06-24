package New.Classes;

import New.Classes.Category;
import New.Classes.Country;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Project {
    private int ID;
    private String name;
    private Category category;
    private String[] members;
    private String description;
    private Country country;
    private Image logo;

    // Getter and Setter for ID
    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    // Getter and Setter for name
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for category
    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // Getter and Setter for members
    public String[] getMembers() {
        return this.members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    // Getter and Setter for description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for country
    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    // Getter and Setter for logo
    public Image getLogo() {
        return this.logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    // Constructor
    public Project(int ID, String name, Category category, String[] members, String description, Country country, String logoPath) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.members = members;
        this.description = description;
        this.country = country;
        this.logo = new Image(new File(logoPath).toURI().toString());
    }

    // Constructor from a list
    public Project(List<String> list) {
        int size = list.size();
        if (size < 7) throw new RuntimeException();

        this.ID = Integer.parseInt(list.get(0));
        this.name = list.get(1);
        this.category = Category.valueOf(list.get(2));
        this.logo = new Image(list.get(size - 1));
        this.country = Country.valueOf(list.get(size - 2));
        this.description = list.get(size - 3);

        List<String> sublist = list.subList(3, size - 3);
        this.members = sublist.toArray(new String[0]);
    }

    // Convert project to a list of strings
    public LinkedList<String> StringList() {
        LinkedList<String> list = new LinkedList<>();
        list.add(this.IDtoStr());
        list.add(this.name);
        list.add(this.category.toString());
        Collections.addAll(list, this.members);
        list.add(this.description);
        list.add(this.country.toString());
        list.add(this.logo.getUrl());
        return list;
    }

    // Convert ID to string
    public String IDtoStr() {
        String s = "";
        int i = this.ID;
        for (int j = 2; j > -1; --j) {
            s = s + String.valueOf((int) (i / Math.pow(10.0, j)));
            i = (int) (i % Math.pow(10.0, j));
        }
        return s;
    }
}
