import filmsService from '@services/films.service';
import { NextFunction, Request, Response } from 'express';
import { IFilm } from '@interfaces/films.interface';
import { FilmDto } from '@dtos/films.dto';

class FilmsController {
  public filmsService = new filmsService();

  public getAllFilmTypes = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const findAllFilmTypesData: IFilm[] = await this.filmsService.findAllFilmTypes();

      res.status(200).json({ data: findAllFilmTypesData, message: 'findAll' });
    } catch (error) {
      next(error);
    }
  };

  public getFilmTypeById = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const filmId: string = req.params.id;
      const findOneFilmTypeData: IFilm = await this.filmsService.findFilmTypeById(filmId);

      res.status(200).json({ data: findOneFilmTypeData, message: 'findOne' });
    } catch (error) {
      next(error);
    }
  };

  public createFilmType = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const filmData: FilmDto = req.body;
      const createFilmType: IFilm = await this.filmsService.createFilmType(filmData);

      res.status(201).json({ data: createFilmType, message: 'created' });
    } catch (error) {
      next(error);
    }
  };

  public updateFilmType = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const filmId: string = req.params.id;
      const filmData: FilmDto = req.body;
      const updateFilmTypeData: IFilm = await this.filmsService.updateFilmType(filmId, filmData);

      res.status(200).json({ data: updateFilmTypeData, message: 'updated' });
    } catch (error) {
      next(error);
    }
  };

  public deleteFilmType = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const filmId: string = req.params.id;
      const deleteFilmData: IFilm = await this.filmsService.deleteFilmTypeById(filmId);

      res.status(200).json({ data: deleteFilmData, message: 'deleted' });
    } catch (error) {
      next(error);
    }
  };
}

export default FilmsController;
