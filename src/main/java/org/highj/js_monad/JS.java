package org.highj.js_monad;

import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.Visibility;
import org.highj.data.List;
import org.highj.data.Maybe;
import org.highj.data.stateful.Effect0;
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

    public List<String> run() {
        MutableJSState s = new MutableJSState();
        class Util {
            private JS<A> comp = JS.this;
            private boolean done = false;

            private <B> Effect0 processBound(Bound<B,A> bound) {
                return () -> {
                    comp = bound.f.apply(bound.ma.run(s).run());
                };
            }
        }
        final Util util = new Util();
        while (!util.done) {
            JSImpl
                .<A>cases()
                .Pure((Effect0)() -> util.done = true)
                .Bind(util::processBound)
                .apply(util.comp)
                .run();
        }
        return List.of(s.code);
    }

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

    public <B> JS<B> andThen(JS<B> a) {
        return this.bind(unused -> a);
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

    private static JS<JSVarName> allocVarName() {
        return liftJSI((MutableJSState s) -> (SafeIO<JSVarName>)() ->
            JSVarName.of("v" + (s.nextVarId++))
        );
    }

    private static JS<JSVarName> evalExpr(JSExpr expr) {
        return hashCons(expr).bind((JSExprId exprId) ->
            lookupVarForExprId(exprId).bind(
                (Maybe<JSVarName> x) ->
                    x.cata(
                        JSExprImpl
                            .cases()
                            .Var(JS::pure)
                            .LitString((String x2) ->
                                allocVarName().bind(
                                    (JSVarName n) ->
                                        trustMe("var " + n.name() + " = \"" + x2 + "\";")
                                            .andThen(storeVarForExprId(n, exprId))
                                            .andThen(JS.pure(n))
                                )
                            )
                            .AppendString((JSExpr e1, JSExpr e2) ->
                                evalExpr(e1).bind(
                                    (JSVarName n1) -> evalExpr(e2).bind(
                                        (JSVarName n2) -> allocVarName().bind(
                                            (JSVarName n3) ->
                                                trustMe("var " + n3.name() + " = " + n1.name() + " + " + n2.name() + ";")
                                                    .andThen(storeVarForExprId(n3, exprId))
                                                    .andThen(JS.pure(n3))
                                        )
                                    )
                                )
                            )
                            .apply(expr)
                        ,
                        JS::pure
                    )
            )
        );
    }

    private static JS<Maybe<JSVarName>> lookupVarForExprId(JSExprId exprId) {
        return liftJSI((MutableJSState s) -> (SafeIO<Maybe<JSVarName>>)() -> {
            JSVarName varName = s.expIdVarMap.get(exprId);
            if (varName != null) {
                return Maybe.Just(varName);
            } else {
                return Maybe.Nothing();
            }
        });
    }

    private static JS<T0> storeVarForExprId(JSVarName varName, JSExprId exprId) {
        return liftJSI((MutableJSState s) -> (SafeIO<T0>)() -> {
            s.expIdVarMap.put(exprId, varName);
            return T0.of();
        });
    }
}
