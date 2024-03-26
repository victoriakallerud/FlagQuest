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

import type { AnswerOption } from './AnswerOption';
import {
    AnswerOptionFromJSON,
    AnswerOptionFromJSONTyped,
    AnswerOptionToJSON,
} from './AnswerOption';

/**
 * 
 * @export
 * @interface Question
 */
export interface Question {
    /**
     * 
     * @type {string}
     * @memberof Question
     */
    description?: string;
    /**
     * 
     * @type {Array<AnswerOption>}
     * @memberof Question
     */
    answerOptions?: Array<AnswerOption>;
}

/**
 * Check if a given object implements the Question interface.
 */
export function instanceOfQuestion(value: object): boolean {
    return true;
}

export function QuestionFromJSON(json: any): Question {
    return QuestionFromJSONTyped(json, false);
}

export function QuestionFromJSONTyped(json: any, ignoreDiscriminator: boolean): Question {
    if (json == null) {
        return json;
    }
    return {
        
        'description': json['description'] == null ? undefined : json['description'],
        'answerOptions': json['answerOptions'] == null ? undefined : ((json['answerOptions'] as Array<any>).map(AnswerOptionFromJSON)),
    };
}

export function QuestionToJSON(value?: Question | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'description': value['description'],
        'answerOptions': value['answerOptions'] == null ? undefined : ((value['answerOptions'] as Array<any>).map(AnswerOptionToJSON)),
    };
}
