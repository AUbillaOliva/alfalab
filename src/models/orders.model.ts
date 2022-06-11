import { model, Schema, Document, Types, Model } from 'mongoose';
import { DigitizedDto, LevelDto, OrderCommentDto, OrderDto, OrderItemDto } from '@dtos/orders.dto';
import { ClientDto } from '@dtos/clients.dto';

const ordersSchema: Schema<OrderDto> = new Schema<OrderDto>({
  order_list: {
    required: true,
    _id: false,
    type: [
      new Schema<OrderItemDto>({
        _id: false,
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
          type: new Schema<DigitizedDto>({
            _id: false,
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
          }),
        },
        level: {
          required: false,
          type: new Schema<LevelDto>({
            _id: false,
            level: {
              required: true,
              type: Number,
              default: 0,
            },
            price: {
              required: true,
              type: Number,
            },
          }),
        },
        status: {
          type: String,
        },
      }),
    ],
  },
  client: {
    required: true,
    type: new Schema<ClientDto>({
      _id: false,
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
    }),
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
      new Schema<OrderCommentDto>({
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

const ordersModel: Model<OrderDto & Document<any, any, any>, {}, {}> = model<OrderDto & Document>('Order', ordersSchema);

export default ordersModel;
