package ast;

public class ConstExpr extends Expr {
    private final long value;

    public ConstExpr(long value, Location loc) {
        super(loc);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}