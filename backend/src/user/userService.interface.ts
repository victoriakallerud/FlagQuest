import { User } from '../interfaces/user.interface';

export interface IUserService {
    createUser(user: User): User;
    getUser(userId: string): Promise<User>;
    updateUser(userId: string, user: User): User;
    deleteUser(userId: string): void;
    removeFriend(userId: string, friendId: string): void;
    sendFriendRequest(userId: string, friendId: string): void;
    acceptFriendRequest(userId: string, inquirerId: string): void;
    rejectFriendRequest(userId: string, inquirerId: string): void;
}