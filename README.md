# recipe-picker

A [re-frame](https://github.com/day8/re-frame) application designed to pick recipes

## Development Mode

### Start Cider from Emacs:

Refer to the [shadow-cljs Emacs / CIDER documentation](https://shadow-cljs.github.io/docs/UsersGuide.html#cider).

**Note: use cider-jackin-cljs (C-c M-S-j) not plain cider-jackin (C-C M-j)

The mentioned `dir-local.el` file has been created.

### Run application:

```
lein dev
```

shadow-cljs will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:8280](http://localhost:8280).

### Run tests:

Install karma and headless chrome

```
npm install -g karma-cli
```

And then run your tests

```
lein karma
```

## Production Build

