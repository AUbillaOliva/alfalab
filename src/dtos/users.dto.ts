import { isEmpty } from '@utils/util';
import { IsEmail, IsMongoId, IsNotEmpty, IsString, MinLength, ValidateIf } from 'class-validator';

export class UserDto {
  @IsNotEmpty({ message: 'Debes ingresar un correo electrónico.' })
  @IsEmail()
  public email: string;

  @IsNotEmpty({ message: 'Debes ingresar una contraseña.' })
  @IsString()
  @MinLength(8, { message: 'Debes ingresar una contraseña mayor a 8 caracteres.' })
  public password: string;

  @IsNotEmpty({ message: 'Debes ingresar un nombre.' })
  @IsString()
  public name: string;

  @ValidateIf((o: string) => isEmpty(o))
  @IsString()
  public location: string;

  @ValidateIf((o: string) => isEmpty(o))
  @IsString()
  public role: string;
}

export class UserOrderDto {
  @IsNotEmpty()
  @IsMongoId()
  public _id: string;

  @IsNotEmpty()
  @IsEmail()
  public email: string;

  @IsNotEmpty()
  @IsString()
  public name: string;

  @IsNotEmpty()
  @IsString()
  public location: string;
}

export class LoginUserDto {
  @IsNotEmpty({ message: 'Debes ingresar un correo electrónico.' })
  @IsEmail()
  public email: string;

  @IsNotEmpty({ message: 'Debes ingresar una contraseña.' })
  @IsString()
  @MinLength(8, { message: 'Debes ingresar una contraseña mayor a 8 caracteres.' })
  public password: string;
}
