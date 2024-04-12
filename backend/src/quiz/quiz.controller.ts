import { Body, Controller, Get, Logger, Post } from '@nestjs/common';
import { QuizService } from './quiz.service';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { CountryDTO } from './dto/country.dto';
import { count } from 'console';

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

    @ApiOperation({summary: 'get a random generated Question'})
    @Get('question')
    async getQuestion(){
      return this.quizService.generateQuestion();
    }
    
    @ApiOperation({summary: 'get a fixed number of random generated Question'})
    @Get('questions')
    async getNumberQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from Continent Europe'})
    @Get('questions/Europe')
    async getNumberEuropeQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from Continent Asia'})
    @Get('questions/Asia')
    async getNumberAsiaQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from Continent Oceania'})
    @Get('questions/Oceania')
    async getNumberOceaniaQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from Continent South America'})
    @Get('questions/SouthAmerica')
    async getNumberSouthAmericaQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from Continent Central America'})
    @Get('questions/CentralAmerica')
    async getNumberCentralAmericaQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from Continent North America'})
    @Get('questions/NorthAmerica')
    async getNumberNorthAmericaQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

    @ApiOperation({summary: 'get a fixed number of random generated Question from Continent Africa'})
    @Get('questions/Africa')
    async getNumberAfricaQuestion(@Body('numberOfQuestions') numberOfQuestions: number){
      return this.quizService.generateNumberOfQuestion(numberOfQuestions);
    }

}
