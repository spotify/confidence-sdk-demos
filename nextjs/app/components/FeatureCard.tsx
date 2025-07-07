import React from 'react';

interface FeatureCardProps {
  icon: React.ReactNode;
  title: string;
  description: string;
}

const FeatureCard: React.FC<FeatureCardProps> = ({ icon, title, description }) => {
  return (
    <div className="bg-white border border-neutral-200 rounded-2xl p-8 hover:shadow-lg transition-shadow shadow-sm">
      <div className="w-12 h-12 bg-neutral-100 rounded-lg flex items-center justify-center mb-6 font-mono text-2xl">
        {icon}
      </div>
      <h3 className="text-xl font-semibold mb-4">{title}</h3>
      <p className="text-neutral-600 leading-relaxed">
        {description}
      </p>
    </div>
  );
};

export default FeatureCard; 