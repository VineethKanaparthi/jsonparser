package org.dbnoobs.jsonparser;

import java.util.Objects;

public class Location {
    private int line;
    private int col;

    public Location(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public Location(Location location){
        this.col = location.getCol();
        this.line = location.getLine();
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void incrementLine(){
        this.line++;
        this.col = 0;
    }

    public void incrementCol(Integer ...incrementValue){
        int i = incrementValue.length>0?incrementValue[0]:1;
        this.col+=i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return line == location.line && col == location.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, col);
    }

    @Override
    public String toString() {
        return "Location{" +
                "line=" + line +
                ", col=" + col +
                '}';
    }
}
