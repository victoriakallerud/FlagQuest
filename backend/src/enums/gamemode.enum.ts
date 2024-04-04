/**
 * @export
 */
export const GameModeEnum = {
    GuessingFlags: 'GuessingFlags',
    GuessingCaptials: 'GuessingCaptials',
    GuessingCountries: 'GuessingCountries'
} as const;
export type GameModeEnum = typeof GameModeEnum[keyof typeof GameModeEnum];