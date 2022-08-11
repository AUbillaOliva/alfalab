import { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
import { HttpException } from "@exceptions/HttpException";
import { isEmpty } from "@utils/util";
import { RequestWithUser } from "@interfaces/auth.interface";
import CONFIG from "@config";
import { IToken } from "@interfaces/tokens.interface";
import userModel from "@models/users.model";

const tokenMiddleware = async (
    req: Request,
    res: Response,
    next: NextFunction
) => {
    try {
        const header = req.headers["authorization"];
        if (isEmpty(header)) throw new HttpException(400, "Bad Request");

        next();
    } catch (error) {
        next(new HttpException(error.status, error.message));
    }
};

export const accessTokenMiddleware = async (
    req: RequestWithUser,
    res: Response,
    next: NextFunction
): Promise<void> => {
    try {
        const header = req.headers["authorization"];
        if (!isEmpty(header)) {
            const secret = CONFIG.TOKEN.ACCESS_TOKEN_SECRET;
            const verificationResponse: IToken = jwt.verify(
                header.split(" ")[1],
                secret
            ) as IToken;
            const userId = verificationResponse._id;
            const findUser = await userModel.findById(userId);
            if (findUser) {
                req.user = findUser;
                next();
            } else {
                next(new HttpException(404, "Invalid token, Unauthorized"));
            }
        } else {
            next(new HttpException(400, "Bad Request"));
        }
    } catch (error) {
        next(new HttpException(500, error.message));
    }
};

export default tokenMiddleware;
