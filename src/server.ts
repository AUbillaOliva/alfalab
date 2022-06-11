import App from '@/app';
import AuthRoute from '@routes/auth.route';
import IndexRoute from '@routes/index.route';
import UsersRoute from '@routes/users.route';
import validateEnv from '@utils/validateEnv';
import FilmRoute from '@routes/film.route';
import OrdersRoute from '@routes/orders.route';
import FormatRoute from '@routes/format.route';
import TokenRoute from './routes/tokens.route';

validateEnv();

const app = new App([new IndexRoute(), new UsersRoute(), new AuthRoute(), new OrdersRoute(), new FilmRoute(), new FormatRoute(), new TokenRoute()]);

app.listen();
