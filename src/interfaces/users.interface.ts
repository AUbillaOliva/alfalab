export interface IUser {
  _id: string;
  email: string;
  password: string;
  name: string;
  registration_date: number;
  location: string;
  role: string;
}

export interface IOrderUser {
  _id: string;
  email: string;
  name: string;
  location: string;
  role: string;
}
