import { NextFunction, Response } from 'express';

// Exceptions
import { HttpException } from '@exceptions/HttpException';

// Interfaces
import { RequestWithUser } from '@interfaces/auth.interface';

// Utils
import { isEmpty } from '@utils/util';

export const sessionMiddleware = async (req: RequestWithUser, res: Response, next: NextFunction): Promise<void> => {
  try {
    const { user } = req.session;
    if (!isEmpty(user)) throw new HttpException(401, 'Ya tienes una sessión iniciada!');
    next();
  } catch (error) {
    next(new HttpException(error.status, error.message));
  }
};
