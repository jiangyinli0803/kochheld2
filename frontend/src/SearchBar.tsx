import {type FormEvent, useState} from "react";
import type {AiRecipe} from "./interfaces/AiRecipe.ts";


export default function SearchBar(){
    const [query, setQuery] = useState('');

    const [aiRecipe, setAiRecipe] = useState<AiRecipe|null>(null);

    async function handleSubmit(e: FormEvent<HTMLFormElement>) {
        e.preventDefault();
        if (!query.trim()) return;

        try {
            const response = await fetch(`/api/recipe/search?ingredient=${encodeURIComponent(query)}`);

            if (!response.ok) {
                throw new Error('Suche fehlgeschlagen. Bitte versuchen Sie es später erneut.');
            }

            const recipeData = await response.json();
            setAiRecipe(recipeData);
        } catch (error) {
            console.error(error);
        }
    }


    return (
        <>
        <div className="search-container">
            <form onSubmit={handleSubmit} className="search-form">
                <input
                    type="search"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    placeholder="Bitte geben Sie einen Zutat ein..."
                    className="search-input"
                />
                <button type="submit" className="search-button">
                </button>
            </form>

            {aiRecipe && (<div className="recipe-result">
                <h2>{aiRecipe.title}</h2>
                <img src="https://i.postimg.cc/63YVHySZ/Food-Photo.png" alt="KochHeld Logo" className="recipe-img"/>
                <div className="recipe-section">
                    <ul>
                        {aiRecipe.ingredients.map((item, index) => (
                            <li key={index}>{item}</li>
                        ))}
                    </ul>
                </div>
                <div className="recipe-section">
                    <h3>{aiRecipe.title}</h3>
                    <h3>Zubereitung</h3>
                    <p>{aiRecipe.description}</p>

                </div>
            </div>)}


        </div>

     </>)
}



