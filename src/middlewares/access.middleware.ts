import { NextFunction, Response } from 'express';

// Exceptions
import { HttpException } from '@exceptions/HttpException';

// Interfaces
import { RequestWithUser } from '@interfaces/auth.interface';
import { IUser } from '@interfaces/users.interface';

// Model
import UserModel from '@models/users.model';

// Utils
import { isEmpty } from '@utils/util';

export const accessMiddleware = async (req: RequestWithUser, res: Response, next: NextFunction): Promise<void> => {
  try {
    const { user } = req.session;
    if (isEmpty(user)) throw new HttpException(401, 'Session no activa, acceso denegado.');
    const foundUser: IUser = await UserModel.findOne({ email: user.email });
    if (isEmpty(foundUser)) throw new HttpException(401, 'No tienes permisos para realizar esta acción, acceso denegado.');
    const role: string = foundUser.role;
    if (role === 'admin' || role === 'mod') {
      next();
    } else throw new HttpException(401, 'No tienes permisos para realizar esta acción, acceso denegado.');
  } catch (error) {
    next(new HttpException(error.status, error.message));
  }
};
