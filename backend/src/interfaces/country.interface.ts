import { LevelEnum } from "src/enums/level.enum";

export interface Country {
    /**
     * 
     * @type {string}
     * @memberof Country
     */
    name: string;
    /**
     * 
     * @type {LevelEnum}
     * @memberof Country
     */
    region: LevelEnum;
}