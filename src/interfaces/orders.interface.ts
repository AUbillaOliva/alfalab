import { IOrderUser } from '@interfaces/users.interface';
import { IClient } from './clients.interface';

export interface IFilm {
  _id: string;
  film_type: string;
  price: number;
}

export interface IFormat {
  _id: string;
  format_type: string;
}

export interface IDigitized {
  quality: string;
  format: string;
  price: number;
}

export interface ILevel {
  level: number;
  price: number;
}

export interface IOrderItem {
  film: IFilm | string;
  format: IFormat | string;
  responsible: IOrderUser | string;
  price: number;
  digitized?: IDigitized;
  level?: ILevel;
  status?: string;
}

export interface IComment {
  author: string | IOrderUser;
  date: Date;
  message: string;
}

export interface IOrder {
  _id?: string;
  order_list: IOrderItem[];
  client: IClient;
  last_edit: string | IOrderUser;
  price: number;
  created_at: Date;
  comments?: IComment[];
  delivered_date?: Date;
}
