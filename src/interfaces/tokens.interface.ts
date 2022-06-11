import { IUser } from '@interfaces/users.interface';

export interface IToken {
  _id: string;
  user: string | IUser;
  token: string;
  created: Date;
  expires: Date;
  expired_token?: string;
  revoked: boolean;
  isActive?: boolean;
  isExpired?: boolean;
}
