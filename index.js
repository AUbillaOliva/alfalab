require('newrelic');
require('dotenv').config();
const express = require('express');
const app = express();
const autoincrement = require('mongoose-auto-increment');
const mongoose = require('mongoose');

const connectDB  = require('./database/db.js');

connectDB();
autoincrement.initialize(mongoose.connection);

const PORT = process.env.PORT || 5000;

app.use(express.json());

app.use('/api/delivered', require('./routes/api/delivered'));
app.use('/api/orders', require('./routes/api/orders'));
app.use('/support/report', require('./routes/support/report'));
app.use('/api/auth', require('./routes/api/auth'));
app.use('/api/users', require('./routes/api/users'));
app.use('/api/posts', require('./routes/api/posts'));

app.listen(PORT, () => {
  process.stdout.write('\033c');
  console.log(`~ Server running on port ${PORT}`);
})
