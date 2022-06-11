import tokensModel from '@models/token.model';
import { HttpException } from '@exceptions/HttpException';
import { isEmpty } from '@utils/util';
import { IToken } from '@interfaces/tokens.interface';

class TokensService {
  public tokens = tokensModel;

  public async findAllTokens(userId: string): Promise<IToken[]> {
    const tokens: IToken[] = await this.tokens.find({ user: userId });
    return tokens;
  }

  public async findTokenById(tokenId: string, userId: string): Promise<IToken> {
    if (isEmpty(tokenId)) throw new HttpException(400, 'Bad request');

    const foundToken: IToken = await this.tokens.findOne({ _id: tokenId, user: userId });
    if (isEmpty(foundToken)) throw new HttpException(404, 'Token not found');

    return foundToken;
  }

  public async revokeTokenById(tokenId: string, userId: string): Promise<IToken> {
    if (isEmpty(tokenId)) throw new HttpException(400, 'Bad request');

    const foundToken: IToken = await this.tokens.findOne({ _id: tokenId, user: userId });
    if (isEmpty(foundToken)) throw new HttpException(404, 'Token not found');

    // TODO: REQUEST CODE
    if (foundToken.revoked) throw new HttpException(428, 'Token is already revoked');

    const revokedToken: IToken = await this.tokens.findByIdAndUpdate(
      foundToken._id,
      {
        revoked: true,
        expires: new Date(Date.now()),
        token: null,
        expired_token: foundToken.token,
      },
      { new: true },
    );

    return revokedToken;
  }

  public async deleteTokenById(tokenId: string, userId: string): Promise<IToken> {
    if (isEmpty(tokenId)) throw new HttpException(400, 'Bad request');

    const foundToken: IToken = await this.tokens.findOne({ _id: tokenId, user: userId });
    if (isEmpty(foundToken)) throw new HttpException(404, 'Token not found');

    const deletedToken: IToken = await this.tokens.findByIdAndDelete(foundToken._id);

    return deletedToken;
  }
}

export default TokensService;
