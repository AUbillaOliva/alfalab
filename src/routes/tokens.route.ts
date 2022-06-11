import TokensController from '@controllers/token.controller';
import authMiddleware from '@middlewares/auth.middleware';
import { Router } from 'express';
import { Routes } from '@interfaces/routes.interface';

class TokenRoute implements Routes {
  public path = '/api/tokens';
  public router = Router();
  public tokensController = new TokensController();

  constructor() {
    this.initializeRoutes();
  }

  private initializeRoutes() {
    this.router.get(`${this.path}`, authMiddleware, this.tokensController.getAllTokens);
    this.router.get(`${this.path}/:id`, authMiddleware, this.tokensController.getTokenById);
    this.router.patch(`${this.path}/:id`, authMiddleware, this.tokensController.revokeTokenById);
    this.router.delete(`${this.path}/:id`, authMiddleware, this.tokensController.deleteTokenById);
  }
}

export default TokenRoute;
