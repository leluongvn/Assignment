var nodemailer = require('nodemailer')

var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'luongdev15@gmail.com',
      pass: 'Leluong150200'
    }
  });
  var code =Math.floor(Math.random()*10000)
  
  var mailOptions = {
    from: 'leluong30032015@gmail.com',
    to: 'leluong.dev@gmail.com',
    subject: 'Vetify email for create account ',
    text: 'Your code is '+code
  };
  
  transporter.sendMail(mailOptions, function(error, info){
    if (error) {
      console.log(error);
    } else {
      console.log('Email sent: ' + info.response);
    }
  });

  module.exports = transporter