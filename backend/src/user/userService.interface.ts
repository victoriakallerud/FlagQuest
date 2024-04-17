import { Score } from 'src/interfaces/score.interface';
import { User } from '../interfaces/user.interface';
import { LevelEnum } from 'src/enums/level.enum';
import { GameModeEnum } from 'src/enums/gamemode.enum';
import { RequestUserScoresDTO } from './dto/requestUserScores.dto';

export interface IUserService {
    createUser(user: User): Promise<User>;
    getUserById(userId: string): Promise<User>;
    getUserIdByName(userName: string): Promise<string>;
    getBestScores(number: number, level: LevelEnum, game: GameModeEnum): Promise<RequestUserScoresDTO[]>;
    updateUser(userId: string, user: User): Promise<User>;
    deleteUser(userId: string): Promise<void>;
    removeFriend(userId: string, friendId: string): Promise<void>;
    sendFriendRequest(userId: string, friendId: string): Promise<void>;
    acceptFriendRequest(userId: string, inquirerId: string): Promise<void>;
    rejectFriendRequest(userId: string, inquirerId: string): Promise<void>;
}