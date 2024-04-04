import { HttpException, Injectable, Logger } from '@nestjs/common';
import { ILobbyService } from './lobbyService.interface';
import { Lobby } from 'src/interfaces/lobby.interface';
import { v4 as uuidv4 } from 'uuid';
import { RequestLobbyDto } from './dto/requestLobby.dto';
import { DatabaseService } from '../database/database.service';
import { LobbyStateEnum } from 'src/enums/lobbyState.enum';

@Injectable()
export class LobbyService implements ILobbyService{

    private readonly logger = new Logger(LobbyService.name);

    constructor(private readonly databaseService: DatabaseService) {
    }

    async createLobby(lobbyDto: RequestLobbyDto): Promise<Lobby> {
        // check if a lobby with the same admin exists
        let lobbyOfAdmin = await this.databaseService.getLobbyByAdmin(lobbyDto.admin);
        if (lobbyOfAdmin !== null) {
            this.logger.error('User is already admin of a lobby');
            throw new HttpException('User is already admin of a lobby', 403);
        }
        // if no lobby with the same admin exists, create a new lobby
        let uuid = uuidv4();
        let lobby: Lobby = {
            id: uuid,
            name: lobbyDto.name,
            admin: lobbyDto.admin,
            options: lobbyDto.options,
            state: LobbyStateEnum.WaitingForPlayers,
            creationTime: new Date().toISOString(),
            players: [lobbyDto.admin]
        }
        // create the lobby in the database
        try {
            let createdLobby = await this.databaseService.createLobby(lobby);
            this.logger.log('Lobby created with id ' + createdLobby.id + ' by user ' + createdLobby.admin);
            return createdLobby;
        } catch (error) {
            if (error.message === 'User is already admin of a lobby') {
                this.logger.error('User is already admin of a lobby');
                throw new HttpException('User is already admin of a lobby', 403);
            } else {
            this.logger.error('Error creating lobby', error);
            throw new HttpException('Error creating lobby', 500);
            }
        }
    }

    async getLobby(lobbyId: string): Promise<Lobby> {
        // get the lobby from the database
        try {
            let lobby = await this.databaseService.getLobbyById(lobbyId);
            this.logger.log('Lobby with id ' + lobby.id + ' retrieved');
            return lobby;
        } catch (error) {
            if (error.message === 'Lobby does not exist') {
                this.logger.error('Lobby does not exist');
                throw new HttpException('Lobby does not exist', 404);
            } else {
                this.logger.error('Error getting lobby', error);
                throw new HttpException('Error getting lobby', 500);
            }
        }
    }

    async updateLobby(lobbyId: string, lobbyDto: RequestLobbyDto): Promise<Lobby> {
        //check if lobby exists
        try {
            let lobby = await this.databaseService.getLobbyById(lobbyId);
        // check if lobby is in the right state
            if (lobby.state !== LobbyStateEnum.WaitingForPlayers) {
                this.logger.error('Lobby is not in the right state');
                throw new Error('Lobby is not in the right state');
            }
            // update the lobby in the database
            lobby.name = lobbyDto.name;
            lobby.options = lobbyDto.options;
            lobby.admin = lobbyDto.admin;
            let updatedLobby = await this.databaseService.updateLobby(lobbyId, lobby);
            this.logger.log('Lobby with id ' + updatedLobby.id + ' updated by user ' + updatedLobby.admin);
            return updatedLobby;
        } catch (error) {
            if (error.message === 'Lobby does not exist') {
                this.logger.error('Lobby does not exist');
                throw new HttpException('Lobby does not exist', 404);
            } else if (error.message === 'Lobby is not in the right state') {
                this.logger.error('Lobby is not in the right state');
                throw new HttpException('Lobby is not in the right state', 403);
            } else {
                this.logger.error('Error updating lobby', error);
                throw new HttpException('Error updating lobby', 500);
            }
    }
}

