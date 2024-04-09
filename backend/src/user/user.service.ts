import { HttpException, Injectable, Logger } from '@nestjs/common';
import { User } from '../interfaces/user.interface';
import { v4 as uuidv4 } from 'uuid';
import { RequestUserDTO } from './dto/requestUser.dto';
import { IUserService } from './userService.interface';
import { DatabaseService } from '../database/database.service';
import* as moment from 'moment-timezone';

@Injectable()
export class UserService implements IUserService {

    private readonly logger = new Logger(UserService.name);

    constructor(private readonly databaseService: DatabaseService) {
    }

    async createUser(userDto: RequestUserDTO): Promise<User> {
        let uuid = uuidv4();
        let user: User = {
            id: uuid,
            userName: userDto.userName,
            creationTime: moment().tz('Europe/Berlin').format(),
            friendUuidList: [],
            highScores: [],
            lastOnline: moment().tz('Europe/Berlin').format(),
            nationality: userDto.nationality,
            pendingFriendRequests: [],
        }
        try{
            let createdUser = await this.databaseService.createUser(user);
            this.logger.log('User created with id ' + user.id);
            return createdUser;
        } catch (error) {
            this.logger.error('Error creating user', error);
            throw new HttpException('Error creating user', 500);
        }
    }

    async getUser(userId: string): Promise<User> {
        // find user with id in database
        try{
            let user  = await this.databaseService.getUserById(userId);
            this.logger.log('User with id ' + userId + ' found');
            return user;
        } catch (error) {
            if(error.message === 'User does not exist'){
                this.logger.error('User does not exist', error);
                throw new HttpException('User does not exist', 404);
            } else {
                this.logger.error('Error getting user', error);
                throw new HttpException('Error getting user', 500);
            }
        }
    }
    
    async updateUser(userId: string, userDto: RequestUserDTO): Promise<User> {
        // Find user in database and update it
        try {
            let user = await this.databaseService.getUserById(userId);
            user.lastOnline = moment().tz('Europe/Berlin').format();
            user.userName = userDto.userName;
            user.nationality = userDto.nationality;
            let updatedUser = await this.databaseService.updateUser(userId, user);
            this.logger.log('User with ID ' + userId + ' updated');
            return updatedUser;
        } catch (error) {
            if(error.message === 'User does not exist'){
                this.logger.error('User does not exist', error);
                throw new HttpException('User does not exist', 404);
            } else {
                this.logger.error('Error getting user', error);
                throw new HttpException('Error getting user', 500);
            }
        }
    }

    async deleteUser(userId: string): Promise<void> {
        //TODO implement database update
        // Find user in database and delete it
        try {
             await this.databaseService.deleteUser(userId);
             this.logger.log('User with ID ' + userId + ' deleted');
        } catch (error) {
            if(error.message === 'User does not exist'){
                this.logger.error('User does not exist', error);
                throw new HttpException('User does not exist', 404);
            } else {
                this.logger.error('Error getting user', error);
                throw new HttpException('Error getting user', 500);
            }
    }
}


    async removeFriend(userId: string, friendId: string): Promise<void> {
    
        try{
            let user = await this.databaseService.getUserById(userId);
            let friend = await this.databaseService.getUserById(friendId);
            await this.databaseService.removeFriend(user,friendId);
            await this.databaseService.removeFriend(friend, userId);
            this.logger.log(`Friend with the id ${friendId} removed`);
        } catch (error) {
            if (error.message === 'User does not exist') {  
                this.logger.error('User does not exist', error);
                throw new HttpException('User does not exist', 404);
            } else if (error.message === 'User with ID ${friendId} is not a friend of the user'){
                this.logger.error('User with ID ${friendId} is not a friend of the user', error);
                throw new HttpException('User with ID ${friendId} is not a friend of the user', 404);
            } else {
                this.logger.error('Error removing friend', error);
                throw new HttpException('Error removing friend', 500);
            }
        }
    }

    async sendFriendRequest(userId: string, receiverId: string): Promise<void> {
      //TODO Check if friend is in friend list, if not add friendrequest in pending friendrequests
        try{
            let user = await this.databaseService.getUserById(userId);
            let receiver = await this.databaseService.getUserById(receiverId);
            if(user.friendUuidList.includes(receiverId)){
                throw new Error('Friend already in friendlist');
            }
            if(receiver.pendingFriendRequests.includes(userId)){
                throw new Error('Friendrequest already sent');
            }
            receiver.pendingFriendRequests.push(userId);
            await this.databaseService.updateUser(receiverId, receiver);
            this.logger.log('Friendrequest sent');
        } catch (error) {
            if (error.message === 'User does not exist') {
                this.logger.error('User does not exist', error);
                throw new HttpException('User does not exist', 404);
            } else if (error.message === 'Friend already in friendlist'){
                this.logger.error('Friend already in friendlist', error);
                throw new HttpException('Friend already in friendlist', 409);
            } else if (error.message === 'Friendrequest already sent'){ 
                this.logger.error('Friendrequest already sent', error);
                throw new HttpException('Friendrequest already sent', 409);
            } else {
                this.logger.error('Error sending friend request', error);
                throw new HttpException('Error sending friend request', 500);
            }
        }
    }

