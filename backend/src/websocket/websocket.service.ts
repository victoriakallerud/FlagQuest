import { Injectable } from '@nestjs/common';
import { Logger } from '@nestjs/common';
import { Socket } from 'socket.io';
import { JoinLobbyDTO } from './dto/joinLobby.dto';
import { DatabaseService } from 'src/database/database.service';
import { StatusMsgDto } from './dto/statusMsg.dto';
import { Lobby } from 'src/interfaces/lobby.interface';
import { LobbyStateEnum } from 'src/enums/lobbyState.enum';

@Injectable()
export class WebsocketService {
    
    private readonly logger = new Logger(WebsocketService.name);

    constructor(private readonly databaseService: DatabaseService) {}

    // --------------------- Websocket Functions ---------------------

    async handleConnection(client: Socket) {
        this.logger.log(`Client connected: ${client.id}`);
    }

    async handleDisconnect(client: Socket) {
        this.logger.log(`Client disconnected: ${client.id}`);
    }

    async handleJoinLobby(client: Socket, joinLobbyDto: JoinLobbyDTO): Promise<StatusMsgDto> {
        this.logger.log(`Client ${client.id} joining lobby ${joinLobbyDto.lobbyId}`);
        if(!await this.databaseService.lobbyExists(joinLobbyDto.lobbyId)) { 
            this.logger.error(`Lobby ${joinLobbyDto.lobbyId} does not exist`);
            return {status: 'ERROR', message: `Lobby ${joinLobbyDto.lobbyId} does not exist`};
        }
        else if(!await this.databaseService.userExists(joinLobbyDto.userId)) {
            this.logger.error(`User ${joinLobbyDto.userId} does not exist`);
            return {status: 'ERROR', message: `User ${joinLobbyDto.userId} does not exist`};
        }
        else if(!await this.databaseService.userInLobby(joinLobbyDto.userId, joinLobbyDto.lobbyId)) {
            this.logger.error(`User ${joinLobbyDto.userId} is not in lobby ${joinLobbyDto.lobbyId}`);
            return {status: 'ERROR', message: `User ${joinLobbyDto.userId} is not in lobby ${joinLobbyDto.lobbyId}`};
        } else {
            // WebSocket client joins the lobby room
            client.join(joinLobbyDto.lobbyId);
            this.logger.log(`Client ${client.id} with user id ${joinLobbyDto.userId} joined lobby ${joinLobbyDto.lobbyId}`);
            return {status: 'SUCCESS', message: `Client ${client.id} with user id ${joinLobbyDto.userId} joined lobby ${joinLobbyDto.lobbyId}`};
        }
    }

    async handleLeaveLobby(client: Socket, joinLobbyDto: JoinLobbyDTO) {
        this.logger.log(`Client ${client.id} leaving lobby ${joinLobbyDto.lobbyId}`);
        client.leave(joinLobbyDto.lobbyId);
        this.logger.log(`Client ${client.id} left lobby ${joinLobbyDto.lobbyId}`);
        return {status: 'SUCCESS', message: `Client ${client.id} left lobby ${joinLobbyDto.lobbyId}`};
    }

    async handleStartGame(client: Socket, userId: string, lobbyId: string): Promise<Lobby> {
        this.logger.log(`Client ${client.id} starting game`);
        const lobbyToBeStarted: Lobby = await this.databaseService.getLobbyById(lobbyId);
        if(lobbyToBeStarted.admin === userId){
            lobbyToBeStarted.state = LobbyStateEnum.InGame;
            await this.databaseService.updateLobby(lobbyId, lobbyToBeStarted);
        }

        return lobbyToBeStarted;
    }
}
