/**
 * @export
 */
export const StatusCodesEnum = {
    SUCCESS: 'SUCCESS',
    ERROR: 'ERROR'
} as const;
export type StatusCodesEnum = typeof StatusCodesEnum[keyof typeof StatusCodesEnum];