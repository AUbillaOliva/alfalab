const express = require('express');
const router = express.Router();
const { check, validationResult } = require('express-validator');

const Order = require('../../modules/Orders');

router.get('/', async (req, res) => {
  try {
    const data = await Order.find().populate('data');
    if(!data){
      res.status(404).send({
        msg: 'There is not orders'
      })
    } else {
      res.send(data);
    }
  } catch(err){
    console.error(err);
  }
});

router.post('/', 
  [
    [
      //check('id').not().isEmpty(),
      //check('number').not().isEmpty(),
      //check('responsible').not().isEmpty(),
      //check('checkin').not().isEmpty()
      //check('client_id').not().isEmpty();
    ]
  ], async (req, res) => {
    const errors = validationResult(req);
    if(!errors.isEmpty()){
      return res.status(400).json({errors: errors.array()});
    }

    const {
      number,
      commentaries,
      responsible,
      checkin,
      checkout,
      type,
      status,
      price,
      client
    } = req.body;

    const orderFields = {};
    if(number){ orderFields.number = number; }
    if(commentaries){ orderFields.commentaries = commentaries.split(',').map(commentary => commentary.trim()); }
    if(checkin){ orderFields.checkin = checkin; }
    if(checkout) { orderFields.checkout = checkout; }
    if(type) { orderFields.status = status; }
    if(price) { orderFields.price = price; }
    if(client) { orderFields.client = client; }

    try {
      let order = await Order.findById(req.params.id);
      if(order) {
        order = await Order.findOneAndUpdate({
          $set: orderFields,
          new: true
        });
        return res.json(order);
      } else {
        order = new Order(orderFields);
        await order.save();
        res.json(order);
      }
      
    } catch(err){
      console.error(err);
      res.status(500).send('server error');
    }
});

module.exports = router;
