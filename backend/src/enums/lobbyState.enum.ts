/**
 * @export
 */
export const LobbyStateEnum = {
    WaitingForPlayers: 'waitingForPlayers',
    Pending: 'pending',
    InGame: 'inGame'
} as const;
export type LobbyStateEnum = typeof LobbyStateEnum[keyof typeof LobbyStateEnum];