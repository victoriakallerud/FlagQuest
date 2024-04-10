import { SubscribeMessage, WebSocketGateway, OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect, ConnectedSocket, MessageBody, WebSocketServer } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import { Logger } from '@nestjs/common';
import { JoinLobbyDTO } from './dto/joinLobby.dto';
import { WebsocketService } from './websocket.service';
import { Lobby } from 'src/interfaces/lobby.interface';
import { QuizService } from 'src/quiz/quiz.service';
import { Quiz } from 'src/interfaces/quiz.interface';
import { GameService } from 'src/game/game.service';

@WebSocketGateway()
export class WebsocketGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {

  private logger: Logger = new Logger('WebsocketGateway');

  constructor(
    private readonly websocketService: WebsocketService,
    private readonly quizService: QuizService,
    private readonly gameService: GameService
  ) {}

  @WebSocketServer()
  server: Server;

  async afterInit(server: any) {
    this.logger.log('Initialized websocket server.');
  }

  handleConnection(client: any, ...args: any[]) {
    this.logger.log(`Client connected: ${client.id}`);
    client.emit('connection', 'Successfully connected to the FlagQuest backend websocket server!');

  }

  handleDisconnect(client: any) {
      this.logger.log(`Client disconnected: ${client.id}`);
  }

  @SubscribeMessage('joinLobby')
  async handleJoinLobby(@ConnectedSocket() client: Socket, @MessageBody() joinLobbyDto: JoinLobbyDTO) {
    const res = await this.websocketService.handleJoinLobby(client, joinLobbyDto);
    client.emit('joinLobby', JSON.stringify(res));
    // Log all rooms the client is in:
    this.logger.log(`Client ${client.id} is in ${client.rooms.size} rooms`);
    client.rooms.forEach((room) => {
      this.logger.log(`Client ${client.id} is in room ${room}`);
    });
    return res;
  }

  @SubscribeMessage('leaveLobby')
  async handleLeaveLobby(@ConnectedSocket() client: Socket, @MessageBody() joinLobbyDto: JoinLobbyDTO) {
    const res = await this.websocketService.handleLeaveLobby(client, joinLobbyDto);
    client.emit('leaveLobby', JSON.stringify(res));
    return res;
  }

  @SubscribeMessage('startGame')
  async handleStartGame(@ConnectedSocket() client: Socket, @MessageBody() joinLobbyDto: JoinLobbyDTO) {
    const res: Lobby = await this.websocketService.handleStartGame(client, joinLobbyDto.userId, joinLobbyDto.lobbyId);
    this.pushUpdatedLobby(joinLobbyDto.lobbyId, res);
    const quiz: Quiz = await this.quizService.generateQuiz(res.options.numberOfQuestions);
    this.pushQuiz(res.id, quiz);
    this.gameService.createGame(res);
  }

  pushUpdatedLobby(lobbyId: string, updatedLobby: Lobby){
    this.logger.log(`Pushing updated lobby ${lobbyId} to all clients in room ${lobbyId}`);
    this.server.to(lobbyId).emit('updateLobby', updatedLobby);
  }

  pushQuiz(lobbyId: string, quiz: any){
    this.server.to(lobbyId).emit('quiz', quiz);
  }


}
