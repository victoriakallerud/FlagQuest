import { Module } from '@nestjs/common';
import { GameService } from './game.service';

@Module({
  exports: [GameService],
  providers: [GameService]
})
export class GameModule {}
