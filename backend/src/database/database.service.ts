import { Injectable } from '@nestjs/common';
import { Logger } from '@nestjs/common';
import * as admin from 'firebase-admin';
import { User } from '../interfaces/user.interface';
import { Lobby } from 'src/interfaces/lobby.interface';
import { Country } from 'src/interfaces/country.interface';
import { LobbyStateEnum } from 'src/enums/lobbyState.enum';
import { LevelEnum } from 'src/enums/level.enum';
import { GameModeEnum } from 'src/enums/gamemode.enum';
import { RequestUserScoresDTO } from 'src/user/dto/requestUserScores.dto';

@Injectable()
export class DatabaseService {
    private db: admin.firestore.Firestore;
    private readonly logger = new Logger(DatabaseService.name);

    constructor() {
        this.db = admin.firestore();
    }

    // --------------------- User Functions ---------------------

    async getUserById(userId: string): Promise<User> {
        try{
            let userDoc = await this.db.collection('user').doc(userId).get();
            if (!userDoc.exists) {
                throw new Error(`User does not exist`);
            } else {
                let user = userDoc.data() as User;
                user.id = userDoc.id;
                return user;
            }
        } catch (error) {
            this.logger.error('Error getting user', error);
            throw error;
        }
    }

    async getUserByFirebaseId(firebaseId: string): Promise<User> {
        try {
            let userDocs = await this.db.collection('user').where('firebaseId', '==', firebaseId).get();
            if (userDocs.empty) {
                this.logger.error(`User with firebase ID ${firebaseId} does not exist`);
                throw new Error(`User does not exist`);
            } else {
                let user = userDocs.docs[0].data() as User;
                user.id = userDocs.docs[0].id;
                return user;
            }
        } catch (error) {
            this.logger.error('Error getting user by firebase ID', error);
            throw error;
        }
    }

    async getUserIdByUserName(userName: string): Promise<string> {
        try {
            let userDocs = await this.db.collection('user').where('userName', '==', userName).get();
            if (userDocs.empty) {
                this.logger.error(`User with username ${userName} does not exist`);
                throw new Error(`User does not exist`);
            } else {
                return userDocs.docs[0].id;
            }
        } catch (error) {
            this.logger.error('Error getting user ID by username', error);
            throw error;
        }
    }

    async getBestScores(number: number, level: LevelEnum, gameMode: GameModeEnum): Promise<RequestUserScoresDTO[]> {
        //gets the top k scores based on level and mode and returns a list of RequestUserScoresDTO which contains the username and score
        try {
            let userDocs = await this.db.collection('user').get();
            let userScores: RequestUserScoresDTO[] = [];
            userDocs.forEach(userDoc => {
                let user = userDoc.data() as User;
                let userScore = user.highScores.find(score => score.level === level && score.gameMode === gameMode);
                if (userScore) {
                    userScores.push({userName: user.userName, score: userScore.value});
                }
            });
            userScores.sort((a, b) => b.score - a.score);
            return userScores.slice(0, number);
        } catch (error) {
            this.logger.error('Error getting best scores', error);
            throw error;
        }
    }

    async createUser(user: User): Promise<User> {
        try {
           await this.db.collection('user').doc(user.id).set(user);
           return this.getUserById(user.id);
        }
        catch (error) {
            this.logger.error('Error creating user', error);
            throw error;
        }
    }

    async updateUser(userId: string, user: User): Promise<User> {
        try {
            let userDoc = await this.db.collection('user').doc(userId).get();
            if (!userDoc.exists) {
                throw new Error(`User does not exist`);
            }
           await this.db.collection('user').doc(userId).update({
                userName: user.userName,
                nationality: user.nationality,
                lastOnline: user.lastOnline,
                highScores: user.highScores,
                friendUuidList: user.friendUuidList,
                pendingFriendRequests: user.pendingFriendRequests
           });
              return this.getUserById(userId);
        } catch (error) {
            this.logger.error('Error updating user', error);
            throw error;
        }
    }

    async updateUserScore(userId: string, level: LevelEnum, gameMode: GameModeEnum, score: number){
        // only updates the score if the new score is higher than the previous one
        if (this.userExistsById(userId)){
                try {
                let user = await this.getUserById(userId);
                let scoreIndex = user.highScores.findIndex(s => s.level === level && s.gameMode === gameMode);
                if (scoreIndex === -1) {
                  user.highScores.push({level: level, gameMode: gameMode, value: score});
                } else {
                    if (user.highScores[scoreIndex].value < score) {
                        user.highScores[scoreIndex].value = score;
                    }
                }
                await this.updateUser(userId, user);
            } catch (error) {
                this.logger.error('Error updating user score', error);
                throw error;
            }
        } else {
            throw new Error(`User with ID ${userId} does not exist`);
        }
    }

