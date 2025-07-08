import Button from "./Button";
import CheckIcon from "./CheckIcon";

export default function SubscriptionCard({
  plan,
  price,
  features,
  isHighlighted,
}: {
  plan: string;
  price: string;
  features: string[];
  isHighlighted?: boolean;
}) {

  return (
    <div
      className={`bg-white rounded-2xl border p-6 shadow-sm flex flex-col h-full transition-shadow ${isHighlighted
        ? 'border-2 border-neutral-900 relative hover:shadow-xl'
        : 'border-neutral-300 hover:shadow-lg'
        }`}
    >
      {isHighlighted && (
        <div className="absolute -top-3 left-1/2 transform -translate-x-1/2">
          <span className="bg-neutral-900 text-white px-3 py-1 rounded-full text-sm font-medium">
            Most Popular
          </span>
        </div>
      )}
      <h3 className="text-2xl font-bold mb-2 text-neutral-600">{plan}</h3>
      <p className="text-3xl font-bold text-neutral-900 mb-6">{price}</p>
      <ul className="space-y-3 mb-8 flex-grow">
        {features.map((feature, index) => (
          <li key={index} className="flex items-center gap-3">
            <CheckIcon />
            <span className="text-neutral-600">{feature}</span>
          </li>
        ))}
      </ul>
      <Button
        variant={isHighlighted ? 'primary' : 'secondary'}
        size="lg"
        className="w-full"
      >
        {isHighlighted ? 'Get Started' : 'Select Plan'}
      </Button>
    </div>
  );
}