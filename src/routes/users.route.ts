import { Router } from 'express';
import UsersController from '@controllers/users.controller';
import { UserDto } from '@dtos/users.dto';
import { Routes } from '@interfaces/routes.interface';
import { validationMiddleware } from '@middlewares/validation.middleware';
import { accessMiddleware } from '@middlewares/access.middleware';
import { ResetPasswordDto } from '@dtos/auth.dtos';
import tokenMiddleware, { accessTokenMiddleware } from '@middlewares/token.middleware';

class UsersRoute implements Routes {
  public path = '/api/users';
  public router = Router();
  public usersController = new UsersController();

  constructor() {
    this.initializeRoutes();
  }

  private initializeRoutes() {
    this.router.get(`${this.path}`, this.usersController.getUsers);
    this.router.put(
			`${this.path}/reset`,
			[accessTokenMiddleware, validationMiddleware(ResetPasswordDto, 'body', false)],
			this.usersController.resetPassword,
		);
    this.router.get(`${this.path}/:id`, this.usersController.getUserById);
    this.router.put(`${this.path}/:id`, validationMiddleware(UserDto, 'body', true), this.usersController.updateUser);
    this.router.delete(`${this.path}/:id`, this.usersController.deleteUser);
  }
}

export default UsersRoute;
