import { Body, Controller, Get, Logger, Post } from '@nestjs/common';
import { QuizService } from './quiz.service';
import { ApiOperation, ApiTags } from '@nestjs/swagger';

@ApiTags('quiz')
@Controller('quiz')
export class QuizController {

    constructor(private readonly quizService: QuizService) {}

    private readonly logger = new Logger(QuizController.name);


    @ApiOperation({summary: 'insertCountries'})
    @Post('questions')
    async createCountries(@Body('countries') countryNames: string[]){
      return this.quizService.createCountries(countryNames);
    }

    @ApiOperation({summary: 'insertCountries'})
    @Get('questions')
    async getQuestion(){
      return this.quizService.generateQuestion();
    }

}
