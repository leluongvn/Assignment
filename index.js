var express = require("express");
var app = express();
var port = 3000;
var account = require('./router/Account')

var connect = require("./connect/connectDB");

app.use('/',account)


app.listen(port, () => {
  console.log("Server runing at port : " + port);
});
