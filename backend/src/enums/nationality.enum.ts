/**
* 
 * @export
 */
export const Nationality = {
    German: 'German',
    Norsk: 'Norsk',
    English: 'English',
    Nederlands: 'Nederlands'
} as const;
export type Nationality = typeof Nationality[keyof typeof Nationality];