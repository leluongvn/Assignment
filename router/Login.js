var express = require("express");
var connect = require("../connect/connectDB");
var uuid = require('uuid')

var app = express();
var bodyParser = require("body-parser");
const { end } = require("../connect/connectDB");
var jsonParser = bodyParser.json();
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post("/register", (req, res) => {

  var Uid = uuid.v4();
  var username = req.body.name;
  var email = req.body.email;
  var password = req.body.password;


  console.log(username,+" "+email +" "+ password)
  connect.query(
    "SELECT * FROM user where email=?",[email],
    function (err, result, fiedls) {
      connect.on("error", function (err) {
        console.log("Error my sql ",err)
      });
      if (result && result.length)
       res.json("User already exits")
      else {
        connect.query(
          "INSERT INTO `user`(`id`, `name`, `email`, `password`) VALUES (?,?,?,?)",
          [Uid,username, email, password],
          function (err, result, fields) {
            connect.on("error", function (err) {
              console.log("MY SQL ERRPR ", err)
              res.json("Register error")
            });
            res.json("Succes fully ")
            console.log("Insert success ")
          }
        );
      }
    }
  );
});

app.post('/login', (req, res) =>{

  var email = req.body.email
  var password = req.body.password
  console.log(email)
  connect.query("SELECT * FROM user where email=?",[email],
  function (err,result,fields) {
    connect.on("Error",function (err) {
      console.log("Error my sql")      
    })
    if(result && result.length){
      var pass = result[0].password
      if(password == pass ){
        res.end(JSON.stringify(result[0]))
      }else{
        res.end(JSON.stringify("Wrong password "))
      }
    }else{
      console.log("User not exists ")
      res.json(" User not exists")
    }
    
  })


})

module.exports = app;
