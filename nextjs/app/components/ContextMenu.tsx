'use client';

import React, { useState, useRef, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import RegionRadio from '@/app/components/RegionRadio';
import { setVisitorId } from '@/app/actions';
import Popover from './Popover';
import InfoIcon from './InfoIcon';

type Props = {
  visitorId: string;
}

const ContextMenu = ({ visitorId }: Props) => {
  const params = useParams();
  const router = useRouter();
  const [isOpen, setIsOpen] = useState(false);
  const dialogRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dialogRef.current && !dialogRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    }

    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [isOpen]);


  const handleRegionChange = async (region: string) => {
    router.replace(`/${region}`);
  };

  const handleGenerateNewId = async () => {
    const newId = Math.random().toString(36).substring(2, 15);
    await setVisitorId(newId);
  };

  return (
    <div className="fixed bottom-6 right-6 z-50">
      {isOpen && (
        <div
          ref={dialogRef}
          className="absolute bottom-16 right-0 w-80 bg-white rounded-2xl shadow-lg border border-gray-200 p-6 space-y-6"
        >
          <div className="space-y-2">
            <h3 className="text-lg font-semibold text-neutral-900">Context</h3>
            <p className="text-sm text-neutral-600">
              Modify visitor ID and region to change the evaluation context for feature flags. This controls which features are shown to different users based on the pre-configured flag rules.
            </p>
          </div>

          <div className="space-y-3">
            <div className="flex items-center gap-2">
              <h4 className="text-sm font-medium text-neutral-900">Visitor ID</h4>
              <Popover content="The visitor ID context will give a 50% chance the premium subscription plan is highlighted or not">
                <InfoIcon className="text-neutral-500 hover:text-neutral-700" />
              </Popover>
            </div>
            <div className="bg-neutral-50 rounded-md p-3 border border-neutral-200">
              <p className="text-sm font-mono text-neutral-800 break-all">{visitorId}</p>
            </div>
            <button
              onClick={handleGenerateNewId}
              className="w-full px-3 py-2 text-sm font-medium text-neutral-700 bg-neutral-100 border border-neutral-200 rounded-md hover:bg-neutral-200 active:bg-neutral-300 cursor-pointer"
            >
              Generate New ID
            </button>
          </div>

          <div className="space-y-3">
            <div className="flex items-center gap-2">
              <h4 className="text-sm font-medium text-neutral-900">Region</h4>
              <Popover content="Only North America and Europe offer the enterprise plan at different pricings">
                <InfoIcon className="text-neutral-500 hover:text-neutral-700" />
              </Popover>
            </div>
            <RegionRadio
              onRegionChange={handleRegionChange}
              value={params.region as string}
            />
          </div>


          <button
            onClick={() => setIsOpen(false)}
            className="w-full px-4 py-2 text-sm font-medium text-neutral-700 bg-neutral-100 rounded-md hover:bg-neutral-200 active:bg-neutral-300 cursor-pointer"
          >
            Close
          </button>
        </div>
      )}

      <button
        onClick={() => setIsOpen(!isOpen)}
        className="bg-white text-neutral-900 px-4 py-3 rounded-xl shadow-md border border-neutral-200 hover:shadow-lg transition-all cursor-pointer"
      >
        Change Context
      </button>
    </div>
  );
};

export default ContextMenu;
