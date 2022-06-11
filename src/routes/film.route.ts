import { Router } from 'express';
import { validationMiddleware } from '@middlewares/validation.middleware';
import FilmsController from '@controllers/film.controller';
import { Routes } from '@interfaces/routes.interface';
import { FilmDto } from '@dtos/films.dto';
import { accessMiddleware } from '@middlewares/access.middleware';

class FilmRoute implements Routes {
  public path = '/api/films';
  public router = Router();
  public filmsController = new FilmsController();

  constructor() {
    this.initializeRoutes();
  }

  private initializeRoutes() {
    this.router.get(`${this.path}`, this.filmsController.getAllFilmTypes);
    this.router.get(`${this.path}/:id`, this.filmsController.getFilmTypeById);
    this.router.post(`${this.path}`, [accessMiddleware, validationMiddleware(FilmDto, 'body')], this.filmsController.createFilmType);
    this.router.put(`${this.path}/:id`, [accessMiddleware, validationMiddleware(FilmDto, 'body', true)], this.filmsController.updateFilmType);
    this.router.delete(`${this.path}/:id`, accessMiddleware, this.filmsController.deleteFilmType);
  }
}

export default FilmRoute;
