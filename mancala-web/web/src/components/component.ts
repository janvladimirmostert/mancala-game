import {Hole, render} from "uhtml";
import {com} from "mancala-web-kotlin"
import ObservableState = com.mancala.state.ObservableState

interface Component {
    readonly observedAttributes: Array<string>;
    readonly register: () => void;
    new(...args: any[]): any;
}

export function Component(constructor: Component) {
    if (constructor.observedAttributes.length) {
        //console.info(`Observing [${constructor.observedAttributes}] for ${constructor.name}`);
    }
}

export function define(tag: string, component: CustomElementConstructor) {
    customElements.get(tag) || customElements.define(tag, component)
}

export abstract class ComponentWithoutState extends HTMLElement {
    abstract setValue(name: string, value: string): void;

    abstract render(): Hole;

    connected(): void {};

    disconnected(): void {};

    constructor() {
        super();
        this.attachShadow({mode: "open"})
    }

    private attributeChangedCallback(name: string, oldValue: string, newValue: string) {
        //console.log(`setValue(name='${name}', value='${newValue}') for`, this);
        this.setValue(name, newValue)
        this.display();
    }

    protected display = () => {
        render(this.shadowRoot as Node, this.render())
    }

    protected connectedCallback() {
        this.dispatchEvent(new CustomEvent<ComponentWithoutState>("init", {
            detail: this
        }));
        this.connected();
        this.display();
    }

    protected disconnectedCallback() {
        this.disconnected();
    }

}

export abstract class ComponentWithState<T extends ObservableState> extends ComponentWithoutState {

    public state!: T

    protected connectedCallback() {
        this.dispatchEvent(new CustomEvent<ComponentWithState<T>>("init", {
            detail: this
        }));
        if (this.state) {
            this.state.subscribe(this.display);
        } else {
            console.error("init for", this, "not passing state")
            throw("missing state");
        }
        this.connected();
        this.display();
    }

    protected disconnectedCallback() {
        this.state?.unsubscribe(this.display)
        this.disconnected();
    }

    protected display = () => {
        if (this.state) {
            render(this.shadowRoot as Node, this.render())
        }
    }
}
