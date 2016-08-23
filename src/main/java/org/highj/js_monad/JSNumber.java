package org.highj.js_monad;

public class JSNumber {
    private final JSExpr _expr;

    private JSNumber(JSExpr expr) {
        this._expr = expr;
    }

    public static JSNumber of(JSExpr expr) {
        return new JSNumber(expr);
    }

    public JSExpr expr() {
        return _expr;
    }
}
