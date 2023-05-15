import {com} from "mancala-web-kotlin"
import {html} from "uhtml";
import {write} from "./components/renderer";
import {MancalaGameComponent} from "./components/mancala-game-component"
import {NewGameComponent} from "./components/new-game-component";
import GameState = com.mancala.state.GameState;

// create a new game state that will control the re-rendering
let gameState = new GameState()

// used for debugging - you can access the game state in the browser console
// game.move(0), game.player1Move, etc
// @ts-ignore
window.game = gameState

// register game component so that we can use it in the html
NewGameComponent.register()
MancalaGameComponent.register()

// implement what is rendered when the game state changes
gameState.change = (field?: string, value?: string) => {
    console.log(`${field} changed to `, value)
    write("main", html`
        ${gameState.ready ? html`
            <mancala-game
                player-1-name="${gameState?.player1Name}"
                player-2-name="${gameState?.player2Name}"    
                @init="${(e: CustomEvent<MancalaGameComponent>) => {
                    e.detail.state = gameState
                }}"
            />
        ` : html`
            <new-game
                @init="${(e: CustomEvent<NewGameComponent>) => {
                    e.detail.state = gameState
                }}"
            />
        `}
        
    `)
}

// trigger an initial change so that the game component renders
gameState.change("init", "game")
