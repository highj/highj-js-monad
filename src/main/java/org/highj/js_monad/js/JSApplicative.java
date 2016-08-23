package org.highj.js_monad.js;

import org.derive4j.hkt.__;
import org.highj.js_monad.JS;
import org.highj.typeclass1.monad.Applicative;

public interface JSApplicative extends JSApply, Applicative<JS.µ> {
    @Override
    default <A> __<JS.µ, A> pure(A a) {
        return JS.pure(a);
    }
}
