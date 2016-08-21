package org.highj.js_monad;

import org.derive4j.Data;
import org.derive4j.Derive;
import org.derive4j.Flavour;
import org.derive4j.Visibility;

@Data(value = @Derive(inClass = "FJImpl", withVisibility = Visibility.Package), flavour = Flavour.HighJ)
public abstract class JS<A> {

    public interface Cases<R,A> {
        R Pure(A pure);
        R Bind(Bound<?,A> bound);
    }

    public abstract <R> R match(Cases<R,A> cases);
}
