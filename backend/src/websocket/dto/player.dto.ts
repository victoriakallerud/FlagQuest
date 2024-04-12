import { IsNotEmpty} from "class-validator";
import { ApiProperty } from "@nestjs/swagger";
export class PlayerDto {
 
    @ApiProperty()
    @IsNotEmpty()
    name: string;

    @ApiProperty()
    @IsNotEmpty()
    score: number;
}