package org.highj.js_monad;

class MutableBiHashMap<A,B> {
    private final java.util.HashMap<A,B> map1 = new java.util.HashMap<>();
    private final java.util.HashMap<B,A> map2 = new java.util.HashMap<>();
}
