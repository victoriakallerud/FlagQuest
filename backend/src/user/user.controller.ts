import { Body, Controller, Get, Param, Post, Put, Delete } from '@nestjs/common';
import { UserService } from './user.service';
import { ApiOperation, ApiTags } from '@nestjs/swagger';
import { User } from 'models';
import { RequestUserDTO } from './dto/requestUser.dto';

@ApiTags('user')
@Controller('user')
export class UserController {
    constructor(private readonly userService: UserService) {}

    @ApiOperation({ summary: 'Create a new user' })
    @Post()
    createUser(@Body() userDto: RequestUserDTO): User {
      return this.userService.createUser(userDto);
    }

    @ApiOperation({ summary: 'Get a user by id' })
    @Get(':userId')
    getUser(@Param('userId') userId: string): User {
      return this.userService.getUser(userId);
    }

    @ApiOperation({ summary: 'Update a user by id' })
    @Put(':userId')
    updateUser(@Param('userId') userId: string, @Body() userDto: RequestUserDTO): User{
      return this.userService.updateUser(userId, userDto);
    }

    @ApiOperation({ summary: 'Delete a user by id' })
    @Delete(':userId')
    deleteUser(@Param('userId') userId: string): void {
      return this.userService.deleteUser(userId);
    }

    @ApiOperation({ summary: 'Delete a user in the friendlist by id' })
    @Delete(':userId/friends/:friendId')
    removeFriend(@Param('userId') userId: string, @Param('friendId') friendId : string): void {
      return this.userService.removeFriend(userId, friendId);
    }

    @ApiOperation({ summary: 'Send a Friendrequest to a user by id' })
    @Put(':userId/friends/:friendId')
    sendFriendRequest(@Param('userId') userId: string, @Param('friendId') friendId : string): void {
      return this.userService.sendFriendRequest(userId, friendId);
    }

    @ApiOperation({ summary: 'Accept a Friendrequest from a inquirer by id' })
    @Put(':userId/friends/requests/:inquirerId')
    acceptFriendRequest(@Param('userId') userId: string, @Param('inquirerId') inquirerId : string): void {
      return this.userService.acceptFriendRequest(userId, inquirerId);
    }

    @ApiOperation({ summary: 'Reject a Friendrequest from a inquirer by id' })
    @Delete(':userId/friends/requests/:inquirerId')
    rejectFriendRequest(@Param('userId') userId: string, @Param('inquirerId') inquirerId : string): void {
      return this.userService.rejectFriendRequest(userId, inquirerId);
    }
}
