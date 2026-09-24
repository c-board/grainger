'use client';

import { useEffect, useState } from 'react';
import { AppShell } from '@/components/AppShell';
import { KpiCard } from '@/components/KpiCard';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
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
      {error ? (
        <Alert variant="destructive">
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      ) : null}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <KpiCard label="Pending approvals" value={String(data?.pendingApprovals ?? '—')} hint="Deltas ≥ 5% wait for a pricing lead" />
        <KpiCard label="Auto-applied" value={String(data?.autoApplied ?? '—')} hint="Small moves applied by the engine" />
        <KpiCard label="Approved" value={String(data?.approved ?? '—')} hint="Manually released recommendations" />
        <KpiCard label="Rejected" value={String(data?.rejected ?? '—')} hint="Held at the previous customer price" />
      </div>

      <Card className="mt-6">
        <CardHeader>
          <CardTitle>Recent competitor moves</CardTitle>
          <CardDescription>Kafka topic competitor-prices, last 20 observations</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>When</TableHead>
                <TableHead>Competitor</TableHead>
                <TableHead>SKU</TableHead>
                <TableHead>Product</TableHead>
                <TableHead className="text-right">Price</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {(data?.recentCompetitorMoves ?? []).map((move) => (
                <TableRow key={`${move.sku}-${move.observedAt}`}>
                  <TableCell className="text-muted-foreground">{formatTime(move.observedAt)}</TableCell>
                  <TableCell>{move.competitor}</TableCell>
                  <TableCell className="font-mono text-xs">{move.sku}</TableCell>
                  <TableCell>{move.productName}</TableCell>
                  <TableCell className="text-right">{formatMoney(move.amount)}</TableCell>
                </TableRow>
              ))}
              {data && data.recentCompetitorMoves.length === 0 ? (
                <TableRow>
                  <TableCell className="py-8 text-center text-muted-foreground" colSpan={5}>
                    Waiting for the competitor simulator to publish the first tick.
                  </TableCell>
                </TableRow>
              ) : null}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </AppShell>
  );
};

export default DashboardPage;
