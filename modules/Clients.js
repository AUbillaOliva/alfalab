const mongoose = require('mongoose');

const ClientSchema = new mongoose.Schema({
  firstname: {
    type: String,
    required: true
  },
  lastname: {
    type: String,
    required: true
  },
  instagram: {
    type: String
  },
  email: {
    type: String
  }
});

module.exports = Client = mongoose.model('client', ClientSchema);
