import { NextFunction, Request, Response } from 'express';
import { UserDto } from '@dtos/users.dto';
import { RequestWithUser } from '@interfaces/auth.interface';
import { IUser } from '@interfaces/users.interface';
import AuthService from '@services/auth.service';
import { LoginUserDto, ResetPasswordFormDto } from '@dtos/auth.dtos';

class AuthController {
  private authService = new AuthService();

  public signUp = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const userData: UserDto = req.body;
      const signUpUserData: IUser = await this.authService.signup(userData);

      res.status(201).json({ data: signUpUserData, message: 'signup' });
    } catch (error) {
      next(error);
    }
  };

  public logIn = async (req: RequestWithUser, res: Response, next: NextFunction) => {
    try {
      const userData: LoginUserDto = req.body;
      const { cookie, user, refresh_token } = await this.authService.login(userData);
      req.session.user = user;
      req.session.createdAt = Date.now();
      res.setHeader('Set-Cookie', [cookie]);
      res.setHeader('Authorization', `Bearer ${refresh_token}`);
      res.status(200).json({ data: user, message: 'login' }).send();
    } catch (error) {
      next(error);
    }
  };

  public logOut = async (req: RequestWithUser, res: Response, next: NextFunction) => {
    try {
      const { user } = req.session;
      const logOutUserData: IUser = await this.authService.logout(user);
      delete req.session.user;
      res.setHeader('Set-Cookie', ['Authorization=; Max-age=0']);
      res.status(200).json({ data: logOutUserData, message: 'logout' }).send();
    } catch (error) {
      next(error);
    }
  };

  public refreshAccess = async (req: RequestWithUser, res: Response, next: NextFunction) => {
    try {
      const token = req.headers['authorization'].split(' ')[1];
      const { cookie, user, refresh_token } = await this.authService.refresh(token);
      req.session.user = user;
      req.session.createdAt = Date.now();
      res.setHeader('Set-Cookie', [cookie]);
      res.setHeader('Authorization', `Bearer ${refresh_token}`);
      res.status(200).json({ data: user, message: 'refresh' }).send();
    } catch (error) {
      next(error);
    }
  };

	public reset = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
		const resetPasswordData: ResetPasswordFormDto = req.body;
		try {
			const transporter = await this.authService.requestResetToken(resetPasswordData);
			res.status(200).json({ data: transporter, message: 'OK' });
		} catch (error) {
			next(error);
		}
	};

  public getAuthService = (): AuthService => {
    return this.authService;
  };
}

export default AuthController;
