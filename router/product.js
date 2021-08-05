var express = require("express");
var connect = require("../connect/connectDB");
var app = express();
var uuid = require("uuid");
var path = require('path')
var multer= require('multer')

var bodyParser = require("body-parser");
app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())
var checkUpload



var storage = multer.diskStorage({
  destination: './upload/images',
  filename: (req, file, cb) => {
      return cb(null, `${file.fieldname}_${Date.now()}${path.extname(file.originalname)}`)
  }
})

var upload = multer({
  storage: storage,
  limits: {
      fileSize: 10000000
  }
})

function errHandler(err, req, res, next) {
  if (err instanceof multer.MulterError) {
    res.json({
      success: 0,
      message: err.message,
    });
  }
}

app.use(errHandler);

app.use("/profile", express.static("upload/images"));

app.post("/upload", upload.single("profile"), (req, res) => {
  
  res.json({
    success: 1,
    profile_url: `http://localhost:3000/product/profile/${req.file.filename}`,
  });

  

  console.log(`http://localhost:3000/product/profile/${req.file.filename}`)
});



app.post("/add", (req, res) => {
  var Uid = uuid.v4();
  var name = req.body.name;
  var type = req.body.type;
  var amount = req.body.amount;
  var price = req.body.price;
  var image = req.body.image;

  console.log(name,type,amount,price)
  connect.query(
    "SELECT * FROM product where name=?",
    [name],
    function (err, result, fiedls) {
      connect.on("error", function (err) {
        console.log("Error my sql ", err);
      });
      if (result && result.length) {
        res.json("Product already exis");
        console.log("Product already exist");
      } else {
        connect.query(
          "INSERT INTO `product`(`id`, `name`, `type`, `amount`, `price`, `image`) VALUES (?,?,?,?,?,?)",
          [Uid, name, type, amount, price, image],
          function (err, fiedls, result) {
            connect.on("error", function (err) {
              console.log("Error my sql");
              res.json("Add error");
            });
            res.json("Success fully");
            console.log("Success fully");
          }
        );
      }
    }
  );
});
module.exports = app;
