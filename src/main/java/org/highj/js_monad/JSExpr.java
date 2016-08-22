package org.highj.js_monad;

import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.Visibility;

@Data(value = @Derive(inClass = "JSExprImpl", withVisibility = Visibility.Package), flavour = Flavour.HighJ)
public abstract class JSExpr {

    public interface Cases<R> {
        R Var(JSVarName varName);
        R LitString(String stringValue);
        R AppendString(JSExpr e1, JSExpr e2);
    }

    public abstract <R> R match(Cases<R> cases);

    public static JSExpr var(JSVarName a) {
        return JSExprImpl.Var(a);
    }

    public static JSExpr litString(String a) {
        return JSExprImpl.LitString(a);
    }

    public static JSExpr appendString(JSExpr e1, JSExpr e2) {
        return JSExprImpl.AppendString(e1, e2);
    }
}
