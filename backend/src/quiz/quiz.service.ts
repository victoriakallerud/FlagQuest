import { Injectable, Logger } from '@nestjs/common';
import { DatabaseService } from 'src/database/database.service';
import { LevelEnum } from 'src/enums/level.enum';
import { AnswerOption } from 'src/interfaces/answerOption.interface';
import { Country } from 'src/interfaces/country.interface';
import { Question } from 'src/interfaces/question.interface';
import { Quiz } from 'src/interfaces/quiz.interface';

@Injectable()
export class QuizService {

    constructor(private readonly databaseService: DatabaseService) {}

    private readonly logger = new Logger(QuizService.name);


    async createCountries(countries: Country[]) {
        this.databaseService.uploadQuestions(countries);
    }

    async generateQuestion(region?: LevelEnum): Promise<Question> {
        let generatedAnswers: string[] = [];
        if (region) {
            generatedAnswers = await this.databaseService.getAnwserOptionsByRegion(region);
        } else {
        generatedAnswers = await this.databaseService.getAnwserOptions();
        }

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

    async generateNumberOfQuestion(numberOfQuestions: number, region?: LevelEnum): Promise<Question[]> {
        this.logger.log(`Generated Quiz of ${region} region with ${numberOfQuestions} of questions`);
        const questions: Question[] = [];
        for (let i = 0; i < numberOfQuestions; i++) {
            questions.push(await this.generateQuestion(region));
        }
        return questions;
    }

    async generateQuiz(numberOfQuestions: number): Promise<Quiz> {
        this.logger.log('Generated Quiz');
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
