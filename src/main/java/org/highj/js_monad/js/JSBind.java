package org.highj.js_monad.js;

import org.derive4j.hkt.__;
import org.highj.js_monad.JS;
import org.highj.typeclass1.monad.Bind;

import java.util.function.Function;

public interface JSBind extends JSApply, Bind<JS.µ> {
    @Override
    default <A, B> __<JS.µ, B> bind(__<JS.µ, A> nestedA, Function<A, __<JS.µ, B>> fn) {
        return JS.narrow(nestedA).bind(a -> JS.narrow(fn.apply(a)));
    }
}
