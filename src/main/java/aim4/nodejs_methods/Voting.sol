pragma solidity ^0.4.2;
// We have to specify what version of compiler this code will compile with

contract Voting {
  /* mapping field below is equivalent to an associative array or hash.
  The key of the mapping is candidate name stored as type bytes32 and value is
  an unsigned integer to store the vote count
  */
    struct ConfirmMessage {
        uint VIN;
        uint IM_ID;
        uint ARRIVAL_TIME_SEC;
        uint ARRIVAL_TIME_MSEC;
    }
    
    struct ProposalMessage {
        uint VIN;
        uint8 IM_ID;
        uint8 ARRIVAL_LANE;
        uint8 DEPARTURE_LANE;
        uint ARRIVAL_TIME_SEC;
        uint ARRIVAL_TIME_MSEC;
    }
  mapping (bytes32 => uint8) public votesReceived;
  mapping (uint8 => int[]) public test;
  ConfirmMessage[] confirmMessage;
  ProposalMessage[] proposalMessage;
  /* Solidity doesn't let you pass in an array of strings in the constructor (yet).
  We will use an array of bytes32 instead to store the list of candidates
  */

  bytes32[] public candidateList;
  uint public vinTest;
  /* This is the constructor which will be called once when you
  deploy the contract to the blockchain. When we deploy the contract,
  we will pass an array of candidates who will be contesting in the election
  */
  function Voting(bytes32[] candidateNames) {
    candidateList = candidateNames;
   
  }

  // This function returns the total votes a candidate has received so far
  function totalVotesFor(bytes32 candidate) constant returns (uint8) {
    if (validCandidate(candidate) == false) throw;
    return votesReceived[candidate];
  }

  // This function increments the vote count for the specified candidate. This
  // is equivalent to casting a vote
  function voteForCandidate(bytes32 candidate) {
    if (validCandidate(candidate) == false) throw;
    votesReceived[candidate] += 1;
  }

  function validCandidate(bytes32 candidate) constant returns (bool) {
    for(uint i = 0; i < candidateList.length; i++) {
      if (candidateList[i] == candidate) {
        return true;
      }
    }
    return false;
  }

  function confirmMsg(uint vin, uint im_id, uint arrival_time_sec,uint arrival_time_msec) {
      //vinTest = vin;
      
      confirmMessage.length++;
      confirmMessage[confirmMessage.length-1].VIN=vin;
      confirmMessage[confirmMessage.length-1].IM_ID=im_id;
      confirmMessage[confirmMessage.length-1].ARRIVAL_TIME_SEC=arrival_time_sec;
      confirmMessage[confirmMessage.length-1].ARRIVAL_TIME_MSEC=arrival_time_msec;
      
  }
  
  function getVinTest() constant returns (uint,uint,uint,uint){
      return (confirmMessage[confirmMessage.length-1].VIN,confirmMessage[confirmMessage.length-1].IM_ID,confirmMessage[confirmMessage.length-1].ARRIVAL_TIME_SEC,confirmMessage[confirmMessage.length-1].ARRIVAL_TIME_MSEC);
      
  }
  
  function proposalMsg(uint vin, uint8 im_id, uint8 arrival_lane,uint8 departure_lane, uint arrival_time_sec,uint arrival_time_msec) {
      //vinTest=vin;
      proposalMessage.length++;
      proposalMessage[proposalMessage.length-1].VIN=vin;
      proposalMessage[proposalMessage.length-1].IM_ID=im_id;
      proposalMessage[proposalMessage.length-1].ARRIVAL_LANE=arrival_lane;
      proposalMessage[proposalMessage.length-1].DEPARTURE_LANE=departure_lane;
      proposalMessage[proposalMessage.length-1].ARRIVAL_TIME_SEC=arrival_time_sec;
      proposalMessage[proposalMessage.length-1].ARRIVAL_TIME_MSEC=arrival_time_msec;
  }
  
  function getProposalMsg() constant returns (uint,uint8,uint8,uint8,uint,uint){
      return (proposalMessage[proposalMessage.length-1].VIN,proposalMessage[proposalMessage.length-1].IM_ID,proposalMessage[proposalMessage.length-1].ARRIVAL_LANE,proposalMessage[proposalMessage.length-1].DEPARTURE_LANE,proposalMessage[proposalMessage.length-1].ARRIVAL_TIME_SEC,proposalMessage[proposalMessage.length-1].ARRIVAL_TIME_MSEC);
      
  }
}