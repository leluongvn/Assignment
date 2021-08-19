var express = require("express");
var connect = require("../connect/connectDB");
var uuid = require("uuid");

var app = express();
var bodyParser = require("body-parser");

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post("/add", (req, res) => {
  var id = uuid.v4();
  var nameProduct = req.body.nameProduct;
  var imageProduct = req.body.imageProduct;
  var priceProduct = req.body.priceProduct;
  var amountProduct = req.body.amountProduct;
  var nameUser = req.body.nameUser;
  var phoneUser = 111;
  var status = req.body.status;

  connect.query(
    "INSERT INTO `cart`(`id`, `name`, `image`, `amount`, `price`, `nameUser`, `phone`, `status`) VALUES (?,?,?,?,?,?,?,?)",
    [
      id,
      nameProduct,
      imageProduct,
      amountProduct,
      priceProduct,
      nameUser,
      phoneUser,
      status,
    ],
    function (err, result, fields) {
      connect.on("error", function (err) {
        console.log("MY SQL ERROR", err);
        res.json("Register error");
      });
      res.json("Success fully");
      console.log("Insert success");
    }
  );
});

app.get("/list", (req, res) => {
  connect.query("SELECT * FROM cart ", function (err, result, fields) {
    if (err) throw err;
    else {
      res.json(result);
      console.log("OK");
    }
  });
});


app.post("/update",(req,res) =>{

  var id = req.body.id
  var status =req.body.status
  connect.query(
    "UPDATE `cart` SET `status`=? WHERE id= ?",
    [status,id],
    function (error, results, fields) {
      if (error){
        console.log("ERROR",error)
      } 
        res.json("Success");
        console.log("Update success")
    }
  );

})

module.exports = app;
