import { AnswerOption } from "./answerOption.interface";

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