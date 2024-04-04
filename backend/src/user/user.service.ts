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
        }
        try{
            let createdUser = await this.databaseService.createUser(user);
            if(!user){
                throw new HttpException('User not found', 404);
            }
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
            let user = await this.databaseService.getUserById(userId);
            if(!user){
                throw new HttpException('User not found', 404);
            }
            this.logger.log('User with id ' + userId + ' found');
            return user;
        } catch (error) {
            this.logger.error('Error getting user', error);
            throw new HttpException('Error getting user', 500);
        }
    }
    
    async updateUser(userId: string, userDto: RequestUserDTO): Promise<User> {
        // Find user in database and update it
        try {
            let user = await this.databaseService.getUserById(userId);
            if(!user){
                throw new HttpException('User not found', 404);
            }
            user.lastOnline = moment().tz('Europe/Berlin').format();
            user.userName = userDto.userName;
            user.nationality = userDto.nationality;
            return await this.databaseService.updateUser(userId, user);
        } catch (error) {
            this.logger.error('Error updating user', error);
            throw new HttpException('Error updating user', 500);
        }
    }

    async deleteUser(userId: string): Promise<void> {
        //TODO implement database update
        // Find user in database and delete it
        try {
            let user = await this.databaseService.getUserById(userId);
            if(!user){
                throw new HttpException('User not found', 404);
            }
             await this.databaseService.deleteUser(userId);
        } catch (error) {
            this.logger.error('Error deleting user', error);
            throw new HttpException('Error deleting user', 500);
            }
    }


    async removeFriend(userId: string, friendId: string): Promise<void> {
    
        try{
            let user = await this.databaseService.getUserById(userId);
            if(!user){
                throw new HttpException('User not found', 404);
            }
            await this.databaseService.removeFriend(user,friendId);
        } catch (error) {
            this.logger.error('Error removing friend', error);
            throw new HttpException('Error removing friend', 500);
        }
    }
    
    sendFriendRequest(userId: string, friendId: string): void {
      //TODO Check if friend is in friend list, if not add friendrequest in pending friendrequests
        throw new HttpException('Not implemented', 501);
        this.logger.log('Friend request from user with id ' + userId + ' to user with id ' + friendId + ' sent');
        return;
    }

    acceptFriendRequest(userId: string, inquirerId: string): void {
        // TODO Check if inquirer is in friend list
        // Find user in database and update it and delete pendingfriendrequest
        throw new HttpException('Not implemented', 501);
        this.logger.log('User with id ' + userId + ' accepted friend request from user with id ' + inquirerId);
        return;
    }
    rejectFriendRequest(userId: string, inquirerId: string): void {
        // TODO check if inquirer is in friend list 
        // update pendingfriendrequest of User in Database
        throw new HttpException('Not implemented', 501);
        this.logger.log('User with id ' + userId + ' rejected friend request from user with id ' + inquirerId);
        return;
    }
}
