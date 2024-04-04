import { ApiProperty } from '@nestjs/swagger';
import { IsEnum, IsNotEmpty, IsString } from 'class-validator';
import { Lobby } from 'src/interfaces/lobby.interface';
import { RequestOptionsDTO } from './requestOptions.dto';

export class RequestLobbyDto implements Lobby {

    @ApiProperty()
    @IsNotEmpty()
    @IsString()
    name: string

    @ApiProperty()
    @IsNotEmpty()
    admin: string;

    @ApiProperty()
    @IsNotEmpty()
    options: RequestOptionsDTO;


}