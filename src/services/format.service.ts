import formatModel from '@models/formats.model';
import { HttpException } from '@exceptions/HttpException';
import { isEmpty } from '@utils/util';
import { FormatDto } from '@dtos/formats.dto';

class FormatsService {
  public formats = formatModel;

  public async findAllFormats(): Promise<FormatDto[]> {
    const formats: FormatDto[] = await this.formats.find();
    return formats;
  }

  public async findFormatById(formatId: string): Promise<FormatDto> {
    if (isEmpty(formatId)) throw new HttpException(400, 'Bad request');

    const foundFormat: FormatDto = await this.formats.findOne({ _id: formatId });
    if (!foundFormat) throw new HttpException(404, 'Format not found');

    return foundFormat;
  }

  public async createFormat(formatData: FormatDto): Promise<FormatDto> {
    if (isEmpty(formatData)) throw new HttpException(400, 'Bad Request');

    const createFormatData: FormatDto = await this.formats.create({ ...formatData });

    return createFormatData;
  }

  public async updateFormat(formatId: string, formatData: FormatDto): Promise<FormatDto> {
    if (isEmpty(formatData)) throw new HttpException(400, 'Bad Request');

    const updateFormatById: FormatDto = await this.formats.findByIdAndUpdate(formatId, { ...formatData });
    if (!updateFormatById) throw new HttpException(404, 'Format not found');

    return updateFormatById;
  }

  public async deleteFormatById(filmId: string): Promise<FormatDto> {
    const deleteFormatById: FormatDto = await this.formats.findByIdAndDelete(filmId);
    if (!deleteFormatById) throw new HttpException(404, 'Format not found');

    return deleteFormatById;
  }
}

export default FormatsService;
