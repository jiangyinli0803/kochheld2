import {Link} from "react-router-dom";

export default function NavBar(){

    return(
        <>
            <ul className="navi">
                <li><Link to={"/"}>Home</Link></li>
                <li><Link to={"/airecipe"}>AI-Rezept</Link></li>

            </ul>

        </>
    )
}