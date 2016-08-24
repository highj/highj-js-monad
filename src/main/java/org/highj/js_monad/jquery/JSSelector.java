package org.highj.js_monad.jquery;

import org.highj.js_monad.JSExpr;

public class JSSelector {
    private JSExpr _expr;

    private JSSelector(JSExpr expr) {
        this._expr = expr;
    }

    public static JSSelector of(JSExpr expr) {
        return new JSSelector(expr);
    }

    public JSExpr expr() {
        return _expr;
    }
}
