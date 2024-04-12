import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty, IsNumber, IsPositive, IsUUID } from "class-validator";

export class submittedAnswerDto {

    @ApiProperty()
    @IsNotEmpty()
    @IsUUID()
    lobbyId: string;

    @ApiProperty()
    @IsNotEmpty()
    @IsUUID()
    playerId: string;

    @ApiProperty()
    @IsNotEmpty()
    isAnswerRight: boolean;

    @ApiProperty()
    @IsNotEmpty()
    @IsNumber()
    @IsPositive()
    //todo add validation for this
    answerTime: number;

}