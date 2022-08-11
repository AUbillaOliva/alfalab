import { IsEmail, IsNotEmpty, MinLength } from "class-validator";

export class LoginUserDto {
	@IsEmail()
	@IsNotEmpty()
	email: string;

	@IsNotEmpty()
	password: string;

	constructor(email: string, password: string) {
		this.email = email;
		this.password = password;
	}
}

export class ResetPasswordFormDto {
	@IsEmail()
	@IsNotEmpty({ message: ' El correo es requerido.' })
	email: string;
}

export class ResetPasswordDto {
	@MinLength(8)
	@IsNotEmpty({ message: ' Debes ingresar una contraseña.' })
	password: string;
}