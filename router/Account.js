var express = require("express")
var connect = require("../connect/connectDB")
var uuid = require("uuid")

var app = express()
var bodyParser = require("body-parser")
var nodemailer = require("nodemailer")
var codeVetify = 0
var check = false
var emailVetify = null

// const { end } = require("../connect/connectDB");
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

app.post("/register", (req, res) => {
  var Uid = uuid.v4();
  var username = req.body.name
  var email = req.body.email
  var password = req.body.password
  connect.query(
    "SELECT * FROM user where email=?",
    [email],
    function (err, result, fiedls) {
      connect.on("error", function (err) {
        console.log("Error my sql ", err);
      });
      if (result && result.length) res.json("User already exits");
      else {
        connect.query(
          "INSERT INTO `user`(`id`, `name`, `email`, `password`) VALUES (?,?,?,?)",
          [Uid, username, email, password],
          function (err, result, fields) {
            connect.on("error", function (err) {
              console.log("MY SQL ERRPR ", err)
              res.json("Register error")
            });
            res.json("Success fully")
            console.log("Insert success ")
          }
        );
      }
    }
  );
});

app.post("/login", (req, res) => {
  var email = req.body.email
  var password = req.body.password
  console.log(email)
  connect.query(
    "SELECT * FROM user where email=?",
    [email],
    function (err, result, fields) {
      connect.on("Error", function (err) {
        console.log("Error my sql")
      });
      if (result && result.length) {
        var pass = result[0].password
        if (password == pass) {
          res.end(JSON.stringify(result[0]))
          // res.json("Success")
        } else {
          // res.json("Wrong password")
          res.end(JSON.stringify("Wrong password"))
        }
      } else {
        console.log("User not exists")
        res.json("User not exists")
      }
    }
  );
});

app.post("/vetify", (req, res) => {
  var email = req.body.email
  connect.query(
    " SELECT * FROM user where email=?",
    [email],
    function (err, result, fiedls) {
      connect.on("error", function (err) {
        console.log("Error my sql " + err)
      })
      if (result && result.length) {
        // send code to email
        res.json("success");
        sendEmail(email);
      } else {
        res.json("Email not found ")
        console.log("Email not found")
      }
    }
  )
})

function sendEmail(email) {
  var transporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: "luongdev15@gmail.com",
      pass: "Leluong150200",
    },
  });
  var code = Math.floor(Math.random() * 10000)
  codeVetify = code
  var mailOptions = {
    from: "leluong30032015@gmail.com",
    to: "" + email,
    subject: "Forgot password ",
    text: "Your code is " + code
  
  };

  transporter.sendMail(mailOptions, function (error, info) {
    if (error) {
      console.log(error)
    } else {
      console.log("Email sent : " + info.response)
      check = true
      emailVetify=email
    }
  });
}

app.post("/confirm", (req, res) => {
  if (check == true) {
    var reqCode = req.body.code;
    var newPassword = req.body.newPassword
    if (reqCode == codeVetify) {
      var sql="UPDATE `user` SET `password`=? WHERE ?"
      console.log("email "+emailVetify);
      connect.query("UPDATE `user` SET `password`=? WHERE email=?",[newPassword,emailVetify],
       function (error, results, fields) {
        if (error) throw error
        else{
          res.json("Success update")
        }
      })

      // update password new
    } else {
      res.json("Code invalid")
      console.log("Invalid ")
    }
  }
});

module.exports = app;
