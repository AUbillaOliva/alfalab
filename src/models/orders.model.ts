import { model, Schema, Document, Types, Model } from 'mongoose';
import { IComment, IDigitized, IOrderItem } from '@interfaces/orders.interface';
import { IClient } from '@interfaces/clients.interface';
import { OrderDto } from '@dtos/orders.dto';

const ordersSchema = new Schema({
  order_list: {
    required: true,
    _id: false,
    type: [
      new Schema<IOrderItem>(
        {
          film: {
            required: true,
            type: Types.ObjectId,
            ref: 'Film',
          },
          format: {
            required: true,
            type: Types.ObjectId,
            ref: 'Format',
          },
          responsible: {
            required: true,
            type: Types.ObjectId,
            ref: 'User',
          },
          price: {
            required: true,
            type: Number,
          },
          digitized: {
            required: false,
            type: new Schema<IDigitized>(
              {
                quality: {
                  required: true,
                  type: String,
                },
                format: {
                  required: true,
                  type: String,
                },
                price: {
                  required: true,
                  type: Number,
                },
              },
              {
                _id: false,
              },
            ),
          },
          level: {
            required: false,
            type: Number,
            default: 0,
          },
          status: {
            type: String,
          },
        },
        {
          _id: false,
        },
      ),
    ],
  },
  client: {
    required: true,
    type: new Schema<IClient>(
      {
        name: {
          required: true,
          type: String,
        },
        email: {
          required: true,
          type: String,
        },
        instagram: {
          type: String,
        },
        phone: {
          type: String,
        },
      },
      {
        _id: false,
      },
    ),
  },
  last_edit: {
    required: true,
    type: Types.ObjectId,
    ref: 'User',
  },
  price: {
    required: true,
    type: Number,
  },
  comments: {
    type: [
      new Schema<IComment>({
        author: {
          required: true,
          type: Types.ObjectId,
          ref: 'User',
        },
        date: {
          required: true,
          type: Date,
          default: Date.now(),
        },
        message: {
          required: true,
          type: String,
        },
      }),
    ],
  },
  delivered_date: {
    type: Date,
  },
  created_at: {
    required: true,
    type: Date,
    default: Date.now(),
  },
});

ordersSchema.loadClass(OrderDto);

const ordersModel: Model<OrderDto & Document<any, any, any>, {}, {}> = model<OrderDto & Document>('Order', ordersSchema);

export default ordersModel;
