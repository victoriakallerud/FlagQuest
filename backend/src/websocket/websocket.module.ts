import { Global, Module } from '@nestjs/common';
import { WebsocketGateway } from './websocket.gateway';
import { WebsocketService } from './websocket.service';
import { QuizModule } from 'src/quiz/quiz.module';
import { GameModule } from 'src/game/game.module';

@Global()
@Module({
  imports: [QuizModule, GameModule],
  providers: [WebsocketGateway, WebsocketService],
  exports: [WebsocketGateway, WebsocketService],
})
export class WebsocketModule {}
