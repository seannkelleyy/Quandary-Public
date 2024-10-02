package ast;

import java.util.ArrayList;
import java.util.List;

public class StmtList extends Stmt {
    private List<Stmt> stmts;

    public StmtList(List<Stmt> stmts, Location loc) {
        super(loc);
        this.stmts = new ArrayList<>(stmts);
    }

    public void addStmt(Stmt stmt) {
        stmts.add(stmt);
    }

    public List<Stmt> getStmts() {
        return stmts;
    }
}