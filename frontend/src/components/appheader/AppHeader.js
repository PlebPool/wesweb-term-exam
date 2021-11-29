import { useEffect, useState } from 'react'
import "./AppHeader.css"

const AppHeader = ({ setToggleFormSlider }) => {
    const [image_src, setImage_src] = useState("amogus1.png")
    const [image_width, setImage_width] = useState("75px")
    
    useEffect(() => {
        let i = 2
        setInterval(() => {
            if(i === 5) i = 1
            if(i === 1) setImage_width("75px")
            if(i === 2) setImage_width("30px")
            if(i === 3) setImage_width("30px")            
            if(i === 4) setImage_width("43px")
            setImage_src(`amogus${i}.png`)
            i++
        }, 20000)
    }, [])

    return (
        <header className="siteHead">
            <button onClick={setToggleFormSlider}>HEY</button>
            <div className="spinner">
                <img className="logo" src={image_src} width={image_width} alt="amogus"></img>
            </div> 
        </header>
    )
}

export default AppHeader
