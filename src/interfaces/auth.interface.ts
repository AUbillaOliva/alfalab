import { Request } from 'express';
import { IUser } from '@interfaces/users.interface';
import { Session, SessionData } from 'express-session';

export interface DataStoredInToken {
  _id: string;
}

export interface TokenData {
  token: string;
  expiresIn: number;
}

interface CustomSessionFields {
  user?: IUser;
  createdAt?: number;
}

export interface RequestWithUser extends Request {
  user: IUser;
  token: string;
  session: Session & Partial<SessionData> & CustomSessionFields;
}
