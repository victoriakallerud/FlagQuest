import { ArgumentsHost, BadRequestException, Catch, Logger } from "@nestjs/common";
import { BaseWsExceptionFilter, WsException } from "@nestjs/websockets";
import { Socket } from "socket.io";

@Catch(BadRequestException)
export class BadRequestExceptionsFilter extends BaseWsExceptionFilter {
    private logger = new Logger('BadRequestExceptionsFilter');
  
  
    catch(exception: BadRequestException, host: ArgumentsHost) {
    const client = host.switchToWs().getClient<Socket>();
    // Here you have the exception and you can check the data
    const errors = exception.getResponse();
    this.logger.error(JSON.stringify(errors));
    const wsException = new WsException(errors);
    client.emit('status', JSON.stringify({status: 'ERROR', message: errors}));
    super.catch(wsException, host);
  }
}