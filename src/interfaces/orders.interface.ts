import { OrderCommentDto, OrderItemDto } from "@dtos/orders.dto";
import { IOrderUser } from "@interfaces/users.interface";
import { Document, Model, Types } from "mongoose";
import { IClient } from "@interfaces/clients.interface";

export interface IFilm extends Document<Types.ObjectId> {
    _id: Types.ObjectId;
    film_type: string;
    price: number;
}

export interface IFormat extends Document<Types.ObjectId> {
    _id: Types.ObjectId;
    format_type: string;
}

export interface IDigitized {
    quality: string;
    format: string;
    price: number;
}

export interface IOrderItem {
    film: IFilm | string;
    format: IFormat | string;
    responsible: IOrderUser | string;
    price: number;
    digitized?: IDigitized;
    level?: number;
    status?: string;
}

export interface IComment {
    author: string | IOrderUser;
    date: Date;
    message: string;
}

export interface IOrder {
    _id?: Types.ObjectId;
    order_list: OrderItemDto[];
    client: IClient;
    last_edit: IOrderUser;
    price: number;
    created_at: Date;
    status: String;
    comments?: OrderCommentDto[];
    delivered_date?: Date;
}

export interface IOrderQuery {
    price?: { $gte?: number; $lte?: number };
    status?: string;
}

export interface IOrderQueryOptions {
    sort?: { sortBy?: string; sortOrder?: string | number };
    skip?: number;
    limit?: number;
}

export interface IOrderDocument
    extends IOrder,
        Document<Types.ObjectId, any, any> {}
export type IOrderModel = Model<IOrderDocument>;
