import {Hole, render} from "uhtml";

export function write(node: string, hole: Hole) {
    // @ts-ignore
    render(document.querySelector(node), hole)
}