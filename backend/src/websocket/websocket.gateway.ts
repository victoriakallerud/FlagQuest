import { SubscribeMessage, WebSocketGateway, OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect, ConnectedSocket, MessageBody, WebSocketServer } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import { Logger } from '@nestjs/common';
import { JoinLobbyDTO } from './dto/joinLobby.dto';
import { WebsocketService } from './websocket.service';
import { Lobby } from 'src/interfaces/lobby.interface';
import { QuizService } from 'src/quiz/quiz.service';
import { Quiz } from 'src/interfaces/quiz.interface';
import { GameService } from 'src/game/game.service';
import { Game } from 'src/interfaces/game.interface';
import { submittedAnswerDto } from './dto/submittedAnswer.dto';
import { Player } from 'src/interfaces/player.interface';
import { PlayerDto } from './dto/player.dto';

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

  pushQuiz(lobbyId: string, quiz: Quiz){
    this.server.to(lobbyId).emit('quiz', quiz);
  }

  pushNotificationToPlayer(lobbyId: string, notification: string){
    this.server.to(lobbyId).emit('notification', notification);
  }

  pushEndScoreToPlayer(lobbyId: string, listOfAllPlayer: PlayerDto[]){
  this.server.to(lobbyId).emit('endScore', listOfAllPlayer);
  }

  @SubscribeMessage('submittedAnswer')
  async handleSubmittedAnswer(@ConnectedSocket() client: Socket, @MessageBody() submittedAnswer: submittedAnswerDto) {
    // get player name and push notification to all players
    const playerName: string = await this.gameService.getPlayerName(submittedAnswer.playerId); 
    this.pushNotificationToPlayer(submittedAnswer.lobbyId, `Player ${playerName} submitted an answer`);

    // update player score and increase number of submitted answers
    this.gameService.updatePlayerScore(client, submittedAnswer.playerId, submittedAnswer.lobbyId, submittedAnswer.isAnswerRight, submittedAnswer.answerTime);
    this.gameService.increaseNumberOfSubmittedAnswers(submittedAnswer.lobbyId);

    // check if all players have submitted answers and change round if so
    if (this.gameService.checkIfAllPlayersSubmittedAnswers(submittedAnswer.lobbyId)){
      this.pushNotificationToPlayer(submittedAnswer.lobbyId, 'All players have submitted their answers');
      this.gameService.changeCurrentRound(submittedAnswer.lobbyId);
    }
    // check if game is over and push end score to all players
    if (this.gameService.gameOver(submittedAnswer.lobbyId)){
      this.pushNotificationToPlayer(submittedAnswer.lobbyId, 'Game Over');
      this.pushEndScoreToPlayer(submittedAnswer.lobbyId, this.gameService.getListOfAllPlayers(submittedAnswer.lobbyId));
    }
  }


}
