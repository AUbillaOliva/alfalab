const mongoose = require('mongoose');
const config = require('config');
const db = config.get("mongoUri");

const connectDB = async (req, res) => {
  try {
    await mongoose.connect(db, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
      useFindAndModify: false,
      useCreateIndex: true
    }).catch((err) => {
      process.stdout.write('\033c');
      console.log(err);
    }).then(() => {
      console.log('Database connected');
    });
  } catch(err) {
    process.stdout.write('\033c');
    console.error(err);
  }
}

module.exports = connectDB;
