import React from 'react';

interface ButtonProps {
  variant: 'primary' | 'secondary';
  size: 'sm' | 'md' | 'lg';
  children: React.ReactNode;
  className?: string;
  onClick?: () => void;
}

const Button: React.FC<ButtonProps> = ({ 
  variant, 
  size, 
  children, 
  className = '', 
  onClick 
}) => {
  const baseClasses = 'font-medium transition rounded-full cursor-pointer hover:scale-102 active:scale-98';
  
  const variantClasses = {
    primary: 'bg-neutral-900 text-white hover:bg-neutral-800',
    secondary: 'border border-neutral-300 hover:border-neutral-400 hover:bg-neutral-100/[0.9]'
  };
  
  const sizeClasses = {
    sm: 'px-4 py-2 text-sm',
    md: 'px-6 py-3',
    lg: 'px-8 py-3 text-lg'
  };
  
  return (
    <button 
      className={`${baseClasses} ${variantClasses[variant]} ${sizeClasses[size]} ${className}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
};

export default Button; 