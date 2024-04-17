import { ApiProperty } from "@nestjs/swagger";
import { IsBoolean, IsNotEmpty, IsNumber, IsPositive, IsUUID } from "class-validator";

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
    @IsBoolean()
    @IsNotEmpty()
    isAnswerRight: boolean;

    @ApiProperty()
    @IsNotEmpty()
    @IsNumber()
    @IsPositive()
    //todo add validation for this
    answerTime: number;

}