import { User } from '../interfaces/user.interface';

export interface IUserService {
    createUser(user: User): Promise<User>;
    getUser(userId: string): Promise<User>;
    updateUser(userId: string, user: User): Promise<User>;
    deleteUser(userId: string): Promise<void>;
    removeFriend(userId: string, friendId: string): Promise<void>;
    sendFriendRequest(userId: string, friendId: string): Promise<void>;
    acceptFriendRequest(userId: string, inquirerId: string): Promise<void>;
    rejectFriendRequest(userId: string, inquirerId: string): Promise<void>;
}