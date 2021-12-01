import './App.css'
import { useState, useEffect } from "react"
import axios from 'axios'
import Posts from './components/posts/Posts'
import AppHeader from './components/appheader/AppHeader'
import FormSlider from './components/formslider/FormSlider'
import LoadingIcons from 'react-loading-icons'
import axiosRetry from 'axios-retry'

function App() {
  const [postRefs, setPostRefs] = useState([])
  const [toggleFormSlider, setToggleFormSlider] = useState(false)
  const [reloadSwitch, setReloadSwitch] = useState(false)
  const [isLoading, setIsLoading] = useState(true)
  const [ifNotLoaded, setIfNotLoaded] = useState({
    failed: false,
    message: "",
  })

  useEffect(() => {
    axiosRetry(axios, {
      retries: 3,
      retryDelay: (retryCount) => {
        console.log(`retry attempt: ${retryCount}`)
        return retryCount * 2000
      },
      retryCondition: (error) => {
        return error.response.status === 500;
      }
    })

    async function getData() {
        setIsLoading(true)
        setTimeout(async () => { // This is just to show off the cool loading icon
          await axios.get("/posts").then((res) => {
            //console.log("Apps: ", res.data)
            setPostRefs(res.data)
            setIsLoading(false)
            setIfNotLoaded({failed: false})
          }).catch((err) => {
            if(err.response.status !== 200) {
              setIfNotLoaded({failed: true, message: "Failed to load Posts, try again later..."})
            }
          })
        }, 1000);
    }
    getData()
    
  }, [reloadSwitch])

  return (
    <div className="App">
      <AppHeader setToggleFormSlider={() => setToggleFormSlider(!toggleFormSlider)} />
      {isLoading 
      ? [
        (ifNotLoaded.failed 
        ? (<h3 style={{color: "white", margin: "10px"}}>{ifNotLoaded.message}</h3>) 
        : (<LoadingIcons.TailSpin speed={3} className="Loading-Icon" />))
      ] 
      : (<Posts postRefs={postRefs} />)}
      <FormSlider toggleFormSlider={toggleFormSlider} 
      setToggleFormSlider={() => setToggleFormSlider(!toggleFormSlider)} 
      reloadSwitch={reloadSwitch}
      setReloadSwitch={setReloadSwitch} />
    </div>
  );
}

export default App;