    async deleteUser(userId: string): Promise<void> {
        try {
            let userDoc = this.db.collection('user').doc(userId);
            let userSnapshot = await userDoc.get();
            if (!userSnapshot.exists) {
                throw new Error(`User does not exist`);
            }
            await userDoc.delete();
        } catch (error) {
            this.logger.error('Error deleting user', error);
            throw error;
        }
    }

    async removeFriend(user: User, friendId: string): Promise<void> {
        try {
            let friendIndex = user.friendUuidList.indexOf(friendId);
            if (friendIndex === -1) {
                throw new Error(`User with ID ${friendId} is not a friend of the user`);
            }
            user.friendUuidList.splice(friendIndex, 1);
            await this.updateUser(user.id, user);
        } catch (error) {
            this.logger.error('Error removing friend', error);
            throw error;
        }
    }  

    async userExistsById(userId: string): Promise<boolean> {
        try {
            let userDoc = await this.db.collection('user').doc(userId).get();
            return userDoc.exists;
        } catch (error) {
            this.logger.error('Error checking if user exists', error);
            throw error;
        }
    }

    async userExistsByFirebaseId(firebaseId: string): Promise<boolean> {
        try {
            let userDocs = await this.db.collection('user').where('firebaseId', '==', firebaseId).get();
            return !userDocs.empty;
        } catch (error) {
            this.logger.error('Error checking if user exists', error);
            throw error;
        }
    }

    async userExistsByUserName(userName: string): Promise<boolean> {
        try {
            let userDocs = await this.db.collection('user').where('userName', '==', userName).get();
            return !userDocs.empty;
        } catch (error) {
            this.logger.error('Error checking if user exists', error);
            throw error;
        }
    }

    // --------------------- Lobby Functions ---------------------

    async createLobby(lobby: Lobby): Promise<Lobby> {
        try {
            await this.db.collection('lobbies').doc(lobby.id).set(lobby);
            return this.getLobbyById(lobby.id);
        } catch (error) {
            this.logger.error('Error creating lobby', error);
            throw error;
        }
    }

    async getLobbyById(lobbyId: string): Promise<Lobby> {
        try {
            let lobbyDoc = await this.db.collection('lobbies').doc(lobbyId).get();
            if (!lobbyDoc.exists) {
                this.logger.error('Lobby does not exist');
            } else {
                let lobby = lobbyDoc.data();
                lobby.id = lobbyDoc.id; 
                return lobby;
            }
        } catch (error) {   
            this.logger.error('Error getting lobby', error);
            throw error;
        }
    }

    async getAllLobbies(): Promise<Lobby[]> {
        try {
            let lobbyDocs = await this.db.collection('lobbies').get();
            let lobbies: Object[] = lobbyDocs.docs.map(doc => { 
                let lobby = doc.data();
                lobby.id = doc.id;
                return lobby;
            });
            return lobbies;
        } catch (error){
            this.logger.error("Collection doesnt exist")
            throw error;
        }
    }

    async getLobbyByAdmin(adminId: string): Promise<Lobby> {
        //todo Since a user can only be in one lobby at a time, this should return a single lobby
        try {
            let lobbyDoc = await this.db.collection('lobbies').where('admin', '==', adminId).get();
            if (lobbyDoc.empty) {
                this.logger.log(`Lobby with the user ${adminId} as an admin does not exist`);
                return null;
            } else {
                let lobby = lobbyDoc.docs[0].data();
                lobby.id = lobbyDoc.docs[0].id;
                return lobby;
            }
        } catch (error) {
            this.logger.error('Error getting lobby', error);
            throw error;
        }
    }

    async getLobbyByPlayer(playerId: string): Promise<Lobby> {
        try {
            let lobbyDoc = await this.db.collection('lobbies').where('players', 'array-contains', playerId).get();
            if (lobbyDoc.empty) {
                this.logger.log(`Lobby with the player with id ${playerId} does not exist`);
                return null;
            } else {
                let lobby = lobbyDoc.docs[0].data();
                lobby.id = lobbyDoc.docs[0].id; 
                return lobby;
            }
        } catch (error) {
            this.logger.error('Error getting lobby', error);
            throw error;
        }
    }

