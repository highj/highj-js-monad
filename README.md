# highj-js-monad
JavaScript Monad built on-top of HighJ infrastructure

## FFI Examples

### Pure FFI
```
public JSNumber sin(JSNumber x) {
  return JSNumber.of(JSExpr.callPure(JSExpr.var(JSVarName.of("Math.sin")), x.expr()));
}
```

### Effectful FFI
```JS<>``` represents effectful JavaScript computations.
```
public JS<JSString> prompt(JSString msg) {
    return JS.evalExpr(msg.expr()).bind(
      (JSVarName nMsg) ->
        JS.allocVarName().bind(
          (JSVarName n) ->
            JS.trustMe("var " + n.name() + " = window.prompt(" + nMsg.name() + ");")
              .andThen(JS.pure(JSString.of(JSExpr.var(n))))
        )
    );
}
```
