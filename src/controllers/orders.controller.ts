import { NextFunction, Request, Response } from 'express';
import ordersService from '@services/orders.service';
import { OrderDto } from '@dtos/orders.dto';

class OrdersController {
  public ordersService = new ordersService();

  public getOrders = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const findAllOrdersData: OrderDto[] = await this.ordersService.findAllOrders();

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
