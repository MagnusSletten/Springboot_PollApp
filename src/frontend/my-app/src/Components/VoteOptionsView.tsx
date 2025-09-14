import { use, useState } from "react";
import { VoteOption, VoteOptions } from "./Model/Poll";
import { useId } from 'react';

export default function VoteOptionsView({ voteoptions }: { voteoptions: VoteOptions }){
    const [newOption, setNewOption] = useState("")
    const [options,setOptions] = useState(voteoptions.getOptions())
    const addOption = (option:VoteOption) => {
        voteoptions.addOption(option);
        setOptions(voteoptions.getOptions())
        setNewOption("")
    }
   
    const removeOption = (id:number) =>{
        voteoptions.removeOption(id);
        setOptions(voteoptions.getOptions())
         }
    let nextId = 1;

    function getNextId() {
    return nextId++;
    }
   
    return(
        <div className="Question">
        <div></div>
        <div className="option-box">
            {voteoptions.getVoteOptions().map((option,o_i) => (
                <div className="pollButtonBox" key={option.optionId}>
                <span>{option.getCaption()}</span>
                <button
                className="delete-button"
                onClick={e => removeOption(option.optionId)} 
                >X</button>
                </div>
            ))}
            <div className="add-option-box">
                <input
                className="option-input"
                value= {newOption}
                id={useId()}
                placeholder="New option"
                onChange={e => setNewOption(e.target.value)}
                ></input>
                <button
                onClick={e => addOption(new VoteOption(newOption,options.length+1,getNextId()))}>
                
            Add Option</button>
            </div>
        </div>

        </div>
         
    )

}