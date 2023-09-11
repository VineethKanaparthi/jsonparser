package org.dbnoobs.jsonparser;

public class Cursor {
    private int pointer;
    private Location location;

    public Cursor(int pointer, Location location) {
        this.pointer = pointer;
        this.location = location;
    }

    public Cursor(Cursor cursor){
        this.pointer = cursor.getPointer();
        this.location = new Location(cursor.getLocation());
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void incrementLine(){
        this.pointer++;
        this.getLocation().incrementLine();
    }

    public void increment(Integer ...incrementValue){
        int i = incrementValue.length>0?incrementValue[0]:1;
        this.pointer+=i;
        this.getLocation().incrementCol(i);
    }

    public void modify(Cursor cursor){
        this.pointer = cursor.getPointer();
        this.location = cursor.getLocation();
    }

}