    async acceptFriendRequest(userId: string, inquirerId: string): Promise<void> {

        try {
            let user = await this.databaseService.getUserById(userId);

             // check if the inquirer user still exists if not remove from pending friendrequests
             try {
                await this.databaseService.getUserById(inquirerId);
            } catch (error) {
                    user.pendingFriendRequests = user.pendingFriendRequests.filter(request => request !== inquirerId);
                    await this.databaseService.updateUser(userId, user);
                    this.logger.log('pending friendrequest from non existing user removed');
                    throw new Error('User with the friendrequest does not exist');
                }
            let inquirer = await this.databaseService.getUserById(inquirerId);
            //check if inquirer is already in friendlist
            if(user.friendUuidList.includes(inquirerId)){
                throw new Error('Friend already in friendlist');
            }
            //check if there is a friendrequest from inquirer
            if(!user.pendingFriendRequests.includes(inquirerId)){
                throw new Error('No friendrequest from this user');
            }
            //adding inquirer to friendlist
            user.pendingFriendRequests = user.pendingFriendRequests.filter(request => request !== inquirerId);
            user.friendUuidList.push(inquirerId);
            await this.databaseService.updateUser(userId, user);

            // adding user to inquirer friendlist
            inquirer.pendingFriendRequests = inquirer.pendingFriendRequests.filter(request => request !== userId);
            inquirer.friendUuidList.push(userId);
            await this.databaseService.updateUser(inquirerId, inquirer);

            this.logger.log('Friendrequest accepted');
        
        } catch (error) {
            if (error.message === 'User does not exist') {
                this.logger.error('User does not exist', error);
                throw new HttpException('User does not exist', 404);
            } else if (error.message === 'User with the friendrequest does not exist') {
                this.logger.error('User with the friendrequest does not exist', error);
                throw new HttpException('User with the friendrequest does not exist', 404);
            }else if (error.message === 'Friend already in friendlist'){
                this.logger.error('Friend already in friendlist', error);
                throw new HttpException('Friend already in friendlist', 409);
            } else if (error.message === 'No friendrequest from this user'){
                this.logger.error('No friendrequest from this user', error);
                throw new HttpException('No friendrequest from this user', 404);
            } else {
                this.logger.error('Error accepting friend request', error);
                throw new HttpException('Error accepting friend request', 500);
            }
        }
    }

    async rejectFriendRequest(userId: string, inquirerId: string): Promise<void> {
        try {
            let user = await this.databaseService.getUserById(userId);
            // check if the inquirer user still exists if not remove from pending friendrequests
            try {
                await this.databaseService.getUserById(inquirerId);
            } catch (error) {
                    user.pendingFriendRequests = user.pendingFriendRequests.filter(request => request !== inquirerId);
                    await this.databaseService.updateUser(userId, user);
                    throw new Error('User with the friendrequest does not exist');
                }
            
            // check if inquirer is already in friendlist
            if(user.friendUuidList.includes(inquirerId)){
                throw new Error('Friend already in friendlist');
            }
            // check if there is a friendrequest from inquirer
            if(!user.pendingFriendRequests.includes(inquirerId)){
                throw new Error('No friendrequest from this user');
            }
            user.pendingFriendRequests = user.pendingFriendRequests.filter(request => request !== inquirerId);
            await this.databaseService.updateUser(userId, user);
        } catch (error) {
            if (error.message === 'User does not exist') {
                this.logger.error('User does not exist', error);
                throw new HttpException('User does not exist', 404);
            } else if (error.message === 'User with the friendrequest does not exist') {
                this.logger.error('User with the friendrequest does not exist', error);
                throw new HttpException('User with the friendrequest does not exist', 404);
            } else if (error.message === 'Friend already in friendlist'){
                this.logger.error('Friend already in friendlist', error);
                throw new HttpException('Friend already in friendlist', 409);
            } else if (error.message === 'No friendrequest from this user'){
                this.logger.error('No friendrequest from this user', error);
                throw new HttpException('No friendrequest from this user', 404);
            } else {
            this.logger.error('Error rejecting friend request', error);
            throw new HttpException('Error rejecting friend request', 500);
           }
        }	
    }
}
