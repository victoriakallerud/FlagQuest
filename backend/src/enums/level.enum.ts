/**
 * @export
 */
export const LevelEnum = {
    Europe: 'Europe',
    Asia: 'Asia',
    Africa: 'Africa',
    NorthAmerica: 'NorthAmerica',
    SouthAmerica: 'SouthAmerica',
    Oceania: 'Oceania',
    All: 'All'
} as const;
export type LevelEnum = typeof LevelEnum[keyof typeof LevelEnum];