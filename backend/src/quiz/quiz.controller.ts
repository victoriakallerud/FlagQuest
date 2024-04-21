import { Body, Controller, Get, Logger, Param, Post } from '@nestjs/common';
import { QuizService } from './quiz.service';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { CountryDTO } from './dto/country.dto';
import { LevelEnum } from 'src/enums/level.enum';

@ApiTags('quiz')
@Controller('quiz')
export class QuizController {

    constructor(private readonly quizService: QuizService) {}

    private readonly logger = new Logger(QuizController.name);


    @ApiOperation({summary: 'insertCountries'})
    @Post('questions')
    async createCountries(@Body('countries') countries: CountryDTO[]){
      return this.quizService.createCountries(countries);
    }
    
    @ApiOperation({summary: 'get a fixed number of random generated Question'})
    @Get('questions/:numberOfQuestions')
    async getNumberQuestion(@Param('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from a specific region in'})
    @Get('questions/:region/:numberOfQuestions')
    async getNumberEuropeQuestion(@Param('region') region: LevelEnum, @Param('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions, region);
    }

}
