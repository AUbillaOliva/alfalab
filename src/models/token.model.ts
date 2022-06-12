import { model, Schema, Document, Types, Model } from 'mongoose';
import { IToken } from '@interfaces/tokens.interface';

const tokensSchema: Schema<IToken & Document> = new Schema<IToken & Document>(
  {
    user: {
      type: Types.ObjectId,
      ref: 'User',
      required: true,
    },
    token: {
      type: String,
      required: true,
      unique: true,
    },
    created: {
      type: Date,
      default: Date.now(),
      required: true,
    },
    expires: {
      type: Date,
      required: true,
    },
    expired_token: {
      type: String,
      default: null,
    },
    revoked: {
      type: Boolean,
    },
  },
  { versionKey: false },
);

tokensSchema.virtual('isExpired').get(function () {
  return Date.now() >= this.expires;
});

tokensSchema.virtual('isActive').get(function () {
  return !this.revoked && !this.isExpired;
});

const tokensModel: Model<IToken & Document> = model<IToken & Document>('Token', tokensSchema);

export default tokensModel;
