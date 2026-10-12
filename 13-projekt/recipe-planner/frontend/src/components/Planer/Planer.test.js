import React from 'react';
import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import axios from 'axios';
import Planer from './Planer';

jest.mock('axios');

describe('Planer', () => {

    beforeEach(() => {
        axios.get.mockResolvedValue({
            data: [
                {id: 'id-1', name: 'Soup'},
                {id: 'id-2', name: 'Pasta'},
            ],
        });
        axios.post.mockResolvedValue({
            data: [
                {name: 'Carrot', unit: 'PIECE', amount: 3},
            ],
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('shows the recipes as checkboxes', async () => {
        render(<Planer/>);

        expect(await screen.findByLabelText('Soup')).toBeInTheDocument();
        expect(screen.getByLabelText('Pasta')).toBeInTheDocument();
    });

    it('posts the selected recipe ids and shows the shopping list', async () => {
        render(<Planer/>);
        await screen.findByLabelText('Soup');

        fireEvent.click(screen.getByLabelText('Soup'));
        fireEvent.click(screen.getByLabelText('Pasta'));
        fireEvent.click(screen.getByRole('button', {name: 'Einkaufsliste erstellen'}));

        await waitFor(() => expect(axios.post).toHaveBeenCalledWith(
            'http://localhost:8080/api/shopping-list', ['id-1', 'id-2']));
        expect(await screen.findByText('3 PIECE Carrot')).toBeInTheDocument();
    });
});
