import { HttpException } from '@exceptions/HttpException';
import { isEmpty } from '@utils/util';
import filmsModel from '@models/films.model';
import { IFilm } from '@interfaces/films.interface';
import { FilmDto } from '@dtos/films.dto';

class FilmsService {
  public films = filmsModel;

  public async findAllFilmTypes(): Promise<IFilm[]> {
    const filmTypes: IFilm[] = await this.films.find();
    return filmTypes;
  }

  public async findFilmTypeById(filmId: string): Promise<IFilm> {
    if (isEmpty(filmId)) throw new HttpException(400, 'Bad request');

    const foundFilmType: IFilm = await this.films.findOne({ _id: filmId });
    if (!foundFilmType) throw new HttpException(404, 'FilmType not found');

    return foundFilmType;
  }

  public async createFilmType(filmData: FilmDto): Promise<IFilm> {
    if (isEmpty(filmData)) throw new HttpException(400, 'Bad Request');

    const createFilmTypeData: IFilm = await this.films.create({ ...filmData });

    return createFilmTypeData;
  }

  public async updateFilmType(filmTypeId: string, filmData: FilmDto): Promise<IFilm> {
    if (isEmpty(filmData)) throw new HttpException(400, 'Bad Request');

    const updateFilmTypeById: IFilm = await this.films.findByIdAndUpdate(filmTypeId, { ...filmData });
    if (!updateFilmTypeById) throw new HttpException(404, 'FilmType not found');

    return updateFilmTypeById;
  }

  public async deleteFilmTypeById(filmId: string): Promise<IFilm> {
    const deletedFilmTypeById: IFilm = await this.films.findByIdAndDelete(filmId);
    if (!deletedFilmTypeById) throw new HttpException(404, 'FilmType not found');

    return deletedFilmTypeById;
  }
}

export default FilmsService;
