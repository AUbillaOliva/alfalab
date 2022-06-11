import { model, Schema, Document, Model } from 'mongoose';
import { FormatDto } from '@dtos/formats.dto';

const formatSchema: Schema<FormatDto> = new Schema<FormatDto>(
  {
    format_type: {
      type: String,
      required: true,
      unique: true,
    },
  },
  { versionKey: false },
);

const formatModel: Model<FormatDto & Document<any, any, any>, {}, {}> = model<FormatDto & Document>('Format', formatSchema);

export default formatModel;
