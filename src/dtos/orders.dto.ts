import {
  IsDate,
  IsDateString,
  IsMongoId,
  IsNotEmpty,
  IsNotEmptyObject,
  IsNumber,
  IsString,
  Max,
  MaxLength,
  ValidateIf,
  ValidateNested,
} from 'class-validator';
import { ClientDto } from '@dtos/clients.dto';
import { isEmpty } from '@utils/util';
import 'reflect-metadata';
import { Type } from 'class-transformer';

export class DigitizedDto {
  constructor(quality: string, format: string, price: number) {
    this.quality = quality;
    this.format = format;
    this.price = price;
  }

  @IsNotEmpty({ message: 'Debes ingresar la calidad del digitalizado.' })
  @IsString()
  public quality: string;

  @IsNotEmpty({ message: 'Debes ingresar el formato del digitalziado (JPG, TIF)' })
  @IsString()
  public format: string;

  @IsNotEmpty({ message: 'Debes ingresar el precio.' })
  @IsNumber()
  public price: number;
}

export class LevelDto {
  constructor(level: number, price: number) {
    this.level = level;
    this.price = price;
  }

  @IsNotEmpty()
  @IsNumber()
  @Max(4, { message: 'Debes ingresar un nivel de forzado entre 0 y 4' })
  public level: number;

  @IsNotEmpty()
  @IsNumber()
  public price: number;
}

export class OrderItemDto {
  constructor(film: string, format: string, responsible: string, price: number, digitized?: DigitizedDto, level?: LevelDto, status?: string) {
    this.film = film;
    this.format = format;
    this.digitized = digitized;
    this.level = level;
    this.responsible = responsible;
    this.status = status;
    this.price = price;
  }
  @IsNotEmpty({ message: 'Debes ingresar el tipo de pelicula' })
  @IsMongoId({ message: 'Debes ingresar un ID de tipo de película' })
  public film: string;

  @IsNotEmpty({ message: 'Debes ingresar el tipo de formato' })
  @IsMongoId()
  public format: string;

  @IsNotEmpty({ message: 'Debes indicar el responsable' })
  @IsMongoId()
  public responsible: string;

  @IsNotEmpty({ message: 'Debes ingresar un precio' })
  @IsNumber()
  public price: number;

  @ValidateIf((o: DigitizedDto) => isEmpty(o))
  @ValidateNested()
  public digitized?: DigitizedDto;

  @ValidateIf((o: LevelDto) => isEmpty(o))
  @ValidateNested()
  public level?: LevelDto;

  @ValidateIf((o: string) => isEmpty(o))
  @IsString()
  public status?: string;
}

export class OrderCommentDto {
  constructor(author: string, date: Date, message: string) {
    this.author = author;
    this.date = date;
    this.message = message;
  }

  @IsNotEmpty()
  @IsMongoId()
  public author: string;

  @IsNotEmpty()
  @IsDate()
  public date: Date;

  @IsNotEmpty()
  @IsString()
  @MaxLength(500, { message: 'Ingresa un mensaje no mayor a 500 caracteres.' })
  public message: string;
}

export class OrderDto {
  constructor(
    order_list: OrderItemDto[],
    client: ClientDto,
    last_edit: string,
    price: number,
    created_at: Date,
    comments?: OrderCommentDto[],
    delivered_date?: Date,
  ) {
    this.order_list = order_list;
    this.client = client;
    this.last_edit = last_edit;
    this.price = price;
    this.created_at = created_at;
    this.comments = comments;
    this.delivered_date = delivered_date;
  }

  @ValidateNested()
  @Type(() => OrderItemDto)
  public order_list: OrderItemDto[];

  @ValidateNested()
  @IsNotEmptyObject()
  @Type(() => ClientDto)
  public client: ClientDto;

  @IsNotEmpty({ message: 'Debes especificar quién fue el último que trabajó en este pedido.' })
  @IsMongoId()
  public last_edit: string;

  @IsNotEmpty({ message: 'Debes ingresar un precio.' })
  @IsNumber()
  public price: number;

  @ValidateIf((o: Date) => isEmpty(o))
  @IsDate()
  public created_at: Date;

  @ValidateIf((o: OrderCommentDto) => isEmpty(o))
  @ValidateNested()
  public comments?: OrderCommentDto[];

  @ValidateIf((o: Date) => isEmpty(o))
  @IsDateString()
  public delivered_date?: Date;
}
