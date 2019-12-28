# recipe-picker

A [re-frame](https://github.com/day8/re-frame) application designed to pick recipes

## Development Mode

### Start Cider from Emacs:

Refer to the [shadow-cljs Emacs / CIDER documentation](https://shadow-cljs.github.io/docs/UsersGuide.html#cider).

**Note: use cider-jackin-cljs (C-c M-S-j) not plain cider-jackin (C-C M-j)

Choose "shadow-cljs" on the first prompt, "app" on the second

The mentioned `dir-local.el` file has been created.

Don't run in parallel with "lein dev" mentioned below.

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

