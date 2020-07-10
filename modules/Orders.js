const mongoose = require('mongoose');
//const autoincrement = require('mongoose-auto-increment');

const OrderSchema = new mongoose.Schema({
  number : {
    type: Number,
    //required: true,
    unique: true,
    ref: 'OrderNumber'
  },
  responsible: {
    type: String,
    //required: true
  },
  checkin: {
    type: String
    //required: true
  },
  checkout: {
    type: String
  },
  type: {
    type: String
  },
  status: {
    type: Boolean
  },
  price: {
    type: Number
  },
  client: {
    type: mongoose.Schema.Types.Object,
    ref: 'client'
  }
});

//OrderSchema.plugin(autoincrement.plugin, 'OrderNumber');
module.exports = Order = mongoose.model('orders', OrderSchema);
