package ast;

public class VarDecl extends Stmt {
    public static final String INT = "int";

    private String type;
    private String identifier;
    private Expr expr;

    public VarDecl(String type, String identifier, Expr expr, Location loc) {
        super(loc);
        this.type = type;
        this.identifier = identifier;
        this.expr = expr;
    }

    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Expr getExpr() {
        return expr;
    }
}