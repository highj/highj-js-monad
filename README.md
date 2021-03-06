# highj-js-monad
JavaScript Monad built on-top of HighJ infrastructure

## FFI Examples

### Pure FFI
```
public JSNumber sin(JSNumber x) {
  return JSNumber.of(JSExpr.callPure(JSExpr.var(JSVarName.of("Math.sin")), List.of(x.expr())));
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

Another Example
```
public JS<JSJQuery> $(JSSelector selector) {
  return JS.narrow(JS.monad.join(JS.monad.apply2(
    (JSVarName nSelector) -> (JSVarName nResult) ->
      JS.trustMe("var " + nResult.name() + " = $(" + nSelector.name() + ");")
        .andThen(JS.pure(JSJQuery.of(JSExpr.var(nResult)))),
      JS.evalExpr(selector.expr()),
      JS.allocVarName()
  )));
}
```

## Usage Example
This one is from the Reference Implementation (in PureScript). It will look a little different in Java.
```
example :: JS Unit
example = do
  userName <- prompt $ jsStr "What is your name?"
  alert $ (jsStr "Hello ") <> userName <> (jsStr "!")
```
