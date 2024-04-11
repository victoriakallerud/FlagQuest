import { Injectable, Logger } from '@nestjs/common';
import { DatabaseService } from 'src/database/database.service';
import { AnswerOption } from 'src/interfaces/answerOption.interface';
import { Question } from 'src/interfaces/question.interface';
import { Quiz } from 'src/interfaces/quiz.interface';

@Injectable()
export class QuizService {

    constructor(private readonly databaseService: DatabaseService) {}

    private readonly logger = new Logger(QuizService.name);


    async createCountries(countries: string[]) {
        this.databaseService.uploadQuestions(countries);
    }

    async generateQuestion(): Promise<Question> {
        this.logger.log('Generating question');
        const generatedAnswers = await this.databaseService.getAnwserOptions();

        const desc: string = generatedAnswers[0];
        const answerOptions: AnswerOption[] = generatedAnswers.map((answer, index) => {
            return {
                answer: answer,
                isCorrect: index === 0
            }
        
        });
        // Shuffle the answer options
        answerOptions.sort(() => Math.random() - 0.5);
        const question: Question = {
            description: desc,
            answerOptions: answerOptions
        }
        return question;
    }

    async generateNumberOfQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateNumberOfEuropeQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        // TODO add logic to only generate questions about Europe
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateNumberOfAsiaQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        // TODO add logic to only generate questions about Asia
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateNumberOfSouthAmericaQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        // TODO add logic to only generate questions about South America
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateNumberOfAfricaQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        // TODO add logic to only generate questions about Africa
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateNumberOfNorthamericaQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        // TODO add logic to only generate questions about North America
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateNumberOfOceaniaQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        // TODO add logic to only generate questions about Oceania
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateNumberOfCentralAmericaQuestion(numberOfQuestions: number): Promise<Question[]> {
        const questions: Question[] = [];
        // TODO add logic to only generate questions about Central America
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        return questions;
    }

    async generateQuiz(numberOfQuestions: number): Promise<Quiz> {
        const questions: Question[] = [];
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion());
        }
        const quiz: Quiz = {
            questions: questions
        }
        return quiz;
    }

}
