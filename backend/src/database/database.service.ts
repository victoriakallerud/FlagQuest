import { Injectable } from '@nestjs/common';
import { Logger } from '@nestjs/common';
import * as admin from 'firebase-admin';
import { User } from '../interfaces/user.interface';
import { Lobby } from 'src/interfaces/lobby.interface';
import { LobbyService } from 'src/lobby/lobby.service';

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

    async userExists(userId: string): Promise<boolean> {
        try {
            let userDoc = await this.db.collection('user').doc(userId).get();
            return userDoc.exists;
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



    // --------------------- Helper Methods ---------------------

}