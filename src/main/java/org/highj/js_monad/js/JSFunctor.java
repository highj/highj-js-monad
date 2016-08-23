package org.highj.js_monad.js;

import org.derive4j.hkt.__;
import org.highj.js_monad.JS;
import org.highj.typeclass1.functor.Functor;

import java.util.function.Function;

public interface JSFunctor extends Functor<JS.µ> {
    @Override
    default <A, B> __<JS.µ, B> map(Function<A, B> function, __<JS.µ, A> p) {
        return JS.narrow(p).map(function::apply);
    }
}
