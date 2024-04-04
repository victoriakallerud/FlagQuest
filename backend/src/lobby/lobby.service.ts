import { HttpException, Injectable, Logger } from '@nestjs/common';
import { ILobbyService } from './lobbyService.interface';
import { Lobby } from 'src/interfaces/lobby.interface';
import { RequestLobbyDto } from './dto/requestLobby.dto';
import { LobbyStateEnum } from 'src/enums/lobbyState.enum';

@Injectable()
export class LobbyService implements ILobbyService{

    private readonly logger = new Logger(LobbyService.name);

    createLobby(lobbyDto: RequestLobbyDto): Lobby {
        let lobby: Lobby = {
            name: lobbyDto.name,
            admin: lobbyDto.admin,
            options: lobbyDto.options,
            state: LobbyStateEnum.WaitingForPlayers,
            creationTime: new Date().toISOString(),
            players: [lobbyDto.admin]
        }
        this.logger.log('Lobby created with id ' + lobby.id + ' by user ' + lobby.admin);
        return lobby;
    }

    updateLobby(lobbyId: string, lobbyDto: RequestLobbyDto): Lobby {
        // TODO:
        // Check if lobby is in the right state
        // Check if user is admin of the lobby
        // if all checks pass, update lobby in database
        // if not, throw exception
        throw new HttpException('Not implemented', 501); 
    }

    getLobby(lobbyId: string): Lobby {
        //TODO get lobby from Database by id and return
        throw new HttpException('Not implemented', 501);
        
    }

    deleteLobby(lobbyId: string): Lobby {
        // TODO:
        // Check if lobby is empty
        // Check if lobby is in the right state
        // if all checks pass, delete lobby from database
        // if not, throw exception
        throw new HttpException('Not implemented', 501);
    }

    joinLobby(lobbyId: string, userId: string): Lobby {
        // TODO:  
        // Check if user is already in the lobby
        // Check if lobby is full
        // Check if lobby is in the right state
        // if all checks pass, add user to lobby in database
        // if not, throw exception
        throw new HttpException('Not implemented', 501);
    }
    
    leaveLobby(lobbyId: string, userId: string): Lobby {
        //TODO Check if user is in the lobby 
        // remove user from lobby in database
        
        throw new HttpException('Not implemented', 501);
    }
    
}
