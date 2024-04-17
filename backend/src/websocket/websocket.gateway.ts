import { SubscribeMessage, WebSocketGateway, OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect, ConnectedSocket, MessageBody, WebSocketServer } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import { Logger, UseFilters, UsePipes, ValidationPipe } from '@nestjs/common';
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
import { BadRequestExceptionsFilter } from './BadRequestException.filter';

@WebSocketGateway()
@UsePipes(new ValidationPipe())
@UseFilters(new BadRequestExceptionsFilter())
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
    client.emit('status', JSON.stringify(res));
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
    client.emit('status', JSON.stringify(res));
    return res;
  }

  @SubscribeMessage('startGame')
  async handleStartGame(@ConnectedSocket() client: Socket, @MessageBody() joinLobbyDto: JoinLobbyDTO) {
    const res: Lobby = await this.websocketService.handleStartGame(client, joinLobbyDto.userId, joinLobbyDto.lobbyId);
    this.pushUpdatedLobby(joinLobbyDto.lobbyId, res);
    const quiz: Quiz = await this.quizService.generateQuiz(res.options.level, res.options.numberOfQuestions);
    this.pushQuizToLobby(res.id, quiz);
    this.gameService.createGame(res);
  }

  // --------------------- Push-to-client Functions ---------------------

  pushUpdatedLobby(lobbyId: string, updatedLobby: Lobby){
    this.logger.log(`Pushing updated lobby ${lobbyId} to all clients in room ${lobbyId}`);
    this.server.to(lobbyId).emit('updateLobby', updatedLobby);
  }

  pushQuizToLobby(lobbyId: string, quiz: Quiz){
    this.logger.log(`Pushing quiz to all clients in room ${lobbyId}`);
    this.server.to(lobbyId).emit('quiz', quiz);
  }

  pushSubmittedAnswerCountToLobby(lobbyId: string, submittedAnswerCount: number){
    this.logger.log(`Pushing submitted answer count ${submittedAnswerCount} to all clients in room ${lobbyId}`);
    this.server.to(lobbyId).emit('answerCount', submittedAnswerCount);
  }

  pushStartNextRoundToLobby(lobbyId: string, roundCount){
    this.logger.log(`Starting next round in lobby ${lobbyId}`);
    this.server.to(lobbyId).emit('nextRound', roundCount);
  }
  
  pushEndScoreToLobby(lobbyId: string, listOfAllPlayers: PlayerDto[]){
    this.logger.log(`Pushing end score to all clients in room ${lobbyId}`);
    this.server.to(lobbyId).emit('endScore', listOfAllPlayers);
  }

  // --------------------- Game Functions ---------------------
  
  @SubscribeMessage('submitAnswer')
  async handleSubmittedAnswer(@ConnectedSocket() client: Socket, @MessageBody() submittedAnswer: submittedAnswerDto) {
    // get player name and push notification to all players
    // Select the first room a client is in as the lobbyId
    
    if(!this.websocketService.isClientInRoomOfLobby(client, submittedAnswer.lobbyId)){
      this.logger.error(`Client ${client.id} submitted answer but is not in room ${submittedAnswer.lobbyId}`);
      client.emit('status', JSON.stringify({status: 'ERROR', message: `Client ${client.id} submitted answer but is not in room ${submittedAnswer.lobbyId}`}));
      return;
    } else {
      // get game, update player score and increase number of subbmited answers
      try{
        if(!this.gameService.hasPlayerAnswered(submittedAnswer.lobbyId, submittedAnswer.playerId)){
          const game: Game = this.gameService.getGameById(submittedAnswer.lobbyId);
          this.gameService.updatePlayerScore(client, submittedAnswer.playerId, submittedAnswer.lobbyId, submittedAnswer.isAnswerRight, submittedAnswer.answerTime);
          this.gameService.setPlayerHasAnswered(submittedAnswer.lobbyId, submittedAnswer.playerId);
          this.pushSubmittedAnswerCountToLobby(submittedAnswer.lobbyId, this.gameService.getSubmittedAnswerCount(submittedAnswer.lobbyId));
          this.logger.log(`Player ${submittedAnswer.playerId} submitted answer in lobby ${submittedAnswer.lobbyId}`);
      
          // check if game is over and push end score to all players
          if (this.gameService.isGameOver(submittedAnswer.lobbyId)){
            this.logger.log(`Game is over in lobby ${submittedAnswer.lobbyId}`);
            this.pushEndScoreToLobby(submittedAnswer.lobbyId, this.gameService.getListOfAllPlayers(submittedAnswer.lobbyId));
            const resLobby: Lobby = await this.gameService.endGame(submittedAnswer.lobbyId);
            this.pushUpdatedLobby(submittedAnswer.lobbyId, resLobby);
            return resLobby;
          }
          // check if all players have submitted answers and change round if so
          if (this.gameService.checkIfAllPlayersSubmittedAnswers(submittedAnswer.lobbyId)){
            this.logger.log(`All players have submitted answers for the current round in lobby ${submittedAnswer.lobbyId}`);
            this.gameService.changeCurrentRound(submittedAnswer.lobbyId);
            this.pushStartNextRoundToLobby(submittedAnswer.lobbyId, game.currentRound);
          }
        } else {
          client.emit('status', JSON.stringify({status: 'ERROR', message: `Player ${submittedAnswer.playerId} has already submitted an answer`}));
        }
      } catch(error) {
        this.logger.error(`Error while submitting answer: ${error}`);
        client.emit('status', JSON.stringify({status: 'ERROR', message: `Error while submitting answer: ${error}`}));
      }

    } 
  }
}
