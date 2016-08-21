package org.highj.js_monad;

import org.highj.function.F1;

class Bound<A,B> {
    final JSI<A> ma;
    final F1<A,JS<B>> f;

    Bound(JSI<A> ma, F1<A,JS<B>> f) {
        this.ma = ma;
        this.f = f;
    }
}
