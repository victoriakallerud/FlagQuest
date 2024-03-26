import { Controller } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';

@ApiTags('lobby')
@Controller('lobby')
export class LobbyController {}
