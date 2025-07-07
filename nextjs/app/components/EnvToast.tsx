'use client';

import { useEffect, useState } from 'react';

export default function EnvToast() {
  const [showToast, setShowToast] = useState(false);

  useEffect(() => {
    if (!process.env.NEXT_PUBLIC_CLIENT_SECRET) {
      setShowToast(true);
    }
  }, []);

  if (!showToast) return null;

  return (
    <div className="fixed top-20 right-4 z-100">
      <div className="bg-red-500 text-white px-6 py-4 rounded-lg shadow-lg max-w-md">
        <div className="flex items-center justify-between">
          <span className="text-sm font-medium">
            Warning: NEXT_PUBLIC_CLIENT_SECRET environment variable is not set
          </span>
          <button
            onClick={() => setShowToast(false)}
            className="ml-4 text-white hover:text-red-200 focus:outline-none"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  );
} 