import type { RecommendResponse } from './types';

export async function fetchRecommendation(query: string): Promise<RecommendResponse> {
  const url = `/api/recommend?q=${encodeURIComponent(query)}`;
  const res = await fetch(url);
  if (!res.ok) {
    throw new Error(`Request failed: ${res.status}`);
  }
  return res.json();
}
