package ast;

public class IfStmt extends Stmt {
    private final Expr condition;
    private final StmtList thenStmt; // Change to StmtList
    private final StmtList elseStmt; // Change to StmtList

    public IfStmt(Expr condition, StmtList thenStmt, StmtList elseStmt, Location loc) {
        super(loc);
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public Expr getCondition() {
        return condition;
    }

    public StmtList getThenStmt() {
        return thenStmt; // Return as StmtList
    }

    public StmtList getElseStmt() {
        return elseStmt; // Return as StmtList
    }

    @Override
    public String toString() {
        String result = "if (" + condition + ") " + thenStmt;
        if (elseStmt != null) {
            result += " else " + elseStmt;
        }
        return result;
    }
}
