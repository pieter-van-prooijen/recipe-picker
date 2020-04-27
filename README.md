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

```
$ lein clean
$ lein prod
```

Copy the contents of resources/public to xs4all:~pprooi/WWW/recipe-picker

## Webpack
Make sure webpack is installed, follow guide at https://clojurescript.org/guides/webpack

See deps.edn for an example to pull in the latest react via npm

(needs change in core.cljs to invoke the init function upon load)

#### Dev compile:
```
$ clj -m cljs.main -co build.edn  -v -c
```

(using the built-in webserver (-s) doesn't work because of changed :asset-path, use ningx with special /recipe-picker location

#### Prod compile:
```
$ clj -m cljs.main -co build.edn -O advanced  -v -c
```

#### Nginx config
Show the site via http://localhost/recipe-picker

in /etc/nginx/sites-available/default

        location /recipe-picker {
                # First attempt to serve request as file, then
                # as directory, then fall back to displaying a 404.
                try_files $uri $uri/ =404;

                # Rewrite so projects under Clojure/ which use resources/public work
                rewrite ^/recipe-picker/(.*)$ /Clojure/recipe-picker/resources/public/$1;
        }

        # Serve everything under ~/projects/Clojure
        location /Clojure {
                # First attempt to serve request as file, then
                # as directory, then fall back to displaying a 404.
                try_files $uri $uri/ =404;

                root /home/pieter/projects;
        }

Use "service nginx reload" to reload the configuration

#### Size Comparison:
closure: 498K
webpack: 621K

#### TODO
hot code reloading (figwheel.main instead of shadow-cljs ?) + cider repl