    async deleteLobby(lobbyId: string): Promise<void> {
        try {
            // check if lobby exists
            let lobby = await this.databaseService.getLobbyById(lobbyId);
            // check if lobby is empty
            if (lobby.players.length > 0) {
                this.logger.error('Lobby is not empty');
                throw new Error('Lobby is not empty');
            }
            // check if lobby is in the right state
            if (lobby.state !== LobbyStateEnum.WaitingForPlayers) {
                this.logger.error('Lobby is not in the right state');
                throw new Error('Lobby is not in the right state');
            }
            // delete the lobby from the database
            await this.databaseService.deleteLobby(lobbyId);
            this.logger.log('Lobby with id ' + lobbyId + ' was deleted');
        } catch (error) {
            if (error.message === 'Lobby does not exist') {
                this.logger.error('Lobby does not exist');
                throw new HttpException('Lobby does not exist', 404);
            } else if (error.message === 'Lobby is not empty') {
                this.logger.error('Lobby is not empty');
                throw new HttpException('Lobby is not empty', 403);
            } else if (error.message === 'Lobby is not in the right state') {
                this.logger.error('Lobby is not in the right state');
                throw new HttpException('Lobby is not in the right state', 403);
            } else {
                this.logger.error('Error deleting lobby', error);
                throw new HttpException('Error deleting lobby', 500);
            }
        }
    }

    async joinLobby(lobbyId: string, userId: string): Promise<Lobby> {
        try {
            //checks if user exists
            await this.databaseService.getUserById(userId);

            //checks if user already is in an other lobby
            let lobbyOfPlayer = await this.databaseService.getLobbyByPlayer(userId);
            if (lobbyOfPlayer !== null) {
                this.logger.error('User is already in a lobby');
                throw new HttpException('User is already in a lobby', 403);
            }
            //checks if lobby exists
            let lobby = await this.databaseService.getLobbyById(lobbyId);
            // checks if Number of players isn't exceeded
            if (lobby.players.length >= lobby.options.maxNumOfPlayers) {
                this.logger.error('Lobby is full');
                throw new Error('Lobby is full');
            }
            //checks if lobby is in the right state
            if (lobby.state !== LobbyStateEnum.WaitingForPlayers) {
                this.logger.error('Lobby is not in the right state');
                throw new Error('Lobby is not in the right state');
            }
            //adds user to the lobby
            lobby.players.push(userId);
            let updatedLobby = await this.databaseService.updateLobby(lobbyId, lobby);
            this.logger.log('User ' + userId + ' joined lobby with id ' + updatedLobby.id);
            return updatedLobby;
        } catch (error) {
            if (error.message === 'Lobby does not exist') {
                this.logger.error('Lobby does not exist');
                throw new HttpException('Lobby does not exist', 404);
            } else if (error.message === 'User does not exist'){
                this.logger.error('User does not exist');
                throw new HttpException('User does not exist', 404);
            } else if (error.message === 'User is already in a  lobby'){
                this.logger.error('User is already in a lobby');
                throw new HttpException('User is already in a lobby', 403);
            } else if (error.message === 'Lobby is full') {
                this.logger.error('Lobby is full');
                throw new HttpException('Lobby is full', 403);
            } else if (error.message === 'Lobby is not in the right state') {
                this.logger.error('Lobby is not in the right state');
                throw new HttpException('Lobby is not in the right state', 403);
            } else {
                this.logger.error('Error joining lobby', error);
                throw new HttpException('Error joining lobby', 500);
            }
        }
    }
    
    async leaveLobby(lobbyId: string, userId: string): Promise<void> {
        try {
            //checks if user exists
            await this.databaseService.getUserById(userId);
            //checks if lobby exists
            let lobby = await this.databaseService.getLobbyById(lobbyId);
            // checks if User is in the lobby
            if (!lobby.players.includes(userId)) {
                this.logger.error('User is not in the lobby');
                throw new Error('User is not in the lobby');
            }
            //removes user from the lobby
            lobby.players = lobby.players.filter(player => player !== userId);
            await this.databaseService.updateLobby(lobbyId, lobby);
            this.logger.log('User ' + userId + ' left the lobby with id ' + lobbyId);
        } catch (error) {
            if (error.message === 'Lobby does not exist') {
                this.logger.error('Lobby does not exist');
                throw new HttpException('Lobby does not exist', 404);
            } else if (error.message === 'User does not exist') {
                this.logger.error('User does not exist');
                throw new HttpException('User does not exist', 404);
            } else if (error.message === 'User is not in the lobby') {
                this.logger.error('User is not in the lobby');
                throw new HttpException('User is not in the lobby', 403);
            } else {
                this.logger.error('Error leaving lobby', error);
                throw new HttpException('Error leaving lobby', 500);
            }
        }
    }
    
}
