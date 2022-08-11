import { IsEmail, IsString, ValidateIf } from 'class-validator';

export class SupportDto {

    constructor(message: string, from: string) {
        this.message = message;
        this.from = from;
    }

	//@IsNotEmpty({ message: ' El mensaje es requerido' })
    @ValidateIf((o: string) => o === null)
	@IsString({ message: ' El mensaje debe ser tipo texto' })
	message: string;

	//@IsNotEmpty({ message: ' El remitente es requierido' })
    @ValidateIf((o: string) => o === null)
	@IsEmail({ message: ' El remitente es requerido' })
	from: string;
}