import { ApiProperty } from "@nestjs/swagger";
import { IsNotEmpty, IsUUID } from "class-validator";

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
    //todo add validation for this
    answerTime: number;

}