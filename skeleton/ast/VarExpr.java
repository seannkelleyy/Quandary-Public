package ast;

public class VarExpr extends Expr {
    private final String identifier;

    public VarExpr(String identifier, Location loc) {
        super(loc);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}