import { GameModeEnum } from "src/enums/gamemode.enum";
import { LevelEnum } from "src/enums/level.enum";

/**
 * 
 * @export
 * @interface Score
 */
export interface Score {
    /**
     * 
     * @type {string}
     * @memberof Score
     */
    gameMode?: GameModeEnum;

    /**
     * 
     * @type {string}
     * @memberof Score
     */
    level?: LevelEnum;
    
    /**
     * 
     * @type {number}
     * @memberof Score
     */
    value?: number;
}