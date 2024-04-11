import { Injectable, Logger } from '@nestjs/common';
import { DatabaseService } from 'src/database/database.service';
import { Game } from 'src/interfaces/game.interface';
import { Lobby } from 'src/interfaces/lobby.interface';
import { Player } from 'src/interfaces/player.interface';
import { User } from 'src/interfaces/user.interface';
import { Socket } from 'socket.io';
import { PlayerDto } from 'src/websocket/dto/player.dto';

@Injectable()
export class GameService {

    private runningGames = new Map<string, Game>();

    private readonly logger = new Logger(GameService.name);


    constructor(private readonly databaseService: DatabaseService) {}

    // --------------------- Game Functions ---------------------

    async createGame(lobby: Lobby) {
        this.logger.log(`Lobby ${lobby.id} is starting a game`);

        // Load details of the players in the lobby from the database

        const users: User[] = [];
        for(const player of lobby.players) {
            const p = await this.databaseService.getUserById(player);
            if(p) {
                users.push(p);
            }
        }

        const players: Player[] = users.map((player) => {
            return {
                id: player.id,
                name: player.userName,
                currentScore: 0
            };
        });

        const newGame: Game = {
            lobbyId: lobby.id,
            players: players,
            creationTime: new Date().toISOString(),
            numberOfRounds: lobby.options.numberOfQuestions,
            submittedAnswersForCurrentRound: 0,
            currentRound: 1
        };
        this.runningGames.set(lobby.id, newGame);
     }

    getGameById(gameId: string): Game {
        return this.runningGames.get(gameId);
     }

    increaseNumberOfSubmittedAnswers(gameId: string) {
        const game = this.getGameById(gameId);
        game.submittedAnswersForCurrentRound++;
        this.updateGame(gameId, game);  
    }

    changeCurrentRound(gameId: string) {
        const game = this.getGameById(gameId);
        game.currentRound++;
        this.updateGame(gameId, game);
    }

    updateGame(gameId: string, game: Game): Game {
        this.runningGames.set(gameId, game);
        return game;
    }
    checkIfAllPlayersSubmittedAnswers(gameId: string): boolean {
        const game = this.getGameById(gameId);
        return game.submittedAnswersForCurrentRound === game.players.length;
    }

    gameOver(gameId: string): boolean {
        const game = this.getGameById(gameId);
        return game.currentRound > game.numberOfRounds;
    }



    // --------------------- Player Functions ---------------------
    
    async getPlayerName(playerId: string): Promise<string> {
        const user = await this.databaseService.getUserById(playerId);
        return user.userName;
    }

    async getPlayerById(playerId: string, gameId: string): Promise<Player> {
        const game = this.getGameById(gameId);
        const player = game.players.find((player) => player.id === playerId);
        return player;
    }

    updatePlayerScore(client: Socket, playerId: string, lobbyId: string, isAnswerRight: boolean, answerTime: number) {
        // get Player by Id
        // add points to player
        const player = this.getPlayerById(playerId, lobbyId);
        if(player) {
            //handle score logic
        } 
        this.logger.log(`Client ${client.id} Score was updated for player ${playerId} in lobby ${lobbyId}`);
    }   

    getListOfAllPlayers(gameId: string): PlayerDto[] {
        const game = this.getGameById(gameId);
        const playerDtos: PlayerDto[] = game.players.map((player) => {
            return {
                id: player.id,
                name: player.name,
                score: player.currentScore
            };
        });
        return playerDtos;
    }
}
