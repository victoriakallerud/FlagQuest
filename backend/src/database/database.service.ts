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
}
