package org.avmrus.fopds;

public class Author {
    private int id;
    private String name;

    public Author() {
        super();
    }

    public Author(String name) {
        this.id = 0;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        String[] items = this.name.split(" ");
        String shortName = items[0];
        int index = 1;
        while (index < items.length) {
            shortName = shortName + " " + items[index].substring(0,1) +".";
            index++;
        }
        return shortName;
    }
}
