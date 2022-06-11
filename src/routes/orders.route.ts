import { Router } from 'express';
import OrdersController from '@controllers/orders.controller';
import { Routes } from '@interfaces/routes.interface';
import { validationMiddleware } from '@middlewares/validation.middleware';
import { OrderDto } from '@dtos/orders.dto';
import authMiddleware from '@middlewares/auth.middleware';

class OrdersRoute implements Routes {
  public path = '/api/orders';
  public router = Router();
  public ordersController = new OrdersController();

  constructor() {
    this.initializeRoutes();
  }

  private initializeRoutes() {
    this.router.get(`${this.path}`, authMiddleware, this.ordersController.getOrders);
    this.router.get(`${this.path}/:id`, this.ordersController.getOrderById);
    this.router.post(`${this.path}`, validationMiddleware(OrderDto, 'body'), this.ordersController.createOrder);
    this.router.put(`${this.path}/:id`, validationMiddleware(OrderDto, 'body', true), this.ordersController.updateOrder);
    this.router.delete(`${this.path}/:id`, this.ordersController.deleteOrder);
  }
}

export default OrdersRoute;
