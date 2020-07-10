const express = require('express');
const app = express();
const config = require('config');

const connectDB  = require('./config/db.js');

//let connection = connectDB();
//autoIncrement.initialize(connection);

connectDB();

const PORT = process.env.PORT || 5000;

app.use(express.json());

app.use('/api/orders', require('./routes/api/orders'));

app.listen(PORT, () => {
  console.log(`server running on port ${PORT}`);
})
