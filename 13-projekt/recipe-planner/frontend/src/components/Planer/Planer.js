import React, {useEffect, useState} from 'react';
import './Planer.css';
import axios from 'axios';
import {Button, Form, ListGroup} from 'react-bootstrap';

const recipesURL = "http://localhost:8080/api/recipes";
const shoppingListURL = "http://localhost:8080/api/shopping-list";

const Planer = () => {

    const [recipes, setRecipes] = useState([]);
    const [selectedIds, setSelectedIds] = useState([]);
    const [shoppingList, setShoppingList] = useState([]);

    useEffect(() => {
        axios.get(recipesURL).then((response) => setRecipes(response.data));
    }, []);

    const toggleRecipe = (id) => {
        setSelectedIds((current) =>
            current.includes(id)
                ? current.filter((selectedId) => selectedId !== id)
                : [...current, id]);
    };

    const createShoppingList = () => {
        axios.post(shoppingListURL, selectedIds).then((response) => setShoppingList(response.data));
    };

    return (
        <div className="Planer">
            <h1>Einkaufsliste</h1>
            <p>Rezepte auswählen:</p>
            <Form>
                {recipes.map((recipe) => (
                    <Form.Check
                        key={recipe.id}
                        type="checkbox"
                        id={recipe.id}
                        label={recipe.name}
                        checked={selectedIds.includes(recipe.id)}
                        onChange={() => toggleRecipe(recipe.id)}
                    />
                ))}
            </Form>
            <Button onClick={createShoppingList}>Einkaufsliste erstellen</Button>
            <ListGroup>
                {shoppingList.map((ingredient, index) => (
                    <ListGroup.Item key={index}>
                        {`${ingredient.amount} ${ingredient.unit} ${ingredient.name}`}
                    </ListGroup.Item>
                ))}
            </ListGroup>
        </div>
    );
};

export default Planer;
