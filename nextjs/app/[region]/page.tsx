import { cookies } from 'next/headers';
import { client } from '@/app/confidence';
import SubscriptionCard from '@/app/components/SubscriptionCard';

export default async function Page(props: { params: Promise<{ region: string }> }) {
  const [params, cookieStore] = await Promise.all([
    props.params,
    cookies()
  ]);
  const visitorId = cookieStore.get('visitor_id')?.value;

  const context = {
    visitor_id: visitorId || '',
    region: params.region,
  }

  const [showEnterprisePlan, subscriptionHighlight] = await Promise.all([
    client.getObjectValue<{ enabled: boolean, price: string }>('show-enterprise-plan', {
      enabled: false,
      price: '-',
    }, context),
    client.getBooleanValue('subscription-highlight.enabled', false, context)
  ]);

  return (
    <>
      <hr className="border-neutral-200 mx-12" />
      <section id="plans" className="max-w-7xl mx-auto py-12">
        <h2 className="text-3xl text-center font-bold mb-8">Choose a plan</h2>
        <div className={`grid md:grid-cols-2 gap-8 mb-20 ${showEnterprisePlan.enabled ? 'lg:grid-cols-4' : 'lg:grid-cols-3'}`}>
          <SubscriptionCard
            plan="Basic"
            price="$9"
            features={[
              'Access to Feature A',
              'Standard support',
              'Basic usage limits',
              'Email notifications',
            ]}
          />

          <SubscriptionCard
            plan="Premium"
            price="$19"
            features={[
              'All Basic features',
              'Access to Feature B',
              'Priority support',
              'Advanced usage limits',
              'API access',
            ]}
            isHighlighted={subscriptionHighlight}
          />

          <SubscriptionCard
            plan="Pro"
            price="$39"
            features={[
              'All Premium features',
              'Access to Feature C',
              'Premium support',
              'Highest usage limits',
              'Custom integrations',
              'Advanced analytics',
            ]}
          />

          {showEnterprisePlan.enabled && (
            <SubscriptionCard
              plan="Enterprise"
              price={showEnterprisePlan.price}
              features={[
                'All Premium features',
                'Unlimited usage',
                'Dedicated account manager',
                'Custom solutions',
                'SLA guarantee',
              ]}
            />
          )}
        </div>
      </section>
    </>
  );
}
