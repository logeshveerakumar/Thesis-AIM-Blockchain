var Web3 = require('web3');
var web3Arr = [new Web3(new Web3.providers.HttpProvider("http://localhost:8545")),
    new Web3(new Web3.providers.HttpProvider("http://localhost:8546")),
    new Web3(new Web3.providers.HttpProvider("http://localhost:8547")),
    new Web3(new Web3.providers.HttpProvider("http://localhost:8548")),
    new Web3(new Web3.providers.HttpProvider("http://localhost:8549"))];
var noOfNodes=5;
var choosenPort=(Math.floor(Math.random()*web3Arr.length))%noOfNodes;
var web3 =  web3Arr[choosenPort];

while (!web3.isConnected()) {
  choosenPort=(Math.floor(Math.random()*web3Arr.length))%noOfNodes;
  web3 =  web3Arr[choosenPort];
}
//console.log("choosen port:"+choosenPort)
var fs = require('fs');
var code;
var contract;
var VotingContract;
var deployedContract;
var contractInstance;
var accounts = web3.eth.accounts;
var solc = require('solc');
var input = fs.readFileSync(process.cwd()+'/src/main/java/aim4/nodejs_methods/Voting.sol').toString();;
var sleep = require('sleep');

//test the load is splitted
if(false){
    var blockNumber=web3.eth.blockNumber;
    if(blockNumber%5==0 && blockNumber>=10){
      var temp=0;
      console.log("Total number of blocks so far:"+blockNumber);
      for(var j=0;j<5;j++){
        temp=0;
        for(var i=1;i<=blockNumber;i++){
          if(web3Arr[j].eth.accounts.toString().indexOf(web3.eth.getBlock(i).miner)>-1)
          temp=temp+1;
        }
        console.log("Number of blocks formed by Node "+(j+1)+":"+temp);
      }
    }
}

if(process.argv[2]==='compile'){
    console.log("Starting compilation");
    compileContract();

}

if(process.argv[2]==='callContract'){
    console.log("Starting test");
    callContract();
}

if(process.argv[2].indexOf('msg')== 0){
    //console.log("Receiving msg");
    requestMsg();
}

if(process.argv[2].indexOf('Pmsg')== 0){
    proposalMsg();
}

function compileContract(){

    contract =solc.compile(input,1);
    storeToFile("abiDefinition",contract.contracts['Voting'].interface);
    VotingContract = web3.eth.contract(JSON.parse(contract.contracts['Voting'].interface));

    //fs.appendFile(process.cwd()+"/target/abiDefinition",contract.info.abiDefinition);
    deployedContract = VotingContract.new(['Rama','Nick','Jose'],{data: "0x"+contract.contracts['Voting'].bytecode, from: web3.eth.accounts[0], gas: 47000000}, function(err,dummy){
        if(!err) {
       // NOTE: The callback will fire twice!
       // Once the contract has the transactionHash property set and once its deployed on an address.

       // e.g. check tx hash on the first call (transaction send)
       if(!dummy.address) {
           //console.log("Contract transaction hash:"+dummy.transactionHash); // The hash of the transaction, which deploys the contract
           console.log("Deploying to Blockchain.....");
       // check address on the second call (contract deployed)
       } else {
           console.log("Contract Deployed to Blockchain"); // the contract address
           storeToFile("deployedContract",dummy.address);
       }

       // Note that the returned "myContractReturned" === "dummy",
       // so the returned "myContractReturned" object will also get the address set.
   }else{
       console.log(err);
   }
    });

}

//This function used to stores deployed address and abi definition
function storeToFile(filename,data){
    //contractInstance = VotingContract.at(deployedContract.address);
    //console.log("Contract deployed at: "+ deployedContract.address);

    fs.unlink(process.cwd()+'/target/'+filename, function(err) {
       if (err) {
          console.log('No Previous record of exist');
       }
    });
    var logger = fs.createWriteStream(process.cwd()+'/target/'+filename, {
      flags: 'a' // 'a' means appending (old data will be preserved)
    });
    logger.write(data); // append string to your file
    logger.end();
}



