import { IUser } from '@interfaces/users.interface';
declare module 'express-session' {
  interface SessionData {
    user?: IUser;
    createdAt?: number;
  }
}
