#!/usr/bin/env node

const {files} = require("./esbuild.config.entry");
require("esbuild").build({
    entryPoints: [...files],
    bundle: true,
    minify: true,
    sourcemap: true,
    target: ["chrome97", "firefox95", "safari15.2", "edge97"],
    outdir: "./dist",
    logLevel: "info",
    loader: {
        ".html": "file",
        ".woff": "file",
        ".woff2": "file",
        ".ttf": "file",
        ".svg": "file",
        ".png": "file",
        ".eot": "file"
    }
}).catch(() => process.exit(1))