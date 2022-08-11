import { NextFunction, Request, Response } from 'express';
import ordersService from '@services/orders.service';
import { OrderDto } from '@dtos/orders.dto';
import { IOrderQuery, IOrderQueryOptions } from '@interfaces/orders.interface';
import { isEmpty } from '@utils/util';

class OrdersController {
  public ordersService = new ordersService();

  public getOrders = async (req: Request, res: Response, next: NextFunction) => {
    try {

      const limit: number = !isEmpty(req.query.limit) ? Number.parseInt(req.query.limit.toString()) : 50;
      const skip: number = !isEmpty(req.query.skip) ? Number.parseInt(req.query.skip.toString()) : 0;
      const sortBy: string = !isEmpty(req.query.sortBy) ? req.query.sortBy.toString() : 'created_at';
      const sortOrder: string = !isEmpty(req.query.sortOrder) ? req.query.sortOrder.toString() : 'desc';

      const query: IOrderQuery = {
        price: { $gte: null, $lte: null },
        status: null
      }

      const options: IOrderQueryOptions = {
        sort: { sortBy: sortBy, sortOrder: sortOrder },
        limit: limit,
        skip: skip,
      }
      
      if (!isEmpty(req.query.max_price)) {
        query.price.$lte = Number.parseInt(req.query.max_price.toString());
      } else delete query.price.$lte;
      if (!isEmpty(req.query.min_price)) {
        query.price.$gte = Number.parseInt(req.query.min_price.toString());
      } else delete query.price.$gte;
      if (!isEmpty(req.query.status)) {
        query.status = req.query.status.toString().toLowerCase().trim();
      } else delete query.status;      

      if (isEmpty(query.price.$lte) && isEmpty(query.price.$gte)) delete query.price;
      if (isEmpty(query.status)) delete query.status;

      const findAllOrdersData: OrderDto[] = await this.ordersService.findAllOrders(query, options);

      res.status(200).json({ data: findAllOrdersData, message: 'findAll' });
    } catch (error) {
      next(error);
    }
  };

  public getOrderById = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const userId: string = req.params.id;
      const findOneOrderData: OrderDto = await this.ordersService.findOrderById(userId);

      res.status(200).json({ data: findOneOrderData, message: 'findOne' });
    } catch (error) {
      next(error);
    }
  };

  public createOrder = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const orderData: OrderDto = req.body;
      const createOrderData: OrderDto = await this.ordersService.createOrder(orderData);

      res.status(201).json({ data: createOrderData, message: 'created' });
    } catch (error) {
      next(error);
    }
  };

  public updateOrder = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const orderId: string = req.params.id;
      const orderData: OrderDto = req.body;
      const updateOrderData: OrderDto = await this.ordersService.updateOrder(orderId, orderData);

      res.status(200).json({ data: updateOrderData, message: 'updated' });
    } catch (error) {
      next(error);
    }
  };

  public deleteOrder = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const orderId: string = req.params.id;
      const deleteOrderData: OrderDto = await this.ordersService.deleteOrder(orderId);

      res.status(200).json({ data: deleteOrderData, message: 'deleted' });
    } catch (error) {
      next(error);
    }
  };
}

export default OrdersController;
