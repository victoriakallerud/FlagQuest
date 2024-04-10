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
