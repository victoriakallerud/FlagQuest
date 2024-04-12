import { Injectable, Logger } from '@nestjs/common';
import { DatabaseService } from 'src/database/database.service';
import { Game } from 'src/interfaces/game.interface';
import { Lobby } from 'src/interfaces/lobby.interface';
import { Player } from 'src/interfaces/player.interface';
import { User } from 'src/interfaces/user.interface';
import { Socket } from 'socket.io';
import { PlayerDto } from 'src/websocket/dto/player.dto';
import { LobbyStateEnum } from 'src/enums/lobbyState.enum';

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

        const players: Player[] = users.map((user) => {
            return {
                id: user.id,
                name: user.userName,
                currentScore: 0,
                hasAnsweredCurrentRound: false
            };
        });

        const newGame: Game = {
            lobbyId: lobby.id,
            players: players,
            creationTime: new Date().toISOString(),
            numberOfRounds: lobby.options.numberOfQuestions,
            currentRound: 1
        };
        this.runningGames.set(lobby.id, newGame);
        this.logger.log(`Game ${lobby.id} was created`);
        this.logger.log(`Currently running games: ${this.runningGames.size}`);
     }

    getGameById(gameId: string): Game {
        const game: Game = this.runningGames.get(gameId);
        if (game) {
            return game;
        } else {
            throw new Error(`Game with id: ${gameId} was not found`);
        }
     }

    setPlayerHasAnswered(gameId: string, playerId: string) {
        const game: Game = this.getGameById(gameId);
        const player: Player = this.getPlayerById(playerId, gameId);
        player.hasAnsweredCurrentRound = true;
        this.updateGame(gameId, game); 
        this.logger.log(`Player ${playerId} has answered round ${game.currentRound} in game ${gameId}`);
    }

    hasPlayerAnswered(gameId: string, playerId: string): boolean {
        const game: Game = this.getGameById(gameId);
        const player: Player = this.getPlayerById(playerId, gameId);
        return player.hasAnsweredCurrentRound;
    }

    changeCurrentRound(gameId: string) {
        const game: Game = this.getGameById(gameId);
        game.currentRound++;
        game.players.forEach((player) => {
            player.hasAnsweredCurrentRound = false;
        });

        this.updateGame(gameId, game);
        this.logger.log(`Current round for game ${gameId} was increased to ${game.currentRound}`);
    }

    updateGame(gameId: string, updateGame: Game): Game {
        const game: Game = this.getGameById(gameId);
        this.runningGames.set(gameId, updateGame);
        return updateGame;
    }

    getSubmittedAnswerCount(gameId: string): number {
        const game: Game = this.getGameById(gameId);
        let submitedAnswersForCurrentRound = 0;
        game.players.forEach((player) => {
            if (player.hasAnsweredCurrentRound){
                submitedAnswersForCurrentRound++;
            }
        });
        return submitedAnswersForCurrentRound;
    }
    
    checkIfAllPlayersSubmittedAnswers(gameId: string): boolean {
        const game: Game = this.getGameById(gameId);
        return this.getSubmittedAnswerCount(gameId) === game.players.length;
    }

    isGameOver(gameId: string): boolean {
        const game: Game = this.getGameById(gameId);
        return (game.currentRound === game.numberOfRounds) && (this.checkIfAllPlayersSubmittedAnswers(gameId));
    }



    // --------------------- Player Functions ---------------------
    

     getPlayerById(playerId: string, gameId: string): Player {
        const game: Game = this.getGameById(gameId);
        const player: Player = game.players.find((player) => player.id === playerId);
        if (player){
            return player;
        } else {
            throw new Error('Player was not found');
        }
    }

    
    async updatePlayerScore(client: Socket, playerId: string, lobbyId: string, isAnswerRight: boolean, answerTimeMs: number, ) {
        // get Player by Id
        // add points to player
        const questionTimeMs = 10000;
        const maxPoints = 500;
        const timeRatio = answerTimeMs / questionTimeMs;
        const scoreRatio = 1 - (timeRatio / 2);
        const rawScore = maxPoints * scoreRatio;
        const roundedScore = Math.round(rawScore);

        const player: Player = this.getPlayerById(playerId, lobbyId);
        if (player) {
            if (isAnswerRight){
                player.currentScore += roundedScore;
            }   
        } 
            this.logger.log(`Client ${client.id} Score was updated for player ${playerId} in lobby ${lobbyId}`);
    }

    getListOfAllPlayers(gameId: string): PlayerDto[] {
        const game: Game = this.getGameById(gameId);
        const playerDtos: PlayerDto[] = game.players.map((player) => {
            return {
                id: player.id,
                name: player.name,
                score: player.currentScore
            };
        });
        return playerDtos;
    }

    async endGame(gameId: string): Promise<Lobby> {
        this.runningGames.delete(gameId);
        this.logger.log(`Game ${gameId} was deleted`);
        this.logger.log(`Currently running games: ${this.runningGames.size}`);
        return await this.databaseService.updateLobbyState(gameId, LobbyStateEnum.WaitingForPlayers);
    }
}
