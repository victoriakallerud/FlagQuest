import { Injectable, Logger } from '@nestjs/common';
import { DatabaseService } from 'src/database/database.service';
import { Game } from 'src/interfaces/game.interface';
import { Lobby } from 'src/interfaces/lobby.interface';
import { Player } from 'src/interfaces/player.interface';
import { User } from 'src/interfaces/user.interface';

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

}
