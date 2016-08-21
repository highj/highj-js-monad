package org.highj.js_monad;

public class JSExprId {
    private final int _id;

    private JSExprId(int id) {
        this._id = id;
    }

    public static JSExprId of(int id) {
        return new JSExprId(id);
    }

    public int getId() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (!(obj instanceof JSExprId)) { return false; }
        JSExprId other = (JSExprId)obj;
        return _id == other._id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(_id);
    }
}
