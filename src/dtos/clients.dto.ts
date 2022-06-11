import { isEmpty } from '@utils/util';
import { IsEmail, IsMobilePhone, IsNotEmpty, IsString, ValidateIf } from 'class-validator';

export class ClientDto {
  constructor(name: String, email: string, instagram?: string, phone?: string) {
    this.name = name;
    this.email = email;
    this.instagram = instagram;
    this.phone = phone;
  }

  @IsNotEmpty({ message: 'Debes ingresar un nombre.' })
  @IsString({ message: 'Debes ingresar un nombre.' })
  public name: String;

  @IsNotEmpty()
  @IsEmail()
  public email: string;

  @ValidateIf((o: string) => isEmpty(o))
  @IsString({ message: 'Instagram' })
  public instagram?: string;

  @ValidateIf((o: string) => isEmpty(o))
  @IsMobilePhone('CL', { message: 'CL' })
  public phone?: string;
}
