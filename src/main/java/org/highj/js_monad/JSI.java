package org.highj.js_monad;

import org.highj.data.stateful.SafeIO;

interface JSI<A> {

    SafeIO<A> run(MutableJSState s);
}
