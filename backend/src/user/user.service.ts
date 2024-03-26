import { HttpException, Injectable, Logger } from '@nestjs/common';
import { User } from '../interfaces/user.interface';
import { v4 as uuidv4 } from 'uuid';
import { RequestUserDTO } from './dto/requestUser.dto';
import { IUserService } from './userService.interface';

@Injectable()
export class UserService implements IUserService {

    private readonly logger = new Logger(UserService.name);

    createUser(userDto: RequestUserDTO): User {
        let user: User = {
        id: uuidv4(),
        userName: userDto.userName,
        creationTime: new Date().toISOString(),
        friendUuidList: [],
        highScores: [],
        lastOnline: new Date().toISOString(),
        nationality: userDto.nationality,
        pendingFriendRequests: [],
        }
        this.logger.log('User created with id ' + user.id);
        return user;
    }

    getUser(userId: string): User {
        //TODO: Implement database query
        let mockUser: User = {
            id: userId,
            userName: 'mockUser',
            friendUuidList: [],
            pendingFriendRequests: [],
            highScores: [],
            lastOnline: new Date().toISOString(),
            creationTime: new Date().toISOString(),
        }
        this.logger.log('User with id ' + userId + ' requested');
        return mockUser;
    }
    
    updateUser(userId: string, userDto: RequestUserDTO): User {
      // find user with id in database and update lastonline and user body params   
      // user.lastOnline = new Date().toISOString();
        // TODO: Implement database update
        let mockUser: User = {
            id: userId,
            userName: userDto.userName,
            nationality: userDto.nationality,
            friendUuidList: [],
            pendingFriendRequests: [],
            highScores: [],
            lastOnline: new Date().toISOString(),
            creationTime: new Date().toISOString(),
        }
        this.logger.log('User with id ' + userId + ' updated');
        return mockUser;
    }

    deleteUser(userId: string): void {
        //TODO implement database update
        throw new HttpException('Not implemented', 501);
        this.logger.log('User with id ' + userId + ' deleted');
    }

    removeFriend(userId: string, friendId: string): void {
      //TODO Check if friend is in friend list
        // Find user in database and update it
        throw new HttpException('Not implemented', 501);
        this.logger.log('Friend with id ' + friendId + ' removed from user with id ' + userId);
        return;
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
