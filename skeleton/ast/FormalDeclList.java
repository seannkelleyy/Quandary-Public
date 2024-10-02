package ast;

import java.util.List;

public class FormalDeclList {
    private List<VarDecl> varDecls;
    private Location loc;

    public FormalDeclList(List<VarDecl> varDecls, Location loc) {
        this.varDecls = varDecls;
        this.loc = loc;
    }

    public List<VarDecl> getVarDecls() {
        return varDecls;
    }

    public Location getLoc() {
        return loc;
    }
}