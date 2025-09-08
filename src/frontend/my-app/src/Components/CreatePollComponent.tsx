import { useState, useSyncExternalStore } from 'react';
import { Poll, VoteOptions } from "./Poll"; 
import PollView from './PollView'

function CustomPoll():Poll  {
  
  const question = ""
  const poll = new Poll(question);
  
  return poll;
}



function CreatePoll() {
  const poll = CustomPoll();
  const [response, setResponse] = useState(""); 

  const sendPoll = async () => {
  const url = 'http://localhost:8080/polls'
  
  try {
  const res = await fetch(url, {
  method: 'POST',
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(poll.toJSON())}); 
  setResponse(await res.text()); 
  }

  catch(error) {
    setResponse(error.message);
  }
  }
 
  return (
    <>
      <PollView poll={poll} />
      <button
      onClick={sendPoll}>
      Send Poll</button>
      <span className='backend-response'>{response}</span>
    </>
   

  )

}

export default CreatePoll
