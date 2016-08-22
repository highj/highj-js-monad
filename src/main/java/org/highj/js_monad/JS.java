package org.highj.js_monad;

import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.Visibility;
import org.highj.data.Maybe;
import org.highj.data.stateful.SafeIO;
import org.highj.data.tuple.T0;
import org.highj.function.F1;

@Data(value = @Derive(inClass = "JSImpl", withVisibility = Visibility.Package), flavour = Flavour.HighJ)
public abstract class JS<A> {

    public interface Cases<R,A> {
        R Pure(A pure);
        R Bind(Bound<?,A> bound);
    }

    public abstract <R> R match(Cases<R,A> cases);

    public static <A> JS<A> pure(A a) {
        return JSImpl.Pure(a);
    }

    public <B> JS<B> map(F1<A,B> f) {
        return this.bind((A a) -> pure(f.apply(a)));
    }

    public <B> JS<B> bind(F1<A,JS<B>> f) {
        return JSImpl
            .<A>cases()
            .Pure(f)
            .Bind((Bound<?,A> bound) -> bindBound(bound, f))
            .apply(this);
    }

    private static <A,B,C> JS<C> bindBound(Bound<A,B> bound, F1<B,JS<C>> f) {
        return JSImpl.Bind(new Bound<>(bound.ma, (A a) -> bound.f.apply(a).bind(f)));
    }

    private static <A> JS<A> liftJSI(JSI<A> m) {
        return JSImpl.Bind(new Bound<>(m, JS::pure));
    }

    public static JS<T0> trustMe(String code) {
        return liftJSI((MutableJSState s) -> (SafeIO<T0>)() -> {
            String indent = "";
            for (int i = 0; i < s.indentLevel; ++i) {
                indent += "  ";
            }
            String line = indent + code;
            s.code.add(line);
            return T0.of();
        });
    }

    private static JS<JSExprId> hashCons(JSExpr expr) {
        F1<JSExprNode,JS<JSExprId>> getOrMakeId = (JSExprNode n) -> JS.liftJSI((MutableJSState s) -> (SafeIO<JSExprId>)() -> {
            Maybe<JSExprId> x = s.dag.lookup2(n);
            if (x.isNothing()) {
                JSExprId r = JSExprId.of(s.nextNodeId++);
                s.dag.put(r, n);
                return r;
            } else {
                return x.get();
            }
        });
        return JSExprImpl
            .cases()
            .Var((JSVarName a) -> getOrMakeId.apply(JSExprNode.var(a)))
            .LitString((String a) -> getOrMakeId.apply(JSExprNode.litString(a)))
            .AppendString((JSExpr e1, JSExpr e2) ->
                hashCons(e1).bind(
                    (JSExprId x1) ->
                        hashCons(e2).bind(
                            (JSExprId x2) ->
                                getOrMakeId.apply(JSExprNode.appendString(x1, x2))
                        )
                )
            )
            .apply(expr);
    }

    private static JS<JSVarName> evalExpr(JSExpr expr) {
        return hashCons(expr).bind((JSExprId exprId) ->
            liftJSI((MutableJSState s) -> (SafeIO<JSVarName>)() -> {
                JSVarName varName = s.expIdVarMap.get(exprId);
                if (varName != null) {
                    return varName;
                } else {
                    varName = JSVarName.of("v" + (s.nextVarId++));
                    s.expIdVarMap.put(exprId, varName);
                    return varName;
                }
            })
        );
    }
}
