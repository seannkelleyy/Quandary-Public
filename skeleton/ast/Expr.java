package ast;

public abstract class Expr {
    private Location loc;

    public Expr(Location loc) {
        this.loc = loc;
    }

    public Location getLoc() {
        return loc;
    }
}