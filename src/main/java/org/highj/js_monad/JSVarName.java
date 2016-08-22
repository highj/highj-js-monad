package org.highj.js_monad;

public class JSVarName {
    private final String _name;

    private JSVarName(String name) {
        this._name = name;
    }

    public static JSVarName of(String name) {
        return new JSVarName(name);
    }

    public String name() {
        return _name;
    }
}
