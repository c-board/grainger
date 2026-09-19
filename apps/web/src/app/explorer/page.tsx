'use client';

import { useEffect, useState } from 'react';
import { AppShell } from '@/components/AppShell';
import { fetchJson } from '@/lib/api';
import { formatMoney, formatPct } from '@/lib/format';
import { Customer, PriceBreakdown, Product } from '@/lib/types';

const ExplorerPage = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [sku, setSku] = useState('');
  const [customerId, setCustomerId] = useState('');
  const [breakdown, setBreakdown] = useState<PriceBreakdown | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      try {
        const [nextProducts, nextCustomers] = await Promise.all([
          fetchJson<Product[]>('/api/products'),
          fetchJson<Customer[]>('/api/customers'),
        ]);
        setProducts(nextProducts);
        setCustomers(nextCustomers);
        setSku(nextProducts[0]?.sku ?? '');
        setCustomerId(nextCustomers[0]?.id ?? '');
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : 'Unable to load explorer');
      }
    }
    void load();
  }, []);

  useEffect(() => {
    if (!sku || !customerId) {
      return;
    }
    async function loadBreakdown() {
      try {
        const next = await fetchJson<PriceBreakdown>(
          `/api/prices?sku=${encodeURIComponent(sku)}&customerId=${encodeURIComponent(customerId)}`
        );
        setBreakdown(next);
        setError(null);
      } catch (cause) {
        setError(cause instanceof Error ? cause.message : 'Unable to derive price');
      }
    }
    void loadBreakdown();
  }, [sku, customerId]);

  return (
    <AppShell
      title="Price explorer"
      subtitle="Walk the rule stack: cost floor, segment discount, competitor match, and the price a customer would pay."
    >
      <div className="mb-6 grid gap-4 sm:grid-cols-2">
        <label className="block text-sm">
          <span className="mb-1 block text-steel">Product</span>
          <select
            className="w-full rounded border border-navy/20 bg-white px-3 py-2"
            value={sku}
            onChange={(event) => setSku(event.target.value)}
          >
            {products.map((product) => (
              <option key={product.sku} value={product.sku}>
                {product.sku} — {product.name}
              </option>
            ))}
          </select>
        </label>
        <label className="block text-sm">
          <span className="mb-1 block text-steel">Customer</span>
          <select
            className="w-full rounded border border-navy/20 bg-white px-3 py-2"
            value={customerId}
            onChange={(event) => setCustomerId(event.target.value)}
          >
            {customers.map((customer) => (
              <option key={customer.id} value={customer.id}>
                {customer.name} ({customer.segment})
              </option>
            ))}
          </select>
        </label>
      </div>

      {error ? <p className="mb-4 text-grainger">{error}</p> : null}

      {breakdown ? (
        <div className="grid gap-6 lg:grid-cols-[2fr,1fr]">
          <ol className="space-y-3">
            {breakdown.steps.map((step, index) => (
              <li key={step.name} className="rounded-lg border border-navy/10 bg-white p-4 shadow-sm">
                <div className="flex items-baseline justify-between">
                  <p className="text-xs uppercase tracking-wide text-steel">
                    {index + 1}. {step.name}
                  </p>
                  <p className="text-lg font-semibold">{formatMoney(step.value)}</p>
                </div>
                <p className="mt-1 text-sm text-steel">{step.note}</p>
              </li>
            ))}
          </ol>
          <aside className="h-fit rounded-lg bg-navy p-5 text-white shadow-sm">
            <p className="text-xs uppercase tracking-wide text-white/70">Customer price</p>
            <p className="mt-2 text-3xl font-semibold">{formatMoney(breakdown.currentPrice)}</p>
            <p className="mt-1 text-sm text-white/70">Source: {breakdown.currentSource}</p>
            <dl className="mt-6 space-y-2 text-sm">
              <div className="flex justify-between">
                <dt>Recommended</dt>
                <dd>{formatMoney(breakdown.recommendedPrice)}</dd>
              </div>
              <div className="flex justify-between">
                <dt>Floor</dt>
                <dd>{formatMoney(breakdown.floor)}</dd>
              </div>
              <div className="flex justify-between">
                <dt>Min margin</dt>
                <dd>{formatPct(breakdown.minMargin)}</dd>
              </div>
              <div className="flex justify-between">
                <dt>Segment off list</dt>
                <dd>{formatPct(breakdown.segmentDiscount)}</dd>
              </div>
              <div className="flex justify-between">
                <dt>Competitor match</dt>
                <dd>{breakdown.matchedCompetitor ? 'Yes' : 'No'}</dd>
              </div>
            </dl>
            <p className="mt-6 text-sm text-white/80">{breakdown.rationale}</p>
          </aside>
        </div>
      ) : null}
    </AppShell>
  );
};

export default ExplorerPage;
