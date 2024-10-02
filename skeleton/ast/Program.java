package ast;

import java.util.List;

public class Program {
    private final FuncDef mainFunction;
    private final Location loc;

    public Program(FuncDef mainFunction, Location loc) {
        this.mainFunction = mainFunction;
        this.loc = loc;
    }

    public FuncDef getMainFunction() {
        return mainFunction;
    }

    public Location getLoc() {
        return loc;
    }
}