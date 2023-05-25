import {Component, ComponentWithState, define} from "./component";
import {com} from "mancala-web-kotlin"
import GameState = com.mancala.state.GameState;
import {Hole, html} from "uhtml";

@Component
export class NewGameComponent extends ComponentWithState<GameState> {

    static observedAttributes = [
        "player-1-name",
        "player-2-name",
        "number-of-cups",
        "stones-per-cup"
    ]
    static register = () => define("new-game", NewGameComponent);

    setValue(name: string, value: string): void {
        console.log("setValue", name, value)
    }

    render(): Hole {
        return html`
            <style>
                :host {
                    display: flex;
                    flex-direction: column;
                    flex: 1 1 auto;
                }
                
                :host > form {
                    display: flex;
                    flex-direction: column;
                    flex: 1 1 auto;
                }
                
                :host > form > section {
                    display: flex;
                    flex-direction: column;
                }
                
                :host > form > * + * {
                    margin-top: 1rem;
                }
                
                input {
                    padding: 0.5rem;
                }
                
                button {
                    text-transform: uppercase;
                    font-weight: bold;
                    padding: 1rem;
                    border: 1px solid var(--button-background-color);
                    background-color: var(--button-background-color);
                    border-radius: var(--radius);
                    color: var(--button-text-color);
                    box-shadow: 0 4px 0-2px rgba(3,3,26,.25)
                }
                
                button:hover {
                    cursor: pointer;
                    background-color: var(--button-background-color-hover);
                }
                
            </style>
            <form>
                <section>
                    <label>
                        Player one name:
                    </label>
                    <input 
                        @input="${(e: InputEvent) => {
                            if (e.target instanceof HTMLInputElement) {
                                this.state.player1Name = e.target.value
                            }                       
                        }}"
                        type="text" 
                        value="${this.state.player1Name}">
                </section>
                <section>
                    <label>
                        Player two name:
                    </label>       
                    <input type="text"
                        @input="${(e: InputEvent) => {
                            if (e.target instanceof HTMLInputElement)     {
                                this.state.player2Name = e.target.value
                            }
                        }}"
                        value="${this.state.player2Name}">
                </section>
                <button
                    @click="${() => {
                        this.state.ready = true;
                    }}">
                    Start
                </button>
            </form>
        `;
    }

}