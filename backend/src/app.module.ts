import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { UserModule } from './user/user.module';
import { LobbyModule } from './lobby/lobby.module';
import { ConfigModule } from '@nestjs/config';
import { DatabaseModule } from './database/database.module';
import { WebsocketModule } from './websocket/websocket.module';

@Module({
  imports: [
    UserModule, 
    LobbyModule, 
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: process.env.NODE_ENV === 'production' ? '/home/debian/FlagQuest/backend/development.env' : 'development.env',
    }), 
    DatabaseModule, WebsocketModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
