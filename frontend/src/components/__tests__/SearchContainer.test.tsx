import '@testing-library/jest-dom';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { SearchContainer } from '../SearchContainer';
import type { RecommendResponse } from '../../types';

describe('SearchContainer', () => {
  const fakeResponse: RecommendResponse = {
    products: [
      { id: 1, title: 'EZ Trainer', description: 'easy', price: 49, category: 'Footwear', level: 'BEGINNER' },
      { id: 2, title: 'Pro Racer', description: 'fast', price: 199, category: 'Footwear', level: 'EXPERT' },
      { id: 3, title: 'Mid Shoe', description: 'neutral', price: 99, category: 'Footwear', level: 'EXPERT' },
    ],
    recommendation:
      'Based on your search, the EZ Trainer is perfect for beginners due to its ease of use, ' +
      'while the Pro Racer offers the advanced features an expert needs.',
  };

  beforeEach(() => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve(fakeResponse),
      } as Response)
    ) as jest.Mock;
  });

  afterEach(() => {
    (global.fetch as jest.Mock).mockReset();
  });

  test('full flow: type, click search, render products + recommendation', async () => {
    const user = userEvent.setup();
    render(<SearchContainer />);

    await user.type(screen.getByRole('textbox'), 'shoes');
    await user.click(screen.getByRole('button', { name: /search/i }));

    await waitFor(() => {
      expect(screen.getByText('EZ Trainer')).toBeInTheDocument();
    });
    expect(screen.getByText('Pro Racer')).toBeInTheDocument();
    expect(screen.getByText('Mid Shoe')).toBeInTheDocument();
    expect(screen.getByText(/perfect for beginners/i)).toBeInTheDocument();

    expect(global.fetch).toHaveBeenCalledWith('/api/recommend?q=shoes');
  });

  test('renders empty state when no products match', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      status: 200,
      json: () => Promise.resolve({ products: [], recommendation: '' }),
    });

    const user = userEvent.setup();
    render(<SearchContainer />);

    await user.type(screen.getByRole('textbox'), 'nope');
    await user.click(screen.getByRole('button', { name: /search/i }));

    await waitFor(() => {
      expect(screen.getByText(/no products matched/i)).toBeInTheDocument();
    });
  });
});
