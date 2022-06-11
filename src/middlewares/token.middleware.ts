import { NextFunction, Request, Response } from 'express';
import { HttpException } from '@exceptions/HttpException';
import { isEmpty } from '@utils/util';

const tokenMiddleware = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const header = req.headers['authorization'];
    if (isEmpty(header)) throw new HttpException(400, 'Bad Request');
    next();
  } catch (error) {
    next(new HttpException(error.status, error.message));
  }
};

export default tokenMiddleware;
