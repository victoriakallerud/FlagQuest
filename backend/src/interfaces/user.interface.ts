import { Score } from './score.interface';
import { Nationality } from './nationality.interface';

/**
 * 
 * @export
 * @interface User
 */
export interface User {
    /**
     * 
     * @type {string}
     * @memberof User
     */
    id?: string;
    /**
     * 
     * @type {string}
     * @memberof User
     */
    userName?: string;
    /**
     * 
     * @type {Array<string>}
     * @memberof User
     */
    friendUuidList?: Array<string>;
    /**
     * 
     * @type {Array<string>}
     * @memberof User
     */
    pendingFriendRequests?: Array<string>;
    /**
     * 
     * @type {Array<Score>}
     * @memberof User
     */
    highScores?: Array<Score>;
    /**
     * 
     * @type {string}
     * @memberof User
     */
    lastOnline?: string;
    /**
     * 
     * @type {string}
     * @memberof User
     */
    creationTime?: string;
    /**
     * 
     * @type {Nationality}
     * @memberof User
     */
    nationality?: Nationality;
}