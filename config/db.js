const mongoose = require('mongoose');
const config = require('config');
const db = config.get('mongoUri');

const connectDB = async (req, res) => {
  try {
    await mongoose.connect(db, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
      useFindAndModify: false,
      useCreateIndex: true
    });
    console.log('db connected');
  } catch(err) {
    console.error(err);
  }
}

module.exports = connectDB;
