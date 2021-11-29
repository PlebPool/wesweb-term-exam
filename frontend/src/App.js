import './App.css'
import { useState, useEffect } from "react"
import axios from 'axios'
import Posts from './components/posts/Posts'
import AppHeader from './components/appheader/AppHeader'
import FormSlider from './components/formslider/FormSlider'

function App() {
  const [posts, setPosts] = useState([])
  const [toggleFormSlider, setToggleFormSlider] = useState(false)
  const [reloadSwitch, setReloadSwitch] = useState(false)

  useEffect(() => {
    axios.get("/posts").then((res) => {
      setPosts(res.data)
    })
  }, [reloadSwitch])

  return (
    <div className="App">
      <AppHeader setToggleFormSlider={() => setToggleFormSlider(!toggleFormSlider)} />
      <Posts posts={posts} />
      <FormSlider toggleFormSlider={toggleFormSlider} 
      setToggleFormSlider={() => setToggleFormSlider(!toggleFormSlider)} 
      reloadSwitch={reloadSwitch}
      setReloadSwitch={setReloadSwitch} />
    </div>
  );
}

export default App;
