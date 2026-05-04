interface Props {
  text: string;
}

export function RecommendationWidget({ text }: Props) {
  if (!text) return null;
  return (
    <div className="recommendation-box">
      <span className="recommendation-box__label">AI Recommendation</span>
      <p>{text}</p>
    </div>
  );
}
