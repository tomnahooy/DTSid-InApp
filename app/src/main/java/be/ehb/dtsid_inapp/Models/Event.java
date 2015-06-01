package be.ehb.dtsid_inapp.Models;

public class Event
{
    //Variables
    private Long id;
    private String name;
    private int acadyear;

    //Constructors
    public Event(Long id, String name, int acadyear)
    {
        super();
        this.id = id;
        this.name = name;
        this.acadyear = acadyear;
    }
    public Event(String name, int acadyear)
    {
        super();
        this.name = name;
        this.acadyear = acadyear;
    }
    public Event()
    {
        super();
    }

    //Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAcadyear() {
        return acadyear;
    }
    public void setAcadyear(int acadyear) {
        this.acadyear = acadyear;
    }
}