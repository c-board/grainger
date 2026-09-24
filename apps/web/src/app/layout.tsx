import type { Metadata } from 'next';
import { ReactNode } from 'react';
import { SidebarProvider } from '@/components/ui/sidebar';
import './globals.css';

export const metadata: Metadata = {
  title: 'Grainger Pricing',
  description: 'Market-relevant MRO pricing from product, customer, and competitor signals.',
};

type RootLayoutProps = {
  children: ReactNode;
};

const RootLayout = ({ children }: RootLayoutProps) => {
  return (
    <html lang="en">
      <body>
        <SidebarProvider>{children}</SidebarProvider>
      </body>
    </html>
  );
};

export default RootLayout;
