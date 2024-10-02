package ast;

public class BinaryExpr extends Expr {
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int TIMES = 3;
    private Expr left;
    private int operator;
    private Expr right;

    public BinaryExpr(Expr left, int operator, Expr right, Location loc) {
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

    @Override
    public String toString() {
        String s = null;
        switch (operator) {
            case PLUS:
                s = "+";
                break;
            case MINUS:
                s = "-";
                break;
            case TIMES:
                s = "*";
                break;
        }
        return "(" + left + " " + s + " " + right + ")";
    }
}