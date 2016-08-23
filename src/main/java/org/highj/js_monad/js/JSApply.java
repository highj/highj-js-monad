package org.highj.js_monad.js;

import org.derive4j.hkt.__;
import org.highj.js_monad.JS;
import org.highj.typeclass1.monad.Apply;

import java.util.function.Function;

public interface JSApply extends JSFunctor, Apply<JS.µ> {
    @Override
    default <A, B> __<JS.µ, B> ap(__<JS.µ, Function<A, B>> mf, __<JS.µ, A> ma) {
        return JS.narrow(mf).bind(f -> JS.narrow(ma).map(f::apply));
    }
}
