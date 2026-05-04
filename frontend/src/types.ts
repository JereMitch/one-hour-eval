export type Level = 'BEGINNER' | 'EXPERT';

export interface Product {
  id: number;
  shopifyId?: number;
  title: string;
  description: string;
  price: number;
  category: string;
  level: Level;
}

export interface RecommendResponse {
  products: Product[];
  recommendation: string;
}
