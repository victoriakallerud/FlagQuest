import { Player } from "./player.interface";

export interface Game {
    /**
     * 
     * @type {string}
     * @memberof Game
     */
    lobbyId: string;
    /**
     * 
     * @type {Array<Player>}
     * @memberof Game
     */
    players: Array<Player>;
    /**
     * 
     * @type {string}
     * @memberof Game
     */
    creationTime: string;
    /**
     * 
     * @type {number}
     * @memberof Game
     */
    numberOfRounds: number;
    /**
     * 
     * @type {number}
     * @memberof Game
     */
    submittedAnswersForCurrentRound: number;
    /**
     * 
     * @type {number}
     * @memberof Game
     */
    currentRound: number;

}

