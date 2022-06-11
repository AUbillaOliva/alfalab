import { model, Schema, Document } from 'mongoose';
import { IFilm } from '@interfaces/films.interface';

const filmsSchema: Schema = new Schema(
  {
    film_type: {
      type: String,
      required: true,
      unique: true,
    },
    price: {
      type: Number,
      required: true,
      default: 0,
    },
  },
  { versionKey: false },
);

const filmsModel = model<IFilm & Document>('Film', filmsSchema);

export default filmsModel;
