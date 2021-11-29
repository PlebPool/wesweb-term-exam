import React from 'react'
import "./FormSlider.css"
import axios from 'axios'
import { useState } from "react"

const FormSlider = ({ toggleFormSlider, setToggleFormSlider, reloadSwitch, setReloadSwitch }) => {
    const [alias, setAlias] = useState("")
    const [title, setTitle] = useState("")
    const [content, setContent] = useState("")

    const handleSubmit = (event) => {
        let data = {
            alias: alias,
            title: title,
            content: content
        }
        axios.post("/posts", data).then((res) => {
            console.log(res)
            setReloadSwitch(!reloadSwitch)
            setToggleFormSlider(false)
        })
        setAlias("")
        setTitle("")
        setContent("")
        event.preventDefault()
    }

    return (
        <main className="formSlider" style={{transform: toggleFormSlider ? "translateX(0)" : "translateX(-100%)"}}>
            <div style={{display: "flex", justifyContent: "space-evenly", flexDirection: "row"}}>
                <button onClick={setToggleFormSlider}>back</button>
                <form className="form" onSubmit={handleSubmit} style={{width: "100%", padding: "10px"}}>

                    <label style={{display: "block"}}>Alias: </label>
                    <input placeholder="Alias (optional)" name="alias" value={alias} type="text" onChange={(e) => setAlias(e.target.value)}/>

                    <label style={{display: "block"}}>Title: </label>
                    <input placeholder="Title (required)" name="title" value={title} type="text" onChange={(e) => setTitle(e.target.value)}/>

                    <label style={{display: "block"}}>Content: </label>
                    <textarea placeholder="Content (required)" name="content" value={content} style={{width: "98%", height: "500px"}} onChange={(e) => setContent(e.target.value)}/>

                    <input type="submit" value="Submit"/>
                </form>
            </div>
        </main>
    )
}

export default FormSlider
