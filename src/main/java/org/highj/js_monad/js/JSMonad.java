package org.highj.js_monad.js;

import org.highj.js_monad.JS;
import org.highj.typeclass1.monad.Monad;

public interface JSMonad extends JSApplicative, JSBind, Monad<JS.µ> {
}
