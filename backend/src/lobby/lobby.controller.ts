import { Body, Controller, Get, Param, Post, Delete, Put } from '@nestjs/common';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { LobbyService } from './lobby.service';
import { RequestLobbyDto } from './dto/requestLobby.dto';

@ApiTags('lobby')
@Controller('lobby')
export class LobbyController {
constructor(private readonly lobbyService: LobbyService) {}

    @ApiOperation({ summary: 'Create a new lobby' })
    @Post()
    async createLobby(@Body() lobbyDto: RequestLobbyDto) {
      return this.lobbyService.createLobby(lobbyDto);
    }

    @ApiOperation({ summary: 'Update lobby options'})
    @Put(':lobbyId')
    async updateLobby(@Param('lobbyId') lobbyId: string, @Body() lobbyDto: RequestLobbyDto) {
      return this.lobbyService.updateLobby(lobbyId, lobbyDto);
    }

    @ApiOperation({ summary: 'Get a Lobby by id' })
    @Get(':lobbyId')
    async getLobby(@Param('lobbyId') lobbyId: string) {
      return this.lobbyService.getLobby(lobbyId);
    }

    @ApiOperation({ summary: 'Get all Lobbies' })
    @Get()
    async getAllLobbies() {
      return this.lobbyService.getAllLobbies();
    }

    @ApiOperation({ summary: 'Delete a lobby by id' })
    @Delete(':lobbyId')
    async deleteLobby(@Param('lobbyId') lobbyId: string){
      return this.lobbyService.deleteLobby(lobbyId);
    }

    @ApiOperation({ summary: 'A user specified by user_id joins a lobby matching the lobby_id' })
    @Put(':lobbyId/:userId')
    async joinLobby(@Param('lobbyId') lobbyId: string,@Param('userId') userId: string){
      return this.lobbyService.joinLobby(lobbyId, userId);
    }

    @ApiOperation({ summary: 'A user specified by user_id leaves a lobby matching the lobby_id' })
    @Delete(':lobbyId/:userId')
    async leaveLobby(@Param('lobbyId') lobbyId: string, @Param('userId') userId: string) {
      return this.lobbyService.leaveLobby(lobbyId, userId);
    }

    @ApiOperation({ summary: 'A user specified by user_id joins a lobby matching the inviteCode' })
    @Put('/invite/:inviteCode/:userId')
    async joinLobbyByInviteCode(@Param('inviteCode') inviteCode: number, @Param('userId') userId: string){
      return this.lobbyService.joinLobbyByInviteCode(Number(inviteCode), userId);
    }
}