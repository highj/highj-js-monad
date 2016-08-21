package org.highj.js_monad;

import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.Visibility;
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
}
