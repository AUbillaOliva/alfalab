import { HttpException } from '@exceptions/HttpException';
import ordersModel from '@models/orders.model';
import { isEmpty } from '@utils/util';
import { OrderDto } from '@dtos/orders.dto';

class UserService {
  public orders = ordersModel;

  public async findAllOrders(): Promise<OrderDto[]> {
    const orders: OrderDto[] = await this.orders
      .find()
      .populate('order_list.format')
      .populate('order_list.film')
      .populate('order_list.responsible')
      .populate('last_edit')
      .populate('comments.author');
    return orders;
  }

  public async findOrderById(orderId: string): Promise<OrderDto> {
    if (isEmpty(orderId)) throw new HttpException(400, 'Bad request');

    const findOrder: OrderDto = await this.orders
      .findOne({ _id: orderId })
      .populate('order_list.format')
      .populate('order_list.film')
      .populate('order_list.responsible')
      .populate('last_edit')
      .populate('comments.author');
    if (!findOrder) throw new HttpException(404, 'Order not found');

    return findOrder;
  }

  public async createOrder(orderData: OrderDto): Promise<OrderDto> {
    if (isEmpty(orderData)) throw new HttpException(400, 'Bad request');

    const createOrderData: OrderDto = await this.orders.create({ ...orderData });

    return createOrderData;
  }

  public async updateOrder(orderId: string, orderData: OrderDto): Promise<OrderDto> {
    if (isEmpty(orderData)) throw new HttpException(400, 'Bad request');

    const updateOrderById: OrderDto = await this.orders.findByIdAndUpdate(orderId, { ...orderData });
    if (!updateOrderById) throw new HttpException(404, 'Order not found');

    return updateOrderById;
  }

  public async deleteOrder(orderId: string): Promise<OrderDto> {
    const deleteUserById: OrderDto = await this.orders.findByIdAndDelete(orderId);
    if (!deleteUserById) throw new HttpException(404, 'Order not found');

    return deleteUserById;
  }
}

export default UserService;
