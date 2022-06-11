import { Router } from 'express';
import { validationMiddleware } from '@middlewares/validation.middleware';
import { Routes } from '@interfaces/routes.interface';
import { accessMiddleware } from '@middlewares/access.middleware';
import FormatController from '@controllers/format.controller';
import { FormatDto } from '@dtos/formats.dto';

class FormatRoute implements Routes {
  public path = '/api/formats';
  public router = Router();
  public formatsController = new FormatController();

  constructor() {
    this.initializeRoutes();
  }

  private initializeRoutes() {
    this.router.get(`${this.path}`, this.formatsController.getAllFormats);
    this.router.get(`${this.path}/:id`, this.formatsController.getFormatById);
    this.router.post(`${this.path}`, [accessMiddleware, validationMiddleware(FormatDto, 'body')], this.formatsController.createFormat);
    this.router.put(`${this.path}/:id`, [accessMiddleware, validationMiddleware(FormatDto, 'body', true)], this.formatsController.updateFormat);
    this.router.delete(`${this.path}/:id`, accessMiddleware, this.formatsController.deleteFormat);
  }
}

export default FormatRoute;
