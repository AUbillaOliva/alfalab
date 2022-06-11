import TokensService from '@services/tokens.service';
import { NextFunction, Request, Response } from 'express';
import { RequestWithUser } from '@interfaces/auth.interface';
import { IToken } from '@interfaces/tokens.interface';

class TokensController {
  tokenService = new TokensService();

  public getAllTokens = async (req: RequestWithUser, res: Response, next: NextFunction) => {
    try {
      const { user } = req.session;
      const findAllTokensData: IToken[] = await this.tokenService.findAllTokens(user._id);

      res.status(200).json({ data: findAllTokensData, message: 'findAll' });
    } catch (error) {
      next(error);
    }
  };

  public getTokenById = async (req: RequestWithUser, res: Response, next: NextFunction) => {
    try {
      const { user } = req.session;
      const tokenId: string = req.params.id;
      const findOneTokenData: IToken = await this.tokenService.findTokenById(tokenId, user._id);

      res.status(200).json({ data: findOneTokenData, message: 'findOne' });
    } catch (error) {
      next(error);
    }
  };

  public revokeTokenById = async (req: RequestWithUser, res: Response, next: NextFunction) => {
    try {
      const tokenId: string = req.params.id;
      const { user } = req.session;
      const revokeTokenData: IToken = await this.tokenService.revokeTokenById(tokenId, user._id);

      res.status(200).json({ data: revokeTokenData, message: 'revoked' });
    } catch (error) {
      next(error);
    }
  };

  public deleteTokenById = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const { user } = req.session;
      const tokenId: string = req.params.id;
      const deleteTokenData: IToken = await this.tokenService.deleteTokenById(tokenId, user._id);

      res.status(200).json({ data: deleteTokenData, message: 'deleted' });
    } catch (error) {
      next(error);
    }
  };
}

export default TokensController;