    async getLobbyByInviteCode(inviteCode: number): Promise<Lobby> {
        try {
            this.logger.log(`Type of invite code: ${typeof inviteCode}`);
            let lobbyDoc = await this.db.collection('lobbies').where('inviteCode', '==', inviteCode).get();
            if (lobbyDoc.empty) {
                this.logger.error(`Lobby with invite code ${inviteCode} does not exist`);
                return null;
            } else {
                let lobby = lobbyDoc.docs[0].data();
                lobby.id = lobbyDoc.docs[0].id;
                return lobby;
            }
        } catch (error) {
            this.logger.error('Error getting lobby', error);
            throw error;
        }
    }

    async updateLobby(lobbyId: string, lobby: Lobby): Promise<Lobby> {
        try {
            let lobbyDoc = await this.db.collection('lobbies').doc(lobbyId).get();
            if (!lobbyDoc.exists) {
                throw new Error(`Lobby does not exist`);
            }
            await this.db.collection('lobbies').doc(lobbyId).update({
                name: lobby.name,
                admin: lobby.admin,
                state: lobby.state,
                players: lobby.players,
                options: lobby.options
            });
            return this.getLobbyById(lobbyId);
        } catch (error) {
            this.logger.error('Error updating lobby', error);
            throw error;
        }
    }

    async updateLobbyState(lobbyId: string, state: LobbyStateEnum):Promise<Lobby>{
        try {
            let lobbyDoc = await this.db.collection('lobbies').doc(lobbyId).get();
            if (!lobbyDoc.exists) {
                throw new Error(`Lobby does not exist`);
            }
            await this.db.collection('lobbies').doc(lobbyId).update({
                state: state
            });
            return this.getLobbyById(lobbyId);
        } catch (error) {
            this.logger.error('Error updating lobby state', error);
            throw error;
        }
    }
    
    async deleteLobby(lobbyId: string): Promise<void> {
        try {
            let lobbyDoc = this.db.collection('lobbies').doc(lobbyId);
            let lobbySnapshot = await lobbyDoc.get();
            if (!lobbySnapshot.exists) {
                throw new Error(`Lobby does not exist`);
            }
            await lobbyDoc.delete();
        } catch (error) {
            this.logger.error('Error deleting lobby', error);
            throw error;
        }
    }

    async lobbyExists(lobbyId: string): Promise<boolean> {
        try {
            let lobbyDoc = await this.db.collection('lobbies').doc(lobbyId).get();
            return lobbyDoc.exists;
        } catch (error) {
            this.logger.error('Error checking if lobby exists', error);
            throw error;
        }
    }

    async userInLobby(userId: string, lobbyId: string): Promise<boolean> {
        try {
            let lobbyDoc = await this.db.collection('lobbies').doc(lobbyId).get();
            if (!lobbyDoc.exists) {
                this.logger.error(`Lobby with ID ${lobbyId} does not exist`);
                return false;
            }
            let lobby = lobbyDoc.data();
            return lobby.players.includes(userId);
        } catch (error) {
            this.logger.error('Error checking if user is in lobby', error);
            throw error;
        }
    }

    // --------------------- Quizz Functions ---------------------

    async uploadQuestions(countries: Country[]) {
        try {
            countries.forEach(async (country, index) => {
                await this.db.collection('countries').doc(`${country.name}`).set(country);
            })
        } catch (error) {
            this.logger.error('Error uploading countries', error);
            throw error;
        }
    }

    async getAnwserOptions(): Promise<string[]> {
        let answerOptions: string[] = [];
        try{
            // Get only 4 random unique countries from the database
            let countries = await this.db.collection('countries').get();
            let countrySet: Set<string> = new Set();
            while (countrySet.size < 4) {
                let randomIndex = Math.floor(Math.random() * countries.size);
                countrySet.add(countries.docs[randomIndex].data().name);
            }
            answerOptions = Array.from(countrySet);

            return answerOptions;
        } catch (error) {
            this.logger.error('Error getting answer options', error);
            throw error;
        }
    }

    async getAnwserOptionsByRegion(region: string): Promise<string[]> {
        let answerOptions: string[] = [];
        try{
            // Get only 4 random unique countries from the database
            let countries = await this.db.collection('countries').where('region', '==', region).get();
            let countrySet: Set<string> = new Set();
            while (countrySet.size < 4) {
                let randomIndex = Math.floor(Math.random() * countries.size);
                countrySet.add(countries.docs[randomIndex].data().name);
            }
            answerOptions = Array.from(countrySet);

            return answerOptions;
        } catch (error) {
            this.logger.error('Error getting answer options', error);
            throw error;
        }
    }

    // --------------------- Helper Methods ---------------------

}