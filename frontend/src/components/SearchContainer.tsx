import { useState } from 'react';
import { fetchRecommendation } from '../api';
import type { Product } from '../types';
import { SearchBar } from './SearchBar';
import { ProductCard } from './ProductCard';
import { RecommendationWidget } from './RecommendationWidget';

export function SearchContainer() {
  const [products, setProducts] = useState<Product[]>([]);
  const [recommendation, setRecommendation] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [hasSearched, setHasSearched] = useState(false);

  const handleSearch = async (query: string) => {
    setLoading(true);
    setError('');
    setHasSearched(true);
    try {
      const data = await fetchRecommendation(query);
      setProducts(data.products);
      setRecommendation(data.recommendation);
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Search failed');
      setProducts([]);
      setRecommendation('');
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="search-container">
      <SearchBar onSearch={handleSearch} disabled={loading} />
      {error && <div className="error-state">{error}</div>}
      <RecommendationWidget text={recommendation} />
      {products.length > 0 ? (
        <ul className="product-list">
          {products.map((p) => (
            <ProductCard key={p.id} product={p} />
          ))}
        </ul>
      ) : (
        hasSearched && !loading && !error && (
          <div className="empty-state">No products matched your search.</div>
        )
      )}
    </section>
  );
}
