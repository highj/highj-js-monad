package org.highj.js_monad;

import org.highj.data.Maybe;

class MutableBiHashMap<A,B> {
    private final java.util.HashMap<A,B> map1 = new java.util.HashMap<>();
    private final java.util.HashMap<B,A> map2 = new java.util.HashMap<>();

    public void put(A a, B b) {
        map1.put(a, b);
        map2.put(b, a);
    }

    public void remove1(A a) {
        B b = map1.remove(a);
        if (b != null) {
            map2.remove(b);
        }
    }

    public void remove2(B b) {
        A a = map2.remove(b);
        if (a != null) {
            map1.remove(a);
        }
    }

    public Maybe<B> lookup1(A a) {
        B b = map1.get(a);
        if (b != null) {
            return Maybe.Just(b);
        } else {
            return Maybe.Nothing();
        }
    }

    public Maybe<A> lookup2(B b) {
        A a = map2.get(b);
        if (a != null) {
            return Maybe.Just(a);
        } else {
            return Maybe.Nothing();
        }
    }
}
