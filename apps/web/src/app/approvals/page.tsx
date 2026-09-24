'use client';

import { useEffect, useState } from 'react';
import { AppShell } from '@/components/AppShell';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
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
      {error ? (
        <Alert variant="destructive">
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      ) : null}
      <div className="space-y-4">
        {items.map((item) => (
          <Card key={item.id}>
            <CardContent className="p-5">
              <div className="flex flex-wrap items-start justify-between gap-4">
                <div>
                  <p className="font-mono text-xs text-muted-foreground">{item.sku}</p>
                  <h2 className="text-lg font-semibold">{item.productName}</h2>
                  <p className="text-sm text-muted-foreground">{item.customerName}</p>
                </div>
                <div className="text-right">
                  <p className="text-sm text-muted-foreground">
                    {formatMoney(item.currentPrice)} → {formatMoney(item.recommendedPrice)}
                  </p>
                  <p className="text-lg font-semibold">{formatPct(item.deltaPct)}</p>
                </div>
              </div>
              <p className="mt-3 text-sm text-muted-foreground">{item.rationale}</p>
              <div className="mt-4 flex gap-3">
                <Button type="button" disabled={busyId === item.id} onClick={() => void decide(item.id, 'approve')}>
                  Approve
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  disabled={busyId === item.id}
                  onClick={() => void decide(item.id, 'reject')}
                >
                  Reject
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
        {items.length === 0 ? (
          <Card className="border-dashed">
            <CardContent className="p-8 text-center text-muted-foreground">
              No pending approvals. Small competitor moves auto-apply under the 5% threshold.
            </CardContent>
          </Card>
        ) : null}
      </div>
    </AppShell>
  );
};

export default ApprovalsPage;
