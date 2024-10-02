package ast;

public class Cond extends Expr {
    public static final int EQ = 1;
    public static final int NEQ = 2;
    public static final int LT = 3;
    public static final int GT = 4;
    public static final int LTE = 5;
    public static final int GTE = 6;

    private Expr left;
    private int operator;
    private Expr right;

    public Cond(Expr left, int operator, Expr right, Location loc) {
        super(loc);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public int getOperator() {
        return operator;
    }
}