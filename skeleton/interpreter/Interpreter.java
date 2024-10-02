package interpreter;

import java.io.*;
import java.util.HashMap;
import java.util.Stack;

import parser.ParserWrapper;
import ast.*;

public class Interpreter {

    // Process return codes
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_PARSING_ERROR = 1;
    public static final int EXIT_STATIC_CHECKING_ERROR = 2;
    public static final int EXIT_DYNAMIC_TYPE_ERROR = 3;
    public static final int EXIT_NIL_REF_ERROR = 4;
    public static final int EXIT_QUANDARY_HEAP_OUT_OF_MEMORY_ERROR = 5;
    public static final int EXIT_DATA_RACE_ERROR = 6;
    public static final int EXIT_NONDETERMINISM_ERROR = 7;

    static private Interpreter interpreter;
    private final Stack<HashMap<String, Object>> environment;

    public static Interpreter getInterpreter() {
        return interpreter;
    }

    public static void main(String[] args) {
        String gcType = "NoGC"; // default for skeleton, which only supports NoGC
        long heapBytes = 1 << 14;
        int i = 0;
        String filename;
        long quandaryArg;
        try {
            for (; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-")) {
                    if (arg.equals("-gc")) {
                        gcType = args[i + 1];
                        i++;
                    } else if (arg.equals("-heapsize")) {
                        heapBytes = Long.valueOf(args[i + 1]);
                        i++;
                    } else {
                        throw new RuntimeException("Unexpected option " + arg);
                    }
                } else {
                    if (i != args.length - 2) {
                        throw new RuntimeException("Unexpected number of arguments");
                    }
                    break;
                }
            }
            filename = args[i];
            quandaryArg = Long.valueOf(args[i + 1]);
        } catch (Exception ex) {
            System.out.println("Expected format: quandary [OPTIONS] QUANDARY_PROGRAM_FILE INTEGER_ARGUMENT");
            System.out.println("Options:");
            System.out.println("  -gc (MarkSweep|Explicit|NoGC)");
            System.out.println("  -heapsize BYTES");
            System.out.println("BYTES must be a multiple of the word size (8)");
            return;
        }

