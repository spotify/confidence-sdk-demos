import Link from 'next/link';
import Button from '@/app/components/Button';
import FeatureCard from '@/app/components/FeatureCard';

export default function Layout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-gradient-to-t from-neutral-50 to-white text-neutral-900 font-[family-name:var(--font-geist-sans)]">
      <nav className="sticky top-0 z-50 bg-white/80 backdrop-blur-md border-b border-neutral-200">
        <div className="max-w-6xl mx-auto px-6 py-4 flex items-center justify-center">
          <span className="font-semibold text-lg font-mono">Example Company</span>
        </div>
      </nav>

      <section className="px-6 pt-20 pb-10 text-center">
        <h1 className="text-5xl md:text-7xl font-bold mb-6 tracking-tight">
          Subscription
          <br />
          <span className="bg-gradient-to-r from-neutral-600 to-stone-400 bg-clip-text text-transparent">Service Platform</span>
        </h1>
        <p className="text-xl text-neutral-600 mb-12 max-w-2xl mx-auto leading-relaxed">
          Access our services through flexible subscription plans designed for different needs and usage levels.
        </p>

        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <Link href="#plans">
            <Button variant="primary" size="lg">
              View Plans
            </Button>
          </Link>
        </div>
      </section>

      <section className="px-6 py-20">
        <div className="grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
          <FeatureCard
            icon={<div className="w-6 h-6 bg-blue-500 rounded-full"></div>}
            title="Feature A"
            description="Core functionality available across all subscription tiers with reliable performance and consistent uptime."
          />

          <FeatureCard
            icon={<div className="w-6 h-6 bg-green-500 rotate-45 rounded-sm"></div>}
            title="Feature B"
            description="Advanced capabilities for power users with enhanced limits and priority support included."
          />

          <FeatureCard
            icon={<div className="w-0 h-0 border-l-[12px] border-r-[12px] border-b-[20px] border-l-transparent border-r-transparent border-b-purple-500"></div>}
            title="Feature C"
            description="Premium tools and integrations available in higher-tier plans for comprehensive workflows."
          />
        </div>
      </section>

      {children}

      <section className="bg-neutral-100 py-20">
        <div className="max-w-4xl mx-auto px-6 text-center">
          <p className="text-sm text-neutral-500">
            Example website for demonstration purposes only.
          </p>
        </div>
      </section>
    </div>
  );
}
