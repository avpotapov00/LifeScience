import React from "react";
import "./advantages-disadvantages.css"
import Page from "../../../Page/SectionPage";
import NoContent from "../no-content";

const Advantages = ({contents}) => {
    if (!contents) {
        return <NoContent/>
    } else {
        return <Page text={contents.text}/>
    }
}


export default Advantages