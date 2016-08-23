package org.highj.js_monad;

import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.Visibility;
import org.highj.data.List;

@Data(value = @Derive(inClass = "JSExprNodeImpl", withVisibility = Visibility.Package), flavour = Flavour.HighJ)
public abstract class JSExprNode {

    public interface Cases<R> {
        R Var(JSVarName varName);
        R LitString(String stringValue);
        R AppendString(JSExprId e1, JSExprId e2);
        R CallPure(JSExprId func, List<JSExprId> args);
    }

    public abstract <R> R match(Cases<R> cases);

    public static JSExprNode var(JSVarName a) {
        return JSExprNodeImpl.Var(a);
    }

    public static JSExprNode litString(String a) {
        return JSExprNodeImpl.LitString(a);
    }

    public static JSExprNode appendString(JSExprId e1, JSExprId e2) {
        return JSExprNodeImpl.AppendString(e1, e2);
    }

    public static JSExprNode callPure(JSExprId func, List<JSExprId> args) {
        return JSExprNodeImpl.CallPure(func, args);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (!(obj instanceof JSExprNode)) { return false; }
        JSExprNode other = (JSExprNode)obj;
        return JSExprNodeImpl
            .cases()
            .Var((JSVarName a1) -> JSExprNodeImpl.cases().Var((JSVarName a2) -> a1.name().equals(a2.name())).otherwise(false))
            .LitString((String a1) -> JSExprNodeImpl.cases().LitString((String a2) -> a1.equals(a2)).otherwise(false))
            .AppendString((JSExprId e11, JSExprId e12) -> JSExprNodeImpl.cases().AppendString((JSExprId e21, JSExprId e22) -> e11.equals(e21) && e12.equals(e22)).otherwise(false))
            .CallPure((JSExprId func1, List<JSExprId> args1) ->
                JSExprNodeImpl.cases().CallPure((JSExprId func2, List<JSExprId> args2) -> {
                    if (!func1.equals(func2)) { return false; }
                    List<JSExprId> at1 = args1;
                    List<JSExprId> at2 = args2;
                    while (!(at1.isEmpty() || at2.isEmpty())) {
                        if (!at1.head().equals(at2.head())) {
                            return false;
                        }
                        at1 = at1.tail();
                        at2 = at2.tail();
                    }
                    return at1.isEmpty() && at2.isEmpty();
                }).otherwise(false)
            )
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
        public Integer Var(JSVarName varName) {
            return varName.name().hashCode();
        }
        @Override
        public Integer LitString(String stringValue) {
            return 1 ^ stringValue.hashCode();
        }
        @Override
        public Integer AppendString(JSExprId e1, JSExprId e2) {
            return 2 ^ e1.hashCode() ^ e2.hashCode();
        }
        @Override
        public Integer CallPure(JSExprId func, List<JSExprId> args) {
            return 3 ^ func.hashCode() ^ args.map(JSExprId::hashCode).foldl(0, a -> b -> a ^ b);
        }
    }
}
