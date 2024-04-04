import { Injectable } from '@nestjs/common';
import { Logger } from '@nestjs/common';
import * as admin from 'firebase-admin';
import { User } from '../interfaces/user.interface';

@Injectable()
export class DatabaseService {
    private db: admin.firestore.Firestore;
    private readonly logger = new Logger(DatabaseService.name);

    constructor() {
        this.db = admin.firestore();
    }

    // User Functions

    async getUserById(userId: string): Promise<User> {
        try{
            const userDoc = await this.db.collection('backend-test').doc(userId).get();
            if (!userDoc.exists) {
                this.logger.error('User not found');
                return null;
            } else {
                const user = userDoc.data() as User;
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
           await this.db.collection('backend-test').doc(user.id).set(user);
           return this.getUserById(user.id);
        }
        catch (error) {
            this.logger.error('Error creating user', error);
            throw error;
        }
    }

    async updateUser(userId: string, user: User): Promise<User> {
        try {
           await this.db.collection('backend-test').doc(userId).update({
                userName: user.userName,
                nationality: user.nationality,
                lastOnline: user.lastOnline,
                highScores: user.highScores,
                friendUuidList: user.friendUuidList
           });
              return this.getUserById(userId);
        } catch (error) {
            this.logger.error('Error updating user', error);
            throw error;
        }
    }

    async deleteUser(userId: string): Promise<void> {
        try {
            // Check if the document exists before attempting deletion
            const userDoc = this.db.collection('backend-test').doc(userId);
            const userSnapshot = await userDoc.get();
    
            if (!userSnapshot.exists) {
                // If the document doesn't exist, respond with 404 Not Found
                throw new Error(`User with ID ${userId} does not exist`);
            }
    
            // Attempt to delete the document
            await userDoc.delete();
        } catch (error) {
            // Log and rethrow the error
            this.logger.error('Error deleting user', error);
            throw error;
        }
    }
    async removeFriend(user: User, friendId: string): Promise<void> {
        try {
            const friendIndex = user.friendUuidList.indexOf(friendId);
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

    async sendFriendRequest(userId: string, friendId: string): Promise<void> {
        throw new Error('Not implemented');
    }
    async acceptFriendRequest(userId: string, inquirerId: string): Promise<void> {
        throw new Error('Not implemented');
    }
    async rejectFriendRequest(userId: string, inquirerId: string): Promise<void> {
        throw new Error('Not implemented');
    }

        

}