        Program astRoot = null;
        Reader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            astRoot = ParserWrapper.parse(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            Interpreter.fatalError("Uncaught parsing error: " + ex, Interpreter.EXIT_PARSING_ERROR);
        }
        // astRoot.println(System.out);
        interpreter = new Interpreter(astRoot);
        interpreter.initMemoryManager(gcType, heapBytes);
        Object returnValue = interpreter.executeRoot(astRoot, quandaryArg);
        if (returnValue != null) {
            System.out.println("Interpreter returned " + returnValue.toString());
        } else {
            System.out.println("Interpreter returned null");
        }
    }

    final Program astRoot;

    private Interpreter(Program astRoot) {
        this.astRoot = astRoot;
        this.environment = new Stack<>();
    }

    void initMemoryManager(String gcType, long heapBytes) {
        if (gcType.equals("Explicit")) {
            throw new RuntimeException("Explicit not implemented");
        } else if (gcType.equals("MarkSweep")) {
            throw new RuntimeException("MarkSweep not implemented");
        } else if (gcType.equals("RefCount")) {
            throw new RuntimeException("RefCount not implemented");
        } else if (gcType.equals("NoGC")) {
            // Nothing to do
        }
    }

    Object executeRoot(Program astRoot, long arg) {
        FuncDef mainFunc = astRoot.getMainFunction();
        return execute(mainFunc.getBody(), mainFunc.getFormalDeclList(), arg);
    }

    Object execute(StmtList body, FormalDeclList params, long arg) {
        environment.push(new HashMap<>());

        if (params.getVarDecls().size() > 0) {
            VarDecl param = params.getVarDecls().get(0);
            environment.peek().put(param.getIdentifier(), arg);
        }

        environment.push(new HashMap<>());

        Object result = executeStmtList(body);

        environment.pop();
        environment.pop();

        return result;
    }

    Object executeStmtList(StmtList stmtList) {
        for (Stmt stmt : stmtList.getStmts()) {
            Object result = executeStmt(stmt);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    Object executeStmt(Stmt stmt) {
        if (stmt instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) stmt;
            Object returnValue = evaluateExpr(returnStmt.getExpr());
            return returnValue;
        } else if (stmt instanceof BlockStmt) {
            BlockStmt blockStmt = (BlockStmt) stmt;
            return executeStmtList(blockStmt.getStmtList());
        } else if (stmt instanceof PrintStmt) {
            PrintStmt printStmt = (PrintStmt) stmt;
            Object value = evaluateExpr(printStmt.getExpr());
            System.out.println(value);
        } else if (stmt instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) stmt;
            Object conditionValue = evaluateExpr(ifStmt.getCond());
            if ((Boolean) conditionValue) {
                return executeStmt(ifStmt.getStmt());
            }
            return null;
        } else if (stmt instanceof IfElseStmt) {
            IfElseStmt ifElseStmt = (IfElseStmt) stmt;
            Object conditionValue = evaluateExpr(ifElseStmt.getCond());
            if ((Boolean) conditionValue) {
                return executeStmt(ifElseStmt.getStmt1());
            } else if (ifElseStmt.getStmt2() != null) {
                return executeStmt(ifElseStmt.getStmt2());
            }
        } else if (stmt instanceof VarDecl) {
            VarDecl varDecl = (VarDecl) stmt;
            Object value = evaluateExpr(varDecl.getExpr());
            environment.peek().put(varDecl.getIdentifier(), value);
        }

        return null;
    }

    Object evaluateExpr(Expr expr) {
        if (expr instanceof ConstExpr) {
            ConstExpr constExpr = (ConstExpr) expr;
            return constExpr.getValue();
        } else if (expr instanceof VarExpr) {
            VarExpr varExpr = (VarExpr) expr;
            Object value = null;
            for (int i = environment.size() - 1; i >= 0; i--) {
                value = environment.get(i).get(varExpr.getIdentifier());
                if (value != null) {
                    break;
                }
            }
            return value;
        } else if (expr instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr) expr;
            Object operand = evaluateExpr(unaryExpr.getExpr());
            switch (unaryExpr.getOperator()) {
                case UnaryExpr.MINUS:
                    return -(Long) operand;
            }
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr) expr;
            Object leftValue = evaluateExpr(binaryExpr.getLeft());
            Object rightValue = evaluateExpr(binaryExpr.getRight());
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS:
                    return (Long) leftValue + (Long) rightValue;
                case BinaryExpr.MINUS:
                    return (Long) leftValue - (Long) rightValue;
                case BinaryExpr.TIMES:
                    return (Long) leftValue * (Long) rightValue;
            }
        } else if (expr instanceof Cond) {
            Cond condExpr = (Cond) expr;
            Object leftValue = evaluateExpr(condExpr.getLeft());
            Object rightValue = evaluateExpr(condExpr.getRight());
            switch (condExpr.getOperator()) {
                case Cond.EQ:
                    System.out.println(leftValue.equals(rightValue));
                    return leftValue.equals(rightValue);
                case Cond.NEQ:
                    return !leftValue.equals(rightValue);
                case Cond.LT:
                    return (Long) leftValue < (Long) rightValue;
                case Cond.GT:
                    return (Long) leftValue > (Long) rightValue;
                case Cond.LTE:
                    return (Long) leftValue <= (Long) rightValue;
                case Cond.GTE:
                    return (Long) leftValue >= (Long) rightValue;
                case Cond.AND:
                    return (Boolean) leftValue && (Boolean) rightValue;
                case Cond.OR:
                    return (Boolean) leftValue || (Boolean) rightValue;
                case Cond.NOT:
                    return !(Boolean) leftValue;
            }
        }
        return null;
    }

    public static void fatalError(String message, int processReturnCode) {
        System.out.println(message);
        System.exit(processReturnCode);
    }
}
