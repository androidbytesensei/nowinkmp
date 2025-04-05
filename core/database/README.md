# :core:database module

https://sqldelight.github.io/sqldelight/2.0.2/js_sqlite/sqljs_worker/

## SQL.js Web Worker
To include the SQL.js worker in your project, first add a dependency on the worker package along with a dependency on SQL.js.

```kotlin
kotlin {
  sourceSets.jsMain.dependencies {
    implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.2"))
    implementation(npm("sql.js", "1.8.0"))
  }
}
```

The SQL.js package includes a WebAssembly binary that must be copied into your application's output. In your project, add an additional Webpack configuration file to configure the copying of the binary to your assembled project.

**webpack.config.d/sqljs-config.js**
```kotlin
// {project}/webpack.config.d/sqljs.js
config.resolve = {
    fallback: {
        fs: false,
        path: false,
        crypto: false,
    }
};

const CopyWebpackPlugin = require('copy-webpack-plugin');
config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            '../../node_modules/sql.js/dist/sql-wasm.wasm'
        ]
    })
);
```
Configuring Karma for Tests¶
For tests, there is also some additional Karma configuration required so that the WebAssembly binaries can be located at runtime. Copy the following into your project's karma.config.d directory.

**karma.config.d/sqljs-config.js**
```kotlin
const path = require("path");
const os = require("os");
const dist = path.resolve("../../node_modules/sql.js/dist/")
const wasm = path.join(dist, "sql-wasm.wasm")

config.files.push({
    pattern: wasm,
    served: true,
    watched: false,
    included: false,
    nocache: false,
});

config.proxies["/sql-wasm.wasm"] = path.join("/absolute/", wasm)

// Adapted from: https://github.com/ryanclark/karma-webpack/issues/498#issuecomment-790040818
const output = {
  path: path.join(os.tmpdir(), '_karma_webpack_') + Math.floor(Math.random() * 1000000),
}
config.set({
  webpack: {...config.webpack, output}
});
config.files.push({
  pattern: `${output.path}/**/*`,
  watched: false,
  included: false,
});
```

Using the Worker¶
The worker script is called sqljs.worker.js and can be referenced in code like this:

```kotlin
val driver = WebWorkerDriver(
  Worker(
    js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
  )
)
```
See "Using a Web Worker" for more details.

## Dependency graph
![Dependency graph](../../docs/images/graphs/dep_graph_core_database.svg)
