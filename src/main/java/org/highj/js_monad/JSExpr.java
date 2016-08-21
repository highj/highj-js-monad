package org.highj.js_monad;

import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.Visibility;

@Data(value = @Derive(inClass = "JSExprImpl", withVisibility = Visibility.Package), flavour = Flavour.HighJ)
public abstract class JSExpr {

    public interface Cases<R> {
        R LitString(String stringValue);
        R AppendString(JSExprId e1, JSExprId e2);
    }

    public abstract <R> R match(Cases<R> cases);

    public static JSExpr litString(String a) {
        return JSExprImpl.LitString(a);
    }

    public static JSExpr appendString(JSExprId e1, JSExprId e2) {
        return JSExprImpl.AppendString(e1, e2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (!(obj instanceof JSExpr)) { return false; }
        JSExpr other = (JSExpr)obj;
        return JSExprImpl
            .cases()
            .LitString((String a1) -> JSExprImpl.cases().LitString((String a2) -> a1.equals(a2)).otherwise(false))
            .AppendString((JSExprId e11, JSExprId e12) -> JSExprImpl.cases().AppendString((JSExprId e21, JSExprId e22) -> e11.equals(e21) && e12.equals(e22)).otherwise(false))
            .apply(this)
            .apply(other);
    }

    @Override
    public int hashCode() {
        return match(HashCases.instance);
    }

    private static class HashCases implements Cases<Integer> {
        public static final HashCases instance = new HashCases();
        @Override
        public Integer LitString(String stringValue) {
            return stringValue.hashCode();
        }
        @Override
        public Integer AppendString(JSExprId e1, JSExprId e2) {
            return 1 ^ e1.hashCode() ^ e2.hashCode();
        }
    }
}
