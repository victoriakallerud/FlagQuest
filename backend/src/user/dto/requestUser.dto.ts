import { ApiProperty } from '@nestjs/swagger';
import { IsEnum, IsNotEmpty, IsString } from 'class-validator';
import { Nationality, User } from 'models';

export class RequestUserDTO implements User {

    @ApiProperty()
    @IsNotEmpty()
    @IsString()
    userName: string;

    @ApiProperty()
    @IsNotEmpty()
    @IsEnum(Nationality)
    nationality: Nationality;
}