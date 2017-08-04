# NEThan

Node.js implementation of a neural network named NEThan (a terrible word play on 'NETwork') to play tic tac toe.

To install, please make sure you have node installed. This can be done by typing 'brew install node' into the terminal. This in turn requires Homebrew, which can be installed by typing the following into your terminal:

  ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

Otherwise, simply type 'node play'! A Nethan instance will automatically be loaded into the play script based on the description provided by 'nethan.json'.

For training the network, simply type ‘node train’. Again, this is dependent on the files provided. As of typing, the input description is ‘temp_nethan.json’, and the training data is ‘training_data_0.json’.

For trying out the brain.js file to confirm that my encoding/data is acceptable, simply type ‘node brain.js’.

For the java code, it would simply be a matter of typing ‘javac clean_game/*.java’, followed by typing ‘java clean_game/Game’ from the project directory. The ouput text file should in the project directory.
