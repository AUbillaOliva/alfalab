import { IsBoolean, IsDate, IsMongoId, IsNotEmpty, IsString, ValidateIf } from 'class-validator';
import { isEmpty } from '@utils/util';

export class TokenDto {
  constructor(user: string, token: string, expires: Date, created?: Date, expired_token?: string, revoked?: boolean) {
    this.user = user;
    this.token = token;
    this.expires = expires;
    this.created = created;
    this.expired_token = expired_token;
    this.revoked = revoked;
  }

  @IsNotEmpty()
  @IsMongoId()
  public user: string;

  @IsNotEmpty()
  @IsString()
  public token: string;

  @ValidateIf((o: Date) => isEmpty(o))
  @IsDate()
  public created?: Date;

  @IsNotEmpty()
  @IsDate()
  public expires: Date;

  @IsNotEmpty()
  @IsString()
  public expired_token?: string;

  @IsBoolean()
  public revoked: boolean;
}
