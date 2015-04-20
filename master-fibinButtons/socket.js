
var app = require('http').createServer(handler)
  , io = require('socket.io').listen(app)
  , fs = require('fs')
  , Gpio = require('onoff').Gpio
  , b4 = new Gpio(23, 'in', 'both') // Currently the "start" button
  , b3 = new Gpio(17, 'in', 'both') // 	
  , b2 = new Gpio(18, 'in', 'both')
  , b1 = new Gpio(22, 'in', 'both')

io.set('log level', 1);
app.listen(8081);

function handler (req, res) {
  fs.readFile(__dirname + '/index.html',
  function (err, data) {
    if (err) {
      res.writeHead(500);
      return res.end('Error loading index.html');
    }

    res.writeHead(200);
    res.end(data);
  });
}


b4.watch(function(err, value) {
    if (err) exit();
    io.sockets.emit('b4', {v:value});
});

b3.watch(function(err, value){
	if (err) exit();
	io.sockets.emit('b3', {v:value});
});

b2.watch(function(err, value){
	if (err) exit();
	io.sockets.emit('b2' , {v:value});
});

b1.watch(function(err, value){
	if (err) exit();
	io.sockets.emit('b1' , {v:value});
});

function exit() {
    b1.unexport();
    b2.unexport();
    b3.unexport();
    b4.unexport();
    process.exit();
}

process.on('SIGINT', exit);
