package org.highj.js_monad.jquery;

import org.highj.js_monad.JS;
import org.highj.js_monad.JSExpr;
import org.highj.js_monad.JSString;
import org.highj.js_monad.JSVarName;

public class JQuery {

    public JS<JSJQuery> $(JSSelector selector) {
        return JS.narrow(JS.monad.join(JS.monad.apply2(
            (JSVarName nSelector) -> (JSVarName nResult) ->
                JS.trustMe("var " + nResult.name() + " = $(" + nSelector.name() + ");")
                    .andThen(JS.pure(JSJQuery.of(JSExpr.var(nResult)))),
            JS.evalExpr(selector.expr()),
            JS.allocVarName()
        )));
    }

    public JS<JSJQuery> add(JSJQuery jQuery, JSSelector selector) {
        return JS.narrow(JS.monad.join(JS.monad.apply3(
            (JSVarName nJQuery) -> (JSVarName nSelector) -> (JSVarName nResult) ->
                JS.trustMe("var " + nResult.name() + " = " + nJQuery.name() + ".add(" + nSelector.name() + ");")
                    .andThen(JS.pure(JSJQuery.of(JSExpr.var(nResult)))),
            JS.evalExpr(jQuery.expr()),
            JS.evalExpr(selector.expr()),
            JS.allocVarName()
        )));
    }

    public JS<JSJQuery> addBack(JSJQuery jQuery) {
        return JS.narrow(JS.monad.join(JS.monad.apply2(
            (JSVarName nJQuery) -> (JSVarName nResult) ->
                JS.trustMe("var " + nResult.name() + " = " + nJQuery.name() + ".addBack();")
                    .andThen(JS.pure(JSJQuery.of(JSExpr.var(nResult)))),
            JS.evalExpr(jQuery.expr()),
            JS.allocVarName()
        )));
    }

    public JS<JSJQuery> addClass(JSJQuery jQuery, JSString className) {
        return JS.narrow(JS.monad.join(JS.monad.apply3(
            (JSVarName nJQuery) -> (JSVarName nClassName) -> (JSVarName nResult) ->
                JS.trustMe("var " + nResult.name() + " = " + nJQuery.name() + ".addClass(" + nClassName.name() + ");")
                    .andThen(JS.pure(JSJQuery.of(JSExpr.var(nResult)))),
            JS.evalExpr(jQuery.expr()),
            JS.evalExpr(className.expr()),
            JS.allocVarName()
        )));
    }
}
