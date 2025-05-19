import {Link, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";

export default function NavBar(){
    const[user, setUser] = useState(null);
    const navigate = useNavigate();

    function login(){
        const host:string = window.location.host === "localhost:5173" ?
            "http://localhost:8080"
            : window.location.origin
        window.open(host + "/oauth2/authorization/github", "_self")
    }

    function logout(){
        axios.post("/logout", null, { withCredentials: true });
        setUser(null);
        navigate("/");
    }

    const loadUser = ()=>{
        axios.get("/api/auth")
            .then(response => setUser(response.data))
    }
    useEffect(() => {
        loadUser()
    }, []);

    return(
        <>
            <ul className="navi">
                <li><Link to={"/"}>Home</Link></li>
                <li><Link to={"/aisearch"}>AI-Rezept</Link></li>
               <li>
                   {user? (
                    <>
                       <span>Hallo, {user}</span>
                       <button onClick={logout}>Logout</button>
                    </>)
                   : (<button onClick={login}>Login</button>)}
               </li>
            </ul>


        </>
    )
}