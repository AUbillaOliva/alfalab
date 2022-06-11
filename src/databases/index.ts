import CONFIG from '@config';

export const dbConnection = {
  url: CONFIG.DATABASE.MONGO_URI,
  options: {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    useFindAndModify: false,
  },
};
