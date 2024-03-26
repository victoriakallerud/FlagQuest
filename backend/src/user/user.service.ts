import { HttpException, Injectable, Logger } from '@nestjs/common';
import { IUserService } from './IUserService';
import { User } from '../interfaces/user.interface';
import { v4 as uuidv4 } from 'uuid';
import { RequestUserDTO } from './dto/requestUser.dto';

@Injectable()
export class UserService implements IUserService {

    private readonly logger = new Logger(UserService.name);

    createUser(user: User): User {
        user.id = uuidv4();
        user.creationTime = new Date().toISOString();
        user.friendUuidList = [];
        user.highScores = [];
        user.lastOnline = new Date().toISOString();
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
        return mockUser;
    }

    deleteUser(userId: string): void {
        //TODO implement database update
        throw new HttpException('Not implemented', 501);
    }

    removeFriend(userId: string, friendId: string): void {
      //TODO Check if friend is in friend list
        // Find user in database and update it
        throw new HttpException('Not implemented', 501);
        return;
    }
    sendFriendRequest(userId: string, friendId: string): void {
      //TODO Check if friend is in friend list, if not add friendrequest in pending friendrequests
        throw new HttpException('Not implemented', 501);
        return;
    }

    acceptFriendRequest(userId: string, inquirerId: string): void {
        // TODO Check if inquirer is in friend list
        // Find user in database and update it and delete pendingfriendrequest
        throw new HttpException('Not implemented', 501);
        return;
    }
    rejectFriendRequest(userId: string, inquirerId: string): void {
        // TODO check if inquirer is in friend list 
        // update pendingfriendrequest of User in Database
        throw new HttpException('Not implemented', 501);
        return;
    }
}
