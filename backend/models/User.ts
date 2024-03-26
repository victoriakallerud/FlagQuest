/* tslint:disable */
/* eslint-disable */
/**
 * FlagQuest Backend
 * The API for the FlagQuest guessing game
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import type { Nationality } from './Nationality';
import {
    NationalityFromJSON,
    NationalityFromJSONTyped,
    NationalityToJSON,
} from './Nationality';
import type { Score } from './Score';
import {
    ScoreFromJSON,
    ScoreFromJSONTyped,
    ScoreToJSON,
} from './Score';

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

/**
 * Check if a given object implements the User interface.
 */
export function instanceOfUser(value: object): boolean {
    return true;
}

export function UserFromJSON(json: any): User {
    return UserFromJSONTyped(json, false);
}

export function UserFromJSONTyped(json: any, ignoreDiscriminator: boolean): User {
    if (json == null) {
        return json;
    }
    return {
        
        'id': json['id'] == null ? undefined : json['id'],
        'userName': json['userName'] == null ? undefined : json['userName'],
        'friendUuidList': json['friendUuidList'] == null ? undefined : json['friendUuidList'],
        'highScores': json['highScores'] == null ? undefined : ((json['highScores'] as Array<any>).map(ScoreFromJSON)),
        'lastOnline': json['lastOnline'] == null ? undefined : json['lastOnline'],
        'creationTime': json['creationTime'] == null ? undefined : json['creationTime'],
        'nationality': json['nationality'] == null ? undefined : NationalityFromJSON(json['nationality']),
    };
}

export function UserToJSON(value?: User | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'id': value['id'],
        'userName': value['userName'],
        'friendUuidList': value['friendUuidList'],
        'highScores': value['highScores'] == null ? undefined : ((value['highScores'] as Array<any>).map(ScoreToJSON)),
        'lastOnline': value['lastOnline'],
        'creationTime': value['creationTime'],
        'nationality': NationalityToJSON(value['nationality']),
    };
}
