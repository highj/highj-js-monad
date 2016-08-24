package org.highj.js_monad.jquery;

import org.highj.js_monad.JSExpr;

public class JSJQuery {
    private final JSExpr _expr;

    private JSJQuery(JSExpr expr) {
        this._expr = expr;
    }

    public static JSJQuery of(JSExpr expr) {
        return new JSJQuery(expr);
    }

    public JSExpr expr() {
        return _expr;
    }
}
