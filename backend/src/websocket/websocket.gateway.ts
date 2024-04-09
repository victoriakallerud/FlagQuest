import { SubscribeMessage, WebSocketGateway, OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect, ConnectedSocket, MessageBody } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import { Logger } from '@nestjs/common';
import { JoinLobbyDTO } from './dto/joinLobby.dto';
import { WebsocketService } from './websocket.service';

@WebSocketGateway()
export class WebsocketGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {

  private logger: Logger = new Logger('WebsocketGateway');

  constructor(private readonly websocketService: WebsocketService) {}

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

  joinLobby(client: Socket, lobbyId: string) {
    client.join(lobbyId);
    this.logger.log(`Client ${client.id} joined lobby ${lobbyId}`);
    client.emit('joinedLobby', `Successfully joined lobby ${lobbyId}`);
  }

  leaveLobby(client: Socket, lobbyId: string) {
    client.leave(lobbyId);
    this.logger.log(`Client ${client.id} left lobby ${lobbyId}`);
    client.emit('leftLobby', `Successfully left lobby ${lobbyId}`);
  }

  @SubscribeMessage('joinLobby')
  async handleJoinLobby(@ConnectedSocket() client: Socket, @MessageBody() joinLobbyDto: JoinLobbyDTO) {
    const res = await this.websocketService.handleJoinLobby(client, joinLobbyDto);
    client.emit('joinLobby', JSON.stringify(res));
    return res;
  }

  @SubscribeMessage('leaveLobby')
  handleLeaveLobby(@ConnectedSocket() client: Socket, @MessageBody() joinLobbyDto: JoinLobbyDTO) {
    return this.websocketService.handleLeaveLobby(client, joinLobbyDto);
  }

  @SubscribeMessage('message')
  handleMessage(client: any, payload: any): string {
    this.logger.log(`Received message on topic MESSAGE: ${payload}`);
    // Send the message back to the client
    return "Hello from the server!"
  }


}
