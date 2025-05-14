export interface IRecipe {
    id: string;
    name: string;
    description: string;
    image?: string;
    ingredients: string[];
}