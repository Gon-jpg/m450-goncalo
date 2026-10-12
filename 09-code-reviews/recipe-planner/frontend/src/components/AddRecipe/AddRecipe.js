import React, {useEffect, useState} from 'react';
import './AddRecipe.css';
import {Alert, Form, Button, Col, Row} from 'react-bootstrap';
import {useNavigate, useParams} from "react-router-dom";

import axios from "axios";
import AddIngredient from "../AddIngredient/AddIngredient";

const baseURL = "http://localhost:8080/api/recipes";

const validate = (formData) => {
    if (formData.name.trim() === '') {
        return 'Recipe name is required.';
    }
    if (formData.ingredients.length === 0) {
        return 'Add at least one ingredient.';
    }
    for (const ingredient of formData.ingredients) {
        if (ingredient.name.trim() === '') {
            return 'Every ingredient needs a name.';
        }
        const amount = Number(ingredient.amount);
        if (!Number.isInteger(amount) || amount < 1 || amount > 2147483647) {
            return `Quantity of "${ingredient.name}" must be a whole number between 1 and 2147483647.`;
        }
    }
    return null;
};

const toRequestBody = (formData) => ({
    name: formData.name.trim(),
    description: formData.description.trim(),
    imageUrl: formData.imageUrl.trim(),
    ingredients: formData.ingredients.map(({id, name, comment, unit, amount}) => ({
        id,
        name: name.trim(),
        comment,
        unit,
        amount: Number(amount),
    })),
});

function AddRecipe() {
    const navigate = useNavigate();
    const {id} = useParams();
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        imageUrl: '',
        ingredients: [],
    })
    const [listId, setListId] = useState(1)
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(!!id)
    const [saving, setSaving] = useState(false)

    useEffect(() => {
        if (!id) return
        setLoading(true)
        axios.get(`${baseURL}/recipe/${id}`)
            .then(({data}) => {
                if (!data) {
                    setError('Recipe not found.')
                    return
                }
                const ingredients = (data.ingredients || []).map((ingredient, index) => ({
                    ...ingredient,
                    listId: index + 1,
                    comment: ingredient.comment ?? '',
                }))
                setFormData({
                    name: data.name ?? '',
                    description: data.description ?? '',
                    imageUrl: data.imageUrl ?? '',
                    ingredients,
                })
                setListId(ingredients.length + 1)
            })
            .catch((err) => setError(err.response
                ? `Loading the recipe failed (status ${err.response.status}).`
                : 'Loading the recipe failed. Is the backend running?'))
            .finally(() => setLoading(false))
    }, [id])

    const handleChange = (field) => (e) => setFormData({...formData, [field]: e.target.value});

    const addIngredient = () => {
        setFormData({
            ...formData,
            ingredients: [
                ...formData.ingredients,
                {listId: listId, name: '', unit: 'PIECE', amount: '', comment: ''}
            ]
        })
        setListId(listId + 1)
    }

    const updateIngredient = (ingredientObj) => {
        const updatedIngredients = formData.ingredients.map((ingredient) =>
            ingredient.listId === ingredientObj.listId ? ingredientObj : ingredient
        )
        setFormData({...formData, ingredients: updatedIngredients})
    }

    const removeIngredient = (ingredientObj) => {
        const updatedIngredients = formData.ingredients.filter((ingredient) => ingredient.listId !== ingredientObj.listId)
        setFormData({...formData, ingredients: updatedIngredients})
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        const validationError = validate(formData)
        if (validationError) {
            setError(validationError)
            return
        }
        setError(null)
        setSaving(true)
        const request = id
            ? axios.put(`${baseURL}/recipe/${id}`, toRequestBody(formData))
            : axios.post(baseURL, toRequestBody(formData))
        request
            .then(() => navigate('/'))
            .catch((err) => {
                setSaving(false)
                setError(err.response
                    ? `Saving the recipe failed (status ${err.response.status}).`
                    : 'Saving the recipe failed. Is the backend running?')
            })
    }

    const renderIngredients = formData.ingredients.map(ingredient => <AddIngredient
        key={ingredient.listId}
        ingredient={ingredient}
        updateIngredient={updateIngredient}
        removeIngredient={removeIngredient}
    />)

    return (
        <>
            <div className="bg">
                <Form className="m-3" onSubmit={handleSubmit}>
                    <h1 className="h3 bg-dark text-bg-primary mt-2">{id ? 'Edit Recipe' : 'Add Recipe'}</h1>
                    {loading && <p className="m-3">Loading recipe…</p>}
                    <Form.Group className="mb-1" controlId="formBasicName">
                        <Form.Label>Recipe Name:</Form.Label>
                        <Form.Control placeholder="Name" value={formData.name} onChange={handleChange('name')} disabled={loading}/>
                    </Form.Group>
                    <Form.Group className="mb-1" controlId="formBasicDescription">
                        <Form.Label>Description:</Form.Label>
                        <Form.Control placeholder="Description" value={formData.description} onChange={handleChange('description')} disabled={loading}/>
                    </Form.Group>
                    <Form.Group className="mb-1 mb-5" controlId="formBasicImageUrl">
                        <Form.Label>Image URL:</Form.Label>
                        <Form.Control placeholder="URL" value={formData.imageUrl} onChange={handleChange('imageUrl')} disabled={loading}/>
                    </Form.Group>
                    <Row>
                        <Col>Ingredient</Col>
                        <Col>Unit</Col>
                        <Col>Quantity</Col>
                        <Col>Comment</Col>
                        <Col xs={1}></Col>
                    </Row>
                    <hr/>
                    {renderIngredients}
                    <Row>
                        <br></br>
                        <Button
                            variant='warning'
                            onClick={addIngredient}
                            className="mt-1"
                            disabled={loading}
                        >Add Ingredient</Button>
                    </Row>
                    {error && <Alert variant="danger" className="mt-3">{error}</Alert>}
                    <Button variant="primary" type="submit" className="mt-3 mb-5" disabled={saving || loading}>
                        Submit
                    </Button>
                </Form>
            </div>
        </>
    )
}

export default AddRecipe;
