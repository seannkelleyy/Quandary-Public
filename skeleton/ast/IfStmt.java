package ast;

public class IfStmt extends Stmt {
    final Expr cond;
    final Stmt stmt;

    public IfStmt(Expr cond, Stmt stmt, Location loc) {
        super(loc);
        this.cond = cond;
        this.stmt = stmt;
    }

    public Expr getCond() {
        return cond;
    }

    public Stmt getStmt() {
        return stmt;
    }
}