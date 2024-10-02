package ast;

public abstract class Stmt {
    private Location loc;

    public Stmt(Location loc) {
        this.loc = loc;
    }

    public Location getLoc() {
        return loc;
    }
}