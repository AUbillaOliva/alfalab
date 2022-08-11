import { config } from 'dotenv';
config({ path: `.env.${process.env.NODE_ENV || 'development'}.local` });

export const CREDENTIALS = process.env.CREDENTIALS === 'true';

// App
const APP_URL = process.env.APP_URL || 'https://alfalab.herokuapp.com';
const APP = {
  URL: APP_URL,
};

// Env
const NODE_ENV = process.env.NODE_ENV || 'development';
const ENV = {
  NODE_ENV,
};

// Database
const MONGO_URI = process.env.MONGO_URI || '';
const DATABASE = {
  MONGO_URI,
};

// Tokens
const ACCESS_TOKEN_SECRET = process.env.ACCESS_TOKEN_SECRET || '';
const ACCESS_TOKEN_MAX_AGE = process.env.ACCESS_TOKEN_MAX_AGE || '0';
const REFRESH_TOKEN_SECRET = process.env.REFRESH_TOKEN_SECRET || '';
const REFRESH_TOKEN_MAX_AGE = process.env.REFRESH_TOKEN_MAX_AGE || '0';
const TOKEN = {
  ACCESS_TOKEN_SECRET,
  ACCESS_TOKEN_MAX_AGE,
  REFRESH_TOKEN_SECRET,
  REFRESH_TOKEN_MAX_AGE,
};

// Session
const SESSION_SECRET = process.env.SESSION_SECRET || '';
const SESSION_NAME = process.env.SESSION_NAME || 'sid';
const SESSION_MAX_AGE = process.env.SESSION_MAX_AGE || '180000';
const SESSION_ABSOLUTE_AGE = process.env.SESSION_ABSOLUTE_AGE || '3600000';
const SESSION = {
  SESSION_SECRET,
  SESSION_NAME,
  SESSION_MAX_AGE,
  SESSION_ABSOLUTE_AGE,
};

// Cookie
const COOKIE_MAX_AGE = process.env.COOKIE_MAX_AGE || '1800000';
const COOKIE = {
  COOKIE_MAX_AGE,
};

// Server
const HOSTNAME = process.env.HOSTNAME || 'localhost';
const PORT = process.env.PORT || '5000';
const URL = process.env.SERVER_URL || 'https://alfalab-api.herokuapp.com';
const SERVER = {
  HOSTNAME,
  PORT,
  URL,
};

// SUPPORT
const SUPPORT_EMAIL = process.env.SUPPORT_EMAIL || '';
const SUPPORT_EMAIL_HOST = process.env.SUPPORT_EMAIL_HOST || '';
const SUPPORT_EMAIL_HOST_USER = process.env.SUPPORT_EMAIL_HOST_USER || '';
const SUPPORT_EMAIL_HOST_PASSWORD = process.env.SUPPORT_EMAIL_HOST_PASSWORD || '';
const SUPPORT_EMAIL_PORT = process.env.SUPPORT_EMAIL_PORT || '';
const SUPPORT = {
  SUPPORT_EMAIL,
  SUPPORT_EMAIL_HOST,
  SUPPORT_EMAIL_HOST_USER,
  SUPPORT_EMAIL_HOST_PASSWORD,
  SUPPORT_EMAIL_PORT,
};

const CONFIG = {
  APP,
  DATABASE,
  ENV,
  TOKEN,
  COOKIE,
  SESSION,
  SERVER,
  SUPPORT,
};

export default CONFIG;
