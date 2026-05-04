import { useState, type KeyboardEvent } from 'react';

interface Props {
  onSearch: (query: string) => void;
  disabled?: boolean;
}

export function SearchBar({ onSearch, disabled }: Props) {
  const [query, setQuery] = useState('');

  const submit = () => onSearch(query);

  const handleKey = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') submit();
  };

  return (
    <div className="search-bar">
      <input
        type="text"
        className="search-input"
        placeholder="Search products..."
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        onKeyDown={handleKey}
        aria-label="Search products"
      />
      <button
        type="button"
        className="search-button"
        onClick={submit}
        disabled={disabled}
      >
        Search
      </button>
    </div>
  );
}
