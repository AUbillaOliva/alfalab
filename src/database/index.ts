import CONFIG from '@config';
import { ConnectOptions } from 'mongoose';

export const dbConnection: { url: string; options?: ConnectOptions } = {
  url: CONFIG.DATABASE.MONGO_URI,
  options: {},
};
