package ast;

public class IfElseStmt extends Stmt {
    final Expr cond;
    final Stmt stmt1;
    final Stmt stmt2;

    public IfElseStmt(Expr cond, Stmt stmt1, Stmt stmt2, Location loc) {
        super(loc);
        this.cond = cond;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    public Expr getCond() {
        return cond;
    }

    public Stmt getStmt1() {
        return stmt1;
    }

    public Stmt getStmt2() {
        return stmt2;
    }
}