var express = require("express");
var app = express();
var port = 3000;
var login = require('./router/Login')

var connect = require("./connect/connectDB");


app.use('/',login)

app.listen(port, () => {
  console.log("Server runing at port : " + port);
});
