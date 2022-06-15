import { IUser } from '@interfaces/users.interface';
import { Session, SessionData } from 'express-session';
declare namespace Express {
  interface CustomSessionFields {
    user?: IUser;
    createdAt?: number;
  }

  export interface Request {
    session: Session & Partial<SessionData> & CustomSessionFields;
  }
}
