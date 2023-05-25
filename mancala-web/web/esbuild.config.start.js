#!/usr/bin/env node

const {files} = require("./esbuild.config.entry");
require("esbuild").serve({
    port: 7777,
    host: "localhost",
    servedir: "./src/",
}, {
    entryPoints: [...files],
    bundle: true,
    minify: true,
    sourcemap: false,
    target: ["chrome97", "firefox95", "safari15.2", "edge97"],
    logLevel: "info",
    loader: {
        ".html": "file",
        ".woff": "file",
        ".woff2": "file",
        ".ttf": "file",
        ".svg": "file",
        ".jpg": "file",
        ".png": "file",
        ".eot": "file",
    }
}).catch((reason) => {
    console.error(reason);
});