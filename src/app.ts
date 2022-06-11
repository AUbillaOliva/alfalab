import MongoStore from 'connect-mongo';
import compression from 'compression';
import cookieParser from 'cookie-parser';
import cors from 'cors';
import express from 'express';
import helmet from 'helmet';
import hpp from 'hpp';
import morgan from 'morgan';
import { connect, set } from 'mongoose';
import swaggerJSDoc from 'swagger-jsdoc';
import swaggerUi from 'swagger-ui-express';
import session from 'express-session';
import CONFIG from '@config';
import { dbConnection } from '@databases';
import { Routes } from '@interfaces/routes.interface';
import errorMiddleware from '@middlewares/error.middleware';
import { logger, stream } from '@utils/logger';

class App {
  public app: express.Application;
  public env: string;
  public port: string | number;

  constructor(routes: Routes[]) {
    this.app = express();
    this.env = CONFIG.ENV.NODE_ENV || 'development';
    this.port = CONFIG.SERVER.PORT || 5000;

    this.connectToDatabase();
    this.initializeMiddlewares();
    this.initializeRoutes(routes);
    this.initializeSwagger();
    this.initializeErrorHandling();
  }

  public listen() {
    this.app.listen(this.port, () => {
      logger.info(`=================================`);
      logger.info(`======= ENV: ${this.env} =======`);
      logger.info(`🚀 App listening on the port ${this.port}`);
      logger.info(`=================================`);
    });
  }

  public getServer() {
    return this.app;
  }

  private connectToDatabase() {
    if (this.env !== 'production') {
      set('debug', true);
    }

    connect(dbConnection.url, dbConnection.options);
  }

  private initializeMiddlewares() {
    if (this.env === 'production') {
      this.app.enable('trust proxy');
      this.app.set('trustproxy', true);
      this.app.use(morgan('combined', { stream }));
      this.app.use(cors({ origin: CONFIG.SERVER.URL, credentials: true, exposedHeaders: 'Authorization' }));
    } else if (this.env === 'development') {
      this.app.use(morgan('dev', { stream }));
      this.app.use(cors({ origin: true, credentials: true, exposedHeaders: 'Authorization' }));
    }
    this.app.use(hpp());
    this.app.use(helmet());
    this.app.use(compression());
    this.app.use(express.json());
    this.app.use(express.urlencoded({ extended: true }));
    this.app.use(cookieParser());
    this.app.use(
      session({
        store: MongoStore.create({
          mongoUrl: CONFIG.DATABASE.MONGO_URI,
          ttl: parseInt(CONFIG.SESSION.SESSION_MAX_AGE),
          dbName: 'main',
        }),
        secret: CONFIG.SESSION.SESSION_SECRET,
        saveUninitialized: false,
        resave: false,
        rolling: true,
        proxy: this.env === 'production',
        cookie: {
          httpOnly: true,
          maxAge: parseInt(CONFIG.COOKIE.COOKIE_MAX_AGE),
          sameSite: this.env === 'production' ? 'none' : 'lax',
          secure: this.env === 'production',
        },
      }),
    );
  }

  private initializeRoutes(routes: Routes[]) {
    routes.forEach(route => {
      this.app.use('/', route.router);
    });
  }

  private initializeSwagger() {
    const options = {
      swaggerDefinition: {
        info: {
          title: 'ALFALAB API',
          version: '1.1.0',
          description: 'Api docs',
        },
      },
      apis: ['swagger.yaml'],
    };

    const specs = swaggerJSDoc(options);
    this.app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(specs));
  }

  private initializeErrorHandling() {
    this.app.use(errorMiddleware);
  }
}

export default App;
