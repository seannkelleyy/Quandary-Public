package ast;

import java.util.List;

public class FuncDef {
    private final String functionName;
    private final FormalDeclList params;
    private final StmtList body;
    private final Location loc;

    public FuncDef(String functionName, FormalDeclList params, StmtList body, Location loc) {
        this.functionName = functionName;
        this.params = params;
        this.body = body;
        this.loc = loc;
    }

    public String getFunctionName() {
        return functionName;
    }

    public FormalDeclList getFormalDeclList() {
        return params;
    }

    public StmtList getBody() {
        return body;
    }

    public Location getLoc() {
        return loc;
    }
}
