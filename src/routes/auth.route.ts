import AuthController from '@controllers/auth.controller';
import authMiddleware from '@middlewares/auth.middleware';
import { validationMiddleware } from '@middlewares/validation.middleware';
import { Router } from 'express';
import { LoginUserDto, UserDto } from '@dtos/users.dto';
import { Routes } from '@interfaces/routes.interface';
import { sessionMiddleware } from '@middlewares/session.middleware';
import tokenMiddleware from '@middlewares/token.middleware';

class AuthRoute implements Routes {
  public path = '/';
  public router = Router();
  private authController = new AuthController();

  constructor() {
    this.initializeRoutes();
  }

  private initializeRoutes() {
    this.router.post(`${this.path}signup`, validationMiddleware(UserDto, 'body'), this.authController.signUp);
    this.router.post(`${this.path}login`, [sessionMiddleware, validationMiddleware(LoginUserDto, 'body')], this.authController.logIn);
    this.router.post(`${this.path}logout`, authMiddleware, this.authController.logOut);
    this.router.post(`${this.path}refresh`, tokenMiddleware, this.authController.refreshAccess);
  }

  private getAuthController() {
    return this.authController;
  }
}

export default AuthRoute;
