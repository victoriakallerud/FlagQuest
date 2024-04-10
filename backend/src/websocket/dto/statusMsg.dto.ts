import { IsEnum, IsString } from "class-validator";
import { StatusCodesEnum } from "src/enums/statusCodes.enum";

export class StatusMsgDto {
 
    @IsEnum(StatusCodesEnum)
    status: StatusCodesEnum;

    @IsString()
    message: string;
}