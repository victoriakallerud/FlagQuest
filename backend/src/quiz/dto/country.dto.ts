import { ApiProperty } from "@nestjs/swagger";
import { IsEnum, IsNotEmpty, IsString } from "class-validator";
import { LevelEnum } from "src/enums/level.enum";
import { Country } from "src/interfaces/country.interface";

export class CountryDTO implements Country {

    @ApiProperty()
    @IsNotEmpty()
    @IsString()
    name: string;

    @ApiProperty()
    @IsNotEmpty()
    @IsEnum(LevelEnum)
    region: LevelEnum;
}