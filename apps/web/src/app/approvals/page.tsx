'use client';

import { useEffect, useState } from 'react';
import { AppShell } from '@/components/AppShell';
import { fetchJson, postJson } from '@/lib/api';
import { formatMoney, formatPct } from '@/lib/format';
import { Recommendation } from '@/lib/types';

const ApprovalsPage = () => {
  const [items, setItems] = useState<Recommendation[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [busyId, setBusyId] = useState<string | null>(null);

  async function load() {
    try {
      setItems(await fetchJson<Recommendation[]>('/api/recommendations?status=PENDING_APPROVAL'));
      setError(null);
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'Unable to load approvals');
    }
  }

  useEffect(() => {
    void load();
    const timer = window.setInterval(() => {
      void load();
    }, 5000);
    return () => window.clearInterval(timer);
  }, []);

  async function decide(id: string, action: 'approve' | 'reject') {
    setBusyId(id);
    try {
      await postJson(`/api/recommendations/${id}/${action}`);
      await load();
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'Unable to update recommendation');
    } finally {
      setBusyId(null);
    }
  }

  return (
    <AppShell
      title="Price approvals"
      subtitle="Large competitor-driven swings stay queued until a pricing owner accepts or rejects them."
    >
      {error ? <p className="mb-4 text-grainger">{error}</p> : null}
      <div className="space-y-4">
        {items.map((item) => (
          <article key={item.id} className="rounded-lg border border-navy/10 bg-white p-5 shadow-sm">
            <div className="flex flex-wrap items-start justify-between gap-4">
              <div>
                <p className="font-mono text-xs text-steel">{item.sku}</p>
                <h2 className="text-lg font-semibold">{item.productName}</h2>
                <p className="text-sm text-steel">{item.customerName}</p>
              </div>
              <div className="text-right">
                <p className="text-sm text-steel">
                  {formatMoney(item.currentPrice)} → {formatMoney(item.recommendedPrice)}
                </p>
                <p className="text-lg font-semibold">{formatPct(item.deltaPct)}</p>
              </div>
            </div>
            <p className="mt-3 text-sm text-steel">{item.rationale}</p>
            <div className="mt-4 flex gap-3">
              <button
                type="button"
                disabled={busyId === item.id}
                onClick={() => void decide(item.id, 'approve')}
                className="rounded bg-grainger px-4 py-2 text-sm font-medium text-white disabled:opacity-50"
              >
                Approve
              </button>
              <button
                type="button"
                disabled={busyId === item.id}
                onClick={() => void decide(item.id, 'reject')}
                className="rounded border border-navy/20 px-4 py-2 text-sm font-medium disabled:opacity-50"
              >
                Reject
              </button>
            </div>
          </article>
        ))}
        {items.length === 0 ? (
          <p className="rounded-lg border border-dashed border-navy/20 bg-white p-8 text-center text-steel">
            No pending approvals. Small competitor moves auto-apply under the 5% threshold.
          </p>
        ) : null}
      </div>
    </AppShell>
  );
};

export default ApprovalsPage;
