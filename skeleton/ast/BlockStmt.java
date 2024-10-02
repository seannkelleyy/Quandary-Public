package ast;

import java.util.List;

public class BlockStmt extends Stmt {
    private final StmtList stmtList;

    public BlockStmt(StmtList stmtList, Location loc) {
        super(loc);
        this.stmtList = stmtList;
    }

    public StmtList getStmtList() {
        return stmtList;
    }
}