/**
 * @export
 */
export const LevelEnum = {
    Europe: 'Europe',
    Asia: 'Asia',
    Africa: 'Africa',
    NorthAmerica: 'NorthAmerica',
    SouthAmerica: 'SouthAmerica',
    Oceania: 'Oceania'
} as const;
export type LevelEnum = typeof LevelEnum[keyof typeof LevelEnum];