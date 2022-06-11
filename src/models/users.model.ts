import { model, Schema, Document } from 'mongoose';
import { IUser } from '@interfaces/users.interface';

const userSchema: Schema = new Schema(
  {
    registration_date: {
      default: Date.now(),
      required: true,
      type: Date,
    },
    email: {
      type: String,
      required: true,
      unique: true,
    },
    password: {
      type: String,
      required: true,
      select: false,
    },
    name: {
      type: String,
    },
    role: {
      type: String,
      required: true,
      default: 'basic',
    },
    location: {
      type: String,
    },
  },
  { versionKey: false },
);

const userModel = model<IUser & Document>('User', userSchema);

export default userModel;
