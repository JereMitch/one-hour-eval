import type { Product } from '../types';

interface Props {
  product: Product;
}

export function ProductCard({ product }: Props) {
  const levelClass =
    product.level === 'BEGINNER'
      ? 'product-card__level product-card__level--beginner'
      : 'product-card__level product-card__level--expert';

  return (
    <li className="product-card">
      <span className={levelClass}>{product.level}</span>
      <h3 className="product-card__title">{product.title}</h3>
      {product.category && <span className="product-card__category">{product.category}</span>}
      <span className="product-card__price">${Number(product.price).toFixed(2)}</span>
    </li>
  );
}
