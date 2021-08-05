var express = require("express");
var app = express();
var port = 3000;
var account = require('./router/Account')
var product = require('./router/product')

var connect = require("./connect/connectDB");

app.use('/',account)
app.use('/product',product)

app.listen(port, () => {
  console.log("Server runing at port : " + port);
});
