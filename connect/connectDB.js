var mysql = require("mysql");

var connect = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
  database: "login_regiester",
});

connect.connect(function (err) {
  if (err) {
    console.log("Fail conect database");
  }else{
    console.log("Connect database success")
  }
});

module.exports = connect;
