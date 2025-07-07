'use client';

import React, { useState } from 'react';

type Props = {
  children: React.ReactNode;
  content: string;
}

const Popover = ({ children, content }: Props) => {
  const [isVisible, setIsVisible] = useState(false);

  return (
    <div className="relative inline-block">
      <div
        onMouseEnter={() => setIsVisible(true)}
        onMouseLeave={() => setIsVisible(false)}
      >
        {children}
      </div>
      
      {isVisible && (
        <div className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 z-50">
          <div className="bg-neutral-800 text-white text-xs rounded-lg px-3 py-2 shadow-lg w-56">
            {content}
          </div>
          <div className="absolute top-full left-1/2 transform -translate-x-1/2 -mt-1 bg-neutral-800 rotate-45 w-2 h-2"></div>
        </div>
      )}
    </div>
  );
};

export default Popover; 