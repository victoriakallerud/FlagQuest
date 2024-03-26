import { Body, Controller, Get, Param, Post, Delete, Put } from '@nestjs/common';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { LobbyService } from './lobby.service';
import { RequestLobbyDto } from './dto/requestLobby.dto';
import { Lobby } from 'src/interfaces/lobby.interface';
import { RequestUserDTO } from 'src/user/dto/requestUser.dto';

@ApiTags('lobby')
@Controller('lobby')
export class LobbyController {
constructor(private readonly lobbyService: LobbyService) {}

    @ApiOperation({ summary: 'Create a new lobby' })
    @Post()
    createLobby(@Body() lobbyDto: RequestLobbyDto): Lobby {
      return this.lobbyService.createLobby(lobbyDto);
    }

    @ApiOperation({ summary: 'Update lobby options'})
    @Put(':lobbyId')
    updateLobby(@Param('lobbyId') lobbyId: string, @Body() lobbyDto: RequestLobbyDto): Lobby {
      return this.lobbyService.updateLobby(lobbyId, lobbyDto);
    }

    @ApiOperation({ summary: 'Get a Lobby by id' })
    @Get(':lobbyId')
    getLobby(@Param('lobbyId') lobbyId: string): Lobby {
      return this.lobbyService.getLobby(lobbyId);
    }

    @ApiOperation({ summary: 'Delete a lobby by id' })
    @Delete(':lobbyId')
    deleteLobby(@Param('lobbyId') lobbyId: string): Lobby{
      return this.lobbyService.deleteLobby(lobbyId);
    }

    @ApiOperation({ summary: 'A user specified by user_id joins a lobby matching the lobby_id' })
    @Put(':lobbyId/:userId')
    joinLobby(@Param('lobbyId') lobbyId: string,@Param('userId') userId: string): Lobby{
      return this.lobbyService.joinLobby(lobbyId, userId);
    }

    @ApiOperation({ summary: 'A user specified by user_id leaves a lobby matching the lobby_id' })
    @Delete(':lobbyId/:userId')
    leaveLobby(@Param('lobbyId') lobbyId: string, @Param('userId') userId: string): Lobby{
      return this.lobbyService.leaveLobby(lobbyId, userId);
    }


}