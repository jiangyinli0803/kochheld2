

import './App.css'
import {Route, Routes} from "react-router-dom";
import SearchBar from "./SearchBar.tsx";
import NavBar from "./NavBar.tsx";

function App() {


  return (
      <>
          <NavBar/>
        <Routes>
            <Route path={"/"} element={
                <h1>Home Page</h1>}/>
            <Route path={"/aisearch"} element={<SearchBar/>}/>

        </Routes>
      </>
  )
}

export default App
