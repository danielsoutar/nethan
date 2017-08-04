var fs = require('fs');
var n = require('./numeric');

var cache = [];

var Nethan = function(filename) {
	var temp = require(filename);
	this.nr_layers = temp.nr_layers;
	this.nr_inputs = temp.nr_inputs;
	this.nr_outputs = temp.nr_outputs;
	this.layers = temp.layers;
	this.prediction = [];
};

Nethan.prototype.init_weights = function() {
	for(var i = 0; i < this.layers.length; i++)
		this.layers[i].input_weights = init_weights(this.layers[i].input_weights);
	return this.layers;
}

function init_weights(input_weights) {
	for(var i = 0; i < input_weights.length; i++)
		for(var j = 0; j < input_weights[i].length; j++)
			input_weights[i][j] = get_gaussian();
	return input_weights;
}

function get_gaussian() {
  var rand = 0;
  for (var i = 0; i < 6; i++)
	rand += Math.random();
  return rand / 6;
}

Nethan.prototype.forward = function(X) {
	var A = X;

	for(var i = 0; i < A.length; i++)
		A[i].unshift(1); //get it? A[i], as in AI? Forget it. Adding a bias
	
	var W, Z;

	for(var l = 0; l < this.layers.length; l++) {
		W = this.layers[l].input_weights;
		Z = n.dot(A, W);
		A = n.div(1.0, n.add(n.exp(n.neg(Z)), 1.0));

		cache[l] = Z;

		for(var i = 0; i < A.length; i++)
			A[i].unshift(1); //as above

	}
	for(var i = 0; i < A.length; i++)
			A[i].shift(1); //remove the bias at the end of forwarding

	this.prediction = A;
	return this.prediction;
};

Nethan.prototype.get_cost = function(X, Y) {
	h = this.forward(X);
	J = n.sum(n.sub(n.mul(n.neg(Y), n.log(h)), n.mul((n.sub(1, Y), n.log(n.sub(1, h)))))); //sum((-y * log(h) - (1 - y) * log(1 - h)))
	return J;
};

Nethan.prototype.get_mse = function(Y) {
	return n.norm2Squared(n.sub(Y, this.prediction));
};

function g(z) {
	return n.div(1.0, n.add(n.exp(n.neg(z)), 1.0));
}

Nethan.prototype.back_propagate = function(X, Y) {
	var m = X.length;
	var l = this.layers.length - 1;
	var D = [l];
	for(var i = 0; i < m; i++) {
		var h = this.forward([X[i]]);
		var delta = [l];

		delta[l] = n.sub(h[0], Y[i]);


		W1 = this.layers[1].input_weights;
		delta2 = [delta[l]];
		Z2 = cache[0];

		D_T = n.dot(delta2, n.transpose(W1));

		D_T = D_T.map(function(val){
			return val.slice(1, );
		});

		delta[--l] = n.mul(D_T, Z2);

		d = n.transpose([delta[l+1]]);
		activations = n.div(1.0, n.add(n.exp(n.neg(Z2)), 1.0));

		D[l] += n.dot(d, activations);
		console.log("D: " + D[l]);
		console.log();
		console.log("////////////////////////////////////////////////////////////////////////////////");
		console.log();
		console.log("weights: " + this.layers[l].input_weights);
		console.log();
		console.log("////////////////////////////////////////////////////////////////////////////////");
		console.log();
		//this.layers[l].input_weights = n.sub(this.layers[l].input_weights, n.mul(0.01, D[l]));
		l = this.layers.length - 1;
	}

	return delta;
};

var nethan = new Nethan("./nethan.json");
nethan.layers = nethan.init_weights();

nethan.get_cost([[ 0, 0, 0, 0, 0, 0, 0, 0, 0 ], 
				 [ 0, 0, 0, 0, 0, 0, 0, 0, 1 ]], 
				[[ 0, 0, 0, 0, 1, 0, 0, 0, 0 ], 
				 [ 0, 0, 0, 0, 1, 0, 0, 0, 0 ]]);

nethan.back_propagate([[ 0, 0, 0, 0, 0, 0, 0, 0, 0 ], 
					   [ 0, 0, 0, 0, 0, 0, 0, 0, 1 ]], 
					  [[ 0, 0, 0, 0, 1, 0, 0, 0, 0 ], 
					   [ 0, 0, 0, 0, 1, 0, 0, 0, 0 ]]);

//nethan.back_propagate([[ 0, 0, 0, 0, 0, 0, 0, 0, 0 ]], [[ 0, 0, 0, 0, 1, 0, 0, 0, 0 ]]);

//var index = nethan.prediction.reduce((iMax, x, i, arr) => x > arr[iMax] ? i : iMax, 0);

//console.log(index);







