import { IsMongoId, IsNotEmpty, IsNumber, IsString, ValidateIf } from 'class-validator';
import { isEmpty } from '@utils/util';

export class FilmDto {
  constructor(film_type: string, price: number, _id?: string) {
    this._id = _id;
    this.film_type = film_type;
    this.price = price;
  }

  @ValidateIf((o: string) => isEmpty(o))
  @IsMongoId()
  public _id?: string;

  @IsNotEmpty({ message: 'Debes ingresar un tipo de película.' })
  @IsString()
  public film_type: string;

  @IsNumber()
  public price: number;
}
