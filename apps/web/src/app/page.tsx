'use client';

import { useEffect, useState } from 'react';
import { AppShell } from '@/components/AppShell';
import { KpiCard } from '@/components/KpiCard';
import { fetchJson } from '@/lib/api';
import { formatMoney, formatTime } from '@/lib/format';
import { Dashboard } from '@/lib/types';

const DashboardPage = () => {
  const [data, setData] = useState<Dashboard | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    async function load() {
      try {
        const next = await fetchJson<Dashboard>('/api/dashboard');
        if (!cancelled) {
          setData(next);
          setError(null);
        }
      } catch (cause) {
        if (!cancelled) {
          setError(cause instanceof Error ? cause.message : 'Unable to load dashboard');
        }
      }
    }

    void load();
    const timer = window.setInterval(() => {
      void load();
    }, 5000);
    return () => {
      cancelled = true;
      window.clearInterval(timer);
    };
  }, []);

  return (
    <AppShell
      title="Pricing control center"
      subtitle="Watch competitor ticks, auto-applied list moves, and the approval queue for the MRO catalog."
    >
      {error ? <p className="mb-4 text-grainger">{error}</p> : null}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <KpiCard label="Pending approvals" value={String(data?.pendingApprovals ?? '—')} hint="Deltas ≥ 5% wait for a pricing lead" />
        <KpiCard label="Auto-applied" value={String(data?.autoApplied ?? '—')} hint="Small moves applied by the engine" />
        <KpiCard label="Approved" value={String(data?.approved ?? '—')} hint="Manually released recommendations" />
        <KpiCard label="Rejected" value={String(data?.rejected ?? '—')} hint="Held at the previous customer price" />
      </div>

      <section className="mt-10 rounded-lg border border-navy/10 bg-white shadow-sm">
        <div className="border-b border-navy/10 px-5 py-4">
          <h2 className="text-lg font-semibold">Recent competitor moves</h2>
          <p className="text-sm text-steel">Kafka topic competitor-prices, last 20 observations</p>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full text-left text-sm">
            <thead className="bg-navy/5 text-xs uppercase tracking-wide text-steel">
              <tr>
                <th className="px-5 py-3">When</th>
                <th className="px-5 py-3">Competitor</th>
                <th className="px-5 py-3">SKU</th>
                <th className="px-5 py-3">Product</th>
                <th className="px-5 py-3 text-right">Price</th>
              </tr>
            </thead>
            <tbody>
              {(data?.recentCompetitorMoves ?? []).map((move) => (
                <tr key={`${move.sku}-${move.observedAt}`} className="border-t border-navy/5">
                  <td className="px-5 py-3 text-steel">{formatTime(move.observedAt)}</td>
                  <td className="px-5 py-3">{move.competitor}</td>
                  <td className="px-5 py-3 font-mono text-xs">{move.sku}</td>
                  <td className="px-5 py-3">{move.productName}</td>
                  <td className="px-5 py-3 text-right">{formatMoney(move.amount)}</td>
                </tr>
              ))}
              {data && data.recentCompetitorMoves.length === 0 ? (
                <tr>
                  <td className="px-5 py-8 text-center text-steel" colSpan={5}>
                    Waiting for the competitor simulator to publish the first tick.
                  </td>
                </tr>
              ) : null}
            </tbody>
          </table>
        </div>
      </section>
    </AppShell>
  );
};

export default DashboardPage;
