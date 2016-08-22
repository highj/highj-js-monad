package org.highj.js_monad;

class MutableJSState {
    final java.util.ArrayList<String> code = new java.util.ArrayList<>();
    final MutableBiHashMap<JSExprId,JSExprNode> dag = new MutableBiHashMap<>();
    final java.util.HashMap<JSExprId,JSVarName> expIdVarMap = new java.util.HashMap<>();
    int indentLevel = 0;
    int nextNodeId = 0;
    int nextVarId = 0;
}
