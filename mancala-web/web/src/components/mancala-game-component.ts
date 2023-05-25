import {Component, ComponentWithState, define} from "./component";
import {com} from "mancala-web-kotlin"
import GameState = com.mancala.state.GameState;
import {Hole, html} from "uhtml";

@Component
export class MancalaGameComponent extends ComponentWithState<GameState> {

    static observedAttributes = [
        "player-1-name",
        "player-2-name",
    ]
    static register = () => define("mancala-game", MancalaGameComponent)

    setValue(name: string, value: string): void {
        console.log("setValue", name, value)
        switch (name) {
            case "player-1-name":
                if (this.state) {
                    this.state.player1Name = value
                }
                break
            case "player-2-name":
                if (this.state) {
                    this.state.player2Name = value
                }
                break
        }
    }

    render(): Hole {
        return html`
            <style>
            
                .boards {
                    display: flex;
                    flex-direction: column;
                    gap: 1rem;
                    margin: 1rem;
                }
                
                .board {
                    display: flex;
                    flex-direction: row;
                    gap: 1rem;
                    justify-content: center;
                }
            
                .player2 {
                    transform: rotate(180deg) ;
                }
                
                .cup {
                    width: 4rem;
                    height: 4rem;
                    border-radius: 50%;
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    justify-content: center;
                    text-transform: uppercase;
                    font-weight: bold;
                    padding: 1rem;
                   
                    text-decoration: underline;
                }
                
                .cup + .filler { 
                    
                }
                
                .cup + .player {
                    border: 1px solid var(--button-background-color);
                    background-color: var(--button-background-color);
                    color: var(--button-text-color);
                    box-shadow: 0 4px 0-2px rgba(3,3,26,.25);
                }
                
                .cup + .scoring {
                    background-color: white;
                    border: 1px solid var(--button-background-color);
                }
                
                .hoverable:hover {
                    cursor: pointer;
                    background-color: var(--button-background-color-hover);
                }
                
                game {
                   display: block;
                }
                
                game.flipped {
                   transform: rotate(180deg);
               }
                
            </style>
            <game class="${this.state.player2Turn ? "flipped" : null}">
                <div class="player2">
                    Player2: <strong>${this.state.player2Name}</strong>
                    <div>
                        ${this.state.player2Message}
                    </div>
                </div>
                <div class="${this.state.player1Turn ? `boards` : `boards flipped`}">
                    <div class="player2 board">
                        <div class="cup filler">
                        
                        </div>
                        ${this.state.player2Cups.map((e: number, index: number) => html`
                            <div 
                                class="${'cup player ' + (this.state.player2Turn ? 'hoverable' : '')}"
                                @click="${(e: MouseEvent) => {
                                    if (this.state.player2Turn) {
                                        this.state.move(index)
                                    }
                                }}">
                                ${e}
                            </div>
                        `)}
                        <div class="cup scoring">
                            ${this.state.player2ScoringCup}
                        </div>
                    </div>
                    <div class="player1 board">
                        <div class="cup filler">
                        
                        </div>
                        ${this.state.player1Cups.map((e: number, index: number) => html`
                            <div 
                                class="${'cup player ' + (this.state.player1Turn ? 'hoverable' : '')}"
                                @click="${(e: MouseEvent) => {
                                    if (this.state.player1Turn) {
                                        this.state.move(index)
                                    }    
                                }}">
                                ${e}
                            </div>
                        `)}            
                        <div class="cup scoring">
                            ${this.state.player1ScoringCup}
                        </div>
                    </div>
                </div>
                <div class="player1">
                    Player1: <strong>${this.state.player1Name}</strong> 
                    <div>
                        ${this.state.player1Message}
                    </div>
                <div>
            </game>
        `;
    }

}