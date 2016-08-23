package org.highj.js_monad;

public class JSString {
    private final JSExpr _expr;

    private JSString(JSExpr expr) {
        this._expr = expr;
    }

    public static JSString of(JSExpr expr) {
        return new JSString(expr);
    }

    public JSExpr expr() {
        return _expr;
    }

    public JSString append(JSString other) {
        return JSString.of(JSExpr.appendString(expr(), other.expr()));
    }
}
