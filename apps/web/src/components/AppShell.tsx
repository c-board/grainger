import Link from 'next/link';
import { ReactNode } from 'react';

const links = [
  { href: '/', label: 'Dashboard' },
  { href: '/products', label: 'Products' },
  { href: '/explorer', label: 'Price explorer' },
  { href: '/approvals', label: 'Approvals' },
];

type AppShellProps = {
  title: string;
  subtitle: string;
  children: ReactNode;
};

export const AppShell = ({ title, subtitle, children }: AppShellProps) => {
  return (
    <div className="min-h-screen bg-paper text-navy">
      <header className="bg-navy text-white">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
          <div>
            <p className="text-xs uppercase tracking-[0.2em] text-white/70">Grainger Pricing</p>
            <p className="font-semibold">Keep the World Working</p>
          </div>
          <nav className="flex gap-6 text-sm">
            {links.map((link) => (
              <Link key={link.href} href={link.href} className="text-white/80 hover:text-white">
                {link.label}
              </Link>
            ))}
          </nav>
        </div>
      </header>
      <main className="mx-auto max-w-6xl px-6 py-8">
        <div className="mb-8 border-l-4 border-grainger pl-4">
          <h1 className="text-3xl font-semibold">{title}</h1>
          <p className="mt-1 text-steel">{subtitle}</p>
        </div>
        {children}
      </main>
    </div>
  );
};
