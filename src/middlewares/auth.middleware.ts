import { NextFunction, Response } from 'express';
import { HttpException } from '@exceptions/HttpException';
import { RequestWithUser } from '@interfaces/auth.interface';
import userModel from '@models/users.model';
import { IUser } from '@interfaces/users.interface';
import { isEmpty } from '@utils/util';

const authMiddleware = async (req: RequestWithUser, res: Response, next: NextFunction) => {
  try {
    const { user } = req.session;
    if (isEmpty(user)) throw new HttpException(401, 'Session no activa, acceso denegado');
    const foundUser: IUser = await userModel.findOne({ email: user.email });
    if (isEmpty(foundUser)) throw new HttpException(401, 'No tienes permisos para realizar esta acción, acceso denegado.');
    next();
  } catch (error) {
    next(new HttpException(error.status, error.message));
  }
};

export default authMiddleware;
