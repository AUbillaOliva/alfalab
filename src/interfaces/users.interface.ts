import { Types, Document } from 'mongoose';

export interface IUser {
  _id: Types.ObjectId;
  email: string;
  password: string;
  name: string;
  registration_date: number;
  location: string;
  role: string;
}

export interface IOrderUser extends Document<Types.ObjectId> {
  _id: Types.ObjectId;
  email: string;
  name: string;
  location: string;
  role: string;
}
