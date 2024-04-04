import { Options } from "./options.interface";
import { LobbyStateEnum } from "src/enums/lobbyState.enum";

export interface Lobby {
    /**
     * 
     * @type {string}
     * @memberof Lobby
     */
    id?: string;
    /**
     * 
     * @type {string}
     * @memberof Lobby
     */
    name?: string;
    /**
     * 
     * @type {string}
     * @memberof Lobby
     */
    state?: LobbyStateEnum;
    /**
     * 
     * @type {string}
     * @memberof Lobby
     */
    creationTime?: string;
    /**
     * uuid of player
     * @type {string}
     * @memberof Lobby
     */
    admin?: string;
    /**
     * 
     * @type {Array<string>}
     * @memberof Lobby
     */
    players?: Array<string>;
    /**
     * 
     * @type {Options}
     * @memberof Lobby
     */
    options?: Options;
}