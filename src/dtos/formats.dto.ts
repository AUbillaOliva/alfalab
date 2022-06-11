import { IsMongoId, IsNotEmpty, IsString, ValidateIf } from 'class-validator';
import { isEmpty } from '@utils/util';

export class FormatDto {
  constructor(format_type: string, _id?: string) {
    this._id = _id;
    this.format_type = format_type;
  }

  @ValidateIf((o: string) => isEmpty(o))
  @IsMongoId()
  public _id?: string;

  @IsNotEmpty({ message: 'Debes ingresar un formato de película.' })
  @IsString()
  public format_type: string;
}
