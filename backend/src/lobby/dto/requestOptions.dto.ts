import { ApiProperty } from '@nestjs/swagger';
import { IsBoolean, IsEnum, IsNotEmpty, IsNumber, IsString } from 'class-validator';
import { Options } from 'src/interfaces/options.interface';
import { GameModeEnum } from 'src/enums/gamemode.enum';
import { LevelEnum } from 'src/enums/level.enum';

export class RequestOptionsDTO implements Options {

    @ApiProperty()
    @IsNotEmpty()
    @IsNumber()
    maxNumOfPlayers: number;

    @ApiProperty()
    @IsNotEmpty()
    @IsNumber()
    numberOfQuestions: number;

    @ApiProperty()
    @IsNotEmpty()
    @IsBoolean()
    showAnswers: boolean;

    @ApiProperty()
    @IsNotEmpty()
    @IsEnum(GameModeEnum)
    gameMode: GameModeEnum;

    @ApiProperty()
    @IsNotEmpty()
    @IsEnum(LevelEnum)
    level: LevelEnum; 

    @ApiProperty()
    @IsNotEmpty()
    @IsBoolean()
    isPrivate: boolean;

}