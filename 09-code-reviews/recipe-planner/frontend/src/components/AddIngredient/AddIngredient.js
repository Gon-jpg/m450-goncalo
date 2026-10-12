import React from 'react';
import PropTypes from 'prop-types';
import './AddIngredient.css';
import {Button, Col, Form, Row} from "react-bootstrap";

export const UNITS = ['PIECE', 'GRAMM', 'KILOGRAMM', 'LITRE', 'DECILITRE'];

const AddIngredient = ({ingredient, updateIngredient, removeIngredient}) => {

    const handleChange = (field) => (e) => updateIngredient({...ingredient, [field]: e.target.value});

    return (
        <Row>
            <Col>
                <Form.Group className="mb-1">
                    <Form.Control
                        placeholder="Name"
                        aria-label="Ingredient name"
                        value={ingredient.name}
                        onChange={handleChange('name')}
                    />
                </Form.Group>
            </Col>
            <Col>
                <Form.Group className="mb-1">
                    <Form.Select
                        aria-label="Unit"
                        value={ingredient.unit}
                        onChange={handleChange('unit')}
                    >
                        {UNITS.map((unit) => <option key={unit} value={unit}>{unit}</option>)}
                    </Form.Select>
                </Form.Group>
            </Col>
            <Col>
                <Form.Group className="mb-1">
                    <Form.Control
                        type="number"
                        min="1"
                        step="1"
                        placeholder="Quantity"
                        aria-label="Quantity"
                        value={ingredient.amount}
                        onChange={handleChange('amount')}
                    />
                </Form.Group>
            </Col>
            <Col>
                <Form.Group className="mb-1">
                    <Form.Control
                        placeholder="Comment (optional)"
                        aria-label="Comment"
                        value={ingredient.comment}
                        onChange={handleChange('comment')}
                    />
                </Form.Group>
            </Col>
            <Col xs={1}>
                <Button
                    onClick={() => removeIngredient(ingredient)}
                    variant='outline-dark'
                    className="mb-1"
                >x</Button>
            </Col>
        </Row>
    )
}

AddIngredient.propTypes = {
    ingredient: PropTypes.shape({
        listId: PropTypes.number.isRequired,
        name: PropTypes.string.isRequired,
        unit: PropTypes.string.isRequired,
        amount: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
        comment: PropTypes.string.isRequired,
    }).isRequired,
    updateIngredient: PropTypes.func.isRequired,
    removeIngredient: PropTypes.func.isRequired,
};

export default AddIngredient;