function callContract(){

    var deployedAddress = fs.readFileSync(process.cwd()+'/target/deployedContract', 'utf8');
    contract =solc.compile(input,1);
    VotingContract = web3.eth.contract(JSON.parse(contract.contracts['Voting'].interface));
    contractInstance = VotingContract.at(deployedAddress);

    console.log(contractInstance.totalVotesFor.call('Rama').toLocaleString());
    contractInstance.voteForCandidate.sendTransaction('Rama', {from: web3.eth.accounts[0]},
        function(error, result)
        {
            for(;;)
                if(web3.eth.getTransaction(result).blockNumber!==null)
                    break;
            console.log(contractInstance.totalVotesFor.call('Rama').toString());
        } );

}

function requestMsg(){
    var deployedAddress = fs.readFileSync(process.cwd()+'/target/deployedContract', 'utf8');
    code = fs.readFileSync(process.cwd()+'/src/main/java/aim4/nodejs_methods/Voting.sol').toString();
    VotingContract = web3.eth.contract(JSON.parse(fs.readFileSync(process.cwd()+'/target/abiDefinition', 'utf8')));
    contractInstance = VotingContract.at(deployedAddress);
    var message = process.argv[2].slice(4);
    var msg = message.split(" ");
    //console.log(msg);
    console.log("IM ------> Car("+msg[0]+")");
    web3.personal.unlockAccount(web3.eth.accounts[1],"test",30);
    var txHash = contractInstance.confirmMsg(msg[0],msg[1],msg[2],msg[3],{from:web3.eth.accounts[1],gas:1000000});

    for(;;)
        if(web3.eth.getTransaction(txHash).blockNumber!==null)
            break;
    console.log("Confirmation Stored:"+contractInstance.getVinTest.call());

    web3.personal.lockAccount(web3.eth.accounts[1]);
}

function proposalMsg(){
    var deployedAddress = fs.readFileSync(process.cwd()+'/target/deployedContract', 'utf8');
    code = fs.readFileSync(process.cwd()+'/src/main/java/aim4/nodejs_methods/Voting.sol').toString();
    VotingContract = web3.eth.contract(JSON.parse(fs.readFileSync(process.cwd()+'/target/abiDefinition', 'utf8')));
    contractInstance = VotingContract.at(deployedAddress);
    var message = process.argv[2].slice(5);
    var msg = message.split(" ");
    //console.log(msg);
    console.log("Car("+ msg[0]+")------> IM");
    var accountToUse=web3.eth.accounts[1];
    //test with non existing account
    if(false)
      accountToUse=accountToUse+"5";
    //test with wrong password
    if(false)
      web3.personal.unlockAccount(accountToUse,"wrongpass");
    else {
      web3.personal.unlockAccount(accountToUse,"test");
    }

    var txHash = contractInstance.proposalMsg(msg[0],msg[1],msg[2],msg[3],msg[4],msg[5],{from:web3.eth.accounts[1],gas:2000000});
    var tempBlockNumber;
    for(;;){
      tempBlockNumber=web3.eth.getTransaction(txHash).blockNumber;
        if(tempBlockNumber!==null)
            break;
    }
    //test individual costing
    if(false)
      console.log("Computational Cost for "+msg[0]+" car is "+web3.eth.getBlock(tempBlockNumber).gasUsed);

    console.log("Proposal Stored:"+contractInstance.getProposalMsg.call());

    web3.personal.lockAccount(accountToUse);
}

function checkConnection(){
    if (!web3.isConnected()) {
        throw new Error('unable to connect to ethereum node at Localhost' );
    } else {
        console.log('connected to ehterum node at Localhost');

    }
}

function store_accounts(){
    fs.unlink(process.cwd()+'/target/accounts', function(err) {
       if (err) {
          console.log('No Previous Records of accounts');
       }
    });//Delete file if exist

    var logger = fs.createWriteStream(process.cwd()+'/target/accounts', {
      flags: 'a' // 'a' means appending (old data will be preserved)
    });
    for(var i=0;i<accounts.length;i++){
      logger.write(accounts[i]); // append string to your file
      if(i!==accounts.length-1)
        logger.write('\n');//no new line for last account
    }
    console.log('List of accounts recorded');
    logger.end();

}
