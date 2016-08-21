package org.highj.js_monad;

class MutableJSState {
    public final java.util.ArrayList<String> code = new java.util.ArrayList<>();
    public final MutableBiHashMap<JSExprId,JSExpr> dag = new MutableBiHashMap<>();
    public int indentLevel = 0;
    public int nextId = 0;
}
