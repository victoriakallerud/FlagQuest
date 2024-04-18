import { Body, Controller, Get, Param, Post, Put, Delete } from '@nestjs/common';
import { UserService } from './user.service';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { RequestUserDTO } from './dto/requestUser.dto';
import { LevelEnum } from 'src/enums/level.enum';
import { GameModeEnum } from 'src/enums/gamemode.enum';

@ApiTags('user')
@Controller('user')
export class UserController {
    constructor(private readonly userService: UserService) {}

    @ApiOperation({ summary: 'Create a new user' })
    @Post()
    async createUser(@Body() userDto: RequestUserDTO) {
      return await this.userService.createUser(userDto);
    }

    @ApiOperation({ summary: 'Get a user by id' })
    @Get(':userId')
    async getUserById(@Param('userId') userId: string) {
      return await this.userService.getUserById(userId);
    }

    @ApiOperation({ summary: 'Get a user by firebase id' })
    @Get('/byFirebaseId/:firebaseId')
    async getUserByFirebaseId(@Param('firebaseId') firebaseId: string) {
      return await this.userService.getUserByFirebaseId(firebaseId);
    }

    @ApiOperation({ summary: 'Get a userId by name' })
    @Get('byName/:userName')
    async getUserIdByName(@Param('userName') userName: string) {
      return await this.userService.getUserIdByName(userName);
    }

    @ApiOperation({ summary: 'Get the best top k scores based on level and mode' })
    @Get('highScores/:level/:gameMode')
    async getBestScoresByRegionAndLevel(@Body('number') number: number, @Param('level') level: LevelEnum, @Param('gameMode') gameMode: GameModeEnum) {
      return await this.userService.getBestScores(number, level, gameMode);
    }

    @ApiOperation({ summary: 'Update a user by id' })
    @Put(':userId')
    async updateUser(@Param('userId') userId: string, @Body() userDto: RequestUserDTO){
      return await this.userService.updateUser(userId, userDto);
    }

    @ApiOperation({ summary: 'Delete a user by id' })
    @Delete(':userId')
    async deleteUser(@Param('userId') userId: string) {
      return await this.userService.deleteUser(userId);
    }

    @ApiOperation({ summary: 'Delete a user in the friendlist by id' })
    @Delete(':userId/friends/:friendId')
    async removeFriend(@Param('userId') userId: string, @Param('friendId') friendId : string) {
      return await this.userService.removeFriend(userId, friendId);
    }

    @ApiOperation({ summary: 'Send a Friendrequest to a user by id' })
    @Put(':userId/friends/:receiverId')
    async sendFriendRequest(@Param('userId') userId: string, @Param('receiverId') receiverId : string) {
      return await this.userService.sendFriendRequest(userId, receiverId);
    }

    @ApiOperation({ summary: 'Accept a Friendrequest from a inquirer by id' })
    @Put(':userId/friends/requests/:inquirerId')
    async acceptFriendRequest(@Param('userId') userId: string, @Param('inquirerId') inquirerId : string) {
      return await this.userService.acceptFriendRequest(userId, inquirerId);
    }

    @ApiOperation({ summary: 'Reject a Friendrequest from a inquirer by id' })
    @Delete(':userId/friends/requests/:inquirerId')
    async rejectFriendRequest(@Param('userId') userId: string, @Param('inquirerId') inquirerId : string) {
      return await this.userService.rejectFriendRequest(userId, inquirerId);
    }
}
