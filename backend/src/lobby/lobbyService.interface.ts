import { Lobby } from "src/interfaces/lobby.interface";

export interface ILobbyService {
    createLobby(lobby: Lobby): Lobby;
    getLobby(lobbyId: string): Lobby;
    deleteLobby(lobbyId: string): void;
    joinLobby(lobbyId: string, userId: string): Lobby;
    leaveLobby(lobbyId: string, userId: string): Lobby;  
}