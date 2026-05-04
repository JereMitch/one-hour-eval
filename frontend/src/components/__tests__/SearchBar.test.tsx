import '@testing-library/jest-dom';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { SearchBar } from '../SearchBar';

describe('SearchBar', () => {
  test('updates input value as user types', async () => {
    const user = userEvent.setup();
    render(<SearchBar onSearch={jest.fn()} />);

    const input = screen.getByRole('textbox', { name: /search products/i }) as HTMLInputElement;
    await user.type(input, 'shoes');

    expect(input.value).toBe('shoes');
  });

  test('calls onSearch with current query when button clicked', async () => {
    const user = userEvent.setup();
    const onSearch = jest.fn();
    render(<SearchBar onSearch={onSearch} />);

    await user.type(screen.getByRole('textbox'), 'running');
    await user.click(screen.getByRole('button', { name: /search/i }));

    expect(onSearch).toHaveBeenCalledWith('running');
  });

  test('calls onSearch when Enter is pressed', async () => {
    const user = userEvent.setup();
    const onSearch = jest.fn();
    render(<SearchBar onSearch={onSearch} />);

    await user.type(screen.getByRole('textbox'), 'hat{Enter}');

    expect(onSearch).toHaveBeenCalledWith('hat');
  });
});
