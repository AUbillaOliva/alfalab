import { hash, compare, genSalt } from 'bcrypt';
import { sign } from 'jsonwebtoken';
import { UserDto, LoginUserDto } from '@dtos/users.dto';
import { HttpException } from '@exceptions/HttpException';
import { DataStoredInToken, TokenData } from '@interfaces/auth.interface';
import { IUser } from '@interfaces/users.interface';
import userModel from '@models/users.model';
import { isEmpty } from '@utils/util';
import CONFIG from '@config';
import tokensModel from '@models/token.model';
import { IToken } from '@interfaces/tokens.interface';
import { TokenDto } from '@dtos/tokens.dto';
import jwt from 'jsonwebtoken';

class AuthService {
  public users = userModel;
  public tokens = tokensModel;

  public async signup(userData: UserDto): Promise<IUser> {
    if (isEmpty(userData)) throw new HttpException(400, 'Bad request');

    const findUser: IUser = await this.users.findOne({ email: userData.email });
    if (findUser) throw new HttpException(409, `El correo ${userData.email} ya existe!`);

    const hashedPassword = await hash(userData.password, await genSalt(10));
    const createUserData: IUser = await this.users.create({ ...userData, password: hashedPassword });

    return createUserData;
  }

  public async login(userData: LoginUserDto): Promise<{ cookie: string; user: IUser; refresh_token: string }> {
    if (isEmpty(userData)) throw new HttpException(400, 'Bad request');

    const findUser: IUser = await this.users.findOne({ email: userData.email }).select('+password');
    if (!findUser) throw new HttpException(409, 'Credenciales invalidas! Acceso no autorizado.');

    const isPasswordMatching: boolean = await compare(userData.password, findUser.password);
    if (!isPasswordMatching) throw new HttpException(409, 'Credenciales invalidas! Acceso no autorizado.');

    const tokenData = this.createToken(findUser);
    const cookie = this.createCookie(tokenData);
    const refreshToken = await this.createRefreshToken(findUser);

    const responseUser: IUser = await this.users.findById(findUser._id);

    return { cookie, user: responseUser, refresh_token: refreshToken.token };
  }

  public async logout(userData: IUser): Promise<IUser> {
    if (isEmpty(userData)) throw new HttpException(400, 'Bad Request');

    const findUser: IUser = await this.users.findById(userData._id);
    if (!findUser) throw new HttpException(409, 'Acceso no autorizado');

    return findUser;
  }

  public async refresh(token: string): Promise<{ cookie: string; user: IUser; refresh_token: string }> {
    if (isEmpty(token)) throw new HttpException(400, 'Bad Request');

    const secretKey: string = CONFIG.TOKEN.REFRESH_TOKEN_SECRET;

    const { _id }: any = jwt.verify(token, secretKey);

    const findUser: IUser = await this.users.findById(_id);
    if (isEmpty(findUser)) throw new HttpException(409, 'Credenciales inválidas! Acceso no autorizado,');

    const tokenData = this.createToken(findUser);
    const cookie = this.createCookie(tokenData);
    const refreshToken = await this.createRefreshToken(findUser);

    const responseUser: IUser = await this.users.findById(findUser._id);

    return { cookie: cookie, user: responseUser, refresh_token: refreshToken.token };
  }

  private async createRefreshToken(user: IUser): Promise<TokenData> {
    const dataStoredInToken: DataStoredInToken = { _id: user._id.toString() };
    const expiresIn: number = parseInt(CONFIG.TOKEN.REFRESH_TOKEN_MAX_AGE);
    const secretKey: string = CONFIG.TOKEN.REFRESH_TOKEN_SECRET;
    let refreshToken: string = sign(dataStoredInToken, secretKey, { expiresIn });

    const foundRefreshToken: IToken = await this.tokens.findOne({ user: user._id });
    if (foundRefreshToken) {
      if (foundRefreshToken.isExpired || !foundRefreshToken.isActive) {
        foundRefreshToken.expired_token = foundRefreshToken.token;
        foundRefreshToken.token = refreshToken;
        const updatedRefreshToken: IToken = await this.tokens.findByIdAndUpdate(foundRefreshToken._id, foundRefreshToken);
        refreshToken = updatedRefreshToken.token;
      }
    } else {
      const newToken: TokenDto = new TokenDto(user._id.toString(), refreshToken, new Date(Date.now() + expiresIn));
      const createdRefreshToken: IToken = await this.tokens.create(newToken);
      refreshToken = createdRefreshToken.token;
    }

    return { expiresIn, token: refreshToken };
  }

  private createToken(user: IUser): TokenData {
    const dataStoredInToken: DataStoredInToken = { _id: user._id.toString() };
    const secretKey: string = CONFIG.TOKEN.ACCESS_TOKEN_SECRET;
    const expiresIn: number = parseInt(CONFIG.TOKEN.ACCESS_TOKEN_MAX_AGE);

    return { expiresIn, token: sign(dataStoredInToken, secretKey, { expiresIn }) };
  }

  private createCookie(tokenData: TokenData): string {
    return `Authorization=${tokenData.token}; HttpOnly; Max-Age=${tokenData.expiresIn};`;
  }
}

export default AuthService;
