import { Lobby } from "src/interfaces/lobby.interface";

export interface ILobbyService {
    createLobby(lobby: Lobby): Promise<Lobby>;
    getLobby(lobbyId: string): Promise<Lobby>;
    getAllLobbies(): Promise<Lobby[]>;
    deleteLobby(lobbyId: string): Promise<void>;
    joinLobby(lobbyId: string, userId: string): Promise<Lobby>;
    leaveLobby(lobbyId: string, userId: string): Promise<void>;  
}