import { GameModeEnum } from "src/enums/gamemode.enum";
import { LevelEnum } from "src/enums/level.enum";

export interface Options {
    /**
     * 
     * @type {number}
     * @memberof Options
     */
    maxNumOfPlayers?: number;
    /**
     * 
     * @type {number}
     * @memberof Options
     */
    numberOfQuestions?: number;
    /**
     * 
     * @type {boolean}
     * @memberof Options
     */
    showAnswers?: boolean;
    /**
     * 
     * @type {string}
     * @memberof Options
     */
    gameMode?: GameModeEnum;
    /**
     * 
     * @type {string}
     * @memberof Options
     */
    level?: LevelEnum;
    /**
     * 
     * @type {boolean}
     * @memberof Options
     */
    isPrivate?: boolean;
}